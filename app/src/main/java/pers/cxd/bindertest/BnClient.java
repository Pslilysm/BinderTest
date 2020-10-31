package pers.cxd.bindertest;

import android.os.RemoteException;

import pers.cxd.corelibrary.Log;
import pers.cxd.corelibrary.SingletonFactory;

public class BnClient extends IClient.Stub {

    public static BnClient getInstance(){
        return SingletonFactory.findOrCreate(BnClient.class);
    }

    private final String TAG = Log.TAG + BnClient.class.getSimpleName();

    @Override
    public void onUploadImageCallback(int code, String msg) throws RemoteException {
        Log.d(TAG, "onUploadImageCallback() called with: code = [" + code + "], msg = [" + msg + "]");
    }

}
