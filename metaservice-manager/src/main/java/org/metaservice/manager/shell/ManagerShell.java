package org.metaservice.manager.shell;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.metaservice.manager.injection.ManagerModule;
import org.metaservice.manager.maven.MavenGuiceModule;
import org.metaservice.manager.shell.commands.*;
import org.metaservice.manager.shell.completer.ManagerCompleteInvocation;
import org.metaservice.manager.shell.validator.ManagerValidationInvocation;

import org.jboss.aesh.cl.exception.CommandLineParserException;

import org.jboss.aesh.console.AeshConsole;
import org.jboss.aesh.console.AeshConsoleBuilder;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.command.completer.CompleterInvocation;
import org.jboss.aesh.console.command.completer.CompleterInvocationProvider;
import org.jboss.aesh.console.command.registry.AeshCommandRegistryBuilder;
import org.jboss.aesh.console.command.registry.CommandRegistry;
import org.jboss.aesh.console.command.validator.ValidatorInvocation;
import org.jboss.aesh.console.command.validator.ValidatorInvocationProvider;
import org.jboss.aesh.console.settings.Settings;
import org.jboss.aesh.console.settings.SettingsBuilder;
import org.jboss.aesh.terminal.*;
import org.metaservice.core.injection.MetaserviceModule;
import org.metaservice.manager.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
public class ManagerShell {

    public static void main(String[] args) throws CommandLineParserException {
        Injector injector = Guice.createInjector(
                new MetaserviceModule(),
                new ManagerModule(),
                new MavenGuiceModule());
        final Manager manager = injector.getInstance(Manager.class);

        SettingsBuilder builder = new SettingsBuilder().logging(true);
        builder.enableMan(true)
                .readInputrc(false);

        Settings settings = builder.create();
        final CommandRegistry registry = new AeshCommandRegistryBuilder()
                .command(new ExitCommand(manager))
                .command(new SaveCommand(manager))
                .command(new AddModuleCommand(manager))
                .command(new InstallModuleCommand(manager))
                .command(new UninstallModuleCommand(manager))
                .command(new ShowStatisticsCommand(manager))
                .command(new ListCommand(manager))
                .command(new RunCommand(manager))
                .command(new AddModuleMavenCommand(manager))
                .command(new UpdateMavenCommand(manager))
                .command(new RemoveModuleCommand(manager))
                .command(new PsCommand(manager))
                .command(new TailCommand(manager))
                .command(new KillCommand(manager))
                .create();


        AeshConsole aeshConsole = new AeshConsoleBuilder()
                .commandRegistry(registry)
                .settings(settings)
                .prompt(new Prompt(getPromptString()))
                .completerInvocationProvider(new CompleterInvocationProvider() {
                    @Override
                    public CompleterInvocation enhanceCompleterInvocation(CompleterInvocation completerInvocation) {
                        return new ManagerCompleteInvocation(completerInvocation.getAeshContext(),completerInvocation.getGivenCompleteValue(),completerInvocation.getCommand(),manager);
                    }
                })
                .validatorInvocationProvider(new ValidatorInvocationProvider<ManagerValidationInvocation>() {
                    @Override
                    public ManagerValidationInvocation enhanceValidatorInvocation(ValidatorInvocation validatorInvocation) {
                        return new ManagerValidationInvocation(manager, validatorInvocation.getValue());
                    }
                })
                .create();
        aeshConsole.start();
    }

    private static List<TerminalCharacter> getPromptString() {
        List<TerminalCharacter> chars = new ArrayList<>();
        chars.add(new TerminalCharacter('[', new TerminalColor(Color.WHITE, Color.DEFAULT)));
        for(char c : "metaservice".toCharArray()){
            chars.add(new TerminalCharacter(c, new TerminalColor(Color.GREEN, Color.DEFAULT)));
        }
        chars.add(new TerminalCharacter(']', new TerminalColor(Color.WHITE, Color.DEFAULT)));
        chars.add(new TerminalCharacter('$', new TerminalColor(Color.GREEN, Color.DEFAULT)));
        chars.add(new TerminalCharacter(' ', new TerminalColor(Color.DEFAULT, Color.DEFAULT)));
        return chars;
    }


}
