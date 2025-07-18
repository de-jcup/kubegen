[![Java CI with Gradle](https://github.com/de-jcup/kubegen/actions/workflows/gradle.yml/badge.svg)](https://github.com/de-jcup/kubegen/actions/workflows/gradle.yml)

# kubegen

`kubegen` stands for **kubernetes file generator**

Shall be a very lightweight approach to handle templating.

Project website: https://github.com/de-jcup/kubegen

## How to use

### Define a template structure

Structure:

----
```
projects
   myproject1
     templates
        01_initial
           01_config_maps.yaml
       02_resources
           01_pods.yaml
           02_services.yaml
           03_deployment.yaml
        03_scripts
            something.sh
        99_end.yaml
       ...
     values
        values.properties
        values_dev.properties
        values_prod.properties
  myproject2
      templates
         01_do_it.yaml
  values
     values.properties
     values_int.properties
   
```


## Call and generate 

```bash
java -jar kubegen-all.jar <command> <options> <systemproperties>
```

**There are following options:**

```
Usage: kubegen [options] [command] [command options]
  Options:
    --help
      Shows this help
    --imageVersion
      Shows imageVersion of kubegen
    --verbose
      Shows verbose output
      Default: false
  Commands:
    build      Build environment specific kubernetes files and create 
            install.sh script for execution
      Usage: build [options]
        Options:
          -c, --contextKey
            Key containing Kubernetes context to use. Generated scripts will 
            make use of this. If none defined KUBEGEN_CONTEXT_NAME will be 
            used 
            Default: KUBEGEN_CONTEXT_NAME
        * -e, --environment
            Environment, e.g. dev|int|prod
        * -i, --imageVersion
            Docker image imageVersion. Will be accessible as variable 
            `KUBEGEN_IMAGE_VERSION` 
          -k, --kubeconfigKey
            Key containing Kubernetes config file to use. Generated scripts 
            will make use of this. If none defined KUBEGEN_KUBECONFIG_FILE 
            will be used
            Default: KUBEGEN_KUBECONFIG_FILE
          -n, --namespaceKey
            Key containing Kubernetes namespace to use. Generated scripts will 
            use this as target namespace. If none defined 
            KUBEGEN_NAMESPACE_NAME will be used
            Default: KUBEGEN_NAMESPACE_NAME
        * -p, --project
            Project name
          -s, --sourceFolder
            Base source folder to search for projects. If not defined current 
            dir will be used
            Default: /home/albert/develop/projects/jcup/kubegen/.
          -t, --targetFolder
            Target output folder. If not defined, $projectFolder/buildCommand 
            will be used

    scaffold      Creates a scaffolding for a new project in current directory
      Usage: scaffold [options]
        Options:
        * -e, --environments
            Environments, e.g. dev|int|prod
        * -p, --project
            Project name

```
### Shell script generation
`install.sh` will be generated and contains ready to go install scripts with automated context change etc. dependening on the used CLI options.


## Environments
Environments are automatically resolved by inspected value_$xxx.properties. Folder `values` - if existing -  in project parent folder is provided to
any project. So there can be a shared setup provided. If project has no own `values` folder only parent folder environments are used. Otherwise 
data will be merged but project does override.  

## System property override
When you call the jar with a system property starting with `kubegen.`you can set custom values from command line and also override fix defined values.

For example setting `-Dkubegen.TEST_VALUE1=mypropvalue` you will have `{{ .TEST_VALUE1 }}` available in templates

## System environment entry override
Same as for system property override: KUBEGEN_TEST_VALUE1 will be mapped in template to `{{ .TEST_VALUE1 }}`.
But system properties are superior to environment entries!

## Example
For example `myproject1` has also environment `int` available because defined in parent.

When calling

```bash
java -jar kubegen-all.jar build -p myproject -e=prod -i=1.0.0 -n=KEY_TO_DEFINE_NAMESPACE
```
we got 

```
myproject
     ...
        01_initial
           01_config_maps.yaml
        02_resources
           01_pods.yaml
           02_services.yaml
           03_deployment.yaml
        03_scripts
           something.sh
        99_end.yaml
        install.sh
```

All keys defined in templates (GO template style) e.g. `{{ .MY_VALUE }}` will be replaced by corresponding values.

value.properties will be overriden by environment specific one.

For more details call

```bash
java -jar kubegen-all.jar --help
```

## How to build locally a distribution
```
./gradlew build distribution
```

Afterwards inside `build/lib/` the file `kubegen-all.jar` is available.
