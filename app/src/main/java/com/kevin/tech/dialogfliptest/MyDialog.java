package com.kevin.tech.dialogfliptest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDialog extends Dialog {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_auto_login)
    CheckBox cbAutoLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.rlDialogRoot)
    RelativeLayout rlDialogRoot;
    private Context context;
    @BindView(R.id.ll_register)
    LinearLayout llRegister;

    private View view;
    private int centerX;
    private int centerY;
    private int depthZ = 700;
    private int duration = 300;

    // 开关动画，旋转的角度是不一样的
    // Rotate3dAnimation 很奇怪，一次完成后要再执行一次
    private Rotate3dAnimation openAnimation;
    private Rotate3dAnimation closeAnimation;

    private boolean isOpen = false;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉系统的黑色矩形边框，不知道为什么没加这个 Dialog 视觉上感觉被压缩了
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_my, null);
        setContentView(view);
        ButterKnife.bind(this);
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        tvForgetPwd.setOnClickListener(new OnWidgetClickListener());
        btnBack.setOnClickListener(new OnWidgetClickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    private class OnWidgetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {
                case R.id.tv_forget_pwd:
                    startAnimation();
                    break;
                case R.id.btn_back:
                    startAnimation();
                    break;
            }
        }
    }

    private void startAnimation() {
        //接口回调传递参数
        centerX = rlDialogRoot.getWidth() / 2;
        centerY = rlDialogRoot.getHeight() / 2;
        if (openAnimation == null) {
            initOpenAnim();
            initCloseAnim();
        }

        //用作判断当前点击事件发生时动画是否正在执行
        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }

        //判断动画执行
        if (isOpen) {
            rlDialogRoot.startAnimation(openAnimation);

        } else {
            rlDialogRoot.startAnimation(closeAnimation);
        }
        isOpen = !isOpen;
    }

    /**
     * 卡牌文本介绍打开效果：注意旋转角度
     */
    private void initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);

                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                rlDialogRoot.startAnimation(rotateAnimation);
            }
        });
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private void initCloseAnim() {
        closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new AccelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                rlDialogRoot.startAnimation(rotateAnimation);
            }
        });
    }
}