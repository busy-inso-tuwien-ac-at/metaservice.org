package org.metaservice.core.management.shell;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jboss.aesh.cl.CommandDefinition;

import org.jboss.aesh.cl.exception.CommandLineParserException;

import org.jboss.aesh.console.AeshConsole;
import org.jboss.aesh.console.AeshConsoleBuilder;
import org.jboss.aesh.console.Prompt;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.completer.CompleterInvocation;
import org.jboss.aesh.console.command.completer.CompleterInvocationProvider;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.console.command.registry.AeshCommandRegistryBuilder;
import org.jboss.aesh.console.command.registry.CommandRegistry;
import org.jboss.aesh.console.command.validator.ValidatorInvocation;
import org.jboss.aesh.console.command.validator.ValidatorInvocationProvider;
import org.jboss.aesh.console.settings.Settings;
import org.jboss.aesh.console.settings.SettingsBuilder;
import org.jboss.aesh.terminal.*;
import org.metaservice.core.injection.MetaserviceModule;
import org.metaservice.core.management.Manager;
import org.metaservice.core.management.shell.commands.*;
import org.metaservice.core.management.shell.completer.ManagerCompleteInvocation;
import org.metaservice.core.management.shell.validator.ManagerValidationInvocation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilo on 10.02.14.
 */
public class MyShell {

    public static void main(String[] args) throws CommandLineParserException {
        Injector injector = Guice.createInjector(new MetaserviceModule());
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
                .create();

        List<TerminalCharacter> chars = new ArrayList<TerminalCharacter>();
        chars.add(new TerminalCharacter('[', new TerminalColor(Color.BLUE, Color.DEFAULT)));
        for(char c : "metaservice".toCharArray()){
            chars.add(new TerminalCharacter(c, new TerminalColor(Color.RED, Color.DEFAULT),
                    CharacterType.ITALIC));
        }
        chars.add(new TerminalCharacter(']', new TerminalColor(Color.BLUE, Color.DEFAULT),
                CharacterType.FAINT));
        chars.add(new TerminalCharacter('$', new TerminalColor(Color.GREEN, Color.DEFAULT),
                CharacterType.UNDERLINE));
        chars.add(new TerminalCharacter(' ', new TerminalColor(Color.DEFAULT, Color.DEFAULT)));
        AeshConsole aeshConsole = new AeshConsoleBuilder()
                .commandRegistry(registry)
                .settings(settings)
                .prompt(new Prompt(chars))
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
}
