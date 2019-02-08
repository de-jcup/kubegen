package de.jcup.kubegen.scaffolding;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = " ", commandDescription = "Creates a scaffolding for a new project in current directory")
public class ScaffoldCommand {

	@Parameter(names = {"-p","--project"}, description = "Project name", required = true)
	public String projectName;
	
	@Parameter(names = {"-e","--environments"}, description = "Environments, e.g. dev|int|prod", required = true)
	public List<String> environments;
	
}