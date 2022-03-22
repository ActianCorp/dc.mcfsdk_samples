
# DataConnect Message Component Framework (MCF) SDK Samples
---

This project provides code samples that demonstrate how to develop new plug-in components using the DataConnect MCF SDK. These samples are intended to be used in conjunction with the Message Component Framework Programmer's Reference Manual (included in PDF format as MCF_Programmers_Reference_Manual.pdf).

The MCF is fundamentally a plug-in mechanism for adding functionality to DataConnect that can be used directly in processes and EZScript.  This adds out-of-phase behavior without rebuilding the product.  MCF Components map to “types” already recognized by Process Designer.  These types include Queue, Iterator, Aggregator, Invoker, Transformer, etc.  MCF Components can be independently developed, versioned and deployed.

The samples and Programmer's Reference Manual in this project demonstrate:

* Implementation of various component types, including queues, invokers, transformers
* Use of DataConnect engine services
* Packaging of components for deployment

---
## Prerequisites 

Before running these samples, you will need:

1. Apache Maven 3.8.0 or higher [maven.apache.org](https://maven.apache.org/)
2. JDK version 11. Note: a JRE is not sufficient to build the samples 
3. Licensed DataConnect 12.0.0 installation with either the DataConnect Studio IDE or a standalone DataConnect Runtime Engine
   
Note: the sample code must be on the same machine as the DataConnect install.

---
## Preparing Your Environment

The MCF SDK Samples project builds and executes using Apache Maven.

1. Start a command shell
1. Ensure that the Apache Maven bin directory is in the path using the command **`mvn -v`** to print the maven version, Java version, and other relevant info
1. Add the **`runtime/di9`** directory relative to the DataConnect engine installation to your **`PATH`** environment variable
1. Set the **`DJLIB`** environment variable to the location of the directory that contains your **`cosmos.ini`** file.  This will typically be in **`\ProgramData\Actian\DataConnect\dc-rcp-64-bit-<version>`** on Windows
1. From the command shell, run `djengine -?` to verify a valid license file and to see command line Help

Note: for more information about installing and configuring the DataConnect Runtime Engine, see the Help topic of the same name in the [DataConnect User Guide on-line documentation](http://docs.actian.com/).

---
## Building the Samples

After you have completed the setup above, open a command shell and change directory to the root of the dc.mcfsdk_samples project.

To build all samples, run the following command: **`mvn clean install`**

---
## Verifying Sample Results

* The console will display the Maven Reactor Summary for all sample modules with SUCCESS for each and a final BUILD SUCCESS message.
* A target folder will be created in each sample module. 
* A jar file with name pattern <component_name>-<component_version>-with-deps.jar will be created in each target folder.
** This file is the "deployment package" which will need to be dropped into the "Plug-Ins" folder, which is typically be in **`\ProgramData\Actian\DataConnect\dc-rcp-64-bit-<version>`** on Windows
* An Engine execution log is created under **`target/work/log/ec`**

---

## Key Concepts

See the developer guide for complete discussion of the samples

---
## Project Structure
```
README.TXT:  This file
MCF_Programmers_Reference_Manual.pdf:  Message Component Framework Programmer's Reference Manual
LICENSE:  Apache License 2.0
pom.xml:  Maven build script
Current Time Queue:
  pom.xml:  Maven build script for the Current Time Queue module
  Current Time Queue/src/main/assemblies/distribution.xml:  Maven assembly plugin configuration which is used to create the <name>-<version>-with-deps.jar deployment package
  Current Time Queue/src/main/resources/MC-INF/package.xml:  The MCF Component deployment descriptor which describes the component to the DataConnect engine
  Current Time Queue/src/main/java/com/actian/dc/mcfsdk/samples/CurrentTimeQueue.java:  The component's Java implementatoin class
File Content Type Invoker:
  pom.xml:  Maven build script for the File Content Type Invoker module
  File Content Type Invoker/src/main/assemblies/distribution.xml:  Maven assembly plugin configuration which is used to create the <name>-<version>-with-deps.jar deployment package
  File Content Type Invoker/src/main/resources/MC-INF/package.xml:  The MCF Component deployment descriptor which describes the component to the DataConnect engine
  File Content Type Invoker/src/main/java/com/actian/dc/mcfsdk/samples/ContentTypeInvoker.java:  The component's Java implementatoin class
MessageBox Invoker:
  pom.xml:  Maven build script for the MessageBox Invoker module
  MessageBox Invoker/src/main/assemblies/distribution.xml:  Maven assembly plugin configuration which is used to create the <name>-<version>-with-deps.jar deployment package
  MessageBox Invoker/src/main/resources/MC-INF/package.xml:  The MCF Component deployment descriptor which describes the component to the DataConnect engine
  MessageBox Invoker/src/main/java/com/actian/dc/mcfsdk/samples/MsgBoxInvoker.java:  The component's Java implementatoin class
Null Transformer:
  pom.xml:  Maven build script for the Null Transformer module
  Null Transformer/src/main/assemblies/distribution.xml:  Maven assembly plugin configuration which is used to create the <name>-<version>-with-deps.jar deployment package
  Null Transformer/src/main/resources/MC-INF/package.xml:  The MCF Component deployment descriptor which describes the component to the DataConnect engine
  Null Transformer/src/main/java/com/actian/dc/mcfsdk/samples/NullTransformer.java:  The component's Java implementatoin class
TestAll Queue:
  pom.xml:  Maven build script for the TestAll Queue module
  TestAll Queue/src/main/assemblies/distribution.xml:  Maven assembly plugin configuration which is used to create the <name>-<version>-with-deps.jar deployment package
  TestAll Queue/src/main/resources/MC-INF/package.xml:  The MCF Component deployment descriptor which describes the component to the DataConnect engine
  TestAll Queue/src/main/java/com/actian/dc/mcfsdk/samples/TestAll.java:  The component's Java implementatoin class
```
---
## Support

Free support is available for registered users of the [Actian Community](https://communities.actian.com/s/?_ga=2.42990309.1976004892.1553019528-1750363259.1548967681)  
Paid plans are also available at [Actian Support Services](https://www.actian.com/support-services/)

---
## Contributing

Actian Corporation welcomes contributions to the DataConnect MCF SDK Samples project.  To contribute, please follow these steps:

* When submitting your pull request, please provide full contact info (Name, company, email, phone)
* Submit your pull request to the dev branch. We will review and test the requested change.  
* Once approved, we will perform the merge to dev.  Your change will be available immediately after our next merge to master.
