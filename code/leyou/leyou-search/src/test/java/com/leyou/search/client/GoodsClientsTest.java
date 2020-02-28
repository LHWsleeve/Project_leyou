package com.leyou.search.client;

import com.leyou.common.PageResult;
import com.leyou.item.pojo.bo.SpuBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GoodsClientsTest {
    @Autowired
    private GoodsClients goodsClients;

    @Test
    public void testQuery(){
        PageResult<SpuBo> result = this.goodsClients.querySpuByPage(null, true, 1, 5);
        result.getItems().forEach(System.out::println);
    }


}