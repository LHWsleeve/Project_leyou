package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.GoodsRepository;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClients;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClients goodsClients;
    //jackson提供的把对象转为json格式（序列化）的工具类
    @Autowired
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();
        //根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        //查询分类，根据cid1，2，3查询对应分类名称
        List<String> names = this.categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //根据spuId查询所有的spku
        List<Sku> skus = this.goodsClients.querySkusById(spu.getId());
        //初始化价格集合
        List<Long> prices=new ArrayList<>();
        //skusu表字段太多，为了减少压力，用map放一部分字段到索引库。
        // 每一个map相当于一个sku，map中key的取值：id，title，image，price
        List<Map<String,Object>> skusMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title",sku.getTitle());
            skuMap.put("image",StringUtils.isNotBlank(sku.getImages())?StringUtils.split(sku.getImages(),",")[0]:"");
            skuMap.put("price",sku.getPrice());
            skusMapList.add(skuMap);
        });
        //查询spudetail，目的是拿到genericspec和specialspec
        SpuDetail spuDetail = this.goodsClients.querySpuDetailBySpuId(spu.getId());

        //反序列化为map
        Map<Long,Object> genericSpec = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        Map<Long,List<Object>> sprcialSpec = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });

        //查询所有的搜索规格参数
        List<SpecParam> params = this.specificationClient.querySpecParams(null, spu.getCid3(), null, true);
        Map<String, Object> spec = new HashMap<>();
        for (SpecParam param : params) {
            //判断规格参数是否是通用的
            if (param.getGeneric()) {
                String value = genericSpec.get(param.getId()).toString();
                //判断是否是数值类型，如果是数值类型：返回范围
                if (param.getNumeric()) {
                    value = chooseSegment(value, param);
                }
                //通用规格参数
                spec.put(param.getName(), value);
            } else {
                //特殊规格参数
                List<Object> value = sprcialSpec.get(param.getId());
                spec.put(param.getName(), value);
            }

        }

        BeanUtils.copyProperties(spu,goods);//直接copy共同参数
        goods.setAll(spu.getTitle()+" "+ brand.getName()+" "+ StringUtils.join(names," "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skusMapList));//序列化为json传入
        goods.setSpecs(spec);
        return goods;
    }

    //数值型---》范围
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 完成基本查询功能
     * @param searchRequest
     * @return
     */
    public PageResult<Goods> search(SearchRequest searchRequest) {
        //判断查询条件是否为空
        if (StringUtils.isBlank(searchRequest.getKey())){
            return null;
        }
        //自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));
        //添加分页,行号从0开始
        queryBuilder.withPageable(PageRequest.of(searchRequest.getPage()-1,searchRequest.getSize()));
        //添加结果集过滤，不需要显示的字段不用 需要:id,subTitle,skus
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //执行查询获取结果集
        Page<Goods> goodsPage = this.goodsRepository.search(queryBuilder.build());
        //返回分页结果集
        return new PageResult<>(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent());
    }
}
