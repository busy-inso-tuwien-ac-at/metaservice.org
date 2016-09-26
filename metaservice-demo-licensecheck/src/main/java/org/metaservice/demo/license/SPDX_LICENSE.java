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

package org.metaservice.demo.license;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Created by ilo on 13.07.2014.
 */
public class SPDX_LICENSE {
    public static final String NS = "http://spdx.org/licenses/";

    public static final URI GPL_2_0;
    public static final URI LGPL_2_0;

    static {
        ValueFactory valueFactory = ValueFactoryImpl.getInstance();
        GPL_2_0 = valueFactory.createURI(NS,"GPL-2.0");
        LGPL_2_0 = valueFactory.createURI(NS,"LGPL-2.0");
    }
}
