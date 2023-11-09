package com.anliantest.project.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.project.entity.Chinas;
import com.anliantest.project.service.ChinasService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @date 2023-08-24 16:01
 */
@RestController
@Api(tags = "省市区三级联动")
@RequestMapping("/chinas")
public class ChinasController extends BaseController {

    @Autowired
    private ChinasService chinasService;

    /**
     * 省市区三级联动
     */
    @GetMapping("/getRegions")
    public R<Object> getRegions() {
        List<Chinas> jsonObject = chinasService.getRegions();
        return R.ok(jsonObject,"查询成功");
    }

    /**
     * 导入省市区
     * @param chinasList
     * @return
     */
    @PostMapping("/import")
    public R importRegions(@RequestBody List<Chinas> chinasList) {
        Boolean b = chinasService.importRegions(chinasList);
        return R.ok();
    }

//    /**
//     * 导入台湾县区
//     * @return
//     */
//    @GetMapping("/import1")
//    public R import1() {
//
//        Boolean b = chinasService.import1();
//
//        return R.ok();
//    }

}
