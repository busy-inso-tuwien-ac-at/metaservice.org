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

package org.metaservice.manager.shell.completer;

import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.manager.RunEntry;

import java.util.List;

/**
 * Created by ilo on 21.02.14.
 */
public class MPidCompleter  extends AbstractManagerCompleter{
    @Override
    public void complete(ManagerCompleteInvocation managerCompleteInvocation) {
        List<RunEntry> runEntryList  = managerCompleteInvocation.getManager().getRunManager().getRunEntries();
        for(RunEntry runEntry : runEntryList){
            String pidString = Integer.toString(runEntry.getMpid());
            if(pidString.startsWith(managerCompleteInvocation.getGivenCompleteValue())){
                managerCompleteInvocation.addCompleterValue(pidString);
            }
        }
    }
}
