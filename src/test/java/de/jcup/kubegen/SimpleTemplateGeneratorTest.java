package de.jcup.kubegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleTemplateGeneratorTest {
	private SimpleTemplateGenerator generatorToTest;
	
	@BeforeEach
	void before() {
		generatorToTest = new SimpleTemplateGenerator();
	}

	@Test
	void replacement_smurf_gargamel_works() {
		/* prepare */
		GenerationContext c = new GenerationContext();
		c.project = new Project(new File("."), "testproject");
		c.project.putValue("SMURF", "Garagamel");

		/* execute */
		String replaced = generatorToTest.replacePlaceHolders("This is {{ .SMURF }}", c);

		/* test */
		assertEquals("This is Garagamel", replaced);
	}
	
	@Test
	void replacement_smurf_smurfette_gargamel_rotznase_works() {
		/* prepare */
		GenerationContext c = new GenerationContext();
		c.project = new Project(new File("."), "testproject");
		c.project.putValue("SMURF", "Garagamel");
		c.project.putValue("SMURFETTE", "Rotznase");

		/* execute */
		String replaced = generatorToTest.replacePlaceHolders("This is {{ .SMURF }} and {{  .SMURFETTE }}", c);

		/* test */
		assertEquals("This is Garagamel and Rotznase", replaced);
	}
	
	@Test
	void replacement_smurf_smurfette_rotznase_works() {
		/* prepare */
		GenerationContext c = new GenerationContext();
		c.project = new Project(new File("."), "testproject");
		c.project.putValue("SMURF", "{{.SMURFETTE}}");
		c.project.putValue("SMURFETTE", "Rotznase");

		/* execute */
		String replaced = generatorToTest.replacePlaceHolders("This is {{ .SMURF }}", c);

		/* test */
		assertEquals("This is Rotznase", replaced);
	}
	
	@Test
	void replacement_impossible_throws_missing_key_exception() {
		/* prepare */
		GenerationContext c = new GenerationContext();
		c.project = new Project(new File("."), "testproject");
		c.project.putValue("SMURF", "{{.SMURFETTE}}");
		c.project.putValue("SMURFETTE", "Rotznase");

		/* prepare test */
		assertThrows(MissingKeyException.class, ()->generatorToTest.replacePlaceHolders("This is {{ .SMURFX }}", c));

	}
	
	@Test
    void bugfix_issue_13_illegal_group_reference() {
        /* prepare */
        GenerationContext c = new GenerationContext();
        c.project = new Project(new File("."), "testproject");
        c.project.putValue("test_pwd", "bla:bla[$bla");

        generatorToTest.enableDebugOutput();

        /* execute */
        String replaced = generatorToTest.replacePlaceHolders("We use {{ .test_pwd }}", c);

        /* test */
        assertEquals("We use bla:bla[$bla", replaced);
    }

}
