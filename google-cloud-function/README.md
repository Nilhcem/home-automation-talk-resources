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
