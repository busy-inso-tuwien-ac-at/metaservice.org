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

/**
* Created by ilo on 05.03.14.
*/
public class BinaryFunction<X, Y extends Term, T extends Term> implements BinaryTerm<X, Y, T> {
    private final String name;
    private final Y t1;
    private final T t2;

    public BinaryFunction(String name, Y t1, T t2) {
        this.name = name;
        this.t1 = t1;
        this.t2 = t2;
    }

    public String getName() {
        return name;
    }

    public Y getT1() {
        return t1;
    }

    public T getT2() {
        return t2;
    }
}
