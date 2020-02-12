package com.leyou.item.service;

import com.leyou.item.mapper.CategroyMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategroyMapper categroyMapper;

    /**
     * 根据父id查询子类目
     * @param pid
     * @return
     */
    public List<Category> queryCategoryByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categroyMapper.select(record);
    }

    /**
     * 根据多个分类id查询分类名称
     * @return
     */
    public List<String> queryNameByIds(List<Long> ids){
        List<Category> categories = this.categroyMapper.selectByIdList(ids);
        //把一个List<category>转化成List<String>
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
