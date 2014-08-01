#!/bin/bash
echo "Knowledge is Power"

#JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
JAREXE="$JAVA_HOME/bin/jar"
MWD_MR_BASE=../mobileworkdesk
MWD_DEPLOY_MWDBASE=mobileworkdesk
WARFILENAME=mobileworkdesk.war

function err_invalidLocation() {
    echo "Could not find directory $MWD_MR_BASE nor $MWD_DEPLOY_MWDBASE. This script is supposed to be executed from within the <distribution_zip>/bin/build folder."
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
    echo Creating web archive from location: $MWD_BASE.
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
	echo "Overwriting existing web archive.)"
    fi

    # *** Execute the jar command. ***
    $JAREXE -cf "$WARFILENAME" -C "$MWD_BASE" .

    # *** Tell user about successful war file creation. ***
    echo Web archive created successfully.
}

if [[ -e $MWD_MR_BASE ]]
then
    MWD_BASE=$MWD_MR_BASE
    createWar
elif [[ -e $MWD_DEPLOY_MWDBASE ]]
then
    MWD_BASE=$MWD_DEPLOY_MWDBASE
    createWar
else
    err_invalidLocation
fi
