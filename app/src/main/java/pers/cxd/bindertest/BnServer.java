package pers.cxd.bindertest;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.RemoteException;

import pers.cxd.corelibrary.Log;
import pers.cxd.corelibrary.SingletonFactory;

public class BnServer extends IServer.Stub{

    public static BnServer getInstance(){
        return SingletonFactory.findOrCreate(BnServer.class);
    }

    private BnServer(){
        android.util.Log.i(TAG, "BnServer: " + this);
    }

    private final String TAG = Log.TAG + BnServer.class.getSimpleName();

    private OnClientUploadImageListener mListener;

    public void setListener(OnClientUploadImageListener listener) {
        this.mListener = listener;
    }

    @Override
    public void uploadImage(Bitmap bitmap, IClient client) throws RemoteException {
        Log.d(TAG, "uploadImage() called with: bitmap = [" + bitmap + "], client = [" + client + "]");
        android.util.Log.i(TAG, "uploadImage: " + Binder.getCallingPid());
        if (mListener != null){
            mListener.onUploadImage(bitmap);
        }
        client.onUploadImageCallback(0,"success");
    }

    @Override
    public void testWriteBpBinder(IServer iServer) throws RemoteException {
        android.util.Log.d(TAG, "testWriteBpBinder() called with: iServer = [" + iServer + "]");
    }

    interface OnClientUploadImageListener{
        void onUploadImage(Bitmap bitmap);
    }

}
