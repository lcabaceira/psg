if [ "$1" = "run" ] ; then
  echo "SINGLE-APP-RUNNER : Will execute the local runner including your custom Solr configuration"
  echo "SINGLE-AMP-RUNNER : Running Solr with your custom configurations"
  echo "Logging output to app_executor.log"
  #Start processes in the background
  nohup mvn clean install -Prun >app_executor.log &
else
  echo "Use app_executor.sh <run>"
fi
