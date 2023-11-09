package com.anliantest.project.service.impl;

import com.anliantest.common.core.utils.StringUtils;
import com.anliantest.project.entity.Chinas;
import com.anliantest.project.mapper.ChinasMapper;
import com.anliantest.project.service.ChinasService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gy
 * @date 2023-08-24 15:58
 */
@Service
public class ChinasServiceImpl extends ServiceImpl<ChinasMapper, Chinas> implements ChinasService {

    @Autowired
    private ChinasMapper chinasMapper;

    /**
     * 省市区对接
     */
    @Override
    public List<Chinas> getRegions() {

        // 全部省数据
        List<Chinas> chinas = chinasMapper.selectList(new QueryWrapper<Chinas>()
                .eq("region_parent_id", "-1")
                .eq("region_level", "1"));

        List<Long> pid = new ArrayList<>();
        chinas.forEach(item -> {
            pid.add(item.getRegionId());
        });
        //全部市数据
        List<Chinas> json1 = chinasMapper.selectList(new QueryWrapper<Chinas>()
                .eq("region_level", "2")
                .in("region_parent_id", pid));
        Map<Long, List<Chinas>> cdv = json1.stream().collect(Collectors.groupingBy(Chinas::getRegionParentId));

        chinas.stream().forEach(a -> a.setCityList(cdv.get(a.getRegionId())));

        List<Long> pid1 = new ArrayList<>();
        json1.forEach(item -> {
            pid1.add(item.getRegionId());
        });
        //全部县 区数据
        List<Chinas> json2 = chinasMapper.selectList(new QueryWrapper<Chinas>()
                .eq("region_level", "3")
                .in("region_parent_id", pid1));
        Map<Long, List<Chinas>> cdv2 = json2.stream().collect(Collectors.groupingBy(Chinas::getRegionParentId));
        chinas.stream().filter(a -> a.getCityList() != null).forEach(a -> a.getCityList().stream()
                .forEach(i -> i.setAreaList(cdv2.get(i.getRegionId()))));

        return chinas;
    }

    /**
     * 导入省市区
     *
     * @param chinasList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importRegions(List<Chinas> chinasList) {
        List<Chinas> chinas1List1 = chinasList.stream().sorted(Comparator.comparing(Chinas::getRegionCode)).collect(Collectors.toList());

        List<Chinas> cityList = new ArrayList<>();
        List<Chinas> areaList = new ArrayList<>();
        for (Chinas chinas : chinas1List1) {
            String level = chinas.getLevel();
            if (StringUtils.isNotBlank(level)) {
                chinas.setRegionLevel(1);
                chinas.setRegionParentId(-1L);
            }
        }
        //保存省数据，得id
        boolean b = this.saveBatch(chinas1List1);

        for (Chinas chinas : chinas1List1) {
            List<Chinas> citys = chinas.getDistricts();
            for (Chinas city : citys) {
                String level = chinas.getLevel();
                if (StringUtils.isNotBlank(level)) {
                    city.setRegionLevel(2);
                    city.setRegionParentId(chinas.getRegionId());
                }
                cityList.add(city);
            }
        }
        List<Chinas> cityList1 = cityList.stream().sorted(Comparator.comparing(Chinas::getRegionCode)).collect(Collectors.toList());
        //保存市数据，得id
        boolean b1 = this.saveBatch(cityList1);

        for (Chinas city : cityList1) {
            List<Chinas> areas = city.getDistricts();
            for (Chinas area : areas) {
                String level = area.getLevel();
                if (StringUtils.isNotBlank(level)) {
                    area.setRegionLevel(3);
                    area.setRegionParentId(city.getRegionId());
                }
                areaList.add(area);
            }
        }
        List<Chinas> areaList1 = areaList.stream().sorted(Comparator.comparing(Chinas::getRegionCode)).collect(Collectors.toList());
        //保存县区数据
        boolean b2 = this.saveBatch(areaList1);

        return b && b1 && b2;
    }


//    @Override
//    public Boolean import1() {
//        ArrayList<Chinas> list = new ArrayList<>();
////        boolean b1 = this.remove(new QueryWrapper<Chinas1>().ge("region_id", 1003880)
////                .le("region_id", 1004247));
//
//
//        List<Chinas2> list1 = chinasMapper.selectList(new QueryWrapper<Chinas2>().ge("region_parent_id", 1003245)
//                .le("region_parent_id", 1003266));
//
//
//        List<Chinas> list2 = this.list(new QueryWrapper<Chinas>().ge("region_id", 1003664)
//                .le("region_id", 1003685));
//
//        Map<Long,List<Chinas2> > map = list1.stream()
//                .collect(Collectors.groupingBy(Chinas2::getRegionParentId));
//
//
//        int j = 1003245;
//        for (int i = 1003664 ;i<=1003685; i++){
//            List<Chinas2> list3 = map.get((long)j);
//            for (Chinas2 chinas2 : list3) {
//                Chinas chinas = new Chinas();
//                chinas.setRegionName(chinas2.getRegionName());
//                chinas.setRegionParentId((long) i);
//                chinas.setRegionLevel(3);
//                list.add(chinas);
//            }
//            j++;
//        }
//
//
//
////        for (Chinas chinas : list1) {
////            Chinas1 chinas1 = new Chinas1();
////            chinas1.setRegionName(chinas.getRegionName());
////            chinas1.setRegionLevel(3);
////            list.add(chinas1);
////        }
//
//
//        boolean b = this.saveBatch(list);
//        return b ;
////        return true;
//    }
}
