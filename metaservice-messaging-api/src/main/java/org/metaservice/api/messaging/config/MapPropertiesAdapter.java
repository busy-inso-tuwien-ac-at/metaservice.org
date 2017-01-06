/*
 * Copyright 2015 Nikola Ilo
 * Research Group for Industrial Software, Vienna University of Technology, Austria
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

package org.metaservice.api.messaging.config;

import java.util.*;
import java.util.Map.Entry;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapPropertiesAdapter extends XmlAdapter<MapPropertiesAdapter.AdaptedProperties, Map<String, String>>{

    public static class AdaptedProperties {
        public List<Property> property = new ArrayList<>();
    }

    public static class Property {
        @XmlAttribute
        public String name;

        @XmlValue
        public String value;
    }

    @Override
    public Map<String, String> unmarshal(AdaptedProperties adaptedProperties) throws Exception {
        if(null == adaptedProperties) {
            return null;
        }
        Map<String, String> map = new HashMap<>(adaptedProperties.property.size());
        for(Property property : adaptedProperties.property) {
            map.put(property.name, property.value);
        }
        return map;
    }

    @Override
    public AdaptedProperties marshal(Map<String, String> map) throws Exception {
        if(null == map) {
            return null;
        }
        AdaptedProperties adaptedProperties = new AdaptedProperties();
        for(Entry<String,String> entry : map.entrySet()) {
            Property property = new Property();
            property.name = entry.getKey();
            property.value = entry.getValue();
            adaptedProperties.property.add(property);
        }
        return adaptedProperties;
    }

}