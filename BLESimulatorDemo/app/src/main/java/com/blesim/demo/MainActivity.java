package com.blesim.demo;

import android.Manifest;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

public class MainActivity extends Activity {

    final String TAG = "BLESimApp";

    final String TARGET_PERIPHERAL_NAME = "Heart Rate";

    private static int REQUEST_ENABLE_BT = 1000;

    ScanCallback scanCallback = null;

    BluetoothGattCallback gattCallback = null;


    private Map<String, String> deviceList = new HashMap<>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "aaa aaa aaa");

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        //Disable the BLE connect button until the peripheral is discovered.
        Button connectButton = (Button) findViewById(R.id.button_c);
        connectButton.setEnabled(false);

        int permissionCheck = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (this.shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_COARSE_LOCATION)){
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else{
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,REQUEST_ENABLE_BT);
        }

        scanCallback = new ScanCallback(){
            @Override
            public void onScanResult (int callbackType,
                                      ScanResult result){

                Boolean found = false;
                for( String address : deviceList.values()){
                    if( address.equals( result.getDevice().getAddress())){
                        found = true;
                        break;
                    }
                }
                if( !found ){
                    deviceList.put( result.getDevice().getName(), result.getDevice().getAddress());
                    Log.d(TAG, "scan result : " + result.getDevice().getName() + " , " +
                            result.getDevice().getAddress() + " , " + result.getScanRecord().getDeviceName() + ", " + result.getRssi());

                    if( TARGET_PERIPHERAL_NAME.equals( result.getDevice().getName())){

                        //enable connect button
                        Button connectButton = (Button) findViewById(R.id.button_c);
                        connectButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onScanFailed (int errorCode){
                Log.d(TAG, "scan failed: " + errorCode);
            }

            @Override
            public void onBatchScanResults (List<ScanResult> results){
                Log.d(TAG, "batch scan result : " + results.size());
            }
        };

        gattCallback = new BluetoothGattCallback() {
            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                Log.d(TAG, "onConnectionStateChange : old : " + status + " -> new : " + newState );
                if( GATT_SUCCESS == status ){
                    Log.d(TAG, "success status");
                }
                if( STATE_CONNECTED == newState ){
                    Log.d(TAG, "BLE connected");
;                }else if( STATE_DISCONNECTED == newState){
                    Log.d(TAG, "BLE disconnected");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }
        };
    }

    @Override
    public Object getSystemService(String name) {

        if( BuildConfig.bleSim ) {
            if (name.equals(BLUETOOTH_SERVICE)) {
                return new com.blesim.bluetooth.BluetoothManager();
            }
        }
        return super.getSystemService(name);
    }

    /*
    private void test(){

        BluetoothManager btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        Log.d("MainActivity", "btAdapter : " + btAdapter.getAddress());

        Log.d("MainActivity", "btAdapter : " + btAdapter.getAddress());
    }*/

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void onClickA( View view)
    {
        Log.d(TAG, "Enter onClickA");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .build();
        btAdapter.getBluetoothLeScanner().startScan(null, settings, scanCallback);
    }

    public void onClickB( View view)
    {
        Log.d(TAG, "Enter onClickB");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();
        btAdapter.getBluetoothLeScanner().stopScan(scanCallback);

    }

    public void onClickC( View view)
    {
        Log.d(TAG, "Enter onClickC - Connect");

        BluetoothManager btManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        final String address = deviceList.get( TARGET_PERIPHERAL_NAME);
        final BluetoothDevice device = btAdapter.getRemoteDevice(address);
        device.connectGatt(this,false,gattCallback);
    }

    public void onClickD( View view)
    {
        Log.d(TAG, "Enter onClickD");
    }

    public void onClickE( View view)
    {
        Log.d(TAG, "Enter onClickE");
    }
}
