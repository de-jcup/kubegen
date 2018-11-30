package de.jcup.kubegen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenerationContext {

	public File templateFile; 
	public Project project; 
	public File targetFolder; 
	public String environment; 
	public String version;
	
	public List<File> allGeneratedYamlFiles = new ArrayList<>();
	public File templateFolder;
	public int lineNr;
}
