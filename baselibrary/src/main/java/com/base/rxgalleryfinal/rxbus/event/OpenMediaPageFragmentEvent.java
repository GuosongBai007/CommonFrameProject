package com.base.rxgalleryfinal.rxbus.event;

import com.base.rxgalleryfinal.bean.MediaBean;

import java.util.ArrayList;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/27 下午11:14
 */
public class OpenMediaPageFragmentEvent {
    private final ArrayList<MediaBean> mediaBeanList;
    private final int position;

    public OpenMediaPageFragmentEvent(ArrayList<MediaBean> mediaBeanList, int position) {
        this.mediaBeanList = mediaBeanList;
        this.position = position;
    }

    public ArrayList<MediaBean> getMediaBeanList() {
        return mediaBeanList;
    }

    public int getPosition() {
        return position;
    }
}
