if [ "$1" = "run" ] ; then
  echo "SINGLE-APP-RUNNER : Will execute the local runner including your overlays"
  echo "SINGLE-AMP-RUNNER : Running your updated Alfresco Repository"
  echo "Logging output to app_executor.log"
  #Start processes in the background
  nohup mvn clean install -Ppurge,run >app_executor.log &
else
  echo "Use app_executor.sh <run>"
fi
