package com.leyou.goods.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClients;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClients goodsClients;
    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId) {
        Map<String, Object> model = new HashMap<>();
        //根据spuId查询spu再得到分类id
        Spu spu = this.goodsClients.querySpuById(spuId);
        //根据分类cid1，2，3，查询分类
        List<Map<String ,Object>> categories = new ArrayList<>();
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

        //根据brandId查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //根据spuid查询skus
        List<Sku> skus = this.goodsClients.querySkusById(spuId);
        //
        SpuDetail spuDetail = this.goodsClients.querySpuDetailBySpuId(spuId);
        //
        List<SpecGroup> groups = this.specificationClient.queryGroupWithParam(spu.getCid3());
        //根据id查询speciacl的规格参数和对应的名称,并且放入map。形成{id：name}的格式
        List<SpecParam> params = this.specificationClient.querySpecParams(null, spu.getCid3(), false, null);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(parm->{
            paramMap.put(parm.getId(),parm.getName());
        });

        model.put("categories",categories);
        model.put("brand",brand);
        model.put("spu",spu);
        model.put("skus",skus);
        model.put("spuDetail",spuDetail);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }
}
