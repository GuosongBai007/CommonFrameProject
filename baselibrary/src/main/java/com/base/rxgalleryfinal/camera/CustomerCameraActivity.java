package com.base.rxgalleryfinal.camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.frame.baselibrary.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CustomerCameraActivity extends Activity implements View.OnClickListener {

    private CameraSurfaceView mView;
    private Button mTakePicture;
    private Button mTakeCancel;
    private Button mTakeOk;
    String filePath = "";
    private byte[] bitmapData;

    public static int RESULT_OK = 100;
    private CheckBox btnLight;
    private boolean isTake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_camera);

        mView = (CameraSurfaceView) findViewById(R.id.CameraView);
        mTakePicture = (Button) findViewById(R.id.btnTake);
        mTakeCancel = (Button) findViewById(R.id.takeCancel);
        mTakeOk = (Button) findViewById(R.id.takeOk);
        btnLight = (CheckBox) findViewById(R.id.btnLight);
        btnLight.setOnClickListener(this);
        mTakePicture.setOnClickListener(this);
        mTakeCancel.setOnClickListener(this);
        mTakeOk.setOnClickListener(this);
//        setTakeView();
        btnLight.setChecked(false);
    }

    public void Camera(View view) {
        mView.takePicture(jpeg);
    }


    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            //获取拍照的图像
            Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix m = new Matrix();
            m.setRotate(90, (float) bm0.getWidth() / 2, (float) bm0.getHeight() / 2);
            Bitmap bm = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);
            bitmapData = Bitmap2Bytes(bm);
            saveBitmap(bitmapData); //保存图片
            Intent intent = new Intent();
            intent.putExtra("data", filePath);
            setResult(Activity.RESULT_OK, intent);    //设置传递的数据
            CustomerCameraActivity.this.finish();
//            setAfterTakeV iew();
        }
    };


    /**
     * 保存图片
     */
    private void saveBitmap(byte[] data) {
        BufferedOutputStream bos = null;
        try {
            // 获得图片
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

//                String path = "/sdcard/Test";
                String path = Environment.getExternalStorageDirectory() + "/Dcb";
                Logger.myLog(path);
                File tmpfile = new File(path);
                if (!tmpfile.exists()) {
                    tmpfile.mkdir();
                }

                filePath = path + "/" + System.currentTimeMillis() + ".jpg";//照片保存路径
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);

            } else {
                Toast.makeText(CustomerCameraActivity.this, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();//输出
                bos.close();//关闭
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置拍照时的布局
     */
    private void setTakeView() {
        if (mTakePicture.getVisibility() == View.GONE) {
            mTakePicture.setVisibility(View.VISIBLE);
        }

        if (mTakeCancel.getVisibility() == View.VISIBLE) {
            mTakeCancel.setVisibility(View.GONE);
        }

        if (mTakeOk.getVisibility() == View.VISIBLE) {
            mTakeOk.setVisibility(View.GONE);
        }
    }


    public void isLightEnable(boolean isEnable) {
        if (isEnable) {
            Camera camera = mView.getmCamera();
            if (camera != null) {
                camera.stopPreview();
                camera.startPreview();
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameter);
            }
        } else {
            Camera camera = mView.getmCamera();
            if (camera != null) {
                camera.stopPreview();
                camera.startPreview();
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameter);
            }
        }
    }

    /**
     * 设置拍照结束的布局
     */
    private void setAfterTakeView() {
        if (mTakePicture.getVisibility() == View.VISIBLE) {
            mTakePicture.setVisibility(View.GONE);
        }

        if (mTakeCancel.getVisibility() == View.GONE) {
            mTakeCancel.setVisibility(View.VISIBLE);
        }

        if (mTakeOk.getVisibility() == View.GONE) {
            mTakeOk.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btnTake) {
            if (!isTake) {
                isTake = true;
                mView.takePicture(jpeg);    //拍照
            }
        } else if (i == R.id.takeCancel) {
            mView.stopPreview();
            mView.startPreview();
            setTakeView();  //设置拍照时的布局

        } else if (i == R.id.takeOk) {
            /*saveBitmap(bitmapData); //保存图片
            Intent intent = new Intent();
            intent.putExtra("data", filePath);
            setResult(Activity.RESULT_OK, intent);    //设置传递的数据
            this.finish();
*/
        } else if (i == R.id.btnLight) {
            if (btnLight.isChecked()) {
                btnLight.setChecked(true);
                isLightEnable(true);
            } else {
                btnLight.setChecked(false);
                isLightEnable(false);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
