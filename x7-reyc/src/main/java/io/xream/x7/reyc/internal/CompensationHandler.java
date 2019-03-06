/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.xream.x7.reyc.internal;


import io.xream.x7.reyc.LogBean;
import io.xream.x7.reyc.ReyCompensationService;
import x7.config.SpringHelper;

public class CompensationHandler {

    private static boolean isNotNeed = false;
    private static ReyCompensationService service = null;

    protected static void handle(LogBean logBean) {

        if (isNotNeed == false) {
            if (service == null){
                try {
                    service = SpringHelper.getObject(ReyCompensationService.class);
                }catch (Exception e){

                }
                if (service == null) {
                    isNotNeed = true;
                    return;
                }
            }
            service.onConnectFailedOrBusy(logBean);
        }
    }

}
