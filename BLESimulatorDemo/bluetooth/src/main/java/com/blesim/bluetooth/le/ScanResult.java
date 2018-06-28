package com.blesim.bluetooth.le;

import com.blesim.bluetooth.BluetoothDevice;

/**
 * Created by cerise on 5/26/18.
 */

public final class ScanResult {
    //public static final Creator<ScanResult> CREATOR = null;

    int rssi;

    public ScanResult(BluetoothDevice device, ScanRecord scanRecord, int rssi, long timestampNanos) {
        //throw new RuntimeException("Stub!");

        this.rssi = rssi;
    }

    //public void writeToParcel(Parcel dest, int flags) {
    //    throw new RuntimeException("Stub!");
    //}

    //public int describeContents() {
    //    throw new RuntimeException("Stub!");
    //}

    public BluetoothDevice getDevice() {
        throw new RuntimeException("Stub!");
    }

    public ScanRecord getScanRecord() {
        throw new RuntimeException("Stub!");
    }

    public int getRssi() {
        //throw new RuntimeException("Stub!");
        return this.rssi;
    }
    public void setRssi( int value ){
        this.rssi = value;
    }

    public long getTimestampNanos() {
        throw new RuntimeException("Stub!");
    }

    public int hashCode() {
        throw new RuntimeException("Stub!");
    }

    public boolean equals(Object obj) {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
