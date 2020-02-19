package cn.itcast.elasticsearch;

import cn.itcast.elasticsearch.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItcastElasticsearchApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
   public void testIndex() {
        this.elasticsearchTemplate.createIndex(Item.class);//基于pojo注解创建索引库
        this.elasticsearchTemplate.putMapping(Item.class);//创建映射
    }

}
