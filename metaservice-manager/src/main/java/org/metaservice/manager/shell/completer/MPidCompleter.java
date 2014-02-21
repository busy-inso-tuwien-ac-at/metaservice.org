package org.metaservice.manager.shell.completer;

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
