package de.jcup.kubegen.build;

import org.junit.Test;

public class InstallBashFileGeneratorTest {

	@Test(expected=IllegalArgumentException.class)
	public void null_key_defined_throws_exception_on_construction() {
		new InstallBashFileGenerator(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void empty_key_defined_throws_exception_on_construction() {
		new InstallBashFileGenerator("");
	}
	

}
