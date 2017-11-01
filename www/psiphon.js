var exec = require('cordova/exec');

module.exports = {
  pause: function (name, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Psiphon", "pause", []);
  },
  start: function (name, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Psiphon", "start", []);
  }
};
