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
