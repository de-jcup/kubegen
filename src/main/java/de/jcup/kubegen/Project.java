package de.jcup.kubegen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project {

	private static final Logger LOG = LoggerFactory.getLogger(Project.class);

	private File projectFolder;
	private String name;
	private List<String> environments = new ArrayList<>();
	private Map<String, Map<String, String>> map = new TreeMap<>();

	public Project(File rootFolder, String name) {
		this.name = name;
		this.projectFolder = new File(rootFolder, name);
	}

	public String getName() {
		return name;
	}

	public List<String> getEnvironments() {
		return environments;
	}

	public File getProjectFolder() {
		return projectFolder;
	}

	public File getTemplateFolder() {
		return new File(projectFolder, "templates");
	}

	public File getValuesFolder() {
		return new File(projectFolder, "values");
	}

	public File getValuesFile() {
		return new File(getValuesFolder(), "values.properties");
	}

	public File getValuesFile(String environment) {
		return new File(getValuesFolder(), "values_" + environment + ".properties");
	}

	public String getValue(String key) {
		return getValue(null, key);
	}

	public String getValue(String environment, String key) {
		if (environment == null) {
			environment = "";
		}
		Map<String, String> envMap = map.computeIfAbsent(environment, e -> new HashMap<>());
		String result = envMap.get(key);
		if (result == null && !key.isEmpty()) {
			/* fallback to common value when not defined */
			return getValue("", key);
		}
		return result;
	}

	public void putValue(String key, String value) {
		putValue(null, key, value);
	}

	public String putValue(String environment, String key, String value) {
		if (environment == null) {
			environment = "";
		}
		Map<String, String> envMap = map.computeIfAbsent(environment, e -> new HashMap<>());
		return envMap.put(key, value);
	}

	public Set<String> getCommonAndEnvKeys(String environment) {
		Set<String> set = new TreeSet<>();
		set.addAll(map.get("").keySet());
		if (environment != null && !environment.isEmpty()) {
			Map<String, String> envMap = map.computeIfAbsent(environment, e -> new HashMap<>());
			set.addAll(envMap.keySet());
		}
		return set;
	}

}
