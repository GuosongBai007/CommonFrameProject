package com.base.rxgalleryfinal.rxjob.job;

import android.content.Context;

import com.base.rxgalleryfinal.bean.MediaBean;
import com.base.rxgalleryfinal.rxjob.Job;
import com.base.rxgalleryfinal.rxjob.JobCreator;

/**
 * Desction:
 * Author:pengjianbo  Dujinyang
 * Date:16/7/31 上午11:46
 */
public class ImageThmbnailJobCreate implements JobCreator {

    private final MediaBean mediaBean;
    private final Context context;

    public ImageThmbnailJobCreate(Context context, MediaBean mediaBean) {
        this.context = context;
        this.mediaBean = mediaBean;
    }

    @Override
    public Job create() {
        Job.Params params = new Job.Params(mediaBean.getOriginalPath(), mediaBean);
        return new ImageThmbnailJob(context, params);
    }


}
