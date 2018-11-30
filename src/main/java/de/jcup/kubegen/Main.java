package de.jcup.kubegen;

import java.io.File;

import com.beust.jcommander.JCommander;

import de.jcup.kubegen.build.BuildCommand;
import de.jcup.kubegen.build.BuildGenerator;
import de.jcup.kubegen.scaffolding.ScaffoldCommand;
import de.jcup.kubegen.scaffolding.ScaffoldGenerator;

public class Main {

	Config config = new Config();
	BuildCommand buildCommand = new BuildCommand();
	ScaffoldCommand scaffoldCommand = new ScaffoldCommand();

	public static void main(String[] args) {

		Main main = new Main();
		main.run(args);

	}

	public void run(String... args) {
		/* @formatter:off*/
		JCommander jc = JCommander.newBuilder().
					programName("kubegen").
					
					addObject(config).
					
					addCommand("build", buildCommand).
					addCommand("scaffold", scaffoldCommand).
					
					build();
		/* @formatter:on*/
		try {
			jc.parse(args);
			execute(jc);
		} catch (Exception e) {
			System.err.println("> Execution failed:" + e.getMessage());
			if (config.verbose){
				e.printStackTrace();
			}
			System.exit(1);
		}
	}

	protected void execute(JCommander jc) throws  Exception {
		String cmd = jc.getParsedCommand();
		if (cmd==null){
			if (config.showVersion){
				System.out.println("kubegen 0.2.0");
				return;
			}
			jc.usage();
			return;
		}
		switch (cmd) {
		case "build":
			File rootFolder = new File(buildCommand.projectHomeFolder);
			ProjectImporter importer = new ProjectImporter();
			Project projectToBuild = importer.importProject(rootFolder, buildCommand.projectName);

			BuildGenerator generator = new BuildGenerator();
			generator.generate(projectToBuild, buildCommand);
			break;
		case "scaffold":
		case "create":
			Project newProject = new Project(new File("."), scaffoldCommand.projectName);
			newProject.getEnvironments().addAll(scaffoldCommand.environments);
			
			ScaffoldGenerator scaffoldGenerator = new ScaffoldGenerator();
			scaffoldGenerator.generate(newProject);
			break;
		default:
			if (config.verbose){
				System.out.println("not able to handle command:"+cmd);
			}
			jc.usage();
		}
	}
}
