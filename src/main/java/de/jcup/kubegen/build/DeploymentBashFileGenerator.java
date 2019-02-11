package de.jcup.kubegen.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import de.jcup.kubegen.GenerationContext;

public class DeploymentBashFileGenerator {

	public void generate(GenerationContext context) throws IOException{
		
		File ouputFile = new File(context.targetFolder, "install.sh");
		SortedSet<String> sortedPathes = new TreeSet<>();
		for (File templateFile: context.allGeneratedYamlFiles) {
			sortedPathes.add(path(context,templateFile).replace('\\', '/'));
		}
		
		try (FileWriter fw = new FileWriter(ouputFile)) {	
			StringBuilder sb = new StringBuilder();
			sb.append("#!/bin/bash\n\n");
			for (String path: sortedPathes) {
				sb.append("kubectl apply -f "+path);
				sb.append("\n");
			}
			fw.write(sb.toString());
		}
	}
	
	private String path(GenerationContext context, File file){
		String common = context.templateFolder.getAbsolutePath();
		String more = file.getAbsolutePath();
		if (! more.startsWith(common)){
			return more;
		}
		return more.substring(common.length()+1);
		
	}
}
