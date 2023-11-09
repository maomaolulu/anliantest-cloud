package com.anliantest.project.service.impl;

import com.anliantest.project.entity.CustomContacterEntity;
import com.anliantest.project.mapper.CustomContacterMapper;
import com.anliantest.project.service.CustomContacterService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author gy
 * @date 2023-08-21 13:36
 */
@Service
public class CustomContacterServiceImpl extends ServiceImpl<CustomContacterMapper, CustomContacterEntity> implements IService<CustomContacterEntity>, CustomContacterService {
}
