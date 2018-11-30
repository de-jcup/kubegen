package de.jcup.kubegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkeletonGenerator {

	public void generate(Project project) throws IOException {

		project.getTemplateFolder().mkdirs();
		project.getValuesFolder().mkdirs();

		/* values */
		project.getValuesFile().createNewFile();

		for (String env : project.getEnvironments()) {
			project.getValuesFile(env).createNewFile();
		}

		/* templates */
		List<String> list = new ArrayList<>();
		list.add("pods");
		list.add("services");
		list.add("nodeports");
		list.add("deployment");

		int index = 0;
		for (String name : list) {
			index++;
			StringBuilder sb = new StringBuilder();
			sb.append(index);
			while (sb.length() < 2) {
				sb.insert(0, "0");
			}
			sb.append("_");
			sb.append(name);
			sb.append(".yaml");
			new File(project.getTemplateFolder(),sb.toString()).createNewFile();
		}
	}

}
