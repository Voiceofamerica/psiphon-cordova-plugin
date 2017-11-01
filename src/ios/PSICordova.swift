@objc(PSICordova) class PSICordova : CDVPlugin {
  func start(command: CDVInvokedUrlCommand) {
    var pluginResult = CDVPluginResult(
      status: CDVCommandStatus_OK
    )

    self.commandDelegate!.sendPluginResult(
      pluginResult, 
      callbackId: command.callbackId
    )
  }

  func pause(command: CDVInvokedUrlCommand) {
    var pluginResult = CDVPluginResult(
      status: CDVCommandStatus_OK
    )

    self.commandDelegate!.sendPluginResult(
      pluginResult,
      callbackId: command.callbackId
    )
  }
}