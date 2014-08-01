#!/bin/bash
echo "Knowledge is Power"

#JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
JAREXE="$JAVA_HOME/bin/jar"
AWD_MR_BASE=../workdesk
AWD_DEPLOY_BASE=workdesk
WARFILENAME=workdesk.war

function err_invalidLocation() {
    echo "Could not find directory $AWD_MR_BASE nor $AWD_DEPLOY_BASE. This script is supposed to be executed from within the <distribution_zip>/bin/build folder."
    exit 1
}

function err_SHOWHINT1() {
    echo "The JAVA_HOME environment variable is empty. Please make sure it is pointing to the root directory of a JDK installation (i.e. \"/usr/lib/jvm/java-7-openjdk-amd64\")."
    exit 11
}

function err_SHOWHINT2() {
    echo "The jar tool $JAREXE does not exist. Please make sure your JAVA_HOME environment variable is set correctly (i.e. \"/usr/lib/jvm/java-7-openjdk-amd64\")."
    exit 12
}

function createWar() {
    echo Creating web archive from location: $AWD_BASE.
    # *** Check if the JAVA_HOME environment variable is empty. ***
    # *** If it is, tell the user to set it and exit. ***
    if [[ -z "$JAVA_HOME" ]] 
    then
        err_SHOWHINT1
    fi
    
    # *** Check if the jar can be found. ***
    # *** If it cannot be found, tell the user and exit. ***
    if ! [[ -e "$JAREXE" ]]
    then
        err_SHOWHINT2
    fi
    
    # *** At this point the jar can be found.***
    # *** Tell user about overwriting if the war file already exists. ***
    if [[ -e $WARFILENAME ]]
    then
        echo Overwriting existing web archive.
    fi
    
    # *** Execute the jar command. ***
    $JAREXE -cf "$WARFILENAME" -C "$AWD_BASE" .
    
    # *** Tell user about successful war file creation. ***
    echo Web archive created successfully.
    exit 0    
}

if [[ -e $AWD_MR_BASE ]]
then
    AWD_BASE=$AWD_MR_BASE
    createWar
elif [[ -e $AWD_DEPLOY_BASE ]]
then
    AWD_BASE=$AWD_DEPLOY_BASE
    createWar
else
    err_invalidLocation
fi