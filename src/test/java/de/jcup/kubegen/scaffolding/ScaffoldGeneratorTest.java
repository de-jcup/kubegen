package de.jcup.kubegen.scaffolding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.jcup.kubegen.Project;
import de.jcup.kubegen.ProjectImporter;

public class ScaffoldGeneratorTest {

    private ScaffoldGenerator generatorToTest;

    @BeforeEach
    void before() {
        generatorToTest = new ScaffoldGenerator();
    }

    @Test
    void test_generate_and_reload() throws Exception {
        File testRoot = Files.createTempDirectory("kubegen-test").toFile();
        testRoot.deleteOnExit();

        Project project = new Project(testRoot, "name1");
        project.getEnvironments().addAll(Arrays.asList("prod", "int", "dev"));

        generatorToTest.generate(project);

        ProjectImporter importer = new ProjectImporter();

        Project project2 = importer.importProject(testRoot, "name1");

        assertEquals("name1", project2.getName());

        Set<String> environments = project2.getEnvironments();
        assertTrue(environments.contains("prod"));
        assertTrue(environments.contains("int"));
        assertTrue(environments.contains("dev"));
    }

}
