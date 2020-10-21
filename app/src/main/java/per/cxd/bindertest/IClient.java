package per.cxd.bindertest;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IClient extends IInterface {

    void onUploadImageCallback(int code, String msg) throws RemoteException;

    abstract class Stub extends Binder implements IClient {

        private static final String DESCRIPTOR = "per.cxd.bindertest.IClient";
        private static final int ON_UPLOAD_IMAGE_CALLBACK_TRANSACTION = FIRST_CALL_TRANSACTION + 1;

        @Nullable
        @Override
        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        public static IClient asInterface(IBinder iBinder){
            if (iBinder == null){
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return iInterface instanceof IClient ? (IClient) iInterface : new Proxy(iBinder);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code){
                case ON_UPLOAD_IMAGE_CALLBACK_TRANSACTION:
                    data.enforceInterface(DESCRIPTOR);
                    int _code = data.readInt();
                    String _msg = data.readString();
                    this.onUploadImageCallback(_code, _msg);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IClient {

            private final IBinder mRemote;

            public Proxy(IBinder mRemote) {
                this.mRemote = mRemote;
            }

            @Override
            public void onUploadImageCallback(int code, String msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try{
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeInt(code);
                    _data.writeString(msg);
                    mRemote.transact(ON_UPLOAD_IMAGE_CALLBACK_TRANSACTION, _data, _reply, 0);
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
