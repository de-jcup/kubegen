package de.jcup.kubegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProjectImporterTest {

	private static final String PROP_KUBEGEN_SOME_OTHER_VALUE = "kubegen.SOME_OTHER_VALUE";
    private static final String ENVI_KUBEGEN_VALUE1 = "KUBEGEN_VALUE1";
    private static final String PROP_KUBEGEN_VALUE1 = "kubegen.VALUE1";
    
    private static final String KUBEGEN_TEST_KEY_OVERRIDDEN = "kubegen.test.key.overridden";
    private static final String KUBEGEN_NEWVALUE_TEST = "kubegen.newvalue.test";
    private ProjectImporter importerToTest;
    private MapDataProvider mockedEntryProvider;

	@BeforeEach
	void before() {
	    System.clearProperty(KUBEGEN_NEWVALUE_TEST);
	    System.clearProperty(KUBEGEN_TEST_KEY_OVERRIDDEN);
	    System.clearProperty(PROP_KUBEGEN_VALUE1);
	    System.clearProperty(PROP_KUBEGEN_SOME_OTHER_VALUE);
	    
	    importerToTest = new ProjectImporter();
	    
	    mockedEntryProvider = mock(MapDataProvider.class);
	    importerToTest.environmentEntryMapDataProvider=mockedEntryProvider;

	}
	

    @Test
    void import_root_4_with_property_set() throws Exception {
        /* prepare */
        System.setProperty(PROP_KUBEGEN_VALUE1, "changed-by-property");
        File testroot1 = TestFileAccess.getTestResource("test-root4");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("changed-by-property", project.getValue("VALUE1"));

    }
    
    @Test
    void import_root_4_with_env_set() throws Exception {
        /* prepare */
        Map<Object, Object> createSingleMap = createSingleMap(ENVI_KUBEGEN_VALUE1,"changed-by-env");
        when(mockedEntryProvider.getMap()).thenReturn(createSingleMap);
      
        File testroot1 = TestFileAccess.getTestResource("test-root4");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("changed-by-env", project.getValue("VALUE1"));

    }
    
    @Test
    void import_root_4_with_env_set_some_other_PROP_value_available_even_when_not_in_template() throws Exception {
        /* prepare */
        System.setProperty(PROP_KUBEGEN_SOME_OTHER_VALUE, "changed-by-property");
      
        File testroot1 = TestFileAccess.getTestResource("test-root4");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("changed-by-property", project.getValue("SOME_OTHER_VALUE"));

    }
    
    @Test
    void import_root_4_with_env_set_some_other_ENV_value_available_even_when_not_in_template() throws Exception {
        /* prepare */
        Map<Object, Object> createSingleMap = createSingleMap("KUBEGEN_SOME_OTHER_VALUE","changed-by-env");
        when(mockedEntryProvider.getMap()).thenReturn(createSingleMap);
      
        File testroot1 = TestFileAccess.getTestResource("test-root4");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("changed-by-env", project.getValue("SOME_OTHER_VALUE"));

    }
	
	@Test
	void an_new_created_importer_has_got_a_system_environment_entry_provider() {
	    ProjectImporter blankImporter = new ProjectImporter();
	    
	    assertNotNull(blankImporter.environmentEntryMapDataProvider);
	    assertEquals(SystemEnvironmentMapDataProvider.class, blankImporter.environmentEntryMapDataProvider.getClass());
	}

	@Test
    void import_test_root1_has_expected_name_from_with_system_property() throws Exception {
        /* prepare */
	    System.setProperty(KUBEGEN_NEWVALUE_TEST, "name-from-system-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("name-from-system-property", project.getValue("newvalue.test"));
        assertEquals("name-from-system-property", project.getValue("prod", "newvalue.test"));

    }
	
	@Test
    void import_test_root1_has_expected_name_override_with_ENV_entry() throws Exception {
        /* prepare */
	    Map<Object, Object> createSingleMap = createSingleMap("KUBEGEN_newvalue.test","name-from-system-env");
        when(mockedEntryProvider.getMap()).thenReturn(createSingleMap);
	    
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("name-from-system-env", project.getValue("newvalue.test"));
        assertEquals("name-from-system-env", project.getValue("prod", "newvalue.test"));

    }
	
	@Test
    void import_test_root1_has_expected_name_fromwith_SysetmProperty_not_ENV_entry_when_both_exist() throws Exception {
        /* prepare */
	    System.setProperty(KUBEGEN_NEWVALUE_TEST, "name-from-system-property");
	    Map<Object, Object> createSingleMap = createSingleMap("KUBEGEN_NEWVALUE_TEST","name-from-system-env");
        when(mockedEntryProvider.getMap()).thenReturn(createSingleMap);
        
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("name-from-system-property", project.getValue("newvalue.test"));
        assertEquals("name-from-system-property", project.getValue("prod", "newvalue.test"));

    }
	
	@Test
    void import_test_root1_has_expected_name_override_with_system_property__overrides_existing_common() throws Exception {
        /* prepare */
        System.setProperty(KUBEGEN_TEST_KEY_OVERRIDDEN, "common-but-by-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("common-but-by-property", project.getValue("test.key.overridden"));

    }
	
	@Test
    void import_test_root1_has_expected_name_override_with_env_entry__overrides_existing_common() throws Exception {
        /* prepare */
	    
	    Map<Object, Object> createSingleMap = createSingleMap("KUBEGEN_test.key.overridden","common-but-by-env");
        when(mockedEntryProvider.getMap()).thenReturn(createSingleMap);
	      
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("common-but-by-env", project.getValue("test.key.overridden"));

    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<Object,Object> createSingleMap(String key, String value){
	    Map<String,String> map = new HashMap<>();
        map.put(key,value);
        return (Map)map;
	}
	
	
	@Test
    void import_test_root1_has_expected_name_override_with_system_property__overrides_existing_prod() throws Exception {
        /* prepare */
        System.setProperty(KUBEGEN_TEST_KEY_OVERRIDDEN, "common-but-by-property");
        File testroot1 = TestFileAccess.getTestResource("test-root1");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("common-but-by-property", project.getValue("prod", "test.key.overridden"));

    }
	
	@Test
	void import_test_root1_has_expected_name() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("name1", project.getName());

	}
	
	@Test
	void import_test_root1_contains_environments() throws Exception {
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
	void import_test_root2_contains_merged_environments() throws Exception {
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
	void import_test_root3_contains_merged_environments_even_when_no_values_folder_in_project() throws Exception {
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
	void import_test_root1_contains_templates() throws Exception {
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
	void import_test_root1_values_common_imported_on_common() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("value1", project.getValue("test.key"));
	}
	
	@Test
	void import_test_root1_values_common_imported_on_prod() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("value1", project.getValue("prod", "test.key"));
	}
	
	@Test
	void import_test_root1_values_common_imported_on_common_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("test.key.overridden"));
	}
	
	@Test
	void import_test_root1_values_common_imported_on_prod_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("prod", project.getValue("prod", "test.key.overridden"));
	}
	
	@Test
	void import_test_root1_values_common_imported_on_dev_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("common", project.getValue("dev", "test.key.overridden"));
	}
	
	@Test
	void import_test_root1_rootvalues_common_imported_on_dev() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");
		
		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");
		
		/* test */
		assertEquals("valueRoot", project.getValue("dev", "test.key.from.root"));
	}
	@Test
	void import_test_root1_rootvalues_common_imported_on_dev_overridden_is_shared() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");
		
		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");
		
		/* test */
		assertEquals("commonRoot", project.getValue("dev", "test.key.from.root.overridden"));
	}
	@Test
	void import_test_root1_rootvalues_common_imported_on_prod_overridden_is_overridden() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("prodRoot", project.getValue("prod", "test.key.from.root.overridden"));
	}
	
	@Test
	void import_test_root1_rootvalues_common_imported_on_int_overridden_is_overridden_from_project() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("intProject", project.getValue("int", "test.key.from.root.overridden.inproject"));
	}
	
	@Test
	void import_test_root1_rootvalues_common_imported_on_prod_overridden_is_not_overridden_from_project() throws Exception {
		/* prepare */
		File testroot1 = TestFileAccess.getTestResource("test-root1");

		/* execute */
		Project project = importerToTest.importProject(testroot1, "name1");

		/* test */
		assertEquals("commonRootNotProject", project.getValue("prod", "test.key.from.root.overridden.inproject"));
	}
	
	@Test
    void import_test_root5_can_be_imported() throws Exception {
        /* prepare */
        File testroot1 = TestFileAccess.getTestResource("test-root5");

        /* execute */
        Project project = importerToTest.importProject(testroot1, "name1");

        /* test */
        assertEquals("commonRootNotProject", project.getValue("prod", "test.key.from.root.overridden.inproject"));
    }
    
	
	

}
