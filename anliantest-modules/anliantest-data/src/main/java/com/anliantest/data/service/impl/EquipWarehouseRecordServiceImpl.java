package com.anliantest.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.StatefulException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.exception.ServiceException;
import com.anliantest.common.core.utils.RandomUtil;
import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.common.datascope.util.DataScopeUtil;
import com.anliantest.common.security.utils.SecurityUtils;
import com.anliantest.data.constant.UrlConstants;
import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipPurchaseRecord;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.mapper.EquipPurchaseRecordMapper;
import com.anliantest.data.mapper.EquipWarehouseRecordMapper;
import com.anliantest.data.service.EquipWarehouseRecordService;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.system.api.RemoteConfigService;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.domain.SysConfig;
import com.anliantest.system.api.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author yrb
 * @Date 2023/8/1 16:33
 * @Version 1.0
 * @Description 仪器设备入库记录实现层
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class EquipWarehouseRecordServiceImpl extends ServiceImpl<EquipWarehouseRecordMapper, EquipWarehouseRecord> implements EquipWarehouseRecordService {
    private final EquipWarehouseRecordMapper equipWarehouseRecordMapper;
    private final EquipPurchaseRecordMapper equipPurchaseRecordMapper;
    private final RemoteSysAttachmentService remoteSysAttachmentService;
    private final RemoteUserService remoteUserService;
    private final RemoteConfigService remoteConfigService;

    public EquipWarehouseRecordServiceImpl(EquipWarehouseRecordMapper equipWarehouseRecordMapper,
                                           EquipPurchaseRecordMapper equipPurchaseRecordMapper,
                                           RemoteSysAttachmentService remoteSysAttachmentService,
                                           RemoteUserService remoteUserService,
                                           RemoteConfigService remoteConfigService) {
        this.equipWarehouseRecordMapper = equipWarehouseRecordMapper;
        this.equipPurchaseRecordMapper = equipPurchaseRecordMapper;
        this.remoteSysAttachmentService = remoteSysAttachmentService;
        this.remoteUserService = remoteUserService;
        this.remoteConfigService = remoteConfigService;
    }

    /**
     * 设备入库
     *
     * @param equipWarehouseRecord 入库信息
     * @return result
     */
    @Override
    public int add(EquipWarehouseRecord equipWarehouseRecord) {
        // 处理入库数量
        if (StrUtil.isBlank(equipWarehouseRecord.getWarehouseAmount())) {
            equipWarehouseRecord.setWarehouseAmount("1");
        }
        // 如果是采购入库需要更新库存
        if (equipWarehouseRecord.getWarehouseType() == 1) {
            Long oldId = equipWarehouseRecord.getOldId();
            EquipPurchaseRecord equipPurchaseRecord = equipPurchaseRecordMapper.selectById(oldId);
            BigDecimal remainAmount = new BigDecimal(equipPurchaseRecord.getRemainAmount());
            remainAmount = remainAmount.subtract(new BigDecimal(equipWarehouseRecord.getWarehouseAmount()));
            EquipPurchaseRecord equipPurchaseRecord1 = new EquipPurchaseRecord();
            equipPurchaseRecord1.setRemainAmount(String.valueOf(remainAmount));
            equipPurchaseRecord1.setId(oldId);
            if (remainAmount.equals(BigDecimal.ZERO)) {
                equipPurchaseRecord1.setFullyStored("1");
            }
            equipPurchaseRecordMapper.updateById(equipPurchaseRecord1);
        }
        // 设置设备编号(打印编号)
        equipWarehouseRecord.setLabelCode(getLabelCode());
        List<SysAttachment> sysAttachmentList = equipWarehouseRecord.getSysAttachmentList();
        if (CollUtil.isNotEmpty(sysAttachmentList)) {
            equipWarehouseRecord.setHasImage(1);
        }
        // 入库人、入库时间
        equipWarehouseRecord.setCreateName(SecurityUtils.getUsernameCn());
        equipWarehouseRecord.setCreateTime(new Date());
        // 将上传的临时文件转为有效文件
        if (baseMapper.insert(equipWarehouseRecord) != 0) {
            // 同步数据到OA
            if (synOaEquipInfo(equipWarehouseRecord) == 0) {
                throw new UnsupportedOperationException("同步数据到OA失败");
            }
            if (CollUtil.isNotEmpty(sysAttachmentList)) {
                sysAttachmentList.forEach(s -> s.setPId(equipWarehouseRecord.getId()));
                MinioDto minioDto = new MinioDto();
                minioDto.setSysAttachmentList(sysAttachmentList);
                if (remoteSysAttachmentService.transformByIds(minioDto, SecurityConstants.INNER)) {
                    throw new UnsupportedOperationException("将上传的文件转为有效文件失败");
                }
            }
        }
        return 1;
    }

    /**
     * 获取入库列表
     *
     * @param equipWarehouseRecord 入库信息
     * @return 入库列表
     */
    @Override
    public List<EquipWarehouseRecord> list(EquipWarehouseRecord equipWarehouseRecord) {
        QueryWrapper<EquipWarehouseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(equipWarehouseRecord.getEquipCode()), "t1.equip_code", equipWarehouseRecord.getEquipCode())
                .like(StrUtil.isNotBlank(equipWarehouseRecord.getCompanyName()), "t2.dept_name", equipWarehouseRecord.getCompanyName())
                .like(StrUtil.isNotBlank(equipWarehouseRecord.getGoodsName()), "t1.goods_name", equipWarehouseRecord.getGoodsName());
        // 处理查询条件 入库时间
        Date createTime = equipWarehouseRecord.getCreateTime();
        if (createTime != null) {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate localDate = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            queryWrapper.between("t1.create_time",
                    LocalDateTime.of(localDate, LocalTime.MIN).format(pattern),
                    LocalDateTime.of(localDate, LocalTime.MAX).format(pattern));
        }
        return equipWarehouseRecordMapper.getInfo(queryWrapper);
    }

    /**
     * 校验设备编号唯一性
     *
     * @param equipCode 验设备编号
     * @return 数量
     */
    @Override
    public int checkEuipCodeUnique(String equipCode) {
        return equipWarehouseRecordMapper.checkEuipCodeUnique(equipCode);
    }

    /**
     * 更新标签打印状态
     *
     * @param equipCode 设备编号
     * @return result
     */
    @Override
    public int updatePrintLabel(String equipCode) {
        return equipWarehouseRecordMapper.updatePrintLabel(equipCode);
    }

    /**
     * 同步修改OA仪器设备信息（新增）
     *
     * @param equipWarehouseRecord 仪器设备信息
     * @return result
     */
    @Override
    public int synOaEquipInfo(EquipWarehouseRecord equipWarehouseRecord) {
        Map<String, Object> map = Maps.newHashMap();
        // 判断更新还是新增
        map.put("id", equipWarehouseRecord.getId());
        // 打印条码
        map.put("labelCode", equipWarehouseRecord.getLabelCode());
        // 物品状态  1在用 2准用 3待修 4维修中 5送检中 6报废 7外借 8封存 9退货
        map.put("state", equipWarehouseRecord.getStatus());
        // 设备编号（资产编号）
        map.put("assetSn", equipWarehouseRecord.getEquipCode());
        // 资产分类(默认仪器设备)
        map.put("assetType", "53");
        // 责任人
        Long chargeId = equipWarehouseRecord.getChargeId();
        SysUser sysUser = remoteUserService.selectUserByUserId(chargeId,SecurityConstants.INNER);
        if (sysUser == null || sysUser.getOaUserId() == null) {
            throw new StatefulException("关联OA责任人ID获取失败");
        }
        map.put("chargeId", sysUser.getOaUserId());
        // 保管人
        map.put("keeper", remoteUserService.selectUserByUserId(equipWarehouseRecord.getKeeperId(),SecurityConstants.INNER).getNickName());
        // 物品名称
        map.put("name", equipWarehouseRecord.getGoodsName());
        // 单位
        map.put("unit", equipWarehouseRecord.getUnit());
        // 规格
        map.put("model", equipWarehouseRecord.getModel());
        // 制造商
        map.put("dealer", equipWarehouseRecord.getProductCompany());
        // 采购订单id
        map.put("orderId", equipWarehouseRecord.getOrderId());
        // 采购单价
        map.put("purchasePrice", equipWarehouseRecord.getSinglePrice());
        map.put("value", equipWarehouseRecord.getSinglePrice());
        // 采购日期
        map.put("purchaseTime", equipWarehouseRecord.getPurchaseDate());
        // 到货日期
        map.put("arriveTime", equipWarehouseRecord.getArrivedTime());
        // 是否为需要检定的仪器
        String calibration = equipWarehouseRecord.getCalibration();
        if ("检定".equals(calibration)) {
            map.put("isInstrument", 1);
        } else {
            map.put("isInstrument", 0);
        }
        // 录入人id
        SysUser user = remoteUserService.selectUserByUserId(SecurityUtils.getUserId(),SecurityConstants.INNER);
        if (user != null && user.getUserId() != null) {
            map.put("userId", user.getOaUserId());
        }
        // 申请编号
        map.put("purchaseCode", equipWarehouseRecord.getPurchaseCode());
        //是否上传图片
        map.put("uploadPic", equipWarehouseRecord.getHasImage());

        SysConfig configUrl = remoteConfigService.findConfigUrl();
        String url;
        if ("test".equals(configUrl.getConfigValue())) {
            url = UrlConstants.OA_EQUIP_TEST;
        } else {
            url = UrlConstants.OA_EQUIP_ONLINE;
        }
        String response = HttpUtil.createPost(url)
                .header("authCode", "d537fa8d-3ec2-48a4-916e-fa834f7f2922")
                .body(JSON.toJSONString(map))
                .execute().body();
        if (response.length() == 1) {
            Integer r = Integer.valueOf(response);
            if (r == 0) {
                throw new StatefulException("仪器设备信息同步OA失败！");
            } else if (r == 2) {
                throw new StatefulException("非法用户！");
            } else if (r == 3) {
                throw new StatefulException("仪器设备编号重复！");
            }
        } else {
            throw new StatefulException("未知异常，请联系管理员!");
        }
        return 1;
    }

    /**
     * 同步修改OA仪器设备信息（修改）
     *
     * @param equipWarehouseRecord 仪器设备信息
     * @return result
     */
    @Override
    @Transactional
    public int updateEquipInfo(EquipWarehouseRecord equipWarehouseRecord) {
        // 校验设备编号唯一性
        QueryWrapper<EquipWarehouseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("id", equipWarehouseRecord.getId());
        queryWrapper.eq("equip_code", equipWarehouseRecord.getEquipCode());
        if (equipWarehouseRecordMapper.selectCount(queryWrapper) > 0) {
            return 2;
        }
        // 更新
        this.updateById(equipWarehouseRecord);
        //根据父级id,新附件列表更新附件信息
        List<SysAttachment> sysAttachmentList = equipWarehouseRecord.getSysAttachmentList();
        if (CollUtil.isNotEmpty(sysAttachmentList)) {
            equipWarehouseRecord.setHasImage(1);
        }
        MinioDto minioDto = new MinioDto();
        minioDto.setId(equipWarehouseRecord.getId());
        minioDto.setSysAttachmentList(sysAttachmentList);
        remoteSysAttachmentService.updateByPid(minioDto, SecurityConstants.INNER);
        if (synOaEquipInfo(equipWarehouseRecord) == 0) {
            throw new ServiceException("同步OA仪器设备信息异常");
        }
        return 1;
    }

    /**
     * 获取 打印条码
     *
     * @return 标签编号
     */
    private String getLabelCode() {
        String s = RandomUtil.randomInt(12);
        if (equipWarehouseRecordMapper.checkLabelCodeUnique(s) != 0) {
            getLabelCode();
        }
        return s;
    }


    /**
     * 获取应检定设备列表
     */
    @Override
    public List<EquipWarehouseRecord> getVerificationList(EquipWarehouseRecord equipWarehouseRecord) {

        String equipCode = equipWarehouseRecord.getEquipCode();
        String goodsName = equipWarehouseRecord.getGoodsName();
        Integer verificationStatus = equipWarehouseRecord.getVerificationStatus();
        List<String> calibrationList = new ArrayList<>();
        calibrationList.add("检定");
        calibrationList.add("校准");
        QueryWrapper<EquipWarehouseRecord> queryWrapper = new QueryWrapper<EquipWarehouseRecord>()
                .ne("status", 6)
                .like(StringUtils.isNotBlank(equipCode), "equip_code", equipCode)
                .like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName);

        if (verificationStatus != null) {
            queryWrapper.eq("verification_status", verificationStatus);
        } else {
            queryWrapper.ne("verification_status", 0);
        }
        queryWrapper.in("calibration", calibrationList)
                .orderByAsc("useful_time");

        return this.list(queryWrapper);
    }

    /**
     * 设备清单分页
     */
    @Override
    public List<EquipWarehouseRecordDto> equipWarehouseRecordList(EquipWarehouseRecordDto equipWarehouseRecordDto) {
        String d = DataScopeUtil.getScopeSql("d", "");
        QueryWrapper<EquipWarehouseRecord> wrapper = new QueryWrapper<>();
        //设备编号
        wrapper.like(StrUtil.isNotBlank(equipWarehouseRecordDto.getEquipCode()), "wr.equip_code", equipWarehouseRecordDto.getEquipCode());
        //设备名称
        wrapper.like(StrUtil.isNotBlank(equipWarehouseRecordDto.getGoodsName()), "wr.goods_name", equipWarehouseRecordDto.getGoodsName());
        //设备类型
        wrapper.eq(equipWarehouseRecordDto.getEquipType() != null, "wr.equip_type", equipWarehouseRecordDto.getEquipType());
        //公司名称
        wrapper.eq(equipWarehouseRecordDto.getCompanyId() != null, "d.dept_id", equipWarehouseRecordDto.getCompanyId());
        //责任部门
        wrapper.eq(equipWarehouseRecordDto.getChargeDeptId() != null, "wr.charge_dept_id", equipWarehouseRecordDto.getChargeDeptId());
        //打印标签：0未打印、1已打印
        wrapper.eq(equipWarehouseRecordDto.getPrintLabel() != null, "wr.print_label", equipWarehouseRecordDto.getPrintLabel());
        //数据权限

        wrapper.apply(StrUtil.isNotBlank(d), d);
        return baseMapper.equipWarehouseRecordList(wrapper);
    }

    ;


    /**
     * 设备详情
     */
    @Override
    public EquipWarehouseRecord getInfo(Long id) {
        return this.getById(id);
    }
}
