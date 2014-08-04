if [ "$1" = "create_and_run" ] ; then
  echo "Creating maven artifacts for your custom Alfresco amp"
  echo "SINGLE-AMP-RUNNER : Will execute the local runner on the artifacts generated."
  echo "SINGLE-AMP-RUNNER : Using MMT to install your custom AMP contents to the Alfresco Share web-application"
  echo "SINGLE-AMP-RUNNER : Running your updated the alfresco Share web-application"
  echo "Logging output to maven_amp_executor.log"
  #Start processes in the background
  nohup mvn clean install -Pamp-to-war >maven_amp_executor.log &
elif [ "$1" = "create_artifacts" ] ; then
   echo "Creating maven artifacts for your custom Share amp"
   echo "Logging output to maven_amp_executor"
  nohup mvn clean install >maven_amp_executor.log &
else
  echo "Use amp_executor.sh <create_and_run|create_artifacts>"
fi
