package ir.FiveMFive.FiveMFive.Utility.Checkers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ir.FiveMFive.FiveMFive.R;
import ir.FiveMFive.FiveMFive.Utility.SnackbarBuilder;

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

    public static void showNoConnectionSnack(Context c, View v) {
        SnackbarBuilder.showSnack(c, v, c.getResources().getString(R.string.warn_connection_failed), SnackbarBuilder.SnackType.WARNING);
    }
    public static void showServerFailSnack(Context c, View v) {
        String errorMessage = c.getString(R.string.error_server_no_response);
        SnackbarBuilder.showSnack(c, v, errorMessage, SnackbarBuilder.SnackType.ERROR);
    }
    public static void showConnectionFailSnack(Context c, View v, Throwable t) {
        String errorMessage;
        if(t instanceof SocketTimeoutException) {
            errorMessage = c.getString(R.string.warn_connection_failed);
        } else {
            errorMessage = c.getString(R.string.error_connecting_to_server);
        }
        SnackbarBuilder.showSnack(c, v, errorMessage, SnackbarBuilder.SnackType.ERROR);
    }
}
