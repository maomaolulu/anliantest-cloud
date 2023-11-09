package com.anliantest.job.task;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.project.api.CleanCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gy
 * @date 2023-10-31 14:54
 */
@Component("CompanyTask")
public class CompanyTask {

    @Autowired
    private CleanCompanyService cleanCompanyService;

    public void cleanCompany(){
        cleanCompanyService.cleanCompany(SecurityConstants.INNER);
    }
}
