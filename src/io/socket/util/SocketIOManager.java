
package io.socket.util;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketIOManager implements IOCallback {
    static final String TAG = SocketIOManager.class.getSimpleName();

    private static Handler sHandler = null;
    private static SocketIO mSocket = null;

    public SocketIOManager(Handler handler) {
        sHandler = handler;
    }

    public SocketIO connect(String url) {
        try {
            mSocket = new SocketIO(url);
            mSocket.connect(this);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mSocket;
    }

    private boolean isHandler() {
        return sHandler != null ? true : false;
    }

    private void setMessage(final String message, final int color) {
        // WebSocketHandlerのonMessageは別スレッドなのでhandlerを用いてviewの書き換えを行う
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                // TextView messageArea = (TextView)
                // activity.findViewById(R.id.message_area);
                // messageArea.setText(message);
                // messageArea.setTextColor(color);
            }
        });
    }

    @Override
    public void onDisconnect() {
        Log.i(TAG, "Connection terminated.");
        if (isHandler()) {
            Message msg = sHandler.obtainMessage(SOCKETIO_DISCONNECT);
            sHandler.sendMessage(msg);
        }
    }

    @Override
    public void onConnect() {
        Log.i(TAG, "Connection established");
        if (isHandler()) {
            Message msg = sHandler.obtainMessage(SOCKETIO_CONNECT);
            sHandler.sendMessage(msg);
        }
    }

    @Override
    public void onMessage(String data, IOAcknowledge ack) {
        Log.i(TAG, "Server said: " + data);
        if (isHandler()) {
            Message msg = sHandler.obtainMessage(SOCKETIO_MESSAGE, data);
            sHandler.sendMessage(msg);
        }
    }

    @Override
    public void onMessage(JSONObject json, IOAcknowledge ack) {
        try {
            Log.i(TAG, "Server said:" + json.toString(2));
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (isHandler()) {
            Message msg = sHandler.obtainMessage(SOCKETIO_JSON_MESSAGE, json);
            sHandler.sendMessage(msg);
        }
    }

    @Override
    public void on(String event, IOAcknowledge ack, Object... args) {
        Log.i(TAG, "Server triggered event '" + event + "'");
        if (event.equals("message")) {
            onMessage((JSONObject) args[0], null);
        }
    }

    @Override
    public void onError(SocketIOException socketIOException) {
        Log.i(TAG, "an Error occured");
        if (isHandler()) {
            Message msg = sHandler.obtainMessage(SOCKETIO_ERROR);
            sHandler.sendMessage(msg);
        }
        socketIOException.printStackTrace();
    }

    public static final int SOCKETIO_DISCONNECT = 0;
    public static final int SOCKETIO_CONNECT = 1;
    public static final int SOCKETIO_HERTBEAT = 2;
    public static final int SOCKETIO_MESSAGE = 3;
    public static final int SOCKETIO_JSON_MESSAGE = 4;
    public static final int SOCKETIO_EVENT = 5;
    public static final int SOCKETIO_ACK = 6;
    public static final int SOCKETIO_ERROR = 7;
    public static final int SOCKETIO_NOOP = 8;
}
