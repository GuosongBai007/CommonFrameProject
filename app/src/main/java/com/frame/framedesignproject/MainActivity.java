package com.frame.framedesignproject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.base.http.interfaces.RequestListener;
import com.base.imageframe.manager.FrescoManager;
import com.base.imageframe.utils.ImageOptions;
import com.base.model.BaseResponse;
import com.base.thread.Dispatcher;
import com.base.ui.BaseActivity;
import com.base.utils.AppLogger;
import com.base.widget.RecyclingImageView;
import com.frame.framedesignproject.model.UserInfo;
import com.frame.framedesignproject.web.UserManager;
import com.thejoyrun.router.Router;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private Handler mHandler;

    private RecyclingImageView mImageView;
    private RecyclingImageView mImageView1;
    private RecyclingImageView mImageView2;
    private RecyclingImageView mImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppLogger.init(true,"GuosongBai");
        mTextView = findViewById(R.id.user_info);

        mImageView = findViewById(R.id.image);
        mImageView1 = findViewById(R.id.image1);
        mImageView2 = findViewById(R.id.image2);
        mImageView3 = findViewById(R.id.image3);

        String url = "http://g.hiphotos.baidu.com/image/h%3D300/sign=4fbff84f6e2762d09f3ea2bf90ed0849/5243fbf2b2119313562db8dc68380cd791238d74.jpg";

        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.isRounded(true);
        builder.setRoundedType(ImageOptions.RoundedType.Corner);//
        builder.setRoundedRadius(15);// 这三个属性都要同事设置 圆角才会生效

        builder.isGrayscale(true);
        ImageOptions opts = builder.build();
        FrescoManager.displayImageView(mImageView,url,opts);

        builder.isGrayscale(false);
        builder.isBlur(true);
        builder.blurRadius(5);
        FrescoManager.displayImageView(mImageView1,url,builder.build());

        ImageOptions.Builder builder1 = new ImageOptions.Builder();
        builder1.isRounded(true);
        builder1.setRoundedType(ImageOptions.RoundedType.Corner);
        builder1.setRoundedRadius(15,15,0,0);
        FrescoManager.displayImageView(mImageView2,url,builder1.build());

        ImageOptions.Builder builder2 = new ImageOptions.Builder();
        builder2.isRounded(true);
        FrescoManager.displayImageView(mImageView3,url,builder2.build());

        mHandler = new Handler();
        mTextView.setOnClickListener(v -> {
            getUserInfo();
        });
    }

    private void getUserInfo() {
        UserManager.getUserInfo("李白", data -> {
            if (data.isSuccess()) {
                mHandler.post(() -> mTextView.setText(data.getResult().toString()));
            } else {
                mHandler.post(() -> mTextView.setText("请求失败"));
            }
        });
    }

    private void userThreadPool(){
        Dispatcher.runOnUiThread(()->{});
        Dispatcher.runOnCommonThread(()->{});
        Dispatcher.runOnDBSingleThread(()->{});
        Dispatcher.runOnHttpThread(()->{});
        Dispatcher.runOnSingleThread(()->{});
    }
}
