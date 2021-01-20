package pers.cxd.bindertest;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pers.cxd.corelibrary.Log;

import static android.os.IBinder.FIRST_CALL_TRANSACTION;

public interface IServer extends IInterface {

    void uploadImage(Bitmap bitmap, IClient client) throws RemoteException;
    void testWriteBpBinder(IServer iServer) throws RemoteException;

    String TAG = Log.TAG + IServer.class.getSimpleName();
    String DESCRIPTOR = "per.cxd.bindertest.IServer";
    int UPLOAD_IMAGE_TRANSACTION_CODE = FIRST_CALL_TRANSACTION + 1;
    int TEST_WRITE_BP_BINDER_TRANSACTION_CODE = FIRST_CALL_TRANSACTION + 2;

    abstract class Stub extends Binder implements IServer {

        public Stub(){
            super();
            attachInterface(this, DESCRIPTOR);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        public static IServer asInterface(IBinder binder){
            if (binder == null){
                return null;
            }
            IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);
            return iInterface instanceof IServer ? (IServer) iInterface : new Proxy(binder);
        }

        @Nullable
        @Override
        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code){
                case UPLOAD_IMAGE_TRANSACTION_CODE:
                    data.enforceInterface(DESCRIPTOR);
                    Bitmap bitmap = data.readParcelable(ClassLoader.getSystemClassLoader());
                    if (bitmap == null){
                        if (reply != null){
                            reply.writeException(new NullPointerException("bitmap is null"));
                        }
                    }else {
                        Log.i(TAG, "onTransact: " + data.dataCapacity() + " bm " + bitmap.getByteCount());
                        IBinder iBinder = data.readStrongBinder();
                        this.uploadImage(bitmap, IClient.Stub.asInterface(iBinder));
                        if (reply != null){
                            reply.writeNoException();
                        }
                    }
                    return true;
                case TEST_WRITE_BP_BINDER_TRANSACTION_CODE:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder binder = data.readStrongBinder();
                    this.testWriteBpBinder(IServer.Stub.asInterface(binder));
                    if (reply != null){
                        reply.writeNoException();
                    }
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IServer {

            private final IBinder mRemote;

            public Proxy(IBinder mRemote) {
                this.mRemote = mRemote;
            }

            @Override
            public void uploadImage(Bitmap bitmap, IClient binder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try{
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeParcelable(bitmap, 0);
                    _data.writeStrongBinder(binder.asBinder());
                    mRemote.transact(UPLOAD_IMAGE_TRANSACTION_CODE, _data, _reply, 0);
                    _reply.readException();
                }finally {
                    _data.recycle();
                    _reply.recycle();
                }
            }

            @Override
            public void testWriteBpBinder(IServer iServer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try{
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder(iServer.asBinder());
                    mRemote.transact(TEST_WRITE_BP_BINDER_TRANSACTION_CODE, _data, _reply, 0);
                    _reply.readException();
                }finally {
                    _data.recycle();
                    _reply.recycle();
                }
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }
        }

    }

}
