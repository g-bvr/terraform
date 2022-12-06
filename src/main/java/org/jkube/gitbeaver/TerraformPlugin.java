package org.jkube.gitbeaver;

import org.jkube.gitbeaver.plugin.SimplePlugin;

import java.util.Arrays;
import java.util.List;

public class TerraformPlugin extends SimplePlugin {

    public TerraformPlugin() {
        super(
                TerraformCommand.class
        );
    }

    @Override
    public List<String> getInstallationScript() {
        return Arrays.asList(
                "wget https://releases.hashicorp.com/terraform/0.12.21/terraform_0.12.21_linux_amd64.zip",
                "unzip terraform_0.12.21_linux_amd64.zip",
                "rm terraform_0.12.21_linux_amd64.zip",
                "mv terraform /usr/bin/terraform"
        );
    }

}
