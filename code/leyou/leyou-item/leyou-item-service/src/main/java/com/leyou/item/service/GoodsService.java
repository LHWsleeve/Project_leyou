package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.leyou.item.pojo.bo.SpuBo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //添加模糊查询
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //添加是否上架过滤
        if (saleable != null){
            criteria.andEqualTo("saleable", saleable);
        }
        //添加分页
        PageHelper.startPage(page,rows);
        //执行查询
        List<Spu> spuList = this.spuMapper.selectByExample(example);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spuList);
        //返回分页结果
        
        //把一个list<spu>转化成list<spubo>集合
        List<SpuBo> spuBoList = spuList.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            //把spu所有的属性值copy给spubo
            BeanUtils.copyProperties(spu, spuBo);
            //设置spubo中扩展的两个名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            List<String> name = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setBname(brand.getName());
            spuBo.setCname(StringUtils.join(name, "-"));
            return spuBo;
        }).collect(Collectors.toList());
        //返回分页结果集
        return new PageResult<>(spuPageInfo.getTotal(),spuBoList);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        //1.新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insert(spuBo);

        //2.新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);
        //增加rbbitmq消息发送
        sendMessage("insert",spuBo.getId());

        //ctrl+alt+m抽取方法
        saveSkuAndStock(spuBo);
    }

        private void sendMessage( String type, Long spuId) {
        try {
            this.amqpTemplate.convertAndSend("item."+type,spuId);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    private void saveSkuAndStock(SpuBo spuBo) {
        List<Sku> skus = spuBo.getSkus();
        skus.forEach(sku -> {
            //遍历每一个sku，执行3，4
            //3/新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(spuBo.getCreateTime());
            sku.setLastUpdateTime(spuBo.getLastUpdateTime());
            this.skuMapper.insertSelective(sku);
            //4.新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuid查询所有sku
     * @param spuId
     * @return
     */
    public List<Sku> querySkusById(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skuList = this.skuMapper.select(record);
        //查询每个sku对应的库存
        skuList.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skuList;
    }

    /**
     * 更新商品=先全部删除后新增
     * @param spuBo
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //先删除sku和stock
          //查询所有的sku，遍历sku删除库存
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
           //删除stock
        skus.forEach(sku -> {this.stockMapper.deleteByPrimaryKey(sku.getId());});
           //删除sku，根据skuid
        this.skuMapper.delete(record);

        //再新增sku和stock
        this.saveSkuAndStock(spuBo);

        //在更新spu和spuDetail,updateByPrimaryKeySelective:当一个字段为null的时候该字段不跟更新
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        sendMessage("update",spuBo.getId());
    }

    public Spu querySpuById(Long spuId) {
        return  this.spuMapper.selectByPrimaryKey(spuId);
    }
}
