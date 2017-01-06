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

package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.console.man.AeshFileDisplayer;
import org.jboss.aesh.console.man.FileParser;
import org.jboss.aesh.console.man.TerminalPage;
import org.jboss.aesh.extensions.page.SimpleFileParser;
import org.jboss.aesh.util.ANSI;
import org.metaservice.manager.Manager;
import org.metaservice.manager.RunEntry;
import org.metaservice.manager.shell.completer.MPidCompleter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ilo on 20.02.14.
 */
@CommandDefinition(name="tail",description = "description")
public class TailCommand extends AeshFileDisplayer {

    @Option(name="mpid",shortName = 'p',required = true,completer = MPidCompleter.class)
    private int mpid;


    @Arguments
    List<File> arguments;

    private SimpleFileParser loader;
    private final Manager manager;

    public TailCommand(Manager manager) {
        super();
        this.manager = manager;
        //todo fix this such that it really is tail and not less ;-)
        // take a look at http://commons.apache.org/proper/commons-io/javadocs/api-release/org/apache/commons/io/input/TailerListener.html

    }

    public void setFile(File file) throws IOException {
        loader.setFile(file);
    }

    public void setFile(String filename) throws IOException {
        loader.setFile(filename);
    }

    public void setInput(String input) throws IOException {
        loader.readPageAsString(input);
    }

    @Override
    public FileParser getFileParser() {
        return loader;
    }

    @Override
    public void displayBottom() throws IOException {
        if(getSearchStatus() == TerminalPage.Search.SEARCHING) {
            clearBottomLine();
            writeToConsole("/"+getSearchWord());
        }
        else if(getSearchStatus() == TerminalPage.Search.NOT_FOUND) {
            clearBottomLine();
            writeToConsole(ANSI.getInvertedBackground()+
                    "Pattern not found (press RETURN)"+
                    ANSI.defaultText());
        }
        else if(getSearchStatus() == TerminalPage.Search.RESULT) {
            writeToConsole(":");
        }
        else if(getSearchStatus() == TerminalPage.Search.NO_SEARCH) {
            if(isAtBottom())
                writeToConsole(ANSI.getInvertedBackground()+"(END)"+ANSI.defaultText());
            else
                writeToConsole(":");
        }
    }

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {
        try {
            loader = new SimpleFileParser();
            setCommandInvocation(commandInvocation);
            RunEntry runEntry = manager.getRunManager().getRunEntryByMPid(mpid);
            if(runEntry == null){
                System.out.println("Could not find process");
                return CommandResult.FAILURE;
            }
            if( runEntry.getProcess() == null)
            {
                System.out.println("Could not access process");
                return CommandResult.FAILURE;
            }
            if(runEntry.getStatus() == RunEntry.Status.STARTING){
                System.out.println("Can only view started");
                return CommandResult.FAILURE;
            }
            setFile(runEntry.getStdout());
            afterAttach();
            return CommandResult.SUCCESS;
        }catch (Exception t){
            LoggerFactory.getLogger(this.getClass()).error("Cannot tail ....", t);
            throw t;
        }

    }
}
