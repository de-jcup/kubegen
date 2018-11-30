package de.jcup.kubegen.scaffolding;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.jcup.kubegen.Project;
import de.jcup.kubegen.ProjectImporter;

public class ScaffoldGeneratorTest {

	private ScaffoldGenerator generatorToTest;

	@Before
	public void before() {
		generatorToTest=new ScaffoldGenerator();
	}
	
	@Test
	public void test_generate_and_reload() throws Exception{
		File testRoot = Files.createTempDirectory("kubegen-test").toFile();
		testRoot.deleteOnExit();
		
		Project project = new Project(testRoot, "name1");
		project.getEnvironments().addAll(Arrays.asList("prod","int","dev"));
		
		generatorToTest.generate(project);
		
		ProjectImporter importer = new ProjectImporter();
		
		Project project2 = importer.importProject(testRoot, "name1");
		
		assertEquals("name1", project2.getName());
		
		List<String> environments = project2.getEnvironments();
		assertTrue(environments.contains("prod"));
		assertTrue(environments.contains("int"));
		assertTrue(environments.contains("dev"));
	}

}
