psg project main Readme
===

Release Management and Application Livecycle on Alfresco

This project illustrates a Release Management and Application Livecycle approach to an Alfresco project.

Pre-Requirements
-------
 
1 - You need to have login credentials on the Alfresco Nexus repository (artifacts.alfresco.com). You can request login credentials on the Alfresco support portal.

2 - Software requirements
<ul>
<li>JDK 1.7 </li>
<li>Apache Maven 3</li>
</ul>

3 - Configuration requirements
During the installation of maven, a new file name settings.xml was created. This file is our entry point to the your local maven settings configuration, including the remote maven repositories.
Edit your settings.xml file and update the server’s section including the alfresco server id and your credentials.



Root pom.xml
-------
On the projects root folder you have the heart of the project, the parent pom.xml. This is the file that aggregates your full build, including all the modules and overlays to the different applications and generates deployable artifacts ready for your release.


