
package ca.psiphon.plugin;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import ca.psiphon.PsiphonTunnel;

public class Psiphon extends CordovaPlugin implements PsiphonTunnel.HostService {

  private AtomicInteger mLocalHttpProxyPort;

  private PsiphonTunnel mPsiphonTunnel;

  private String config = "{}";

  @Override
  protected void pluginInitialize() {
    mLocalHttpProxyPort = new AtomicInteger(0);
    mPsiphonTunnel = PsiphonTunnel.newPsiphonTunnel(this);
  }

  @Override
  public void onDestroy() {
    mPsiphonTunnel.stop();
  }

  @Override
  public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {

    if (action.equals("config")) {
      config = data.getJSONObject(0).toString();
      callbackContext.success();

      return true;
    } else if (action.equals("pause")) {
      cordova.getThreadPool().execute(new Runnable() {
        public void run() {
          mPsiphonTunnel.stop();
          callbackContext.success();
        }
      });

      return true;
    } else if (action.equals("start")) {
      cordova.getThreadPool().execute(new Runnable() {
        public void run() {
          try {
            mPsiphonTunnel.startTunneling("");
            callbackContext.success();
          } catch (PsiphonTunnel.Exception e) {
            logMessage("failed to start Psiphon");
            callbackContext.error(e.getMessage());
          }
        }
      });

      return true;
    } else {
      return false;
    }
  }

  //----------------------------------------------------------------------------------------------
  // NOTE: these are callbacks from the Psiphon Library
  //----------------------------------------------------------------------------------------------

  @Override
  public String getAppName() {
    return "Voice of America";
  }

  @Override
  public Context getContext() {
    return cordova.getActivity();
  }

  @Override
  public Object getVpnService() {
    return null;
  }

  @Override
  public Object newVpnServiceBuilder() {
    return null;
  }

  @Override
  public String getPsiphonConfig() {
    return config;
  }

  @Override
  public void onDiagnosticMessage(String message) {
    logMessage(message);
  }

  @Override
  public void onAvailableEgressRegions(List<String> regions) {
    for (String region : regions) {
      logMessage("available egress region: " + region);
    }
  }

  @Override
  public void onSocksProxyPortInUse(int port) {
    logMessage("local SOCKS proxy port in use: " + Integer.toString(port));
  }

  @Override
  public void onHttpProxyPortInUse(int port) {
    logMessage("local HTTP proxy port in use: " + Integer.toString(port));
  }

  @Override
  public void onListeningSocksProxyPort(int port) {
    logMessage("local SOCKS proxy listening on port: " + Integer.toString(port));
  }

  @Override
  public void onListeningHttpProxyPort(int port) {
    logMessage("local HTTP proxy listening on port: " + Integer.toString(port));
    setHttpProxyPort(port);
  }

  @Override
  public void onUpstreamProxyError(String message) {
    logMessage("upstream proxy error: " + message);
  }

  @Override
  public void onConnecting() {
    logMessage("connecting...");
  }

  @Override
  public void onConnected() {
    logMessage("connected");
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        WebViewProxySettings.setLocalProxy(getContext(), mLocalHttpProxyPort.get());
      }
    });
  }

  @Override
  public void onHomepage(String url) {
    logMessage("home page: " + url);
  }

  @Override
  public void onClientUpgradeDownloaded(String filename) {
    logMessage("client upgrade downloaded");
  }

  @Override
  public void onClientIsLatestVersion() {

  }

  @Override
  public void onSplitTunnelRegion(String region) {
    logMessage("split tunnel region: " + region);
  }

  @Override
  public void onUntunneledAddress(String address) {
    logMessage("untunneled address: " + address);
  }

  @Override
  public void onBytesTransferred(long sent, long received) {
    logMessage("bytes sent: " + Long.toString(sent));
    logMessage("bytes received: " + Long.toString(received));
  }

  @Override
  public void onStartedWaitingForNetworkConnectivity() {
    logMessage("waiting for network connectivity...");
  }

  @Override
  public void onClientVerificationRequired(String s, int i, boolean b) {

  }

  @Override
  public void onExiting() {

  }

  @Override
  public void onClientRegion(String region) {
    logMessage("client region: " + region);
  }

  private static String readInputStreamToString(InputStream inputStream) throws IOException {
    return new String(readInputStreamToBytes(inputStream), "UTF-8");
  }

  private static byte[] readInputStreamToBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    int readCount;
    byte[] buffer = new byte[16384];
    while ((readCount = inputStream.read(buffer, 0, buffer.length)) != -1) {
      outputStream.write(buffer, 0, readCount);
    }
    outputStream.flush();
    inputStream.close();
    return outputStream.toByteArray();
  }

  private void logMessage(final String message) {
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        android.util.Log.i("Psiphon", message);
      }
    });
  }

  private void setHttpProxyPort(int port) {
    mLocalHttpProxyPort.set(port);
  }
}
