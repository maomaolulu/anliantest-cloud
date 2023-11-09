package com.anliantest.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2023/7/3 9:31
 * @Author maoly
 **/
@Slf4j
@Component("testTask")
public class TestTask {

    public void ryNoParams() {
        log.info("测试一下");
    }
}
