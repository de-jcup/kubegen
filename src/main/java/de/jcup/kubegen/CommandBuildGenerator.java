package de.jcup.kubegen;

import java.io.File;
import java.io.IOException;

import de.jcup.kubegen.Config.CommandBuild;

public class CommandBuildGenerator {

	public void generate(Project project, CommandBuild build) throws IOException {
		File targetFolder=null;
		if (build.targetFolder!=null) {
			targetFolder=new File(project.getProjectFolder(),"build");
		}else {
			targetFolder=new File(build.targetFolder);
		}
		
		targetFolder.mkdirs();
		
		SimpleTemplateGenerator generator = new SimpleTemplateGenerator();
		
		GenerationContext c= new GenerationContext();
		c.project=project;
		c.targetFolder=targetFolder;
		c.environment=build.environment;
		c.version=build.version;
		
		for (File file: project.getTemplateFiles()) {
			c.templateFile=file;
			generator.generate(c);
		}
		DeploymentBashFileGenerator dbgen = new DeploymentBashFileGenerator();
		dbgen.generate(c);
	}
}
