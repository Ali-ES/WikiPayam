package ir.FiveMFive.FiveMFive.Utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;

import ir.FiveMFive.FiveMFive.R;

public class ConnectivityChecker {
    public static final String TAG = "ConnectivityChecker";
    public static final String HOST = "5m5.ir";
    private final ConnectionListener connectionListener;
    public interface ConnectionListener {
        void isConnected(boolean status);
    }
    public ConnectivityChecker(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
    public void checkConnection(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()) {
            Thread thread = new Thread(() -> {
                try {
                    InetAddress ipAddr = InetAddress.getByName(HOST);
                    activity.runOnUiThread(() -> connectionListener.isConnected(!ipAddr.equals("")));
                } catch (UnknownHostException e) {
                    activity.runOnUiThread(() -> connectionListener.isConnected(false));

                }
            });
            thread.start();
        } else {
            connectionListener.isConnected(false);
        }

    }

    public static void showConnectionFailSnack(Context c, View v) {
        SnackbarBuilder.showSnack(c, v, c.getResources().getString(R.string.warn_connection_failed), SnackbarBuilder.SnackType.WARNING);
    }

}
