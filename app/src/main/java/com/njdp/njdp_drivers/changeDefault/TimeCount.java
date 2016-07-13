package com.njdp.njdp_drivers.changeDefault;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.njdp.njdp_drivers.R;

public class TimeCount extends CountDownTimer {
    private Button btn;
    private Context mContext;

    // 在这个构造方法里需要传入4个参数，一个是Activity，一个总时间millisInFuture，一个间隔时间countDownInterval,一个按钮
    public TimeCount (long millisInFuture, long countDownInterval,Button btn,Context context) {
        super(millisInFuture, countDownInterval);
        this.btn =btn;
        this.mContext=context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "秒后可重新获取");//设置倒计时时间
        btn.setTextColor(Color.GRAY);//倒计时，文字显示为灰色

    }
    @Override
    public void onFinish() {
        btn.setText("重新获取验证码");
        btn.setTextColor(ContextCompat.getColor(mContext, R.color.whiteFont));//倒计结束时，文字显示为白色
        btn.setEnabled(true);

    }
}
