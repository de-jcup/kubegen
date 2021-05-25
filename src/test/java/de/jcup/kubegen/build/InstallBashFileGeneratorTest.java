package de.jcup.kubegen.build;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jcup.kubegen.GenerationContext;
import de.jcup.kubegen.Project;

public class InstallBashFileGeneratorTest {

    private static final Logger LOG = LoggerFactory.getLogger(InstallBashFileGeneratorTest.class);


	@Test(expected=IllegalArgumentException.class)
	public void null_key_defined_throws_exception_on_construction() {
		new InstallBashFileGenerator(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void empty_namespace_defined_throws_exception_on_construction() {
		BuildCommand data = new BuildCommand();
		data.k8sNamespace="";
        new InstallBashFileGenerator(data);
	}
	
	@Test
	public void generate_simple_install_shellscript_and_check_content_namespace_set() throws Exception{
	    /* prepare */
	    Path projectFolder = Files.createTempDirectory("kubegen-project");
	    Path templateFolder = projectFolder.resolve("templates");
	    Path targetFolder = Files.createTempDirectory("kubegen-target");
	    Path path1 = targetFolder.resolve("file1.yaml");
	    Path path2 = targetFolder.resolve("file2.yaml");

        GenerationContext context = new GenerationContext();
        context.allGeneratedYamlFiles.add(path1.toFile());
        context.allGeneratedYamlFiles.add(path2.toFile());
        context.project=new Project(projectFolder.toFile(), "project-name" );
        context.templateFolder=templateFolder.toFile();
        context.targetFolder=targetFolder.toFile();
        context.environment="dev";

        context.project.putValue("dev","KUBEGEN_NAMESPACE_NAME","namespace_name");

        BuildCommand data = new BuildCommand();
        InstallBashFileGenerator generator = new InstallBashFileGenerator(data);
        
        /* execute */
        generator.generate(context);
        
        /* test */
        String result = Files.readString(targetFolder.resolve("install.sh"));
        String apply1 = "kubectl --namespace=\"namespace_name\" apply -f "+path1.toAbsolutePath().toString();
        String apply2 = "kubectl --namespace=\"namespace_name\" apply -f "+path1.toAbsolutePath().toString();
        
        LOG.debug("resulting install.sh file:\n{}",result);
        
        assertTrue(result.contains(apply1));
        assertTrue(result.contains(apply2));
        
	}
	
	@Test
    public void generate_simple_install_shellscript_and_check_content__only_context_set() throws Exception{
        /* prepare */
        Path projectFolder = Files.createTempDirectory("kubegen-project");
        Path templateFolder = projectFolder.resolve("templates");
        Path targetFolder = Files.createTempDirectory("kubegen-target");
        Path path1 = targetFolder.resolve("file1.yaml");
        Path path2 = targetFolder.resolve("file2.yaml");

        GenerationContext context = new GenerationContext();
        context.allGeneratedYamlFiles.add(path1.toFile());
        context.allGeneratedYamlFiles.add(path2.toFile());
        context.project=new Project(projectFolder.toFile(), "project-name" );
        context.templateFolder=templateFolder.toFile();
        context.targetFolder=targetFolder.toFile();
        context.environment="dev";

        context.project.putValue("dev","KUBEGEN_CONTEXT_NAME","context_name");

        BuildCommand data = new BuildCommand();
        InstallBashFileGenerator generator = new InstallBashFileGenerator(data);
        
        /* execute */
        generator.generate(context);
        
        /* test */
        String result = Files.readString(targetFolder.resolve("install.sh"));
        String apply1 = "kubectl --context=\"context_name\" apply -f "+path1.toAbsolutePath().toString();
        String apply2 = "kubectl --context=\"context_name\" apply -f "+path1.toAbsolutePath().toString();
        
        LOG.debug("resulting install.sh file:\n{}",result);
        
        assertTrue(result.contains(apply1));
        assertTrue(result.contains(apply2));
        
    }
	
	@Test
    public void generate_simple_install_shellscript_and_check_content__namespace_set_context_set_and_kubeconfig_set() throws Exception{
        /* prepare */
        Path projectFolder = Files.createTempDirectory("kubegen-project");
        Path templateFolder = projectFolder.resolve("templates");
        Path targetFolder = Files.createTempDirectory("kubegen-target");
        Path path1 = targetFolder.resolve("file1.yaml");
        Path path2 = targetFolder.resolve("file2.yaml");

        GenerationContext context = new GenerationContext();
        context.allGeneratedYamlFiles.add(path1.toFile());
        context.allGeneratedYamlFiles.add(path2.toFile());
        context.project=new Project(projectFolder.toFile(), "project-name" );
        context.templateFolder=templateFolder.toFile();
        context.targetFolder=targetFolder.toFile();
        context.environment="dev";

        context.project.putValue("dev","KUBEGEN_NAMESPACE_NAME","namespace_name");
        context.project.putValue("dev","KUBEGEN_CONTEXT_NAME","context_name");
        context.project.putValue("dev","KUBEGEN_KUBECONFIG_FILE","cube_configfile");

        BuildCommand data = new BuildCommand();
        InstallBashFileGenerator generator = new InstallBashFileGenerator(data);
        
        /* execute */
        generator.generate(context);
        
        /* test */
        String result = Files.readString(targetFolder.resolve("install.sh"));
        String apply1 = "kubectl --kubeconfig=\"cube_configfile\" --context=\"context_name\" --namespace=\"namespace_name\" apply -f "+path1.toAbsolutePath().toString();
        String apply2 = "kubectl --kubeconfig=\"cube_configfile\" --context=\"context_name\" --namespace=\"namespace_name\" apply -f "+path1.toAbsolutePath().toString();
        
        LOG.debug("resulting install.sh file:\n{}",result);
        
        assertTrue(result.contains(apply1));
        assertTrue(result.contains(apply2));
        
    }
	

}
