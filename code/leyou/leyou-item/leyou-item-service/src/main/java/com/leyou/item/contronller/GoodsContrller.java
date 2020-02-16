package com.leyou.item.contronller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.bo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsContrller {
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("/spu/page")//http://api.leyou.com/api/item/spu/page?key=&saleable=true&page=1&rows=5
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    ){
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(key,saleable,page,rows);
        if (CollectionUtils.isEmpty(result.getItems())){
        return   ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * @param spuBo
     * @return
     */
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新
     * @param spuBo
     * @return
     */
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * 根据主键查询spu的detail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuid查询所有sku
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusById(@RequestParam("id") Long spuId){
        List<Sku> skus = this.goodsService.querySkusById(spuId);
        if (CollectionUtils.isEmpty(skus)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

}
