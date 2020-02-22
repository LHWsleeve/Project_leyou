package com.leyou.goods.client;

import com.leyou.item.pojo.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClients extends GoodsApi {


}
