package de.jcup.kubegen;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

// see http://jcommander.org/
public class Config {

	CommandBuild build = new CommandBuild();
	
	@Parameters(separators = "=", commandDescription = "Build ")
	public class CommandBuild {

		@Parameter(names = "-project", description = "Project name", required = true)
		String projectName;
		
		@Parameter(names = "-dockerImageVersion", description = "Docker image version. Will be accessible as variable `dockerImageVersion`", required = true)
		String version;
		
		@Parameter(names = "-env", description = "Environment, e.g. dev|int|prod", required = true)
		String environment;
		

		@Parameter(names = "-sourceFolder", description = "Base source folder to search for projects. If not defined current dir will be used", required = false)
		String projectHomeFolder = new File(".").getAbsolutePath();
		
		@Parameter(names = "-targetFolder", description = "Target output folder. If not defined, $projectFolder/build will be used", required = false)
		String targetFolder;
	}
	

	@Parameter(names = "--help", help = true)
	boolean help;

}
