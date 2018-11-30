README gekube
=============

`geKube` stands for **generate kubernetes files.**

Shall be a very lightweight approach to handle templating.


Structure:

----
```
myproject
     templates
        01_pods.yaml
        02_services.yaml
        03_deployment.yaml
     values
        values.properties
        values_dev.properties
        values_prod.properties
```
When calling

```bash
gekube build -project=myproject -env=prod -dockerImageVersion=1.0.0
```
we got 

```
myproject
     templates
        01_pods.yaml
        02_services.yaml
        03_deployment.yaml
     values
        values.properties
        values_dev.properties
        values_prod.properties
     build
        01_pods.yaml
        02_services.yaml
        03_deployment.yaml
        install.sh
```