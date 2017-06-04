package com.example.adidas.bluetooth20.Bluetooth;

/**
 * Created by Adidas on 2017/5/13.
 */
public interface Constants {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final int STATE_NOT_CONNECTED=0;
    public static final int STATE_CONNECTED=1;
    public static final int STATE_CONNECTING=2;

}
