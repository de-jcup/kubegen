package de.jcup.kubegen.build;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = " ", commandDescription = "Build environment specific kubernetes files and create install.sh script for execution")
public class BuildCommand {

	@Parameter(names = {"-p","--project"}, description = "Project name", required = true)
	public String projectName;
	
	@Parameter(names = {"-i", "--imageVersion"}, description = "Docker image imageVersion. Will be accessible as variable `KUBEGEN_IMAGE_VERSION`", required = true)
	public String imageVersion;
	
	@Parameter(names = {"-e","--environment"}, description = "Environment, e.g. dev|int|prod", required = true)
	public String environment;
	
	@Parameter(names = {"-n","--namespaceKey"}, description = "Key containing Kubernetes namespace to use. Generated scripts will use this as target namespace. If none defined KUBEGEN_NAMESPACE_NAME will be used", required = false)
	public String k8sNamespace = "KUBEGEN_NAMESPACE_NAME";
	
	@Parameter(names = {"-c","--contextKey"}, description = "Key containing Kubernetes context to use. Generated scripts will make use of this. If none defined KUBEGEN_CONTEXT_NAME will be used", required = false)
	public String k8sContext = "KUBEGEN_CONTEXT_NAME";
	
    @Parameter(names = {"-k","--kubeconfigKey"}, description = "Key containing Kubernetes config file to use. Generated scripts will make use of this. If none defined KUBEGEN_KUBECONFIG_FILE will be used", required = false)
    public String k8sConfigFileName = "KUBEGEN_KUBECONFIG_FILE";
    
	@Parameter(names = {"-s","--sourceFolder"}, description = "Base source folder to search for projects. If not defined current dir will be used", required = false)
	public String projectHomeFolder = new File(".").getAbsolutePath();
	
	@Parameter(names = {"-t","--targetFolder"}, description = "Target output folder. If not defined, $projectFolder/buildCommand will be used", required = false)
	public String targetFolder;
}