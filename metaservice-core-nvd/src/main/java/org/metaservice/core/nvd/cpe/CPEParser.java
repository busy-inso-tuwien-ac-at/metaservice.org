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

package org.metaservice.core.nvd.cpe;

import org.metaservice.api.archive.ArchiveAddress;
import org.metaservice.api.parser.Parser;
import org.metaservice.nist.cpe.jaxb.ItemType;
import org.metaservice.nist.cpe.jaxb.ListType;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.Reader;
import java.util.List;


/**
 * Created by ilo on 25.02.14.
 */
public class CPEParser implements Parser<ItemType> {

    @Inject
    public CPEParser(){

    }

    @Override
    public List<ItemType> parse(Reader reader, ArchiveAddress archiveParameters) {
        ListType listType = JAXB.unmarshal(reader, ListType.class);
        return listType.getCpeItem();
    }
}
