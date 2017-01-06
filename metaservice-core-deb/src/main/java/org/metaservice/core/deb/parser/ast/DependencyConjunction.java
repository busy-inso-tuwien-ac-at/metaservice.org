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

package org.metaservice.core.deb.parser.ast;

import org.jsoup.helper.StringUtil;
import org.parboiled.errors.ParserRuntimeException;

public class DependencyConjunction extends SuperNode {
    @Override
    public void addChild(int i, SuperNode superNode) {
        if(! (superNode instanceof DependencyDisjunction)){
            throw new ParserRuntimeException();
        }
        DependencyDisjunction x = (DependencyDisjunction) superNode;
        if(x.getChildren().size() == 1){
            super.addChild(i, x.getChildren().get(0));
        } else if(x.getChildren().size() > 1){
            super.addChild(i, superNode);
        }
    }

    @Override
    public String toString() {
        return StringUtil.join(this.getChildren(), " , ");
    }
}
