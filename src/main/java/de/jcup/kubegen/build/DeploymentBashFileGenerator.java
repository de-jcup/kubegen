package de.jcup.kubegen.build;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.jcup.kubegen.GenerationContext;

public class DeploymentBashFileGenerator {

	public void generate(GenerationContext context) throws IOException{
		
		File ouputFile = new File(context.targetFolder, "install.sh");
		
		try (FileWriter fw = new FileWriter(ouputFile)) {	
			StringBuilder sb = new StringBuilder();
			sb.append("#!/bin/bash\n\n");
			for (File templateFile: context.allGeneratedYamlFiles) {
				sb.append("kubectl apply -f "+path(context,templateFile).replace('\\', '/'));
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
