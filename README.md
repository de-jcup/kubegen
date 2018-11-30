README kubegen
==============

`kubegen` stands for **kubernetes file generator**

Shall be a very lightweight approach to handle templating.


Structure:

----
```
myproject
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
```

Environments are automatically resolved by inspected value_$xxx.properties 

When calling

```bash
kubegen build -p myproject -e=prod -i=1.0.0
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

All keys defined in templates (GO template style) e.g. `{{ .MY_VALUE }}` will be replaced by correspondig values.

value.properties will be overriden by environment specific one.

For more details call

```bash
kubegen --help
```