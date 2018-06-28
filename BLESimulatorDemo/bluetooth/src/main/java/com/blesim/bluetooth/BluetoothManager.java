package com.blesim.bluetooth;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by cerise on 4/21/18.
 */

public class BluetoothManager {

    //final private String TAG = BluetoothManager.class.getSimpleName();

    final private int id = ThreadLocalRandom.current().nextInt(1, 1000 + 1);

    public BluetoothManager(){
        GLog.d(Integer.toString(id));
    }

    public BluetoothAdapter getAdapter(){
        GLog.d();

        return new BluetoothAdapter();
    }

    @Override
    protected void finalize(){
        GLog.d(Integer.toString(id));
    }
}
