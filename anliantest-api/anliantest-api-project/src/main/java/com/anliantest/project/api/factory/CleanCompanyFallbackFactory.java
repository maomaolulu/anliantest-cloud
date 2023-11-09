package com.anliantest.project.api.factory;

import com.anliantest.project.api.CleanCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 客户服务降级处理
 *
 * @author ruoyi
 */
@Component
public class CleanCompanyFallbackFactory implements FallbackFactory<CleanCompanyService>
{
    private static final Logger log = LoggerFactory.getLogger(CleanCompanyFallbackFactory.class);

    @Override
    public CleanCompanyService create(Throwable throwable)
    {
        log.error("客户服务调用失败:{}", throwable.getMessage());
        return new CleanCompanyService()
        {
            @Override
            public void cleanCompany(String source) {
                log.error("客户清理服务调用失败:{}", throwable.getMessage());
            }

        };
    }
}
