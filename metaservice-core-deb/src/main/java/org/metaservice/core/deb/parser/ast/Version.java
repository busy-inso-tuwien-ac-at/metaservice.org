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

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ilo
 * Date: 15.10.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class Version extends SuperNode {
    public String epoch;
    public String upstreamVersion;
    public String debversion;

    public String relation = "=";

    @NotNull
    @Override
    public String toString() {
        boolean needParenthesis = (relation != null);
        if(epoch!=null || upstreamVersion!= null ||debversion!= null)
            return (needParenthesis?"(":"") + ((relation!= null)? relation + " ":"")+ ((epoch != null)?epoch + ':':"") + upstreamVersion + ((debversion!=null)?'-'+ debversion :"") +((needParenthesis)?')':"");
        else
            return "";
    }


    @NotNull
    public String toFileNameString(){
        if(epoch!=null || upstreamVersion!= null ||debversion!= null)
            return  ((epoch != null)?epoch + '.':"") + upstreamVersion + ((debversion!=null)?'-'+ debversion :"");
        else
            return "";
    }
}
