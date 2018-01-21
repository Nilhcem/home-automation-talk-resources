// Change those 2 values:
const userId = 'tP7PVhRDNHTgznofXFJPS6VDem83'; // User ID in Google Cloud Firestore
const expectedToken = 'psokmCxKjfhk7qHLeYd1'; // OAuth2 token from the fake-oauth2-server

// Initialize Cloud Firestore
const admin = require('firebase-admin');
admin.initializeApp({
    credential: admin.credential.applicationDefault()
});
var db = admin.firestore();

// HTTP POST /ha
exports.ha = function(req, res) {
  let reqBody = req.body;
  let authToken = req.headers.authorization ? req.headers.authorization.split(' ')[1] : null;
  let intent = (authToken === expectedToken) ? reqBody.inputs[0].intent : null;

  switch (intent) {
    case 'action.devices.SYNC':
      sync(reqBody, res);
      break;
    case 'action.devices.QUERY':
      query(reqBody, res);
      break;
    case 'action.devices.EXECUTE':
      execute(reqBody, res);
      break;
    default:
      showError(res, 'Missing intent');
      break;
  }
};

function sync(reqBody, res) {
  let resBody = {
    requestId: reqBody.requestId,
    payload: {
      devices: [{
        id: 'lightbulb',
        type: 'action.devices.types.LIGHT',
        traits: [
          'action.devices.traits.OnOff',
          'action.devices.traits.ColorSpectrum'
        ],
        name: {
          name: 'Lightbulb'
        },
        willReportState: true
      }]
    }
  };
  res.status(200).json(resBody);
}

function query(reqBody, res) {
  getDevicesDataFromFirebase(res, devices => {
    let resBody = {
      requestId: reqBody.requestId,
      payload: {
        devices: {
          'lightbulb': {
            on: devices['lightbulb'].on,
            online: true,
            color: {
              spectrumRGB: devices['lightbulb'].spectrumRGB
            }
          }
        }
      }
    };
    res.status(200).json(resBody);
  });
}

function execute(reqBody, res) {
  getDevicesDataFromFirebase(res, devices => {
    let deviceIdsToUpdate = new Set();
    let bodyCommands = [];

    reqBody.inputs[0].payload.commands.forEach(command => {
      command.execution.forEach(execution => {
        command.devices.forEach(device => {
          let deviceId = device.id;
          let success = true;

          switch (execution.command) {
            case 'action.devices.commands.OnOff':
              devices[deviceId].on = execution.params.on;
              break;
            case 'action.devices.commands.ColorAbsolute':
              devices[deviceId].spectrumRGB = execution.params.color.spectrumRGB;
              break;
            default:
              success = false;
              break;
            }

            if (success) {
              deviceIdsToUpdate.add(deviceId);
              bodyCommands.push({ids: [ deviceId ], status: 'SUCCESS'});
            } else {
              bodyCommands.push({ids: [ deviceId ], status: 'ERROR'});
            }
        });
      });
    });

    persistDevicesDataToFirebase(devices, deviceIdsToUpdate);

    let resBody = {
      requestId: reqBody.requestId,
      payload: {
        commands: bodyCommands
      }
    };
    res.status(200).json(resBody);
  });
}

function getDevicesDataFromFirebase(res, action) {
  var devicesRef = db.collection(`users/${userId}/devices`);

  devicesRef.get()
    .then(snapshot => {
      let devices = new Object();
      snapshot.forEach(doc => {
        devices[doc.id] = doc.data();
      });
      action(devices);
    })
    .catch(err => {
      console.log('Error getting documents', err);
      showError(res, 'Error getting devices data');
    });
}

function persistDevicesDataToFirebase(data, deviceIdsToUpdate) {
  deviceIdsToUpdate.forEach(deviceId => {
    db.doc(`users/${userId}/devices/${deviceId}`).set(data[deviceId]);
  });
}

function showError(res, message) {
  res.status(401).set({
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization'
  }).json({error: message});
}
