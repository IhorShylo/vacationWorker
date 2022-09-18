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
gcloud config set project vacation-worker-project
gcloud functions deploy vacation-worker-function-manual --region europe-west3  --entry-point org.bat2.vacationworker.functions.VacationWorkerFunction --runtime java17 --trigger-http --memory 512MB --allow-unauthenticated --timeout 90 --min-instances 0 --max-instances 1 --service-account vacation-worker-function@vacation-worker-project.iam.gserviceaccount.com
```

where user-function-manual is the registered name by which your function will be identified in the console, and
--entry-point specifies your function's fully qualified class name (FQN).

To view logs for your function with the gcloud CLI, use the logs read command, followed by the name of the function:

```
gcloud functions logs read vacation-worker-function-manual --region europe-west3 
```

# trello webhooks guide

trigger deployed url

```
https://europe-west3-vacation-worker-project.cloudfunctions.net/vacation-worker-function 
```

to create webhook

```
curl -X POST -H "Content-Type: application/json" \
https://api.trello.com/1/tokens/{APIToken}/webhooks/ \
-d '{
  "key": "d2dfccf7084e0a6b400742de00baa8d0",
  "callbackURL": "https://europe-west3-vacation-worker-project.cloudfunctions.net",
  "idModel":"",
  "description": "Vacation worker web hook"
}' 
```

trigger local endpoint

```
curl -X POST -H "Content-Type: application/json" \
localhost:8080 \
-d '{
  "action": {
    "id": "51f9424bcd6e040f3c002412",
    "idMemberCreator": "4fc78a59a885233f4b349bd9",
    "data": {
      "board": {
        "name": "Trello Development",
        "id": "4d5ea62fd76aa1136000000c"
      },
      "card": {
        "idShort": 1458,
        "name": "Webhooks",
        "id": "51a79e72dbb7e23c7c003778"
      },
      "voted": true
    },
    "type": "voteOnCard",
    "date": "2013-07-31T16:58:51.949Z",
    "memberCreator": {
      "id": "4fc78a59a885233f4b349bd9",
      "avatarHash": "2da34d23b5f1ac1a20e2a01157bfa9fe",
      "fullName": "Doug Patti",
      "initials": "DP",
      "username": "doug"
    }
  },
  "model": {
    "id": "4d5ea62fd76aa1136000000c",
    "name": "Trello Development",
    "desc": "Trello board used by the Trello team to track work on Trello.  How meta!\n\nThe development of the Trello API is being tracked at https://trello.com/api\n\nThe development of Trello Mobile applications is being tracked at https://trello.com/mobile",
    "closed": false,
    "idOrganization": "4e1452614e4b8698470000e0",
    "pinned": true,
    "url": "https://trello.com/b/nC8QJJoZ/trello-development",
    "prefs": {
      "permissionLevel": "public",
      "voting": "public",
      "comments": "public",
      "invitations": "members",
      "selfJoin": false,
      "cardCovers": true,
      "canBePublic": false,
      "canBeOrg": false,
      "canBePrivate": false,
      "canInvite": true
    },
    "labelNames": {
      "yellow": "Infrastructure",
      "red": "Bug",
      "purple": "Repro'd",
      "orange": "Feature",
      "green": "Mobile",
      "blue": "Verified"
    }
  }
}' 
```