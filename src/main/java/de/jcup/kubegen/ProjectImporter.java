package de.jcup.kubegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProjectImporter {

	public Project importProject(File rootFolder, String name) throws IOException {
		Project project = new Project(rootFolder, name);
		if (!project.getProjectFolder().exists()) {
			throw new FileNotFoundException("Did not found:" + project.getProjectFolder().getAbsolutePath());
		}
		for (File file : project.getValuesFolder().listFiles()) {
			String fileName = file.getName();
			if (fileName.equals("values.properties")) {
				pushValues(project, "", file);
				continue;
			}
			int index = fileName.indexOf("values_");
			if (index == -1) {
				continue;
			}
			index = index + "values_".length();
			int lastIndex = fileName.indexOf(".properties", index);
			if (lastIndex == -1) {
				continue;
			}
			String environment = fileName.substring(index, lastIndex);
			project.getEnvironments().add(environment);

			pushValues(project, environment, file);
		}


		return project;
	}
	
	private void pushValues(Project project, String env, File file) throws IOException{
		/* import values */
		Properties p = new Properties();
		try (FileReader fileReader = new FileReader(file)) {
			p.load(fileReader);
			for (Object key : p.keySet()) {
				String keyString = key.toString();
				String found = p.getProperty(keyString);
				if (found != null) {
					project.putValue(env, keyString, found);
				}
			}
		}
	}
}
