package per.cxd.bindertest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import pers.cxd.corelibrary.Log;

public class SecondActivity extends AppCompatActivity {

    private String TAG = Log.TAG + SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle bundle = getIntent().getBundleExtra(Bundle.class.getSimpleName());
        IBinder bp = bundle.getBinder(IServer.Stub.DESCRIPTOR);
        IServer proxy = IServer.Stub.asInterface(bp);
        Log.i(TAG, "onCreate: bp " + bp + " proxy = " + proxy);
        File imgFile = new File("/sdcard/DCIM/Camera/IMG20200820154355.jpg");
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(imgFile));
            Bitmap bm = BitmapFactory.decodeStream(bis);
            Log.i(TAG, "onCreate: bm = " + bm);
            proxy.uploadImage(bm, BnClient.getInstance());
        } catch (FileNotFoundException | RemoteException e) {
            e.printStackTrace();
        }finally {
            if (bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}