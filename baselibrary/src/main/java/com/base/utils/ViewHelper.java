package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Author:GuosongBai
 * Date:2018/11/8 14:15
 * Description:View 辅助类
 */
public class ViewHelper {
    public static final int NOT_OUT_OF_BOUNDS = 0;
    public static final int OUT_OF_LEFT_BOUNDS = 1;
    public static final int OUT_OF_TOP_BOUNDS = 2;
    public static final int OUT_OF_RIGHT_BOUNDS = 3;
    public static final int OUT_OF_BOTTOM_BOUNDS = 4;

    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    public static <T extends View> T findView(View v, int id) throws ClassCastException {
        if (v == null) {
            return null;
        }
        return (T) v.findViewById(id);
    }

    public static float dp2pxf(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static int dp2px(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics));
    }

    public static int sp2px(Context context, float sp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics));
    }

    public static int px2dp(Context context, float px) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((px / scale) + 0.5f);
    }

    /**
     * 获取文字高度和行高
     *
     * @param fontSize
     * @return
     */
    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 设置TextView控件的值，当值的长度过长时，在最后用省略号代替
     *
     * @param view TextView控件
     * @param text TextView控件的值
     * @param dp   dp值
     */
    public static void setEllipsize(TextView view, CharSequence text, float dp) {
        CharSequence charSequence = TextUtils.ellipsize(text, view.getPaint(),
                ViewHelper.dp2px(view.getContext(), dp), TextUtils.TruncateAt.END);
        view.setText(charSequence);
    }

    /**
     * 计算键盘高度。该方法是通过计算窗口根布局的高度减去Activity可用区域的高度来实现的。
     * 需要Activity设置为AdjustResize
     *
     * @param context Activity的Context
     * @return 键盘搞定
     */
    public static int getKeyBoardHeight(Context context) {
        if (context instanceof Activity) {
            //获得窗口根布局
            View view = ((Activity) context).getWindow().getDecorView();
            //获得窗口根布局的区域
            Rect decorRect = new Rect();
            view.getGlobalVisibleRect(decorRect);
            //获得应用程序可用的区域
            Rect frameRect = new Rect();
            view.getWindowVisibleDisplayFrame(frameRect);
            //用根布局的底部减去程序区域的底部，得出键盘高度
            return decorRect.bottom - frameRect.bottom;
        }
        return 0;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    /**
     * 获取导航栏高度（使用该方法前应该先用hasPermanentMenuKey方法判断是否是虚拟导航栏）
     *
     * @param context Context实例
     * @return 导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int resId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resId > 0) {
            return res.getDimensionPixelSize(resId);
        }
        return 0;
    }

    /**
     * 从资源文件获取ColorStateList
     *
     * @param res   资源实例
     * @param resId 资源ID
     * @return ColorStateList实例
     */
    public static ColorStateList getColorStateList(Resources res, int resId) {
        XmlResourceParser parser = res.getXml(resId);
        ColorStateList colorStateList = null;
        try {
            colorStateList = ColorStateList.createFromXml(res, parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colorStateList;
    }

    /**
     * 检查是否超过X边界
     *
     * @param bounds 边界
     * @param x      x坐标
     * @return
     */
    public static int checkOutOfBoundsX(Rect bounds, int x) {
        if (x < bounds.left) {
            return OUT_OF_LEFT_BOUNDS;
        } else if (x > bounds.right) {
            return OUT_OF_RIGHT_BOUNDS;
        } else {
            return NOT_OUT_OF_BOUNDS;
        }
    }

    /**
     * 检查是否超过Y边界
     *
     * @param bounds 边界
     * @param y      y坐标
     * @return
     */
    public static int checkOutOfBoundsY(Rect bounds, int y) {
        if (y < bounds.top) {
            return OUT_OF_TOP_BOUNDS;
        } else if (y > bounds.bottom) {
            return OUT_OF_BOTTOM_BOUNDS;
        } else {
            return NOT_OUT_OF_BOUNDS;
        }
    }

    /**
     * 获取View在屏幕上的坐标(View左上角)
     *
     * @param v View对象
     * @return View左上角坐标
     */
    public static Point getLocationOnScreen(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    /**
     * 获取View在窗口上的坐标
     *
     * @param v View对象
     * @return View左上角坐标
     */
    public static Point getLocationOnWindow(View v) {
        int[] location = new int[2];
        v.getLocationInWindow(location);
        return new Point(location[0], location[1]);
    }

    public static Rect getLocalVisibleRect(View v) {
        Rect rect = new Rect();
        v.getLocalVisibleRect(rect);
        return rect;
    }

    /**
     * @param view 目标显示控件
     * @return
     */
    public static RectF getViewBounds(View view) {
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        RectF rect = new RectF();
        rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());

        if (view.getContext() instanceof Activity) {
            View content = ((Activity) view.getContext()).findViewById(android.R.id.content);
            content.getLocationInWindow(loc);
            rect.offset(0, -loc[1]);
        }
        return rect;
    }

    /**
     * 禁用OverScrollMode模式
     *
     * @param view 待禁用该模式的View示例
     */
    public static void disableOverScrollMode(View view) {
        view.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    /**
     * 设置视图的显示与隐藏
     *
     * @param view
     * @param isVisibility 为true 视图为显示 {@link View#VISIBLE}，<br/>为false 视图隐藏{@link View#GONE}
     */
    public static void setViewVisibility(View view, boolean isVisibility) {
        view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
    }

    /**
     * 从View的父布局中移除它
     *
     * @param view
     * @return 是否移除成功
     */
    public static boolean removeViewFromParent(View view) {
        if (view == null) {
            return false;
        }
        if (view.getParent() == null) {
            return true;
        }
        if (view.getParent() instanceof ViewManager) {
            try {
                ((ViewManager) view.getParent()).removeView(view);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 创建View的快照。
     *
     * @param view   view
     * @param config 图片的配置。参考Bitmap.Config
     * @return 一张View快照的位图
     */
    public static Bitmap createViewSnapshot(View view, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 根据资源名称获取Drawable的资源ID
     *
     * @param context 上下文
     * @param name    资源名称
     * @return 资源ID。返回0表示没有找到该资源
     */
    public static int getDrawableIdWithName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static Drawable getDrawable(int id) {
        return AppUtils.getContext().getResources().getDrawable(id);
    }

    /**
     * 测量控件尺寸
     *
     * @param parent
     * @param child
     */
    public static void measureView(ViewGroup parent, View child) {
        final ViewGroup.LayoutParams lp = child.getLayoutParams();

        final int childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(parent.getMeasuredWidth(),
                parent.getPaddingLeft() + parent.getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(parent.getMeasuredHeight(),
                parent.getPaddingBottom() + parent.getPaddingTop(), lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * 注册可拖动的View
     *
     * @param dragView  被拖动的View
     * @param container 可拖动范围
     */
    public static void registerDragableView(final View dragView, final Rect container) {
        if (!(dragView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) dragView.getLayoutParams();
        dragView.setOnTouchListener(new View.OnTouchListener() {
            float mLastX;
            float mLastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算并更新窗口浮动信息
                        int dx = (int) (event.getRawX() - mLastX);
                        int dy = (int) (event.getRawY() - mLastY);

                        int left = dragView.getLeft() + dx;
                        int top = dragView.getTop() + dy;

                        if (left < container.left) {
                            left = container.left;
                        }

                        if (left + dragView.getWidth() > container.right) {
                            left = container.right - dragView.getWidth();
                        }

                        if (top < container.top) {
                            top = container.top;
                        }

                        if (top + dragView.getHeight() > container.bottom) {
                            top = container.bottom - dragView.getHeight();
                        }
                        layoutParams.leftMargin = left;
                        layoutParams.topMargin = top;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        dragView.setLayoutParams(layoutParams);

                        mLastX = event.getRawX();
                        mLastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getEventTime() - event.getDownTime() < 150) {
                            dragView.performClick();
                        }
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    /**
     * 保证ViewStub处于展开状态。
     */
    public static void inflateViewStubIfNeed(ViewStub viewStub) {
        if (viewStub == null) {
            return;
        }
        final ViewParent viewParent = viewStub.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            viewStub.inflate();
        }
    }

    /**
     * 检查ViewStub是否处于展开状态
     *
     * @return true如果已经被展开，false如果还没有加载
     */
    public static boolean checkViewStub(ViewStub viewStub) {
        if (viewStub == null) {
            return true;
        }
        final ViewParent viewParent = viewStub.getParent();
        return viewParent == null || !(viewParent instanceof ViewGroup);
    }

    /**
     * 获取屏幕宽高
     */
    public static int getDefaultDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getDefaulDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取手机屏幕分辨率
     */
    public static float getWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static float getHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 创建Activity的快照。
     *
     * @param activity Activity
     * @return 一张Activity快照的位图
     */
    public static Bitmap createActivitySnapshot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        // 去掉状态栏
        Bitmap viewBmp = createViewSnapshot(view, Bitmap.Config.RGB_565);
        Bitmap bmp = Bitmap.createBitmap(viewBmp, 0, statusBarHeights,
                viewBmp.getWidth(), viewBmp.getHeight() - statusBarHeights);
        // 销毁缓存信息
        return bmp;
    }

    /**
     * 该函数主要作用是用于通过传入当前View  和根View 然后获取子 view的相对位置
     * 获取位置比getLocationOnScreen()精准
     */
    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }

        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmp = child;

        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        while (tmp != decorView && tmp != parent && tmp != null) {
            tmp.getHitRect(tmpRect);
            if (!tmp.getClass().equals(FRAGMENT_CON)) {
                result.left += tmpRect.left;
                result.top += tmpRect.top;
            }
            tmp = (View) tmp.getParent();

            if (tmp != null && tmp.getParent() != null && (tmp.getParent() instanceof ViewPager)) {
                tmp = (View) tmp.getParent();
            }
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }

    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }

    /**
     * 获取屏幕宽高比
     */
    public static float screenResolvingPower(Context context) {
        float width = getWidthPixels(context);
        float height = getHeightPixels(context);
        float resolvingPower = height / width;
        AppLogger.i("screenResolvingPower", "width==" + width + "heigt==" + height + "   比例 ==" + resolvingPower);
        return resolvingPower;
    }


    /**
     * view创建bitmap
     * @param context
     * @param view
     * @return
     */
    public static Bitmap createBitmapFromLayout(Activity context,View view){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int measuredWidth=View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels,View.MeasureSpec.EXACTLY);
        int measuredHeight=View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels,View.MeasureSpec.EXACTLY);
        view.measure(measuredWidth, measuredHeight);
        view.layout(0,0, view.getMeasuredWidth(), view.getMeasuredHeight());
        return createBitmap(view);
    }

    public static Bitmap createBitmapFromView(View view){
        int measured=View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        view.measure(measured, measured);

        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.TRANSPARENT);
        view.draw(canvas);
        return bmp;
    }

    private static Bitmap createBitmap(View view) {
        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.TRANSPARENT);
        view.layout(0, 0, w, h);
        view.draw(c);
        return bmp;
    }


}

