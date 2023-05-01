package org.jkube.gitbeaver;

import org.jkube.gitbeaver.util.ExternalProcess;
import org.jkube.gitbeaver.util.FileUtil;
import org.jkube.util.Expect;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jkube.gitbeaver.TerraformCommand.TERRAFORM_BINARY;


public class TerraformOutputCommand extends AbstractCommand {

    private static final String FILE = "file";

    public TerraformOutputCommand() {
        super("Execute terraform output and parse the obtained key-value map into a file (using gitbeaver format)");
        commandline("TERRAFORM OUTPUT "+FILE);
    }

    @Override
    public void execute(Map<String, String> variables, WorkSpace workSpace, Map<String, String> arguments) {
        ExternalProcess terraform = new ExternalProcess();
        terraform.command(TERRAFORM_BINARY, List.of("output"));
        List<String> result = terraform
                .dir(workSpace.getWorkdir())
                .logConsole(GitBeaver.getApplicationLogger(variables).createSubConsole())
                .execute()
                .getOutput();
        FileUtil.store(workSpace.getAbsolutePath(arguments.get(FILE)), result.stream().map(this::tf2gitbeaver).collect(Collectors.toList()));
    }

    /**
     * Translate from syntax key = "value" to syntax key value
     * @param tfline terraform line
     * @return gitbeaver line
     */
    private String tf2gitbeaver(String tfline) {
        String[] split = tfline.split("\s*=\s*", 2);
        Expect.equal(split.length, 2).elseFail("not parsable terraform output received: "+tfline);
        String key = split[0];
        String value = split[1];
        Expect.isTrue(value.startsWith("\"")).elseFail("expected \" char at start of value in tfline: "+tfline);
        Expect.isTrue(value.endsWith("\"")).elseFail("expected \" char at end of value in tfline: "+tfline);
        return key + " " + value.substring(1, value.length()-1);
    }

}
