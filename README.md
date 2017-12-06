# Cordova Psiphon Plugin
## A plugin to send all traffic from cordova web view through a Psiphon tunnel

--------------------------

### Installation

To install the plugin, issue the following command in your cordova app directory:

```
cordova plugin add @voiceofamerica/psiphon-cordova-plugin
```

### Usage

NOTE:
* The psiphon tunnel will not be started until you start it manually.
* Like most cordova plugins, you must wait for the `deviceready` event before you can start using this plugin.

#### Configuring & Starting tunnel

**ES 2016**
```javascript
import { config, start, pause } from '@voiceofamerica/psiphon-cordova-plugin'

// your psiphon configuration file
import psiphonConfig from './psiphon-config.json'

document.addEventListener("deviceready", () => {
  config(psiphonConfig, () => {
    start(() => {
      alert('psiphon tunnel opened!')
    })
  })
}, false)
```

**ES pre-2016**
```javascript
// your psiphon configuration file
var psiphonConfig = '{ ... }'

document.addEventListener("deviceready", function () {
  window.psiphon.config(psiphonConfig, function () {
    window.psiphon.start(function () {
      alert('psiphon tunnel opened!')
    })
  })
}, false)
```

#### Additional notes

Any traffic that is made before the `start()` function is called will not be made through the tunnel.  To guarantee that all requests are made through the tunnel, make sure this call is made before any network requests.
