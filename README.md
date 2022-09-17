# vacationWorker

https://developers.google.com/api-client-library/java
https://developer.atlassian.com/cloud/trello/rest/api-group-actions/

# google fucntions guide

For all the details see https://cloud.google.com/functions/docs/running/function-frameworks

The Rest api is inspired by https://help.sonatype.com/iqserver/automating/rest-apis/user-rest-api---v2

Install Google Cloud SDK as in guide at https://cloud.google.com/sdk/docs/install

Run the following command to confirm that your function builds:

```
gradlew build
```

To test the function, run the following command:

```
./gradlew runFunction -Prun.functionTarget=org.bat2.vacationworker.functions.VacationWorkerFunction
```

To run in debug mode, run the following command:

```
./gradlew runFunction -Prun.functionTarget=org.bat2.vacationworker.functions.VacationWorkerFunction --debug-jvm
```

If testing completes successfully, it displays the URL you can visit in your web browser to see the function in
action: http://localhost:8080/. You should see a Hello World! message.

Alternatively, you can send requests to this function using curl from another terminal window: (commands are escaped for
windows)

```
curl -i localhost:8080

```

To deploy the function with an HTTP trigger, run the following command in the helloworld-gradle directory:

```
gcloud auth login
gcloud config set project PROJECT_ID
gcloud functions deploy user-function-manual --region europe-west3  --entry-point functions.UserFunction --runtime java17 --trigger-http --memory 512MB --allow-unauthenticated --timeout 90 --min-instances 0 --max-instances 1 --service-account user-function@for-developers-358919.iam.gserviceaccount.com
```

where user-function-manual is the registered name by which your function will be identified in the console, and
--entry-point specifies your function's fully qualified class name (FQN).

To view logs for your function with the gcloud CLI, use the logs read command, followed by the name of the function:

```
gcloud functions logs read user-function-manual --region europe-west3 
```
