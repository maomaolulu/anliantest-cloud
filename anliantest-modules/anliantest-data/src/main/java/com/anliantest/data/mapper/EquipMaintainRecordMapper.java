package com.anliantest.data.mapper;

import com.anliantest.data.domain.vo.EquipMaintainVo;
import com.anliantest.data.domain.vo.PeriodCheckVo;
import com.anliantest.data.entity.EquipMaintainRecord;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-08-07 10:53
 */
@Mapper
public interface EquipMaintainRecordMapper extends BaseMapper<EquipMaintainRecord> {

    /**
     * 设备维修-列表
     * @param wrapper 查询条件
     * @return list
     */
    @Select("select emr.id, ewr.id as equip_id, ewr.equip_code, ewr.goods_name, ewr.model, emr.repair_status, ewr.company_id, ewr.charge_dept_id, DATE_FORMAT( emr.breakdown_time ,'%Y-%m-%d %H:%i') as breakdown_time, DATE_FORMAT( emr.complete_time ,'%Y-%m-%d %H:%i') as complete_time, emr.remark, emr.repair_cost\n" +
            "from equip_maintain_record as emr\n" +
            "left join equip_warehouse_record as ewr\n" +
            "on emr.equip_id = ewr.id\n" +
            "${ew.customSqlSegment} ")
    List<EquipMaintainVo> selectEquipMaintainList(@Param(Constants.WRAPPER) QueryWrapper<EquipMaintainVo> wrapper);

    /**
     * 设备维修-更新
     * @param equipId 设备id
     * @param breakdownTime 故障时间
     * @param updateBy 更新者
     * @param updateTime 更新时间
     * @param id 主键id
     * @return row
     */
    @Update("update equip_maintain_record set equip_id = #{equipId}, breakdown_time = #{breakdownTime}, update_by = #{updateBy}, update_time = #{updateTime} where id = #{id} ;")
    int updateEquipMaintainRecordById(@Param("equipId") Long equipId, @Param("breakdownTime") Date breakdownTime, @Param("updateBy") String updateBy, @Param("updateTime") Date updateTime, @Param("id") Long id);

    /**
     * 设备维修-详情
     * @param id 维修记录id
     * @return 结果
     */
    @Select("select ewr.equip_code, ewr.goods_name, ewr.model, ewr.charge_dept_id, emr.breakdown_time, emr.complete_time, emr.remark, emr.repair_cost\n" +
            "from equip_maintain_record as emr\n" +
            "left join equip_warehouse_record as ewr\n" +
            "on emr.equip_id = ewr.id\n" +
            "where emr.id = #{id} ;")
    EquipMaintainVo selectInfoById(@Param("id") Long id);

    /**
     * 维修记录-删除
     * @param id 维修记录id
     * @param updateBy 更新者
     * @param updateTime 更新时间
     * @return row
     */
    @Update("update equip_maintain_record set del_flag = 1, update_by = #{updateBy}, update_time = #{updateTime} where id = #{id} ")
    int deleteRepairRecordById(@Param("id") Long id, @Param("updateBy") String updateBy, @Param("updateTime") Date updateTime);

    /**
     * 期间核查-列表
     * @param wrapper 查询条件
     * @return 结果
     */
    @Select("select evr.equip_id, ewr.equip_code, ewr.goods_name, ewr.model, ewr.verify_state, ewr.charge_dept_id, evr.useful_time as expiration_date, ewr.verify_date\n" +
            "from equip_verification_record as evr\n" +
            "left join equip_warehouse_record as ewr\n" +
            "on evr.equip_id = ewr.id\n" +
            "${ew.customSqlSegment} and evr.useful_time = ewr.useful_time\n" +
            "and ADDDATE(DATE(evr.verification_completion_time), INTERVAL FLOOR(DATEDIFF(evr.useful_time, DATE(evr.verification_completion_time)) / 2 - 10) DAY) >= CURDATE() ")
    List<PeriodCheckVo> selectPeriodCheckList(@Param(Constants.WRAPPER) QueryWrapper<PeriodCheckVo> wrapper);

}
