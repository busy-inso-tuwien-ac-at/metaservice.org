package org.metaservice.manager.shell.commands;

import com.bethecoder.ascii_table.ASCIITable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;
import org.metaservice.manager.RunEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ilo on 20.02.14.
 */
@CommandDefinition(name = "ps",description = "show running instances")
public class PsCommand  extends AbstractManagerCommand{
    public PsCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "all", shortName = 'a',hasValue = false)
    private boolean all;

    @Option(name = "overview", shortName = 'o',hasValue = false)
    private boolean overview;

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        ArrayList<String[]> data = new ArrayList<>();
        String[] header;
        if(all){
            for(RunEntry runEntry : manager.getRunManager().getRunEntries()){
                data.add(new String[]{
                        String.valueOf(runEntry.getMpid()),
                        runEntry.getName(),
                        runEntry.getMachine(),
                        runEntry.getStartTime().toString(),
                        (runEntry.getStatus() == RunEntry.Status.FINISHED)?"finished("+String.valueOf(runEntry.getExitValue())+")":"running",
                        String.valueOf(runEntry.getStdout()),
                        String.valueOf(runEntry.getStderr())
                }
                );
            }
            header = new String[]{"mPID","name","host","starttime","status","stdout","stderr"};
        }else if(overview){
            class AggregateData {
                ArrayList<String> mpids = new ArrayList<>();
                int finished =0;
                int running =0;
                int starting =0;
                int other = 0;
                String name;
            }

            HashMap<String,AggregateData> aggregateDataHashMap = new HashMap<>();

            for(RunEntry runEntry : manager.getRunManager().getRunEntries()){
                if(!aggregateDataHashMap.containsKey(runEntry.getName())){
                    AggregateData newAggregateData =  new AggregateData();
                    newAggregateData.name = runEntry.getName();
                    aggregateDataHashMap.put(runEntry.getName(),newAggregateData);
                }
                AggregateData t =aggregateDataHashMap.get(runEntry.getName());
                switch (runEntry.getStatus()){
                    case FINISHED:
                        t.finished++;
                        break;
                    case RUNNING:
                        t.running++;
                        break;
                    case STARTING:
                        t.starting++;
                        break;
                    default:
                        t.other++;
                        break;
                }
                t.mpids.add(String.valueOf(runEntry.getMpid()));
            }
            for(AggregateData entry : aggregateDataHashMap.values()){
                String[] lines =  WordUtils.wrap(StringUtils.join(entry.mpids, " "),30).split("\n");
                data.add(new String[]{
                        lines[0],
                        entry.name,
                        String.valueOf(entry.starting),
                        String.valueOf(entry.running),
                        String.valueOf(entry.finished),
                        String.valueOf(entry.other)
                });
                for(int i =1 ; i < lines.length; i++){
                    data.add(new String[]{lines[i],"","", "","",""});
                }

            }
            header = new String[]{"mPIDs","name","starting","running","finished","other"};
        }
        else{
            for(RunEntry runEntry : manager.getRunManager().getRunEntries()){
                data.add(new String[]{
                        String.valueOf(runEntry.getMpid()),
                        runEntry.getName(),
                        runEntry.getStartTime().toString(),
                        (runEntry.getStatus() == RunEntry.Status.FINISHED)?"FINISHED("+String.valueOf(runEntry.getExitValue())+")":runEntry.getStatus().toString()
                }
                );
            }
            header = new String[]{"mPID","name","starttime","status"};
        }
        if(data.size()> 0 ){
            assert data.get(0).length == header.length;
            System.out.println(ASCIITable.getInstance().getTable(header,data.toArray(new String[data.size()][])));
        }
        else
            System.out.println("nothing to show");
        return CommandResult.SUCCESS;
    }
}
