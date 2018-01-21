# Fake-oauth2-server

This project helps setting up quickly "Actions on Google Smart Home with Account linking for testing purposes / individual usage.  
Not recommended for production. Use a real OAuth2 server instead.

## Configuration

- Open the `datastore.js` file and replace occurrences to the `psokmCxKjfhk7qHLeYd1` token with a randomly generated token
- Modify the `../google-cloud-function/app.js` `expectedToken` value with your new token
- Deploy the server locally with `npm install; node app.js`
- Use ngrok to expose the server to the Internet with `ngrok http 3000`
- In your Android phone's Home control settings, add your Actions on Google test device, and login
- Once done, the Google Assistant now has a token. Stop the OAuth2 server. You won't need it anymore.
