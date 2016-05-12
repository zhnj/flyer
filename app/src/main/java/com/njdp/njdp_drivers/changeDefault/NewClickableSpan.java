package com.njdp.njdp_drivers.changeDefault;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NewClickableSpan extends ClickableSpan {
    int color = -1;
    private Intent intent;
    private Context context;

    public NewClickableSpan(Context context, Intent intent) {
        this(-1,context, intent);
    }

    public NewClickableSpan(int color,Context context, Intent intent) {
        if (color!=-1) {
            this.color = color;
        }
        this.intent = intent;
        this.context=context;
    }

    public void onClick(View widget){
        context.startActivity(intent);
    };

    @Override
    public void updateDrawState(TextPaint ds) {
        if (color == -1) {
            ds.setColor(ds.linkColor);
        } else {
            ds.setColor(color);
        }
        ds.setUnderlineText(false);
    }
}
