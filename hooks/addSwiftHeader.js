
var child_process = require('child_process')
var fs = require('fs')
var path = require('path')
var prependFile = require('prepend-file')

const PLUGIN_ID = 'psiphon-cordova-plugin'

module.exports = function(context) {
  // Ensure we only run this after this plugin itself is run
  if (!(context.opts.plugin.id === PLUGIN_ID && context.opts.plugin.platform === 'ios')) {
    return
  }

  var projectRoot = context.opts.projectRoot
  var projectName = getConfigParser(context, path.join(projectRoot, 'config.xml')).name()
  var platformPath = path.join(projectRoot, 'platforms', 'ios')
  var projectPath = path.join(platformPath, projectName)
  var pluginsPath = path.join(projectPath, 'Plugins')
  var ownPath = path.join(pluginsPath, PLUGIN_ID, 'PSICordova')
  var fileNeedingHeader = path.join(ownPath, 'Psiphon', 'JAHPAuthenticatingHTTPProtocol.m')

  prependFile(fileNeedingHeader, '#import "' +projectName + '-Swift.h"\n', function(err) {
    if (err) {
      console.log('Something went wrong')
    }
  })
}

function getConfigParser(context, config){
  var semver = context.requireCordovaModule('semver');

  if(semver.lt(context.opts.cordova.version, '5.4.0')) {
    ConfigParser = context.requireCordovaModule('cordova-lib/src/ConfigParser/ConfigParser');
  } else {
    ConfigParser = context.requireCordovaModule('cordova-common/src/ConfigParser/ConfigParser');
  }

  return new ConfigParser(config);
}

function unquote(str) {
  if (str) return str.replace(/^"(.*)"$/, "$1");
}