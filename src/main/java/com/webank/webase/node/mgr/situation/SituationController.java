/**
 * Copyright 2014-2019  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webase.node.mgr.situation;

import com.alibaba.fastjson.JSON;
import com.webank.webase.node.mgr.base.code.ConstantCode;
import com.webank.webase.node.mgr.base.entity.BaseResponse;
import com.webank.webase.node.mgr.base.exception.NodeMgrException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Log4j2
@RestController
@RequestMapping(value = "situation")
public class SituationController {

    @Autowired
    private SituationService situationService;

    @GetMapping(value = "/getLatestSituationInfo/{frontId}")
    public BaseResponse getLatestSituationInfo(@PathVariable("frontId") Integer frontId,
        @RequestParam(required = false, defaultValue = "1") int groupId)
        throws NodeMgrException {
        Instant startTime = Instant.now();
        BaseResponse response = new BaseResponse(ConstantCode.SUCCESS);
        log.info(
            "start getLatestSituationInfo mapping. startTime:{} frontId:{} groupId:{}",
                startTime.toEpochMilli(), frontId, groupId);
        Object rspObj = situationService
            .getLatestSituationInfo(frontId, groupId);

        response.setData(rspObj);
        log.info("end getSituationInfo. endTime:{} response:{}",
            Duration.between(startTime, Instant.now()).toMillis(), JSON.toJSONString(response));

        return response;
    }
}
