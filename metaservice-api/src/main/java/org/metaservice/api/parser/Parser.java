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

package org.metaservice.api.parser;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.archive.ArchiveParameters;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 05.12.13
 * Time: 23:55
 * To change this template use File | Settings | File Templates.
 */
public interface Parser<T> {
    public List<T> parse(Reader s, ArchiveAddress archiveParameters) throws ParserException;
}
