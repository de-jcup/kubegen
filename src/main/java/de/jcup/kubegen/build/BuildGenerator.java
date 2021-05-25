package de.jcup.kubegen.build;

import java.io.File;
import java.io.IOException;

import de.jcup.kubegen.GenerationContext;
import de.jcup.kubegen.Project;
import de.jcup.kubegen.SimpleTemplateGenerator;

public class BuildGenerator {

	public void generate(Project project, BuildCommand build) throws IOException {
		File targetFolder=null;
		if (build.targetFolder==null) {
			targetFolder=new File(project.getProjectFolder(),"build/"+build.environment);
		}else {
			targetFolder=new File(build.targetFolder);
		}
		project.putValue("KUBEGEN_IMAGE_VERSION", build.imageVersion);
		targetFolder.mkdirs();
		
		SimpleTemplateGenerator generator = new SimpleTemplateGenerator();
		
		GenerationContext c= new GenerationContext();
		c.project=project;
		c.targetFolder=targetFolder;
		c.environment=build.environment;
		c.version=build.imageVersion;
		
		File parentFolder = project.getTemplateFolder();
		c.templateFolder = parentFolder;
		
		generate(generator, c, parentFolder,targetFolder);
		
		/* go back...*/
		c.targetFolder=targetFolder;
		InstallBashFileGenerator dbgen = new InstallBashFileGenerator(build);
		dbgen.generate(c);
	}

	protected void generate(SimpleTemplateGenerator generator, GenerationContext c, File parentFolder, File targetFolder)
			throws IOException {
		for (File file: parentFolder.listFiles()){
			if (file.isDirectory()){
				generate(generator,c,file,new File(targetFolder,file.getName()));
			}else{
				targetFolder.mkdirs();
				
				c.templateFile=file;
				c.targetFolder=targetFolder;
				
				generator.generate(c);
				if (file.getName().endsWith(".yaml")){
					c.allGeneratedYamlFiles.add(file);
				}
			}
		}
	}
}
