package com.leyou.item.contronller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/brand")
public class BrandController{
    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param key
     * @param rows
     * @param page
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/page")//key=&page=1&rows=5&sortBy=id&desc=false
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = ("key"),required = false) String key,
            @RequestParam(value = ("rows"),defaultValue = "1") Integer rows,
            @RequestParam(value = ("page"),defaultValue = "5") Integer page,
            @RequestParam(value = ("sortBy"),required = false) String sortBy,
            @RequestParam(value = ("desc"),required = false) Boolean desc
    ){
        PageResult<Brand> result = this.brandService.queryBrandsPage(key,page,rows,sortBy,desc);
        if (CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
