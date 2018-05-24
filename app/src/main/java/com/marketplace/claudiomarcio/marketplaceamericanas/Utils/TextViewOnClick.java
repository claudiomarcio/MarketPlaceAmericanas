package com.marketplace.claudiomarcio.marketplaceamericanas.Utils;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TextViewOnClick implements TextView.OnClickListener {

    private TextView answerView;

    public TextViewOnClick(TextView answerView) {
        this.answerView = answerView;
    }
    @Override
    public void onClick(View v) {
        if(answerView.getVisibility() == TextView.GONE) {
            answerView.setAlpha(0);
            answerView.setVisibility(TextView.VISIBLE);
            answerView.animate().alpha(1).setDuration(500).withEndAction(new Runnable(){
                @Override
                public void run(){
                    answerView.setBackgroundColor(Color.parseColor("#A6E7FF"));
                }
            });
        }
    }
}