package com.anliantest.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.anliantest.common.core.exception.UtilException;
import com.anliantest.data.domain.dto.EquipWarehouseRecordDto;
import com.anliantest.data.entity.EquipWarehouseRecord;
import com.anliantest.data.service.EquipWarehouseRecordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.anliantest.data.entity.EquipBorrowingRecord;
import com.anliantest.data.mapper.EquipBorrowingRecordMapper;
import com.anliantest.data.service.EquipBorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 * @date 2023-08-04
 * @desc : 设备借用记录
 */
@Service
public class EquipBorrowingRecordServiceImpl extends ServiceImpl<EquipBorrowingRecordMapper, EquipBorrowingRecord> implements EquipBorrowingRecordService {

    private final EquipWarehouseRecordService equipWarehouseRecordService;
    @Autowired
    public EquipBorrowingRecordServiceImpl(EquipWarehouseRecordService equipWarehouseRecordService) {
        this.equipWarehouseRecordService = equipWarehouseRecordService;
    }

    /**
     * 设备借出列表
     * @param equipWarehouseRecordDto
     * @return
     */
    @Override
    public List<EquipWarehouseRecord> lendListPage(EquipWarehouseRecordDto equipWarehouseRecordDto) {


        if(equipWarehouseRecordDto.getEquipType()==null||equipWarehouseRecordDto.getStartTime()==null||equipWarehouseRecordDto.getEndTime()==null){
           return  new ArrayList<>();
        }
        //排除 选择时间段内已被占用的设备id
        List<Long> longs = baseMapper.equipIdList(equipWarehouseRecordDto.getStartTime(), equipWarehouseRecordDto.getEndTime());
        List<EquipWarehouseRecord> list1 = equipWarehouseRecordService.list(new QueryWrapper<EquipWarehouseRecord>()
                //类型
                .eq(equipWarehouseRecordDto.getEquipType()!=null, "equip_type", equipWarehouseRecordDto.getEquipType())
                //时间内可选设备
                .notIn(CollUtil.isNotEmpty(longs), "id", longs)
                //设备状态：1在用，4维修，6报废，7外借，8停用，10库存
                .in("status",1,10)
                //检定状态：0无需检定，1待送检，2送检中
                .eq("verification_status",0)
        );

        return list1;
    }

    @Override
    public List<EquipBorrowingRecord> listPage(EquipBorrowingRecord equipBorrowingRecord) {
        QueryWrapper<EquipBorrowingRecord> wrapper = new QueryWrapper<>();
        //借用公司
        wrapper.eq(equipBorrowingRecord.getCompanyId()!=null,"br.company_id",equipBorrowingRecord.getCompanyId());
        //类型 0:内部设备1：外部设备
        wrapper.eq("br.type",0);
        //设备名称
        wrapper.like(StrUtil.isNotBlank(equipBorrowingRecord.getGoodsName()),"wr.goods_name",equipBorrowingRecord.getGoodsName());
        //设备编号
        wrapper.like(StrUtil.isNotBlank(equipBorrowingRecord.getEquipCode()),"wr.equip_code",equipBorrowingRecord.getEquipCode());
        Date date = new Date();
        //状态(0待借出,1待归还,2已归还3撤销)
        if(equipBorrowingRecord.getStatus()!=null){
            if(equipBorrowingRecord.getStatus()==0){
                wrapper.ge("br.start_time",date);
                wrapper.ne("br.status",3);
            }else if (equipBorrowingRecord.getStatus()==1){
                wrapper.lt("br.start_time",date);
                wrapper.ge("br.actual_end_time",date);
                wrapper.ne("br.status",3);
            }else if(equipBorrowingRecord.getStatus()==2){
                wrapper.lt("br.actual_end_time",date);
                wrapper.ne("br.status",3);
            }else
            {
                wrapper.eq("br.status",3);
            }
        }

        List<EquipBorrowingRecord> equipBorrowingRecords = baseMapper.listPage(wrapper);
        dateCompare(equipBorrowingRecords,date);
        return equipBorrowingRecords;
    }

    @Override
    public List<EquipBorrowingRecord> externalListPage(EquipBorrowingRecord equipBorrowingRecord) {
        QueryWrapper<EquipBorrowingRecord> wrapper = new QueryWrapper<>();
        //借用公司
        wrapper.eq(equipBorrowingRecord.getCompanyId()!=null,"br.company_id",equipBorrowingRecord.getCompanyId());
        //类型 0:内部设备1：外部设备
        wrapper.eq("br.type",1);
        //设备名称
        wrapper.like(StrUtil.isNotBlank(equipBorrowingRecord.getGoodsName()),"wr.goods_name",equipBorrowingRecord.getGoodsName());
        //设备编号
        wrapper.like(StrUtil.isNotBlank(equipBorrowingRecord.getEquipCode()),"wr.equip_code",equipBorrowingRecord.getEquipCode());
        //隶属公司
        wrapper.like(StrUtil.isNotBlank(equipBorrowingRecord.getCompanyName()),"wr.company_name",equipBorrowingRecord.getCompanyName());
        Date date = new Date();
        //状态(0待借出,1待归还,2已归还3撤销)
        if(equipBorrowingRecord.getStatus()!=null){
            if(equipBorrowingRecord.getStatus()==0){
                wrapper.ge("br.start_time",date);
                wrapper.ne("br.status",3);
            }else if (equipBorrowingRecord.getStatus()==1){
                wrapper.lt("br.start_time",date);
                wrapper.ge("br.actual_end_time",date);
                wrapper.ne("br.status",3);
            }else if(equipBorrowingRecord.getStatus()==2){
                wrapper.lt("br.actual_end_time",date);
                wrapper.ne("br.status",3);
            }else
            {
                wrapper.eq("br.status",3);
            }
        }

        List<EquipBorrowingRecord> equipBorrowingRecords = baseMapper.externalListPage(wrapper);
        dateCompare(equipBorrowingRecords,date);

        return equipBorrowingRecords;
    }
    //状态赋值
    private void dateCompare(List<EquipBorrowingRecord> equipBorrowingRecords,Date date){

        if(CollUtil.isNotEmpty(equipBorrowingRecords)){
            for (EquipBorrowingRecord equipBorrowingRecord1:equipBorrowingRecords
            ) {
                if(equipBorrowingRecord1.getStatus()==null||equipBorrowingRecord1.getStatus()!=3){
                    Date startTime = equipBorrowingRecord1.getStartTime();
                    Date actualEndTime = equipBorrowingRecord1.getActualEndTime();
                    //0相等，<0 之前，>0之后
                    int i = date.compareTo(startTime);
                    int i2 = date.compareTo(actualEndTime);
                    //待借入  当前时间在开始时间之前
                    if(i<0){
                        equipBorrowingRecord1.setStatus(0);
//                    }else if(startTime<=date&&actualEndTime>actualEndTime){//借用中 当前时间再开始之后 and 当前时间在结束时间之前
                    }else if(i>=0&&i2<0){//借用中
                        equipBorrowingRecord1.setStatus(1);
                    }else {//已归还
                        equipBorrowingRecord1.setStatus(2);
                    }
                }
            }
        }
    }
}
