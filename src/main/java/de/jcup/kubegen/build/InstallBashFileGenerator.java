package de.jcup.kubegen.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import de.jcup.kubegen.GenerationContext;

public class InstallBashFileGenerator {

	private String namespaceKey;

	public InstallBashFileGenerator(String namespaceKey) {
		if (namespaceKey==null || namespaceKey.isEmpty()) {
			/* should never happen, but...*/
			throw new IllegalArgumentException("No namespace key defined!");
		}
		this.namespaceKey=namespaceKey;
	}

	public void generate(GenerationContext context) throws IOException{
		String namespace = context.project.getValue(context.environment, namespaceKey);
		File ouputFile = new File(context.targetFolder, "install.sh");
		SortedSet<String> sortedPathes = new TreeSet<>();
		for (File templateFile: context.allGeneratedYamlFiles) {
			sortedPathes.add(path(context,templateFile).replace('\\', '/'));
		}
		
		try (FileWriter fw = new FileWriter(ouputFile)) {	
			StringBuilder sb = new StringBuilder();
			sb.append("#!/bin/bash\n\n");
			for (String path: sortedPathes) {
				sb.append("kubectl ");
				if (namespace!=null) {
					sb.append("--namespace=");
					sb.append(namespace);
					sb.append(" ");
				}
				sb.append("apply -f "+path);
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
