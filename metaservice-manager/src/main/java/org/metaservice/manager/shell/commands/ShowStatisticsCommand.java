package org.metaservice.manager.shell.commands;


import com.bethecoder.ascii_table.ASCIITable;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.terminal.Key;
import org.jboss.aesh.terminal.Shell;
import org.metaservice.core.config.ManagerConfig;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@CommandDefinition(name = "stats", description = "show statistics")
public class ShowStatisticsCommand extends AbstractManagerCommand {
    public ShowStatisticsCommand(Manager manager) {
        super(manager);
    }

    @Option(name = "activemq",hasValue = false)
    boolean activemq;

    @Option(name = "rdf-statements",shortName = 's',hasValue = false)
    boolean statements;

    @Option(name="watch",shortName = 'w',hasValue = false)
    boolean watch;

    private final AtomicBoolean syncObject = new AtomicBoolean();

    @Override
    public CommandResult execute2(final CommandInvocation commandInvocation) throws IOException {
        if(watch){
            try{
            syncObject.set(watch);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (syncObject.get())
                            if(commandInvocation.getInput().getInputKey()== Key.q){
                                synchronized (syncObject){
                                    syncObject.set(false);
                                }
                            }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            do {
                commandInvocation.getShell().enableAlternateBuffer();
                draw(commandInvocation.getShell());
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (syncObject){
                    watch = syncObject.get();
                }
                commandInvocation.getShell().enableMainBuffer();
            }while (watch);

            }catch (Exception e){
                LOGGER.error("Exception",e);
            }
        }else{
            draw(commandInvocation.getShell());
        }

        return CommandResult.SUCCESS;
    }

    private void draw(Shell shell) {
        if(activemq){
            String[]   header = new String[]{"Queue","Count"};
            ArrayList<String[]> data = new ArrayList<>();
            Map<String,Map<String,Object>> stats = manager.getCurrentActiveMQStatisitics();
            for(Map<String,Object> entry: stats.values()){
                data.add(new String[]{
                        String.valueOf(entry.get("destinationName")),
                        String.format("%,d", (Long) entry.get("size"))
                });
            }
            asciiprint(header,data.toArray(new String[data.size()][]),shell);
        }
        if(statements){
            try {
                String[]   header = new String[]{"type","count"};
                ArrayList<String[]> data = new ArrayList<>();
                Map<String,Integer> result = manager.getStatementStatistics();
                for(Map.Entry<String,Integer> entry : result.entrySet()){
                    data.add(new String[]{entry.getKey(), String.format("%,d", entry.getValue())});
                }
                asciiprint(header, data.toArray(new String[data.size()][]),shell);
            } catch (ManagerException e) {
                e.printStackTrace();
            }


        }
    }


    private  static void asciiprint(String[] header, String[][] values,Shell shell){
        if(values.length> 0 ){
            assert values[0].length == header.length;
            shell.out().println(ASCIITable.getInstance().getTable(header, values));
        }
        else
            shell.out().println("nothing to show");
    }
}
