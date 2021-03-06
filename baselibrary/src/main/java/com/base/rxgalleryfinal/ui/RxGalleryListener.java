package com.base.rxgalleryfinal.ui;

import com.base.rxgalleryfinal.ui.adapter.MediaGridAdapter;
import com.base.rxgalleryfinal.ui.base.IMultiImageCheckedListener;
import com.base.rxgalleryfinal.ui.base.IRadioImageCheckedListener;
import com.base.rxgalleryfinal.ui.fragment.MediaGridFragment;

/**
 * 处理回调监听
 * Created by KARL on 2017-03-17 04-42-25.
 */
public class RxGalleryListener {

    private static final class RxGalleryListenerHolder {
        private static final RxGalleryListener RX_GALLERY_LISTENER = new RxGalleryListener();
    }

    public static RxGalleryListener getInstance() {
        return RxGalleryListenerHolder.RX_GALLERY_LISTENER;
    }

    /**
     * 图片多选的事件
     */
    public void setMultiImageCheckedListener(IMultiImageCheckedListener checkedImageListener) {
        MediaGridAdapter.setCheckedListener(checkedImageListener);
    }


    /**
     * 图片单选的事件
     */
    public void setRadioImageCheckedListener(IRadioImageCheckedListener checkedImageListener) {
        MediaGridFragment.setRadioListener(checkedImageListener);
    }
}
