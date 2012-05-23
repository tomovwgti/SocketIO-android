
package com.tomovwgti.socketio;

import io.socket.SocketIO;
import io.socket.util.SocketIOManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class SampleSocketIOActivity extends Activity {
    static final String TAG = SampleSocketIOActivity.class.getSimpleName();

    private SocketIO mSocket;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketIOManager.SOCKETIO_DISCONNECT:
                    Log.i(TAG, "SOCKETIO_DISCONNECT");
                    break;
                case SocketIOManager.SOCKETIO_CONNECT:
                    Log.i(TAG, "SOCKETIO_CONNECT");
                    break;
                case SocketIOManager.SOCKETIO_HERTBEAT:
                    Log.i(TAG, "SOCKETIO_HERTBEAT");
                    break;
                case SocketIOManager.SOCKETIO_MESSAGE:
                    Log.i(TAG, "SOCKETIO_MESSAGE");
                    break;
                case SocketIOManager.SOCKETIO_JSON_MESSAGE:
                    Log.i(TAG, "SOCKETIO_JSON_MESSAGE");
                    break;
                case SocketIOManager.SOCKETIO_EVENT:
                    Log.i(TAG, "SOCKETIO_EVENT");
                    break;
                case SocketIOManager.SOCKETIO_ERROR:
                    Log.i(TAG, "SOCKETIO_ERROR");
                    break;
                case SocketIOManager.SOCKETIO_ACK:
                    Log.i(TAG, "SOCKETIO_ACK");
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final SocketIOManager socketManager = new SocketIOManager(mHandler);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket = socketManager.connect("http://192.168.1.3:3000");
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("value", "Hello SocketIO");
                    mSocket.emit("message", json);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
