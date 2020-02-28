package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.pojo.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.temporal.JulianFields;
import java.util.List;

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
}
