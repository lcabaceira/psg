psg project main Readme
===

Release Management and Application Livecycle on Alfresco

This project illustrates a Release Management and Application Livecycle approach to an Alfresco project.

Pre-Requirements
-------
 
<b>1 - Credentials</b><br/>

You need to have login credentials on the Alfresco Nexus repository (artifacts.alfresco.com). You can request login credentials on the Alfresco support portal.

<b>2 - Software requirements</b><br/>
<ul>
<li>JDK 1.7 </li>
<li>Apache Maven 3</li>
</ul>

<b>3 - Configuration requirements</b><br/><br/>
During the installation of maven, a new file name settings.xml was created. This file is our entry point to the your local maven settings configuration, including the remote maven repositories.
Edit your settings.xml file and update the server’s section including the alfresco server id and your credentials.

Note that the root pom.xml references 3 different repositories : <b>alfresco-private</b>, <b>alfresco-private-snapshots</b> and <b>workdesk-internal</b>. The id of each repository must match with a server id on your settings.xml (where you specify your credentials for that server).



Root pom.xml
-------
On the projects root folder you have the heart of the project, the parent pom.xml. This is the file that aggregates your full build, including all the modules and overlays to the different applications and generates deployable artifacts ready for your release.

How to run with maven
-------
Issue the following maven command to run the project from the root

<b>mvn clean install -Prun</b> <br/>

This will build and run all the modules and it's the easiest way to build the project

