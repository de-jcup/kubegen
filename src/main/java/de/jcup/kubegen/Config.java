package de.jcup.kubegen;

import com.beust.jcommander.Parameter;

public class Config {

	@Parameter(names = "--imageVersion", description = "Shows imageVersion of kubegen", help = true)
	boolean showVersion;

	@Parameter(names = "--help", description = "Shows this help", help = true)
	boolean help;

	@Parameter(names = "--verbose", description = "Shows verbose output")
	boolean verbose;

}

