package com.leyou.search.client;

import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("item-service")
public interface GoodsClients extends GoodsApi {


}
