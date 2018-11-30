package de.jcup.kubegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleTemplateGenerator {

	private Map<String,Pattern> patternMap = new HashMap<>();
	
	public void generate(GenerationContext c) throws IOException {
		File ouputFile = new File(c.targetFolder, c.templateFile.getName());
		try (BufferedReader br = new BufferedReader(new FileReader(c.templateFile));
				FileWriter fw = new FileWriter(ouputFile)) {	
			String line = null;
			StringBuilder sb = new StringBuilder();
			c.lineNr=0;
			while ((line=br.readLine())!=null) {
				c.lineNr++;
				String newLine = replacePlaceHolders(line,c);
				sb.append(newLine);
				sb.append("\n");
			}
			fw.write(sb.toString());
			
		}

	}

	String replacePlaceHolders(String line, GenerationContext c) {
		Set<String> keys = c.project.getCommonAndEnvKeys(c.environment);
		for (String key: keys) {
			if (line.indexOf(key)!=-1) {
				Pattern pattern = patternMap.get(key);
				if (pattern==null){
					String regex = "\\{\\{\\s*\\."+key+"\\s*\\}\\}";
					pattern = Pattern.compile(regex);
					patternMap.put(key, pattern);
				}
				line = pattern.matcher(line).replaceAll(c.project.getValue(c.environment,key));
			}
		}
		int index = line.indexOf("{{");
		int lastIndex = line.indexOf("}}");
		boolean foundKeyNotResolved = index!=-1 && lastIndex!=-1;
		if (foundKeyNotResolved){
			throw new MissingKeyException("Line:"+c.lineNr+"="+line.substring(index, lastIndex), c.templateFile);
		}
		return line;
	}

}
