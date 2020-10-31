package pers.cxd.bindertest;

import android.graphics.Bitmap;
import android.os.RemoteException;

import pers.cxd.corelibrary.Log;
import pers.cxd.corelibrary.SingletonFactory;

public class BnServer extends IServer.Stub{

    public static BnServer getInstance(){
        return SingletonFactory.findOrCreate(BnServer.class);
    }

    private final String TAG = Log.TAG + BnServer.class.getSimpleName();

    private OnClientUploadImageListener mListener;

    public void setListener(OnClientUploadImageListener listener) {
        this.mListener = listener;
    }

    @Override
    public void uploadImage(Bitmap bitmap, IClient client) throws RemoteException {
        Log.d(TAG, "uploadImage() called with: bitmap = [" + bitmap + "], client = [" + client + "]");
        if (mListener != null){
            mListener.onUploadImage(bitmap);
        }
        client.onUploadImageCallback(0,"success");
    }

    interface OnClientUploadImageListener{
        void onUploadImage(Bitmap bitmap);
    }

}
