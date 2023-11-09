package com.anliantest.system.service.impl;

import com.anliantest.common.core.enums.Numbers;
import com.anliantest.common.core.enums.RegionLevel;
import com.anliantest.common.core.utils.ObjectUtils;
import com.anliantest.common.core.utils.uuid.UUID;
import com.anliantest.system.api.model.*;
import com.anliantest.system.constants.ChineseAddressConstant;
import com.anliantest.system.domain.ChineseAddress;
import com.anliantest.system.domain.vo.ChineseAddressVo;
import com.anliantest.system.mapper.ChineseAddressMapper;
import com.anliantest.system.service.IChineseAddressService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Date 2023/8/28 9:12
 * @Author maoly
 **/
@Service
@Slf4j
public class ChineseAddressServiceImpl implements IChineseAddressService {

    @Autowired
    private ChineseAddressMapper chineseAddressMapper;


    @Override
    @Transient
    public void saveChineseAddress(AmapData amapData) {
        Country country = amapData.getDistricts().get(Numbers.ZERO.ordinal());
        List<ChineseAddress> chineseAddressList = new ArrayList<>();
        if(country != null){
            List<Province> provinceList = country.getDistricts();
            if(CollectionUtils.isNotEmpty(provinceList)){
                String province_id = null;
                String xg_id = null;
                String am_id = null;
                for(Province province : provinceList){
                    if(province != null){
                        province_id = UUID.randomUUID().toString();
                        ChineseAddress chineseAddressProvince = ChineseAddress.builder()
                                .id(province_id)
                                .regionCode(province.getAdcode())
                                .regionName(province.getName())
                                .center(province.getCenter())
                                .regionLevel(RegionLevel.province.ordinal()).build();
                        chineseAddressList.add(chineseAddressProvince);
                        if(province.getAdcode().equals(ChineseAddressConstant.XG_AD_CODE)){
                            xg_id = UUID.randomUUID().toString();
                            ChineseAddress chineseAddressCityXg = ChineseAddress.builder()
                                    .id(xg_id)
                                    .regionParentId(province_id)
                                    .regionCode(ChineseAddressConstant.XG_CITY_AD_CODE)
                                    .regionName(province.getName())
                                    .center(province.getCenter())
                                    .regionLevel(RegionLevel.city.ordinal()).build();
                            chineseAddressList.add(chineseAddressCityXg);
                        }
                        if(province.getAdcode().equals(ChineseAddressConstant.AM_AD_CODE)){
                            am_id = UUID.randomUUID().toString();
                            ChineseAddress chineseAddressCityAm = ChineseAddress.builder()
                                    .id(am_id)
                                    .regionParentId(province_id)
                                    .regionCode(ChineseAddressConstant.AM_CITY_AD_CODE)
                                    .regionName(province.getName())
                                    .center(province.getCenter())
                                    .regionLevel(RegionLevel.city.ordinal()).build();
                            chineseAddressList.add(chineseAddressCityAm);
                        }
                        List<City> cityList = province.getDistricts();
                        if(CollectionUtils.isNotEmpty(cityList)){
                            String city_id = null;
                            String xg_city_id = null;
                            String am_city_id = null;
                            for(City city : cityList){
                                if(city != null){
                                    if(province.getAdcode().equals(ChineseAddressConstant.XG_AD_CODE)){
                                        xg_city_id = UUID.randomUUID().toString();
                                        ChineseAddress chineseAddressCity = ChineseAddress.builder()
                                                .id(xg_city_id)
                                                .regionCode(city.getAdcode())
                                                .regionParentId(xg_id)
                                                .regionName(city.getName())
                                                .center(city.getCenter())
                                                .regionLevel(RegionLevel.district.ordinal()).build();
                                        chineseAddressList.add(chineseAddressCity);
                                    }else if(province.getAdcode().equals(ChineseAddressConstant.AM_AD_CODE)){
                                        am_city_id = UUID.randomUUID().toString();
                                        ChineseAddress chineseAddressCity = ChineseAddress.builder()
                                                .id(am_city_id)
                                                .regionCode(city.getAdcode())
                                                .regionParentId(am_id)
                                                .regionName(city.getName())
                                                .center(city.getCenter())
                                                .regionLevel(RegionLevel.district.ordinal()).build();
                                        chineseAddressList.add(chineseAddressCity);
                                    }else{
                                        city_id = UUID.randomUUID().toString();
                                        ChineseAddress chineseAddressCity = ChineseAddress.builder()
                                                .id(city_id)
                                                .regionCode(city.getAdcode())
                                                .regionParentId(province_id)
                                                .regionName(city.getName())
                                                .center(city.getCenter())
                                                .regionLevel(RegionLevel.city.ordinal()).build();
                                        chineseAddressList.add(chineseAddressCity);
                                        List<District> districts = city.getDistricts();
                                        if(CollectionUtils.isNotEmpty(districts)){
                                            String district_id = null;
                                            for(District district : districts){
                                                if(district != null){
                                                    district_id =  UUID.randomUUID().toString();
                                                    ChineseAddress chineseAddressDistrict = ChineseAddress.builder()
                                                            .id(district_id)
                                                            .regionCode(district.getAdcode())
                                                            .regionParentId(city_id)
                                                            .regionName(district.getName())
                                                            .center(district.getCenter())
                                                            .regionLevel(RegionLevel.district.ordinal()).build();
                                                    chineseAddressList.add(chineseAddressDistrict);
                                                    List<Street> streetList = district.getDistricts();
                                                    if(CollectionUtils.isNotEmpty(streetList)){
                                                        String street_id = null;
                                                        for(Street street : streetList){
                                                            street_id = UUID.randomUUID().toString();
                                                            ChineseAddress chineseAddressStreet = ChineseAddress.builder()
                                                                    .id(street_id)
                                                                    .regionCode(street.getAdcode())
                                                                    .regionParentId(district_id)
                                                                    .regionName(street.getName())
                                                                    .center(street.getCenter())
                                                                    .regionLevel(RegionLevel.street.ordinal()).build();
                                                            chineseAddressList.add(chineseAddressStreet);
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        if(CollectionUtils.isNotEmpty(chineseAddressList)){
            try {
                chineseAddressMapper.deleteChineseAddress();
                int count = chineseAddressMapper.selectCount();
                if(count == Numbers.ZERO.ordinal()){
                    List<List<ChineseAddress>> partition = Lists.partition(chineseAddressList, ChineseAddressConstant.BATCH_SIZE);
                    for(List<ChineseAddress> chineseAddresses : partition){
                        chineseAddressMapper.batchSaveChineseAddress(chineseAddresses);
                    }
                }
            }catch (Exception e){
                log.error("保存全国地图数据失败，e:{}",e.getMessage());
            }

        }

    }

    @Override
    public List<ChineseAddressVo> getIdAndName(String regionParentId) {
        return ObjectUtils.transformArrObj(chineseAddressMapper.getRegions(regionParentId), ChineseAddressVo.class);
    }

    @Override
    public List<ChineseAddress> getRegions(String regionParentId) {
        return chineseAddressMapper.getRegions(regionParentId);
    }

}
