package com.anliantest.system.controller;

import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.redis.service.RedisService;
import com.anliantest.common.security.annotation.InnerAuth;
import com.anliantest.system.api.model.AmapData;
import com.anliantest.system.domain.vo.ChineseAddressVo;
import com.anliantest.system.service.IChineseAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Description 全国地图数据
 * @Date 2023/8/25 14:43
 * @Author maoly
 **/
@RestController
@RequestMapping("/address")
public class ChineseAddressController extends BaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IChineseAddressService chineseAddressService;

    /**
     * 保存地址数据
     * @return
     */
    @GetMapping("/saveChineseAddress/{param}")
    @InnerAuth
    public R  saveChineseAddress(@PathVariable String param){
        if(StringUtils.isNotBlank(param)){
            String redis_key = param;
            if(redisService.hasKey(redis_key)){
                AmapData amapData  = redisService.getCacheObject(redis_key);
                if(amapData != null){
                    chineseAddressService.saveChineseAddress(amapData);
                }
            }
        }
        return R.ok();
    }

    /**
     * 省市区筛选组件(只返回用于查询下一级的id和名称)
     */
    @GetMapping("/getIdAndName")
    public R<List<ChineseAddressVo>> getIdAndName(String regionParentId)
    {
        return R.ok(chineseAddressService.getIdAndName(regionParentId), "查询成功");
    }

    /**
     * 获取地区树形查询
     */
    @GetMapping("/getRegions")
    public R<Object> getRegions(String regionParentId)
    {
        return R.ok(chineseAddressService.getRegions(regionParentId),"查询成功");
    }


}
