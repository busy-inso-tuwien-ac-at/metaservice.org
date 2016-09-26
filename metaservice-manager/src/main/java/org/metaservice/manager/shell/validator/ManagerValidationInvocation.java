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

package org.metaservice.manager.shell.validator;

import org.jboss.aesh.console.command.validator.ValidatorInvocation;
import org.metaservice.manager.Manager;

/**
 * Created by ilo on 10.02.14.
 */
public class ManagerValidationInvocation implements ValidatorInvocation {
    private final Manager manager;
    private final Object value;
    public ManagerValidationInvocation(Manager manager, Object value) {
        this.manager = manager;
        this.value = value;
    }

    public Manager getManager() {
        return manager;
    }

    @Override
    public Object getValue() {
        return value;
    }


}
