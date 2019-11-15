package de.jcup.kubegen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ProjectImporter {

    private static final String SYSTEM_PROPERTY_PREFIX = "kubegen.";
    private static final String SYSTEM_ENV_PREFIX = "KUBEGEN_";

    MapDataProvider environmentEntryMapDataProvider;
    MapDataProvider systemPropertyMapDataProvider;

    public ProjectImporter() {
        environmentEntryMapDataProvider = new SystemEnvironmentMapDataProvider();
        systemPropertyMapDataProvider= new SystemPropertyMapDataProvider();
    }

    public Project importProject(File rootFolder, String name) throws IOException {
        Project project = new Project(rootFolder, name);
        if (!project.getProjectFolder().exists()) {
            throw new FileNotFoundException("Did not found:" + project.getProjectFolder().getAbsolutePath());
        }
        appendValueFiles(project, getValueFilesFromRoot(project));
        appendValueFiles(project, getValueFilesFromProject(project));

        /* override by system environment parts */
        appendSystemEnvironment(project);
        /* at the end add systemp properties - so override */
        appendSystemProperties(project);

        return project;
    }

    private void appendSystemProperties(Project project) {
        appendMap(project, systemPropertyMapDataProvider.getMap(), new SystemPropertyToPropertyKeyConverter());
    }

    private void appendSystemEnvironment(Project project) {
        appendMap(project, environmentEntryMapDataProvider.getMap(), new SystemEnvToPropertyKeyConverter());
    }

    private abstract class KeyConverter {
        private String keyPrefix;

        public KeyConverter(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }

        public String convertKey(Object key) {
            String keyString = key.toString();
            if (!keyString.startsWith(keyPrefix)) {
                return null;
            }
            int length = keyPrefix.length();
            if (keyString.length() < length + 1) {
                return null;
            }
            String targetKey = keyString.substring(length);
            if (targetKey == null) {
                return null;
            }
            return convertKeyImpl(targetKey);
        }

        protected abstract String convertKeyImpl(String key);
    }

    private class SystemEnvToPropertyKeyConverter extends KeyConverter {

        public SystemEnvToPropertyKeyConverter() {
            super(SYSTEM_ENV_PREFIX);
        }

        public String convertKey(Object key) {
            return super.convertKey(key);
        }
        
        @Override
        public String convertKeyImpl(String key) {
            return key;
        }

    }

    private class SystemPropertyToPropertyKeyConverter extends KeyConverter {

        public SystemPropertyToPropertyKeyConverter() {
            super(SYSTEM_PROPERTY_PREFIX);
        }

        @Override
        public String convertKeyImpl(String key) {
            return key;
        }

    }

    private void appendMap(Project project, Map<?, ?> properties, KeyConverter keyConverter) {
        for (Object key : properties.keySet()) {
            if (!(key instanceof String)) {
                continue;
            }
            String targetKey = keyConverter.convertKey(key);
            if (targetKey == null) {
                continue;
            }
            Object value = properties.get(key);
            if (!(value instanceof String)) {
                continue;
            }
            String valueString = value.toString();

            project.putValue(targetKey, valueString);
            for (String environment : project.getEnvironments()) {
                String originValue = project.getValue(environment, targetKey);
                if (originValue != null && !valueString.equals(originValue)) {
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
        if (!rootValuesFolder.exists()) {
            return new File[] {};
        }
        return rootValuesFolder.listFiles();
    }

    private void pushValues(Project project, String env, File file) throws IOException {
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
