package org.metaservice.core.provider;

import org.metaservice.api.parser.Parser;

import javax.inject.Inject;
import java.util.HashMap;

/**
 * Created by ilo on 12.07.2014.
 */
public class ParserFactory<T> {
    @Inject
    public ParserFactory(){

    }
    private final HashMap<String,Parser<T>> map = new HashMap<>();

    public void addParser(String type, Parser<T> parser){
        map.put(type,parser);
    }

    public Parser<T> getParser(String type){
        return map.get(type);
    }
}
