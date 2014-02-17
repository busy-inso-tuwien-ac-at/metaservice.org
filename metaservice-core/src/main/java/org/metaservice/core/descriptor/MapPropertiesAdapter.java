package org.metaservice.core.descriptor;

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