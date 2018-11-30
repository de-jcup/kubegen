package de.jcup.kubegen;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SimpleTemplateGeneratorTest {
	private SimpleTemplateGenerator generatorToTest;
	
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Before
	public void before() {
		generatorToTest = new SimpleTemplateGenerator();
	}

	@Test
	public void replacement_smurf_gargamel_works() {
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
	public void replacement_smurf_smurfette_gargamel_rotznase_works() {
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
	public void replacement_smurf_smurfette_rotznase_works() {
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
	public void replacement_impossible_throws_missing_key_exception() {
		/* prepare */
		GenerationContext c = new GenerationContext();
		c.project = new Project(new File("."), "testproject");
		c.project.putValue("SMURF", "{{.SMURFETTE}}");
		c.project.putValue("SMURFETTE", "Rotznase");

		/* prepare test */
		expected.expect(MissingKeyException.class);
		
		/* execute */
		generatorToTest.replacePlaceHolders("This is {{ .SMURFX }}", c);

	}

}
