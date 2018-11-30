package de.jcup.kubegen;

import java.io.File;

import com.beust.jcommander.JCommander;

public class Main {


	public static void main(String[] args) {
		 Config config = new Config();
	        JCommander jc = JCommander.newBuilder()
	            .addObject(config)
	            .addCommand("build", config.build).build();
	        jc.parse(args);
	        
	        Main main = new Main();
	        try {
	        	main.run(jc, config);
	        }catch(Exception e) {
	        	System.err.println("Execution failed:"+e.getMessage());
	        	System.exit(1);
	        }

	}
	
	public void run(JCommander jc, Config config) throws Exception{
		if ("build".equals(jc.getParsedCommand())){
			ProjectImporter importer = new ProjectImporter();
			
			Project project = importer.importProject(new File(config.build.projectHomeFolder), config.build.projectName);
			CommandBuildGenerator generator = new CommandBuildGenerator();
			generator.generate(project, config.build);
		}else {
			jc.usage();
		}
	}
}
