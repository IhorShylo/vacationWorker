# vacationWorker

https://docs.spring.io/spring-cloud-function/docs/current/reference/html/spring-cloud-function.html#_function_catalog_and_flexible_function_signatures
https://developer.atlassian.com/cloud/trello/rest/api-group-actions/

# google functions guide

For all the details see https://cloud.google.com/functions/docs/running/function-frameworks

The Rest api is inspired by https://help.sonatype.com/iqserver/automating/rest-apis/user-rest-api---v2

Install Google Cloud SDK as in guide at https://cloud.google.com/sdk/docs/install

## gradle (currently not in use)

Run the following command to confirm that your function builds:

```
./gradlew build
```

To test the function, run the following command:

```
./gradlew runFunction -Prun.functionTarget=org.bat2.vacationworker.functions.VacationWorkerFunction
```

To run in debug mode, run the following command:

```
./gradlew runFunction -Prun.functionTarget=org.bat2.vacationworker.functions.VacationWorkerFunction --debug-jvm
```

## maven

Run the following command to confirm that your function builds:

```
./mvnw compile
```

Another option is to use the mvn package command to compile your Java code, run any tests, and package the code up in a
JAR file within the target directory. You can learn more about the Maven build lifecycle here.

To run the function locally, run the following command:

```
./mvnw function:run
```

To test vacation worker function run bash script

```
src/test/resources/localTest.sh

```

## Deploy to GCP

To deploy the function with an HTTP trigger, run the following command in the helloworld-gradle directory:

```
gcloud auth login
gcloud config set project vacation-worker-project
gcloud functions deploy vacation-worker-function --region europe-west3  --entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher --runtime java17 --trigger-http --source target/deploy --memory 512MB --allow-unauthenticated --timeout 90 --min-instances 0 --max-instances 1 --service-account vacation-worker-function@vacation-worker-project.iam.gserviceaccount.com
```

where vacation-worker-function is the registered name by which your function will be identified in the console, and
--entry-point specifies your function's fully qualified class name (FQN).

To view logs for your function with the gcloud CLI, use the logs read command, followed by the name of the function:

```
gcloud functions logs read vacation-worker-function --region europe-west3 
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
  "model": {
    "id": "62f227668555a62731adef73",
    "name": "?????????????????? ?? ?????????????? ??????????????????????",
    "closed": false,
    "idBoard": "62f227668555a62731adef6a",
    "pos": 49152
  },
  "action": {
    "id": "6326fe2a8e0327015cd59891",
    "idMemberCreator": "58ff63c4de44163bb34295c8",
    "data": {
      "card": {
        "idList": "62f227668555a62731adef72",
        "id": "632450e2022d65013426ef80",
        "name": "26: ???????????? ???????????? ???????????????????? ?? 20.09.2022 ???????? ???? 3+1",
        "idShort": 285,
        "shortLink": "Vd77nebv"
      },
      "old": {
        "idList": "62f227668555a62731adef73"
      },
      "board": {
        "id": "62f227668555a62731adef6a",
        "name": "??????????????????",
        "shortLink": "oFHklB3l"
      },
      "listBefore": {
        "id": "62f227668555a62731adef73",
        "name": "?????????????????? ?? ?????????????? ??????????????????????"
      },
      "listAfter": {
        "id": "62f227668555a62731adef72",
        "name": "???? ???????????????? ?? ?????????? ??????????"
      }
    },
    "appCreator": null,
    "type": "updateCard",
    "date": "2022-09-18T11:16:58.693Z",
    "limits": null,
    "display": {
      "translationKey": "action_move_card_from_list_to_list",
      "entities": {
        "card": {
          "type": "card",
          "idList": "62f227668555a62731adef72",
          "id": "632450e2022d65013426ef80",
          "shortLink": "Vd77nebv",
          "text": "26: ???????????? ???????????? ???????????????????? ?? 20.09.2022 ???????? ???? 3+1"
        },
        "listBefore": {
          "type": "list",
          "id": "62f227668555a62731adef73",
          "text": "?????????????????? ?? ?????????????? ??????????????????????"
        },
        "listAfter": {
          "type": "list",
          "id": "62f227668555a62731adef72",
          "text": "???? ???????????????? ?? ?????????? ??????????"
        },
        "memberCreator": {
          "type": "member",
          "id": "58ff63c4de44163bb34295c8",
          "username": "ihorshylo",
          "text": "Tofus"
        }
      }
    },
    "memberCreator": {
      "id": "58ff63c4de44163bb34295c8",
      "activityBlocked": false,
      "avatarHash": "c4cd8a017262481e1d7bf18d0c91f7e4",
      "avatarUrl": "https://trello-members.s3.amazonaws.com/58ff63c4de44163bb34295c8/c4cd8a017262481e1d7bf18d0c91f7e4",
      "fullName": "Tofus",
      "idMemberReferrer": null,
      "initials": "T",
      "nonPublic": {},
      "nonPublicAvailable": true,
      "username": "ihorshylo"
    }
  }
}' 
```
