const userToken = 'psokmCxKjfhk7qHLeYd1'; // Replace me
const userId = '1234';
const userName = 'Nilhcem';
const clientId = 'RKkWfsi0Z9';
const clientSecret = 'eToBzeBT7OwrPQO8mZHsZtLp1qhQbe';

const Data = {};

const Auth = {
  clients: {
    [clientId]: {
      clientId: clientId,
      clientSecret: clientSecret
    }
  },
  tokens: {
    [userToken]: {
      uid: userId,
      accessToken: userToken,
      refreshToken: userToken,
      userId: userId
    }
  },
  users: {
    [userId]: {
      uid: userId,
      name: userName,
      password: 'Follow me on #Twitter!',
      tokens: [userToken]
    }
  },
  usernames: {
    [userName]: userId
  },
  authcodes: {}
};

Data.version = 0;

Data.getUid = function (uid) {
  return Data[uid];
};

/**
 * checks if user and auth exist and match
 *
 * @param uid
 * @param authToken
 * @returns {boolean}
 */
Data.isValidAuth = function (uid, authToken) {
  return (Data.getUid(uid));
};

exports.getUid = Data.getUid;
exports.isValidAuth = Data.isValidAuth;
exports.Auth = Auth;
