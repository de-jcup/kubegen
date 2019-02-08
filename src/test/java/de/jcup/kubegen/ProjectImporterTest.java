package de.jcup.kubegen;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProjectImporterTest {

	private ProjectImporter importerToTest;

	@Before
	public void before() {
		importerToTest = new ProjectImporter();
	}

	@Test
	public void import_test_root1_has_expected_name() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("name1", project.getName());

	}
	
	@Test
	public void import_test_root1_contains_environments() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		List<String> environments = project.getEnvironments();
		assertTrue(environments.contains("prod"));
		assertTrue(environments.contains("int"));
		assertTrue(environments.contains("dev"));

	}

	@Test
	public void import_test_root1_contains_templates() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		File templatesFolder = new File(testroot1, "name1/templates");
		assertEquals(templatesFolder, project.getTemplateFolder());
		assertEquals(4,project.getTemplateFolder().listFiles().length);
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_common() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("value1", project.getValue("test.key"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_prod() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("value1", project.getValue("prod", "test.key"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_common_overriden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("test.key.overriden"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_prod_overriden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("prod", project.getValue("prod", "test.key.overriden"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_dev_overriden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("dev", "test.key.overriden"));
	}

}
