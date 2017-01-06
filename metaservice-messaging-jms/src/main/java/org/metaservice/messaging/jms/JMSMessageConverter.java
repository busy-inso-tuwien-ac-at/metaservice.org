/*
 * Copyright 2015 Nikola Ilo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaservice.messaging.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilo on 18.02.14.
 */
public class JMSMessageConverter {

    public static Map<String,Object> getMap(MapMessage mapMessage){
        HashMap<String,Object> hashMap = new HashMap<>();
        try {
            Enumeration keys = mapMessage.getMapNames();
            while (keys.hasMoreElements()){
                String key = (String) keys.nextElement();
                hashMap.put(key,mapMessage.getObject(key));
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
}
