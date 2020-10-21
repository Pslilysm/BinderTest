package pers.cxd.bindertest;

import android.graphics.Bitmap;
import android.os.RemoteException;

import pers.cxd.corelibrary.Log;
import pers.cxd.corelibrary.SingletonManager;

public class BnServer extends IServer.Stub{

    public static BnServer getInstance(){
        return SingletonManager.getInstance(BnServer.class);
    }

    private BnServer() {
        super();
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
    }

    interface OnClientUploadImageListener{
        void onUploadImage(Bitmap bitmap);
    }

}
