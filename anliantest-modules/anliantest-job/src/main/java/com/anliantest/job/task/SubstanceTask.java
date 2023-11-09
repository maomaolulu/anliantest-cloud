package com.anliantest.job.task;

import com.anliantest.common.core.constant.SecurityConstants;
import com.anliantest.common.core.domain.R;
import com.anliantest.data.api.RemoteSubstanceService;
import com.anliantest.system.api.RemoteUserService;
import com.anliantest.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author gy
 * @Description
 * @date 2023-07-03 18:38
 */
@Component("substanceTask")
public class SubstanceTask {

    @Autowired
    private RemoteSubstanceService remoteSubstanceService;

    /**
     * 定时刷新表中所有检测标准/法律法规状态
     */
    public void refeshStatus(){
        remoteSubstanceService.refreshStatus(SecurityConstants.INNER);
    }
}
