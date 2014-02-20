package org.metaservice.manager.shell.commands;

import com.bethecoder.ascii_table.ASCIITable;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.RunEntry;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ilo on 20.02.14.
 */
@CommandDefinition(name = "ps",description = "show running instances")
public class PsCommand  extends AbstractManagerCommand{
    public PsCommand(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        ArrayList<String[]> data = new ArrayList<>();
        for(RunEntry runEntry : manager.getRunManager().getRunEntries()){
            LOGGER.error(runEntry.getName());
            data.add(new String[]{
                    String.valueOf(runEntry.getMpid()),
                    runEntry.getName(),
                    runEntry.getStartTime().toString(),
                    (runEntry.getStatus() == RunEntry.Status.FINISHED)?"finished("+String.valueOf(runEntry.getExitValue())+")":"running",
                    String.valueOf(runEntry.getStdout())}
            );
        }
        String[] header = new String[]{"mPID","name","starttime","status","Logfile"};

        System.out.println(ASCIITable.getInstance().getTable(header,data.toArray(new String[data.size()][])));
        return CommandResult.SUCCESS;
    }
}
