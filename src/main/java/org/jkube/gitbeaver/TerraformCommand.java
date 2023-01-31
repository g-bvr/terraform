package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.ExternalProcess;

import java.util.List;
import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;


public class TerraformCommand extends AbstractCommand {
    /**
     * For security reasons we specify absolute path to binaries (instead of adding their location
     * in the path environment variablde, so that one cannot temper with which binary is going top be executed)
     */
    private static final String TERRAFORM_BINARY = "/usr/bin/terraform";

    public TerraformCommand() {
        super("Execute terraform command");
        commandline("TERRAFORM *");
        argument(REST, "the command to be executed (the commandline arguments to be passed to the terraform executable)");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        ExternalProcess terraform = new ExternalProcess();
        terraform.command(TERRAFORM_BINARY, List.of(arguments.get(REST).split(" ")));
        terraform
                .dir(workSpace.getWorkdir())
                .successMarker("Created ")
                .logConsole(GitBeaver.getApplicationLogger(variables).createSubConsole())
                .execute();
    }

}
