# OMRS Messages

## Description
The Messages module will provide receiving messages (Call or SMS) from health facility. Module will support a few 
messages types e.g adherence reporting (pill reminder), adherence feedback, visit reminder, health tips, surveys. 
Messages will be delivered directly to patients or their caregivers. For health facility employees module will support 
possibility of scheduling messages for a patient/caregiver and ability to view scheduled messages.


## Prerequisites
#### JDK 1.7 and/or JDK 1.8
To make sure that you have JDK installed properly enter in console or terminal:

on Windows:

`"%JAVA_HOME%/bin/java.exe" -version`

on Linux/Mac:

`"$JAVA_HOME/bin/java" -version`

It should display your java version. If you do not have installed Java please follow 
[Oracle's guide](https://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/) to install.


#### Maven 3.x
To make sure that you have Apache Maven 3.x installed, open a console/terminal and enter:

`mvn -v`

You should see your Maven version. If you do not have installed please go ahead and install it. 
Here are tutorials for [Winodws](https://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/), 
[MacOSX](https://www.journaldev.com/2348/how-to-install-maven-on-mac-os-x-mavericks-10-9), 
[Ubuntu](https://www.mkyong.com/maven/how-to-install-maven-in-ubuntu/).

#### OpenMRS SDK

To start work with OpenMRS modules you will need to setup the OpenMRS SDK. In terminal or console enter a command:

`mvn org.openmrs.maven.plugins:openmrs-sdk-maven-plugin:setup-sdk`

After proper command executing you can make sure the OpenMRS SDK works fine.

`mvn openmrs-sdk:help`

It should produce the following output:

[INFO] Scanning for projects...

[INFO]

[INFO] ------------------------------------------------------------------------

[INFO] Building Maven Stub Project (No POM) 1

[INFO] ------------------------------------------------------------------------

[INFO]

[INFO] --- openmrs-sdk-maven-plugin:3.0.0:help (default-cli) @ standalone-pom ---


 
OpenMRS SDK *your-version-of-OpenMRS-SDK*

For more info, see SDK documentation: https://wiki.openmrs.org/display/docs/OpenMRS+SDK

...

If that is the case, you have successfully installed the SDK.

Building from Source
--------------------
You will need to have Java 1.8+ and Maven 3.x+ installed.  Use the command 'mvn package' to 
compile and package the module.  The .omod file will be in the omod/target folder.

Alternatively you can add the snippet provided in the [Creating Modules](https://wiki.openmrs.org/x/cAEr) page to your 
omod/pom.xml and use the mvn command:

    mvn package -P deploy-web -D deploy.path="../../openmrs-1.8.x/webapp/src/main/webapp"

It will allow you to deploy any changes to your web 
resources such as jsp or js files without re-installing the module. The deploy path says 
where OpenMRS is deployed.

Installation
------------
1.Build the module to produce the .omod file using:

`mvn clean install`

You can build .omod file with skip tests:

`mvn clean install -DskipTests`

It creates *.omod file in omod/target directory.

**Note:**
Building the module takes some time because during module building are executed static code analysis tools and the UI is building using the npm tool. If you want to build the module faster (during the developing) then you can use one (or both) of following maven profiles:
* no-npm - disable building the module UI (Note: the UI have to be at least one's time built before and the built zip file won't be deleted during maven 'clean' phase). Example of usage: `mvn clean install -P no-npm`
* dev - disable executing of static code analysis tools. Example of usage: `mvn clean install -P dev`

You can use both profiles in the same time. Example of usage:
`mvn clean install -P dev,no-npm`

Additionally if you want to skip executing the tests you can add `-DskipTests` parameter.

2.Use the OpenMRS Administration > Manage Modules screen to upload and install the .omod file.

If uploads are not allowed from the web (changable via a runtime property), you can drop the omod
into the ~/.OpenMRS/modules folder.  (Where ~/.OpenMRS is assumed to be the Application 
Data Directory that the running openmrs is currently using.)  After putting the file in there 
simply restart OpenMRS/tomcat and the module will be loaded and started.
