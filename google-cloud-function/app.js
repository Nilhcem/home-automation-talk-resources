// Initialize Cloud Firestore
const admin = require('firebase-admin');
admin.initializeApp({
    credential: admin.credential.applicationDefault()
});
var db = admin.firestore();

// User Id in Firestore
const userId = 'user-id';

exports.ha = function(req, res) {
  console.log(`About to get data for userId: ${userId}`);
  var docRef = db.doc(`users/${userId}/devices/lightbulb`);

  docRef.get()
      .then(doc => {
          if (!doc.exists) {
            console.log('No such document!');
          } else {
            var data = doc.data();
            console.log('Document data:', data);

            if (req.query.on) {
              data.on = req.query.on === 'true';
            }

            if (req.query.spectrumRGB) {
              data.spectrumRGB = req.query.spectrumRGB;
            }

            docRef.set(data);
            res.send('OK!');
          }
      })
      .catch(err => {
          console.log('Error getting document', err);
      });
};
