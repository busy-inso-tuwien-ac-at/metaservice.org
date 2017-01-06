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

import org.jboss.aesh.cl.completer.CompleterData;
import org.jboss.aesh.console.AeshContext;
import org.jboss.aesh.console.command.Command;
import org.metaservice.manager.Manager;

/**
 * Created by ilo on 10.02.14.
 */
public class ManagerCompleteInvocation extends CompleterData {
    private final Manager manager;

    public ManagerCompleteInvocation(AeshContext aeshContext, String completeValue, Command command, Manager manager) {
        super(aeshContext, completeValue, command);
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }
}
