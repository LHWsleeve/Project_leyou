package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private GoodClient goodClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void addCart(Cart cart) {
        //先查询
        UserInfo userInfo = LoginInterceptor.get();
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();
        //判断是否有
        if (hashOperations.hasKey(skuId)){
            //有，更新数量
            String cartJson = hashOperations.hasKey(skuId).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(num);
        }else {
            //没有，新增
            cart.setUserId(userInfo.getId());
                //查询商品信息
            Sku sku = this.goodClient.querSkuById(cart.getSkuId());
            cart.setPrice(sku.getPrice());
            cart.setImage(StringUtils.isBlank(sku.getImages())?"":StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(cart.getTitle());
        }
        hashOperations.put(skuId, JsonUtils.serialize(cart));



    }

    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.get();
        //判断hash操作对象是否存在
       if (!this.redisTemplate.hasKey(userInfo.getId().toString())){
           return  null;
       }

        //先查询
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
       List<Object> cartJsons = hashOperations.values();
       return cartJsons.stream().map(cartJson-> JsonUtils.parse(cartJson.toString(), Cart.class)).collect(Collectors.toList());

    }
}
