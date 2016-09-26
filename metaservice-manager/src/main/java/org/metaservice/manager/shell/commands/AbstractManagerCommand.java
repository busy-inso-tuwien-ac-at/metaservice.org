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

package org.metaservice.manager.shell.commands;

import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.messaging.descriptors.DescriptorHelper;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ilo on 10.02.14.
 */
public abstract  class AbstractManagerCommand implements Command{
    protected final Manager manager;
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    protected final DescriptorHelper descriptorHelper;
    public AbstractManagerCommand(Manager manager) {
        this.manager = manager;
        descriptorHelper = manager.getDescriptorHelper();
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        try{
            return execute2(commandInvocation);
        }catch (ManagerException e){
            LOGGER.error("Could not execute Command",e);
            return CommandResult.FAILURE;
        }
        catch (Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstract  CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException;
}
