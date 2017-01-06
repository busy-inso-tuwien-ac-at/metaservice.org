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

package org.metaservice.api.sparql.nodes;

import org.openrdf.model.Value;

/**
* Created by ilo on 05.03.14.
*/
public class QuadPattern implements Pattern {
    private final Value s;
    private final Value p;
    private final Value o;
    private final Value c;

    public QuadPattern(Value s, Value p, Value o, Value c) {
        this.s = s;
        this.p = p;
        this.o = o;
        this.c = c;

    }

    public Value getS() {
        return s;
    }

    public Value getP() {
        return p;
    }

    public Value getO() {
        return o;
    }

    public Value getC() {
        return c;
    }

}
