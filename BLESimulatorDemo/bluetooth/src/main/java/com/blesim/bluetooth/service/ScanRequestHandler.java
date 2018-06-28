package com.blesim.bluetooth.service;

import com.blesim.bluetooth.GLog;

import java.util.UUID;
import com.blesim.bluetooth.le.ScanCallback;
import com.blesim.bluetooth.le.ScanResult;

import static com.blesim.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES;

/**
 * Created by cerise on 6/8/18.
 */

public class ScanRequestHandler {
    final String TAG = ScanRequestHandler.class.getSimpleName();
    final UUID id = UUID.randomUUID();

    private Boolean reScanWithNewOption = false;

    private Boolean terminate = false;

    private ScanCallback scanCallback = null;

    //static var shall use unique name to avoid unpredictable duplication.
    private Thread scanRequestThread;

    private Boolean stopScan = true;

    private MessageConsumer consumer;

    public ScanRequestHandler(){
        consumer = new MessageConsumer();
        scanRequestThread = new Thread( consumer );
        scanRequestThread.start();
    }

    public void scan( ScanCallback callback ){
        GLog.d(TAG, "scan : " + id);
        this.scanCallback = callback;

        reScanWithNewOption = true;

        stopScan = false;

        try {

            synchronized (consumer) {
                consumer.notify();
            }
            //I got this :
            //    resume thread failed: java.lang.IllegalMonitorStateException: object not locked by thread before notify()

        }catch ( Exception e ){
            GLog.e(TAG, "resume thread failed: " + e.toString());
        }
    }

    public void terminate(){
        stopScan();
        terminate = true;
        scanCallback = null;
    }

    public void stopScan(){
        GLog.d(TAG, "stop scan : " + id);

        stopScan = true;
    }

    class MessageConsumer implements Runnable
    {
        public void run()
        {
            GLog.d(TAG, "MessageConsumer - run");

            int rssiValue = 0;

            ScanResult result = new ScanResult( null, null, rssiValue, 123);

            while( !terminate ){

                try {
                    synchronized (this) {
                        while (stopScan) {
                            GLog.d(TAG, "scan request thread suspended");
                            this.wait();
                        }
                    }

                    if (reScanWithNewOption) {
                        GLog.d(TAG, "reset the scan parameters, namely callback result");

                        //ToDo
                    }
                    assert null != scanCallback;

                    result.setRssi(++rssiValue);
                    GLog.d(TAG, "return scan result");
                    scanCallback.onScanResult(CALLBACK_TYPE_ALL_MATCHES, result);

                    Thread.sleep(2000);
                } catch (Exception e) {
                    GLog.e(TAG, "thread sleep exception: " + e.toString());
                }
            }
        }
    }
}
