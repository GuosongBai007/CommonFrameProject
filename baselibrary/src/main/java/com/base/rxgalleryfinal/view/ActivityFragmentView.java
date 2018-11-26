package com.base.rxgalleryfinal.view;

import com.base.rxgalleryfinal.bean.MediaBean;

import java.util.ArrayList;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 下午9:56
 */
public interface ActivityFragmentView {

    void showMediaGridFragment();

    void showMediaPageFragment(ArrayList<MediaBean> list, int position);

    void showMediaPreviewFragment();
}
