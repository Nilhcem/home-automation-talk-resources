# google-cloud-function

A Cloud Function that updates a Firebase Cloud Firestore when receiving device Actions on Google Smart Home traits


## Description

* `01-simple`: Compatible with a single lightbulb
* `02-advanced`: Compatible with a lightbulb, a 3dlamp, a vacuum, and an outlet


## Configuration

* Enable the Cloud Functions API and install the Google Cloud SDK ([quickstart guide](https://cloud.google.com/functions/docs/quickstart))
* Deploy the `ha` (home automation) function with the following command:
```bash
gcloud beta functions deploy ha --trigger-http
```
When the function finishes deploying, take note of the httpsTrigger's url property, then try to access it on your web browser:
[https://us-central1-<PROJECT ID>.cloudfunctions.net/ha?on=true&spectrumRGB=255]()

* To consult the logs:
```bash
gcloud beta functions logs read
```

* To delete the cloud function:
```bash
gcloud beta functions delete ha
```
