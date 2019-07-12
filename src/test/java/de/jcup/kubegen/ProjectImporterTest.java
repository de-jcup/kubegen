package de.jcup.kubegen;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ProjectImporterTest {

	private ProjectImporter importerToTest;

	@Before
	public void before() {
		importerToTest = new ProjectImporter();
		System.clearProperty("kubegen.newvalue.test");
		System.clearProperty("kubegen.test.key.overridden");
	}

	@Test
    public void import_test_root1_has_expected_name_override_with_system_property() throws Exception {
        /* prepare */
	    System.setProperty("kubegen.newvalue.test", "name-from-system-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("name-from-system-property", project.getValue("newvalue.test"));
        assertEquals("name-from-system-property", project.getValue("prod", "newvalue.test"));

    }
	
	@Test
    public void import_test_root1_has_expected_name_override_with_system_property__overrides_existing_common() throws Exception {
        /* prepare */
        System.setProperty("kubegen.test.key.overridden", "common-but-by-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("common-but-by-property", project.getValue("test.key.overridden"));

    }
	
	@Test
    public void import_test_root1_has_expected_name_override_with_system_property__overrides_existing_prod() throws Exception {
        /* prepare */
        System.setProperty("kubegen.test.key.overridden", "common-but-by-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("common-but-by-property", project.getValue("prod", "test.key.overridden"));

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
		Set<String> environments = project.getEnvironments();
		assertTrue(environments.contains("prod"));
		assertTrue(environments.contains("int"));
		assertTrue(environments.contains("dev"));
		assertEquals(3, environments.size());

	}
	
	@Test
	public void import_test_root2_contains_merged_environments() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root2");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		Set<String> environments = project.getEnvironments();
		assertTrue(environments.contains("prod"));
		assertTrue(environments.contains("dev"));
		assertEquals(2, environments.size());

	}
	
	@Test
	public void import_test_root3_contains_merged_environments_even_when_no_values_folder_in_project() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root3");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		Set<String> environments = project.getEnvironments();
		assertTrue(environments.contains("prod"));
		assertTrue(environments.contains("dev"));
		assertEquals(2, environments.size());
		assertEquals("value1", project.getValue("prod", "test.key"));
		assertEquals("value1", project.getValue("dev", "test.key"));
		assertEquals("prod", project.getValue("prod", "test.key.overridden"));

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
	public void import_test_root1_values_common_imported_on_common_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("test.key.overridden"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_prod_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("prod", project.getValue("prod", "test.key.overridden"));
	}
	
	@Test
	public void import_test_root1_values_common_imported_on_dev_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("dev", "test.key.overridden"));
	}
	
	@Test
	public void import_test_root1_rootvalues_common_imported_on_dev() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");
		
		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");
		
		/* test */
		assertEquals("valueRoot", project.getValue("dev", "test.key.from.root"));
	}
	@Test
	public void import_test_root1_rootvalues_common_imported_on_dev_overridden_is_shared() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");
		
		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");
		
		/* test */
		assertEquals("commonRoot", project.getValue("dev", "test.key.from.root.overridden"));
	}
	@Test
	public void import_test_root1_rootvalues_common_imported_on_prod_overridden_is_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("prodRoot", project.getValue("prod", "test.key.from.root.overridden"));
	}
	
	@Test
	public void import_test_root1_rootvalues_common_imported_on_int_overridden_is_overridden_from_project() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("intProject", project.getValue("int", "test.key.from.root.overridden.inproject"));
	}
	
	@Test
	public void import_test_root1_rootvalues_common_imported_on_prod_overridden_is_not_overridden_from_project() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("commonRootNotProject", project.getValue("prod", "test.key.from.root.overridden.inproject"));
	}
	
	
	

}
