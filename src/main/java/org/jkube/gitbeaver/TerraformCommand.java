package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.ExternalProcess;
import org.jkube.gitbeaver.util.FileUtil;
import org.jkube.logging.Log;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.jkube.gitbeaver.CommandParser.REST;


public class TerraformCommand extends AbstractCommand {
    /**
     * For security reasons we specify absolute path to binaries (instead of adding their location
     * in the path environment variablde, so that one cannot temper with which binary is going top be executed)
     */
    static final String TERRAFORM_BINARY = "/usr/bin/terraform";
    private static final String OUTPUT_FOLDER = "folder";

    public TerraformCommand() {
        super("Execute terraform command");
        commandlineVariant("TERRAFORM INTO "+OUTPUT_FOLDER+" *", "execute a terraform command and store logs, warnings and errors into text files in a folder");
        commandlineVariant("TERRAFORM *", "execute a terraform command");
        argument(OUTPUT_FOLDER, "a folder (relative to current workspace) in which the following files will be created: logs.txt, warnings.txt, errors.txt");
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
        String folder = arguments.get(OUTPUT_FOLDER);
        if (folder != null) {
            storeOutput(terraform, workSpace.getAbsolutePath(folder));
        }
        if (terraform.hasFailed()) {
            String message = terraform.getErrors().stream().findFirst().orElse("no message");
            Log.warn("Terraform failed: {}", message);
            throw new RuntimeException("Terraform failed: "+message);
        }
    }

    private void storeOutput(ExternalProcess terraform, Path folder) {
        FileUtil.createIfNotExists(folder);
        FileUtil.append(terraform.getOutput(), folder.resolve("logs.txt").toFile());
        FileUtil.append(terraform.getWarnings(), folder.resolve("warnings.txt").toFile());
        FileUtil.append(terraform.getErrors(), folder.resolve("errors.txt").toFile());
    }

}
