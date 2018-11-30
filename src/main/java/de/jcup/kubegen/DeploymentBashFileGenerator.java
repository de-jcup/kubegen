package de.jcup.kubegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeploymentBashFileGenerator {

	public void generate(GenerationContext c) throws IOException{
		File ouputFile = new File(c.targetFolder, "install.sh");
		try (FileWriter fw = new FileWriter(ouputFile)) {	
			StringBuilder sb = new StringBuilder();
					
			for (File templateFile: c.project.getTemplateFiles()) {
				sb.append("kubectl apply -f "+templateFile.getName());
				sb.append("\n");
			}
			
			fw.write(sb.toString());
			
		}
	}
}
