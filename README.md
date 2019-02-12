README kubegen
==============

`kubegen` stands for **kubernetes file generator**

Shall be a very lightweight approach to handle templating.


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

Environments are automatically resolved by inspected value_$xxx.properties. Folder `values` - if existing -  in project parent folder is provided to
any project. So there can be a shared setup provided. If project has no own `values` folder only parent folder environments are used. Otherwise 
data will be merged but project does override.  

For example `myproject1` has also environment `int` available because defined in parent.

When calling

```bash
kubegen build -p myproject -e=prod -i=1.0.0 -n=KEY_TO_DEFINE_NAMESPACE
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
kubegen --help
```