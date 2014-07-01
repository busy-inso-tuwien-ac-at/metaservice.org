package org.metaservice.manager.shell.commands;

import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.metaservice.api.rdf.vocabulary.ADMSSW;
import org.metaservice.api.rdf.vocabulary.CPE;
import org.metaservice.api.rdf.vocabulary.CVE;
import org.metaservice.manager.Manager;
import org.metaservice.manager.ManagerException;

import java.io.IOException;

/**
 * Created by ilo on 22.02.14.
 */
@CommandDefinition(name = "test",description = "")
public class TestCommand extends AbstractManagerCommand{

    @Option(name="reponame")
    String repo;

    @Option(name="packages",hasValue = false)
    boolean packages;

    @Option(name="releases",hasValue = false)
    boolean releases;

    @Option(name="projects",hasValue = false)
    boolean projects;

    @Option(name="cves",hasValue = false)
    boolean cves;

    @Option(name="cpes",hasValue = false)
    boolean cpes;

    @Option(name="postprocess")
    String postProcess;

    public TestCommand(Manager manager) {
        super(manager);
    }

    @Override
    public CommandResult execute2(CommandInvocation commandInvocation) throws IOException, ManagerException {
        if(repo != null) {
            manager.loadAllDataFromArchive(repo);
        }
        if(packages){
            manager.postProcessAllByClass(ADMSSW.SOFTWARE_PACKAGE);
        }
        if(releases){
            manager.postProcessAllByClass(ADMSSW.SOFTWARE_RELEASE);
        }
        if(projects){
            manager.postProcessAllByClass(ADMSSW.SOFTWARE_PROJECT);
        }
        if(cpes){
            manager.postProcessAllByClass(CPE.CPE);
        }
        if(cves){
            manager.postProcessAllByClass(CVE.CVE);
        }
        if(postProcess != null){
            manager.postProcess(postProcess);
        }
        return CommandResult.SUCCESS;
    }
}
