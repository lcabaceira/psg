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
<li>Apache Maven 3.0.4+</li>
</ul>

<b>3 - Configuration requirements</b><br/><br/>
During the installation of maven, a new file name settings.xml was created. This file is our entry point to the your local maven settings configuration, including the remote maven repositories.
Edit your settings.xml file and update the server’s section including the alfresco server id and your credentials.

Note that the root pom.xml references 3 different repositories : <b>alfresco-private</b>, <b>alfresco-private-snapshots</b> and <b>workdesk-internal</b>. The id of each repository must match with a server id on your settings.xml (where you specify your credentials for that server).

Section from the settings.xml maven configuration file

```xml
...
        <repository>
            <id>alfresco-private</id>
            <url>https://artifacts.alfresco.com/nexus/content/groups/private</url>
        </repository>
        <repository>
            <id>alfresco-private-snapshots</id>
            <url>https://artifacts.alfresco.com/nexus/content/groups/private-snapshots</url>
        </repository>
        
         <repository>
            <id>workdesk-internal</id>
            <url>https://artifacts.alfresco.com/nexus/content/groups/workdesk/</url>
        </repository> 
 ...
```

Section from the root pom.xml

```xml
...
        <server>
            <id>alfresco-private</id>
            <username>YOUR_USERNAME</username>
            <password>YOUR_PASSWORD</password>
        </server>
        <server>
            <id>alfresco-private-snapshots</id>
            <username>YOUR_USERNAME</username>
            <password>YOUR_PASSWORD</password>
        </server>
        
        <server>
            <id>workdesk-internal</id>
            <username>YOUR_USERNAME</username>
            <password>YOUR_PASSWORD</password>
        </server> 
 ...
```

configuration for developers 
-------
Each developer must be able to commit, checkout and perform operations on de source code, for that he needs to have a valid username on the source control and permissions on the target project.
Configure your source control mechanims and credentials on the scm section of the root pom.xml. 
The default configuration uses my git username and our git project, adapt your username accordingly.

```xml
 <scm>
      <connection>scm:git:git@github.com:lcabaceira/psg.git</connection>
      <url>scm:git:git@github.com:lcabaceira/psg.git</url>
      <developerConnection>scm:git:git@github.com:lcabaceira/psg.git</developerConnection>
      <tag>HEAD</tag>
    </scm>
```


Extra configuration for  release managers
-------
If you are the release manager or member of the release management team you need to configure the maven repository that will be used to store your released artifacts whenever you do a release. You need to configure two new repositories on the parent pom and 2 servers on your local settings.xml file.

Section from the settings.xml maven configuration file


```xml
  <server>
        <id>cloudbees-private-snapshot-repository</id>
        <username>YOUR_PRIVATE_MAVEN_REPOSITORY_USERNAME</username>
        <password>YOUR_PRIVATE_MAVEN_REPOSITORY_PASSWORD</password>
        <filePermissions>664</filePermissions>
        <directoryPermissions>775</directoryPermissions>
    </server>
    <server>
        <id>cloudbees-private-release-repository</id>
        <username>YOUR_PRIVATE_MAVEN_REPOSITORY_USERNAME</username>
        <password>YOUR_PRIVATE_MAVEN_REPOSITORY_PASSWORD</password>
        <filePermissions>664</filePermissions>
        <directoryPermissions>775</directoryPermissions>
    </server>
```

Section from the root pom.xml

```xml
    <distributionManagement>
        <repository>
          <id>cloudbees-private-release-repository</id>
          <url>dav:https://repository-lcabaceira.forge.cloudbees.com/release/</url>
        </repository>
        <snapshotRepository>
          <id>cloudbees-private-snapshot-repository</id>
          <url>dav:https://repository-lcabaceira.forge.cloudbees.com/release/</url>
        </snapshotRepository>
    </distributionManagement>
```

This will enable you to perform releases with 
````xml
Prepare release              : mvn release:prepare 
Perform release              : mvn release:perform
Prepare and Perform  release : mvn release:prepare release:perform
````


Root pom.xml
-------
On the projects root folder you have the heart of the project, the parent pom.xml. This is the file that aggregates your full build, including all the modules and overlays to the different applications and generates deployable artifacts ready for your release.

What is included in this project build ?
-------
<ul>
<li>
applications(apps folder)</li>
<ul>
<li><a href="https://github.com/lcabaceira/psg/tree/master/apps/psg-alfresco">alfresco respository overlay module</a> </li>
<li><a href="https://github.com/lcabaceira/psg/tree/master/apps/psg-share">alfresco share overlay module </a></li>
<li><a href="https://github.com/lcabaceira/psg/tree/master/apps/psg-workdesk">alfresco workdesk overlay module</a> </li>
<li><a href="https://github.com/lcabaceira/psg/tree/master/apps/psg-solr-configuration">alfresco solr configuration overlay module </a></li>
</ul>
<li>alfresco module packages(amps folder)</li>
<ul>
<li><a href="https://github.com/lcabaceira/psg/tree/master/amps/psg-content-model-repo-amp">alfresco respository extension amp module </a> </li>
<li><a href="https://github.com/lcabaceira/psg/tree/master/amps/psg-ui-customization-share-amp">alfresco share extension amp module </a></li>
</ul>
</ul>

How to run with maven ?
-------
Issue the following maven command to run the project from the root

<b>mvn clean install -Prun</b> <br/>

This will build and run all the modules and it's the easiest way to build the project



