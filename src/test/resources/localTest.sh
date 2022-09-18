curl -X POST -H "Content-Type: application/json" \
localhost:8080 \
-d '{
  "model": {
    "id": "62f227668555a62731adef73",
    "name": "Погоджено з номером відпускного",
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
        "name": "26: Гришко Василь Михайлович з 20.09.2022 року на 3+1",
        "idShort": 285,
        "shortLink": "Vd77nebv"
      },
      "old": {
        "idList": "62f227668555a62731adef73"
      },
      "board": {
        "id": "62f227668555a62731adef6a",
        "name": "Відпустки",
        "shortLink": "oFHklB3l"
      },
      "listBefore": {
        "id": "62f227668555a62731adef73",
        "name": "Погоджено з номером відпускного"
      },
      "listAfter": {
        "id": "62f227668555a62731adef72",
        "name": "На розгляді в штабі полку"
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
          "text": "26: Гришко Василь Михайлович з 20.09.2022 року на 3+1"
        },
        "listBefore": {
          "type": "list",
          "id": "62f227668555a62731adef73",
          "text": "Погоджено з номером відпускного"
        },
        "listAfter": {
          "type": "list",
          "id": "62f227668555a62731adef72",
          "text": "На розгляді в штабі полку"
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