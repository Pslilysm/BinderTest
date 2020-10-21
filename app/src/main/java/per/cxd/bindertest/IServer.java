package per.cxd.bindertest;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IServer extends IInterface {

    void uploadImage(Bitmap bitmap, IClient client) throws RemoteException;

    abstract class Stub extends Binder implements IServer {

        public static final String DESCRIPTOR = "per.cxd.bindertest.IServer";

        private static final int UPLOAD_IMAGE_TRANSACTION_CODE = FIRST_CALL_TRANSACTION + 1;

        protected Stub(){
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
                        reply.writeException(new NullPointerException("bitmap is null"));
                    }else {
                        IBinder iBinder = data.readStrongBinder();
                        this.uploadImage(bitmap, IClient.Stub.asInterface(iBinder));
//                        bitmap.recycle();
                        reply.writeNoException();
                    }
                    return true;
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
                    bitmap.recycle();
                }
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }
        }

    }

}
