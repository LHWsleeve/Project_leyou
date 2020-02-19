package cn.itcast.elasticsearch;

import cn.itcast.elasticsearch.pojo.Item;
import cn.itcast.elasticsearch.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ItcastElasticsearchApplicationTests {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private ItemRepository itemRepository;

    @Test
   public void testIndex() {
        this.elasticsearchTemplate.createIndex(Item.class);//基于pojo注解创建索引库
        this.elasticsearchTemplate.putMapping(Item.class);//创建映射
    }

    @Test
    public void testCreate(){
        Item item = new Item(1L, "小米手机7", " 手机", "小米",
                3499.00, "http://image.leyou.com/13123.jpg");
        this.itemRepository.save(item);
    }

    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    @Test
    public void testFind(){
//        Optional<Item> item = this.itemRepository.findById(1l);
//        System.out.println(item.get());
//        List<Item> items = this.itemRepository.findByTitle("手机");
        List<Item> items = this.itemRepository.findByPriceBetween(3499d, 3699d);
        items.forEach(System.out::println);//方法引用
    }
}
