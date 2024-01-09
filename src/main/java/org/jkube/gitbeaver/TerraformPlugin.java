package org.jkube.gitbeaver;

import org.jkube.gitbeaver.plugin.SimplePlugin;

import java.util.Arrays;
import java.util.List;

public class TerraformPlugin extends SimplePlugin {

    private final static String TF_VERSION = "1.6.6";
    public TerraformPlugin() {
        super("allows issuing of terraform commands",
                TerraformOutputCommand.class,
                TerraformCommand.class
        );
    }

    @Override
    public List<String> getInstallationScript() {
        return Arrays.asList(
                "wget https://releases.hashicorp.com/terraform/"+TF_VERSION+"/terraform_"+TF_VERSION+"_linux_amd64.zip",
                "unzip terraform_"+TF_VERSION+"_linux_amd64.zip",
                "rm terraform_"+TF_VERSION+"_linux_amd64.zip",
                "mv terraform /usr/bin/terraform"
        );
    }

}
