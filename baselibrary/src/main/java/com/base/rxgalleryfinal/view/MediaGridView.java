package com.base.rxgalleryfinal.view;

import com.base.rxgalleryfinal.bean.BucketBean;
import com.base.rxgalleryfinal.bean.MediaBean;

import java.util.List;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/5/14 上午11:00
 */
public interface MediaGridView {
    void onRequestMediaCallback(List<MediaBean> list);

    void onRequestBucketCallback(List<BucketBean> list);
}
