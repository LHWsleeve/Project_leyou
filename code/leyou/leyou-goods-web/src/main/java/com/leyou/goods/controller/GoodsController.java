package com.leyou.goods.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoodsController {

    @GetMapping("/item/{spuId}.html")
    public String toItemPage(Model model){

    return "item";
    }



}
