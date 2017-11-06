
module.exports = {
  config: function (configObject, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Psiphon", "config", [configObject])
  },
  pause: function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Psiphon", "pause", [])
  },
  start: function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "Psiphon", "start", [])
  },
}
