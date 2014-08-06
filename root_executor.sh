if [ "$1" = "create_and_run" ] ; then
  echo "Creating maven artifacts for your custom Alfresco amps and application overlays"
  echo "RUNNER : Will execute the local runner on the artifacts generated"
  echo "Logging output to root_executor.log"
  #Start processes in the background
  nohup mvn clean install -Ppurge,run >root_executor.log &
elif [ "$1" = "create_artifacts" ] ; then
   echo "Creating maven artifacts for your custom Alfresco amps and application overlays"
   echo "Logging output to root_executor.log"
  nohup mvn clean install -Ppurge >root_executor.log &
else
  echo "Use root_executor.sh <create_and_run|create_artifacts>"
fi
