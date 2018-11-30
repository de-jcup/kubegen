package de.jcup.kubegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class SimpleTemplateGenerator {

	public void generate(GenerationContext c) throws IOException {
		File ouputFile = new File(c.targetFolder, c.templateFile.getName());
		try (BufferedReader br = new BufferedReader(new FileReader(c.templateFile));
				FileWriter fw = new FileWriter(ouputFile)) {	
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line=br.readLine())!=null) {
				String newLine = replacePlaceHolders(line,c);
				sb.append(newLine);
				sb.append("\n");
			}
			fw.write(sb.toString());
			
		}

	}

	private String replacePlaceHolders(String line, GenerationContext c) {
		Set<String> keys = c.project.getCommonAndEnvKeys(c.environment);
		for (String key: keys) {
			if (line.indexOf(key)!=-1) {
				line = line.replaceAll("{{\\s*"+key+"\\s*}}", c.project.getValue(c.environment,key));
			}
		}
		return line;
	}

}
