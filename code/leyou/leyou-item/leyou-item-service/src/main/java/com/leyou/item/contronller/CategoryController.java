package com.leyou.item.contronller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父类目的id查询所有子节点
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategorysByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid < 0) {
            //参数不合法相应400,三种写法
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().build();
        }

        //执行查询获取结果集
        List<Category> categories = this.categoryService.queryCategoryByPid(pid);
        //判断查询结果是否为空，若为空相应404
        if (CollectionUtils.isEmpty(categories)) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //正常找到相应200
        return ResponseEntity.ok(categories);
    }

    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNameByIds(ids);

        if (CollectionUtils.isEmpty(names)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(names);
    }
}
