package de.jcup.kubegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ProjectImporter {

	private static final String SYSTEM_PROPERTY_PREFIX = "kubegen.";

    public Project importProject(File rootFolder, String name) throws IOException {
		Project project = new Project(rootFolder, name);
		if (!project.getProjectFolder().exists()) {
			throw new FileNotFoundException("Did not found:" + project.getProjectFolder().getAbsolutePath());
		}
		appendValueFiles(project, getValueFilesFromRoot(project));
		appendValueFiles(project, getValueFilesFromProject(project));
		/* at the end add systemp properties - so override */
		appendSystemProperties(project);
		
		return project;
	}

	private void appendSystemProperties(Project project) {
        Properties properties = System.getProperties();
        for (Object key: properties.keySet()) {
            if (! (key instanceof String)) {
                continue;
            }
            String keyString = key.toString();
            if (!keyString.startsWith(SYSTEM_PROPERTY_PREFIX)){
                continue;
            }
            int length = SYSTEM_PROPERTY_PREFIX.length();
            if (keyString.length()<length+1) {
                continue;
            }
            Object value = properties.get(keyString);
            if (! (value instanceof String)) {
                continue;
            }
            String targetKey = keyString.substring(length);
            String valueString = value.toString();
            
            project.putValue(targetKey, valueString);
            for (String environment: project.getEnvironments()) {
                String originValue = project.getValue(environment, targetKey);
                if (originValue!=null && ! valueString.equals(originValue)) {
                    /* self defined, so override */
                    project.putValue(environment, targetKey, valueString);
                }
            }
        }
        
    }

    private void appendValueFiles(Project project, File[] valueFiles) throws IOException {
		for (File file : valueFiles) {
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
	}

	private File[] getValueFilesFromProject(Project project) {
		return getFilesOrEmptyArray(project.getValuesFolder());
	}
	
	private File[] getValueFilesFromRoot(Project project) {
		return getFilesOrEmptyArray(project.getRootValuesFolder());
	}

	private File[] getFilesOrEmptyArray(File rootValuesFolder) {
		if (!rootValuesFolder.exists()){
			return new File[] {};
		}
		return rootValuesFolder.listFiles();
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
