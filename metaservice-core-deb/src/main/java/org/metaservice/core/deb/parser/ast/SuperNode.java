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

package org.metaservice.core.deb.parser.ast;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.parboiled.trees.MutableTreeNodeImpl;

public class SuperNode extends MutableTreeNodeImpl<SuperNode> {

    @Nullable
    @Override
    public String toString() {
        return StringUtils.join(this.getChildren(), "\n");
    }
}
