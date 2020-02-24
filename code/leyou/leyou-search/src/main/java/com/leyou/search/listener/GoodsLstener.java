package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

//对静态页面加上rabbitMq监听器
@Component
public class GoodsLstener {
    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SEARCH.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}//routing.key
    ))
    public void saveListener(Long spuId) throws IOException {
        if (spuId==null){
            return;
        }
        this.searchService.saveIndex(spuId);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.SERACH.DELECT.QUEUE",durable = "true"),
            exchange = @Exchange(value = "LEYOU.ITEM.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delect"}))
    public void delectListener(Long spuId){
        if (spuId==null){
            return;
        }
        this.searchService.delete(spuId);
    }

}
