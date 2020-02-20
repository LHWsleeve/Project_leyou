package com.leyou.item.pojo.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/spec")
public interface SpecificationApi {
    /**
     * 根据分类id查询规格组
     * @param cid
     * @return
     */
    @GetMapping("/groups/{cid}")
    public List<SpecGroup> queryGroupsByCid(@PathVariable("cid") Long cid);

    /**
     * 根据条件查询规格组的对应参数组
     * @param gid
     * @return
     */
    @GetMapping("/params")
    public List<SpecParam> querySpecParams(@RequestParam(value = "gid",required = false)Long gid,
                                           @RequestParam(value = "cid", required = false) Long cid,
                                           @RequestParam(value = "generic", required = false) Boolean generic,
                                           @RequestParam(value = "searching", required = false) Boolean searching
    );
}
