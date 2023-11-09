package com.anliantest.third.controller;

import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.third.service.OaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/7/26 14:07
 * @Version 1.0
 * @Description OA系统Controller层
 */
@RestController
@RequestMapping("/oa")
public class OaController extends BaseController {
    private final OaService oaService;

    public OaController(OaService oaService) {
        this.oaService = oaService;
    }

    /**
     * 获取OA用户id
     * @param param 邮箱
     * @return 用户ID
     */
    @PostMapping("/findUserInfo")
    public Map<String, String> findUserInfo(@RequestBody Map<String, String> param) {
        return oaService.getUserInfo(param);
    }
}
