package com.base.rxgalleryfinal.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Fizzer on 2016/12/27.
 * Email: doraemonmqq@sina.com
 * 自定义相机界面
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {

    private Context mContext;
    private SurfaceHolder mHolder;

    private Camera mCamera;

    private int mScreenWidth;
    private int mScreenHeight;

    public CameraSurfaceView(Context context) {
        super(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getScreenMetrix(mContext);
        initView();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getScreenMetrix(mContext);
        initView();
    }

    /**
     * 获取屏幕参数
     *
     * @param context context
     */
    private void getScreenMetrix(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;
    }

    /**
     * 初始化
     */
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        mCamera.startPreview();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();  //停止预览
        mCamera.release();  //释放相机资源
        if (mHolder != null) {
            mHolder.removeCallback(this);
        }
        mCamera = null;
        mHolder = null;
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

        Logger.myLog("自动对焦");
        if (success) {
            Logger.myLog("自动对焦成功");
        }
    }


    /**
     * 设置参数
     */
    private void setCameraParams(Camera camera, int width, int height) {

        try {
            Camera.Parameters parameters = camera.getParameters();
            // 获取摄像头支持的PreviewSize列表
            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();

            Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
            if (null != preSize) {
                // TODO: 2017/12/7 取最大值
                parameters.setPreviewSize(preSize.width, preSize.height);
            } else {
                preSize = camera.getParameters().getSupportedPreviewSizes().get(0);
            }


            //获取支持的pictureSize列表
            List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
            //从列表中选择合适的分辨率
            Camera.Size picSize = pictureSizeList.get(0);

            for (Camera.Size size : pictureSizeList) {
                float currentRatio = ((float) size.width) / size.height;
                if (currentRatio - ((float) preSize.width) / preSize.height == 0) {
                    picSize = size;
                    break;
                } else {
//                picSize = size;
                }
            }


            //根据选出的picSize重新设置surfaceView的大小
            parameters.setPictureSize(picSize.width, picSize.height);
//        this.setLayoutParams(new FrameLayout.LayoutParams(picSize.width, picSize.height));
            Logger.myLog("选出来的宽=" + picSize.width + "选出来的高=" + picSize.height);

            parameters.setJpegQuality(100); // 设置照片质量
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
            }

            mCamera.cancelAutoFocus();//自动对焦。
            mCamera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
            mCamera.setParameters(parameters);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择合适的分辨率
     *
     * @return Camera.Size
     * 这里的 w 对应屏幕的height
     * h 对应屏幕的width
     */
    private Camera.Size getProperSize(List<Camera.Size> sizeList, float screenRatio) {

        Camera.Size result = null;
        int lastMaxSize = 0;
        for (Camera.Size size : sizeList) {
            float currentRatio = size.width / size.height;
//            if (currentRatio - screenRatio == 0) {
            if (size.width > lastMaxSize) {
                lastMaxSize = size.width;
                result = size;
//                }
            }
        }

/*
        if (result == null) {
            for (Camera.Size size : sizeList) {
                float currentRation = size.width / size.height;
                if (currentRation == 4 / 3) { //默认的宽高比是4：3
                    result = size;
                    break;
                }
            }
        }

       List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
        List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size psize;
        for (int i = 0; i < pictureSizes.size(); i++) {
            result = pictureSizes.get(i);

        }
        for (int i = 0; i < previewSizes.size(); i++) {
            result = previewSizes.get(i);
        }*/


        return result;
    }


    public Camera getmCamera() {
        return mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }


    /**
     * 停止预览
     */
    public void stopPreview() {
        mCamera.stopPreview();
    }

    /**
     * 开启预览
     */
    public void startPreview() {
        mCamera.startPreview();
    }

    public void takePicture(Camera.PictureCallback jpeg) {
        if (mCamera == null) {
            mCamera = Camera.open();
            try {
                if (mHolder == null) {
                    mHolder = getHolder();
                    mHolder.addCallback(this);
                }
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //设置参数,并拍照
        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
        mCamera.takePicture(null, null, jpeg);


    }

}
