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
