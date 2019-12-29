/**
 * Copyright 2014-2019  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.webank.webase.node.mgr.situation;

import com.alibaba.fastjson.JSON;
import com.webank.webase.node.mgr.base.code.ConstantCode;
import com.webank.webase.node.mgr.base.exception.NodeMgrException;
import com.webank.webase.node.mgr.base.properties.ConstantProperties;
import com.webank.webase.node.mgr.base.tools.NodeMgrTools;
import com.webank.webase.node.mgr.front.FrontService;
import com.webank.webase.node.mgr.front.entity.TbFront;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.webank.webase.node.mgr.frontinterface.FrontRestTools.URI_SITUATION;

@Log4j2
@Service
public class SituationService {

    @Autowired
    private ConstantProperties cproperties;
    @Autowired
    private FrontService frontService;
    @Autowired
    private RestTemplate genericRestTemplate;

    /**
     * get situation info.
     */
    public Object getLatestSituationInfo(Integer frontId, int groupId) {
        log.debug(
            "start getLatestSituationInfo.  frontId:{} groupId:{}", frontId, groupId);

        // request param to str
        List<Object> valueList = Arrays
            .asList(groupId);
        List<String> nameList = Arrays
            .asList("groupId");

        String situationUrlParam = NodeMgrTools.convertUrlParam(nameList, valueList);

        // query by front Id
        TbFront tbFront = frontService.getById(frontId);
        if (tbFront == null) {
            throw new NodeMgrException(ConstantCode.INVALID_FRONT_ID);
        }

        // request url
        String url = String
            .format(cproperties.getFrontUrl(), tbFront.getFrontIp(), tbFront.getFrontPort(),
                    URI_SITUATION);
//        url = url + "?" + situationUrlParam;
        log.info("getSituationMonitorInfo request url:{}", url);

        Object rspObj = genericRestTemplate.getForObject(url, Object.class);
        log.debug("end getSituationMonitorInfo. rspObj:{}", JSON.toJSONString(rspObj));
        return rspObj;
    }
}
