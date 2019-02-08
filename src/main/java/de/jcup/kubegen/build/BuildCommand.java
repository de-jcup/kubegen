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
	

	@Parameter(names = {"-s","--sourceFolder"}, description = "Base source folder to search for projects. If not defined current dir will be used", required = false)
	public String projectHomeFolder = new File(".").getAbsolutePath();
	
	@Parameter(names = {"-t","--targetFolder"}, description = "Target output folder. If not defined, $projectFolder/buildCommand will be used", required = false)
	public String targetFolder;
}