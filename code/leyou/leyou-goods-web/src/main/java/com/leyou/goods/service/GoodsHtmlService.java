package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService  {
    @Autowired
   private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;

    public void createHtml(Long spuId){
        //初始化上下文对象
        Context context = new Context();
        //设置数据模型给上下文
        context.setVariables(this.goodsService.loadData(spuId));
        //写道服务器本地：
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("C:\\Users\\liuha\\Documents\\GitHub\\Project_leyou\\tools\\nginx-1.16.1\\html\\item\\"+spuId+".html"));
            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (printWriter!=null){
                printWriter.close();
            }
        }
    }

}
