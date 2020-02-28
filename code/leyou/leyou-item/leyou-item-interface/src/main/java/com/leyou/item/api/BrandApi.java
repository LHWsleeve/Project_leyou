package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/brand")
public interface BrandApi {


    @GetMapping("/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);
}
