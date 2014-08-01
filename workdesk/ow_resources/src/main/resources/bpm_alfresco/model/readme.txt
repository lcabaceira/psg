Alfresco Workdesk BPM Model
Copyright (c) Alfresco Software, Inc. All Rights Reserved.

Install the owdBPMModel.xml and owdBPMModel-context.xml files into your 
Alfresco system in order to use the Alfresco Workdesk BPM functionalities:
- Resubmission (property: owdbpm:resubmissionDate)
- JSP Step processor (property: owdbpm:jspStepProcessor)

Copy the files to: ...\Alfresco\tomcat\shared\classes\alfresco\extension

WARNING: this model should be only installed if there was no previous
installation of the Workdesk Contract Management Scenario AMP 
file (workdeskCMGSolutionTemplate-<version>.amp) into your system.