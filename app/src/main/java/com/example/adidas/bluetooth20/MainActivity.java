package com.example.adidas.bluetooth20;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.adidas.bluetooth20.Bluetooth.BluetoothActivity;
import com.example.adidas.bluetooth20.Bluetooth.BluetoothChatService;
import com.example.adidas.bluetooth20.Bluetooth.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BluetoothActivity implements Constants{


    private int testnum=0;
    private FragmentManager fm;
    public FragmentSend fragmentSend;
    public FragmentGet fragmentGet;
    public FragmentBluetooth fragmentBluetooth;

    private Toolbar toolbar;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;

    public int state;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mBTAdapter= BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter==null){
            Toast.makeText(this, "Not supported", Toast.LENGTH_LONG).show();
        }

        if (!mBTAdapter.isEnabled()){
            Intent enableBTIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent,REQUEST_ENABLE_BT);
        }


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Timer timer=new Timer(true);
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                fm=getSupportFragmentManager();
                fragmentSend = (FragmentSend) mSectionsPagerAdapter.instantiateItem(mViewPager,2);
                fragmentGet= (FragmentGet) mSectionsPagerAdapter.instantiateItem(mViewPager,1);
                fragmentBluetooth= (FragmentBluetooth) mSectionsPagerAdapter.instantiateItem(mViewPager,0);
                fragmentBluetooth.setOnDiscoverable(new FragmentBluetooth.OnDiscoverable() {
                    @Override
                    public void discover() {
                        ensureDiscoverable();
                    }
                });
            }
        };

        timer.schedule(task,100);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            displayAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void setupChat() {

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    protected void setStatus(int resId) {
/*        FragmentActivity activity = MainActivity.this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();*/
        if (null == toolbar) {
            return;
        }
        toolbar.setSubtitle(resId);
    }

    @Override
    protected void setStatus(CharSequence subTitle) {
/*        FragmentActivity activity = MainActivity.this;
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();*/
        if (null == toolbar) {
            return;
        }
        toolbar.setSubtitle(subTitle);
    }

    @Override
    protected void subToast(String text) {
        Toast.makeText(MainActivity.this,text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;

        }
    }

    public void connectDevice(Intent data, boolean secure){
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return FragmentBluetooth.newInstance(position);
                case 1:
                    return FragmentGet.newInstance(position);
                case 2:
                    return FragmentSend.newInstance(position);

            }
            return FragmentBluetooth.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "蓝牙设置";
                case 1:
                    return "接收测试";
                case 2:
                    return "输出测试";
            }
            return null;
        }
    }

    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            state=STATE_CONNECTED;
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            state=STATE_CONNECTING;
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            state=STATE_NOT_CONNECTED;
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    /*mConversationArrayAdapter.add("Me:  " + writeMessage);*/
                    /*onSendTextChange.OnSendTextChange("Me:  " +writeMessage);*/
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    int length=analyse(readMessage);
                    if (length==7){

                    }else{
                    onReceiveMessage.readMsg(readMessage);
                        try{
                            fragmentGet.onOrderReceived.receiveOrder();
                        }catch (Exception e){

                        }

                    }
                    /*onGetTextChange.OnGetTextChange(mConnectedDeviceName + ":  "+readMessage);*/
                   /* mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);*/
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                    Toast.makeText(getBaseContext(), "连接到 " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    break;
                case Constants.MESSAGE_TOAST:

                    Toast.makeText(getBaseContext(), msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    public interface OnReceiveMessage{
        void readMsg(String msg);
    }

    private OnReceiveMessage onReceiveMessage;

    public void setOnReceiveMessage(OnReceiveMessage onReceiveMessage){
        this.onReceiveMessage=onReceiveMessage;
    }

    public interface OnReceiveOrder{
        void onOrder(String msg);
    }

    private OnReceiveOrder onReceiveOrder;

    public void setOnReceiveOrder(OnReceiveOrder onReceiveOrder){
        this.onReceiveOrder=onReceiveOrder;
    }

    private int analyse(String message){
        String[] strs=new String[]{};
        strs=message.split(",");
        return strs.length;
    }

    private void displayAboutDialog() {
        final int paddingSizeDp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        final int dpAsPixels = (int) (paddingSizeDp * scale + 0.5f);

        final TextView textView = new TextView(this);
        final SpannableString text = new SpannableString(getString(R.string.about_dialog_text));

        textView.setText(text);
        textView.setAutoLinkMask(RESULT_OK);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

        Linkify.addLinks(text, Linkify.ALL);
        new AlertDialog.Builder(this)
                .setTitle("关于")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setView(textView)
                .show();
    }

    @Override
    public void onDestroy() {
        fragmentSend.saveSH();
        super.onDestroy();
    }
}
