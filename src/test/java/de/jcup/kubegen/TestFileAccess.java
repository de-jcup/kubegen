package de.jcup.kubegen;

import java.io.File;

public class TestFileAccess {

	static File projectRootFolder;
	
	static File testResources;
	
	static {
		testResources=new File("src/test/resources");
		if (! testResources.exists()) {
			testResources=new File("kubegen-client/src/test/resources");
		}
		if (! testResources.exists()) {
			throw new IllegalStateException();
		}
		projectRootFolder=testResources.getParentFile().getParentFile();
	}
	
	public static File getTestResource(String path) {
		File file = new File(testResources,path);
		if (! file.exists()) {
			throw new IllegalStateException();
		}
		return file;
	}
	

}
