[![Build status:](https://travis-ci.org/de-jcup/kubegen.svg?branch=master)](https://travis-ci.org/de-jcup/kubegen)

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
-p = project name
-i = image name, will be available in templates by `{{ .KUBEGEN_IMAGE_VERSION}}`
-n = namespace to use, will be available in templates by `{{ .NAMESPACE_NAME}}`
-e = environment to use

## Environments
Environments are automatically resolved by inspected value_$xxx.properties. Folder `values` - if existing -  in project parent folder is provided to
any project. So there can be a shared setup provided. If project has no own `values` folder only parent folder environments are used. Otherwise 
data will be merged but project does override.  

## System property override
When you call the jar with a system property starting with `kubegen.`you can set custom values from command line and also override fix defined values.

For example setting `-Dkubegen.TEST_VALUE1=mypropvalue` you will have `{{ .TEST_VALUE1 }}` available in templates

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