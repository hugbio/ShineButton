package com.hugbio.shinebuttonlib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ShineImageButton extends ImageView implements ShineButton {
    private static final String TAG = "ShineButton";
    private boolean isChecked = false;

    private int btnImgId;
    private int btnFillImgId;

    int DEFAULT_WIDTH = 50;
    int DEFAULT_HEIGHT = 50;

    DisplayMetrics metrics = new DisplayMetrics();


    Activity activity;
    ShineView shineView;
    ValueAnimator shakeAnimator;
    ShineView.ShineParams shineParams = new ShineView.ShineParams();

    OnCheckedChangeListener listener;

    private int bottomHeight;
    private int realBottomHeight;

    public ShineImageButton(Context context) {
        super(context);
        if (context instanceof Activity) {
            init((Activity) context);
        }
    }

    public ShineImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButton(context, attrs);
    }


    public ShineImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButton(context, attrs);
    }

    private void initButton(Context context, AttributeSet attrs) {

        if (context instanceof Activity) {
            init((Activity) context);
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShineImageButton);
        btnImgId = a.getResourceId(R.styleable.ShineImageButton_btn_img,0);
        btnFillImgId = a.getResourceId(R.styleable.ShineImageButton_btn_fill_Img,0);
        shineParams.allowRandomColor = a.getBoolean(R.styleable.ShineImageButton_allow_random_color, false);  //扩散圆点随机颜色
        shineParams.animDuration = a.getInteger(R.styleable.ShineImageButton_shine_animation_duration, (int) shineParams.animDuration);  //扩散圆点动画时间
        shineParams.bigShineColor = a.getColor(R.styleable.ShineImageButton_big_shine_color, shineParams.bigShineColor);  //扩散的大圆点颜色
        shineParams.clickAnimDuration = a.getInteger(R.styleable.ShineImageButton_click_animation_duration, (int) shineParams.clickAnimDuration);  //按钮点击时动画的时间
        shineParams.enableFlashing = a.getBoolean(R.styleable.ShineImageButton_enable_flashing, false);  //是否启动闪光灯效果
        shineParams.shineCount = a.getInteger(R.styleable.ShineImageButton_shine_count, shineParams.shineCount);  //扩散圆点的数量
        shineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineImageButton_shine_distance_multiple, shineParams.shineDistanceMultiple);  //扩散的距离（值为按钮宽度的倍数）
        shineParams.shineTurnAngle = a.getFloat(R.styleable.ShineImageButton_shine_turn_angle, shineParams.shineTurnAngle);  //扩散角度
        shineParams.smallShineColor = a.getColor(R.styleable.ShineImageButton_small_shine_color, shineParams.smallShineColor);  //扩散的小圆点颜色
        shineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineImageButton_small_shine_offset_angle, shineParams.smallShineOffsetAngle); //扩散小圆点的角度
        shineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineImageButton_shine_size, shineParams.shineSize);  //扩散圆点大小
        a.recycle();
        if(btnImgId > 0){
            setImageResource(btnImgId);
        }
    }

    public int getBottomHeight(boolean real) {
        if (real) {
            return realBottomHeight;
        }
        return bottomHeight;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked, boolean anim) {
        setChecked(checked, anim, true);
    }

    private void setChecked(boolean checked, boolean anim, boolean callBack) {
        isChecked = checked;
        if (checked) {
            setImageResource(btnFillImgId);
            isChecked = true;
            if (anim) showAnim();
        } else {
            setImageResource(btnImgId);
            isChecked = false;
            if (anim) setCancel();
        }
        if (callBack) {
            onListenerUpdate(checked);
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, false, false);
    }

    private void onListenerUpdate(boolean checked) {
        if (listener != null) {
            listener.onCheckedChanged(this, checked);
        }
    }

    public void setCancel() {
        setImageResource(btnImgId);
        if (shakeAnimator != null) {
            shakeAnimator.end();
            shakeAnimator.cancel();
        }
    }

    public void setAllowRandomColor(boolean allowRandomColor) {
        shineParams.allowRandomColor = allowRandomColor;
    }

    public void setAnimDuration(int durationMs) {
        shineParams.animDuration = durationMs;
    }

    public void setBigShineColor(int color) {
        shineParams.bigShineColor = color;
    }

    public void setClickAnimDuration(int durationMs) {
        shineParams.clickAnimDuration = durationMs;
    }

    public void enableFlashing(boolean enable) {
        shineParams.enableFlashing = enable;
    }

    public void setShineCount(int count) {
        shineParams.shineCount = count;
    }

    public void setShineDistanceMultiple(float multiple) {
        shineParams.shineDistanceMultiple = multiple;
    }

    public void setShineTurnAngle(float angle) {
        shineParams.shineTurnAngle = angle;
    }

    public void setSmallShineColor(int color) {
        shineParams.smallShineColor = color;
    }

    public void setSmallShineOffAngle(float angle) {
        shineParams.smallShineOffsetAngle = angle;
    }

    public void setShineSize(int size) {
        shineParams.shineSize = size;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof OnButtonClickListener) {
            super.setOnClickListener(l);
        } else {
            if (onButtonClickListener != null) {
                onButtonClickListener.setListener(l);
            }
        }
    }

    public void setOnCheckStateChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }


    OnButtonClickListener onButtonClickListener;

    public void init(Activity activity) {
        this.activity = activity;
        onButtonClickListener = new OnButtonClickListener();
        setOnClickListener(onButtonClickListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calPixels();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void showAnim() {
        if (activity != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            shineView = new ShineView(activity, this, shineParams);
            rootView.addView(shineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            doShareAnim();
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    public void removeView(View view) {
        if (activity != null) {
            final ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            rootView.removeView(view);
        } else {
            Log.e(TAG, "Please init.");
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public int getColor() {
        return Color.parseColor("#3ac7a8");
    }

    public void setImageResource(int imgId, int checkImgId) {
        if (imgId > 0) {
            this.btnImgId = imgId;
        }
        if (checkImgId > 0) {
            this.btnFillImgId = checkImgId;
        }
        setImageResource(isChecked ? btnFillImgId : btnImgId);
    }

    private void doShareAnim() {
        shakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f);
        shakeAnimator.setInterpolator(new LinearInterpolator());
        shakeAnimator.setDuration(500);
        shakeAnimator.setStartDelay(180);
        invalidate();
        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setScaleX((float) valueAnimator.getAnimatedValue());
                setScaleY((float) valueAnimator.getAnimatedValue());
            }
        });
        shakeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setImageResource(btnFillImgId);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setImageResource(isChecked ? btnFillImgId : btnImgId);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                setImageResource(btnImgId);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        shakeAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void calPixels() {
        if (activity != null && metrics != null) {
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int[] location = new int[2];
            getLocationInWindow(location);
            Rect visibleFrame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
            realBottomHeight = visibleFrame.height() - location[1];
            bottomHeight = metrics.heightPixels - location[1];
        }
    }

    public class OnButtonClickListener implements OnClickListener {
        public void setListener(OnClickListener listener) {
            this.listener = listener;
        }

        OnClickListener listener;

        public OnButtonClickListener() {
        }

        public OnButtonClickListener(OnClickListener l) {
            listener = l;
        }

        @Override
        public void onClick(View view) {
            if (!isChecked) {
                isChecked = true;
                showAnim();
            } else {
                isChecked = false;
                setCancel();
            }
            onListenerUpdate(isChecked);
            if (listener != null) {
                listener.onClick(view);
            }
        }
    }
}
