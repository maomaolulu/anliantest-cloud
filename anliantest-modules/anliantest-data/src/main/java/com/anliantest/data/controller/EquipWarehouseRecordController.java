package com.anliantest.data.controller;

import com.anliantest.common.core.constant.HttpStatus;
import com.anliantest.common.core.constant.MinioConstants;
import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.common.core.web.controller.BaseController;
import com.anliantest.common.core.web.page.TableDataInfo;
import com.anliantest.common.log.annotation.Log;
import com.anliantest.common.log.enums.BusinessType;
import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.service.EquipWarehouseRecordService;
import com.anliantest.file.api.RemoteSysAttachmentService;
import com.anliantest.file.api.domain.SysAttachment;
import com.anliantest.file.api.domain.dto.MinioDto;
import com.anliantest.system.api.RemoteUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备库存
 *
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 设备库存
 */
@RestController
@Slf4j
@Api(tags = "设备库存")
@RequestMapping("/equip_warehouse_record")
public class EquipWarehouseRecordController extends BaseController {


    private EquipWarehouseRecordService equipWarehouseRecordService;
    private RemoteUserService remoteUserService;
    private RemoteSysAttachmentService remoteSysAttachmentService;

    @Autowired
    public EquipWarehouseRecordController(EquipWarehouseRecordService equipWarehouseRecordService, RemoteUserService remoteUserService, RemoteSysAttachmentService remoteSysAttachmentService) {
        this.equipWarehouseRecordService = equipWarehouseRecordService;
        this.remoteUserService = remoteUserService;
        this.remoteSysAttachmentService = remoteSysAttachmentService;
    }


    /**
     * 设备库存/清单分页
     *
     * @param equipWarehouseRecordDto
     * @return
     */
    @GetMapping("listPage")
    public TableDataInfo listPage(EquipWarehouseRecordDto equipWarehouseRecordDto) {
        startPage();
        List<EquipWarehouseRecordDto> equipWarehouseRecordDtos = equipWarehouseRecordService.equipWarehouseRecordList(equipWarehouseRecordDto);
        return getDataTable(equipWarehouseRecordDtos);
    }


    /**
     * 设备库存/清单详情
     *
     * @param id
     * @return
     */
    @GetMapping("info")
    public R info(Long id) {

        EquipWarehouseRecord byId = equipWarehouseRecordService.getById(id);
        //获取证书附件列表
        List<Long> idList = new ArrayList<>();
        idList.add(byId.getId());

        //根据父级id获取附件列表map
        MinioDto minioDto = new MinioDto();
        minioDto.setBucketName(MinioConstants.EQUIP_EQUIPMENT_FILE);
        minioDto.setIdList(idList);
        List<SysAttachment> sysAttachments = null;

        R<Map<Long, List<SysAttachment>>> r = remoteSysAttachmentService.getSysAttachmentMap(minioDto, SecurityConstants.INNER);
        if (r.getCode() == HttpStatus.SUCCESS) {
            Map<Long, List<SysAttachment>> sysAttachmentMap = r.getData();
            if (sysAttachmentMap != null) {
                sysAttachments = sysAttachmentMap.get(byId.getId());
            }
        }
        byId.setSysAttachmentList(sysAttachments);
        return R.ok(byId);
    }


    /**
     * 修改设备库存
     *
     * @param equipWarehouseRecord
     * @return
     */
    @Log(title = "修改设备库存", businessType = BusinessType.UPDATE)
    @PostMapping("update")
    public R update(@RequestBody EquipWarehouseRecord equipWarehouseRecord) {
        try {
            if (equipWarehouseRecord.getId() == null) {
                return R.fail("id不存在，请检查！");
            }
            int i = equipWarehouseRecordService.updateEquipInfo(equipWarehouseRecord);
            if (i == 2) {
                return R.fail("设备编号已存在");
            }
            return i == 0 ? R.fail("编辑失败") : R.ok("编辑成功");
        } catch (Exception e) {
            logger.error("更新设备信息发生异常======" + e);
            return R.fail("更新设备信息异常");
        }
    }
}
