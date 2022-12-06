package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.ExternalProcess;

import java.util.List;
import java.util.Map;


public class TerraformCommand extends AbstractCommand {
    /**
     * For security reasons we specify absolute path to binaries (instead of adding their location
     * in the path environment variablde, so that one cannot temper with which binary is going top be executed)
     */
    private static final String TERRAFORM_BINARY = "/usr/bin/terraform";

    public TerraformCommand() {
        super(1, null, "terraform");
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, List<String> arguments) {
        ExternalProcess terraform = new ExternalProcess();
        terraform.command(TERRAFORM_BINARY, arguments);
        terraform
                .dir(workSpace.getWorkdir())
                .successMarker("Created ")
                .logConsole(GitBeaver.getApplicationLogger(variables).createSubConsole())
                .execute();
    }

}
