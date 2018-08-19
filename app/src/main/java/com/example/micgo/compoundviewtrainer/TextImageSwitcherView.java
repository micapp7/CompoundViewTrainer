package com.example.micgo.compoundviewtrainer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class TextImageSwitcherView extends LinearLayout {
    private TextView mTextView;
    private ImageView mImageView;
    private String mImageUrl;
    private String mTextTitle;
    private boolean mShowText; // this can be a ViewModel or other subject of interest.
    private int mAnimStartDelay;

    public TextImageSwitcherView(Context context) {
        super(context);
        init();
    }

    public TextImageSwitcherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextImageSwitcherView,
                0, 0);
        try {
            mShowText = a.getBoolean(R.styleable.TextImageSwitcherView_showText, false);
            mImageUrl = a.getString(R.styleable.TextImageSwitcherView_imageUrl);
            mTextTitle = a.getString(R.styleable.TextImageSwitcherView_titleText);
            mAnimStartDelay = a.getInteger(R.styleable.TextImageSwitcherView_animStartDelay, 0);

        } finally {
            // Note that TypedArray objects are a shared resource and must be recycled after use.
            a.recycle();
        }
        init();
    }

    public TextImageSwitcherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextImageSwitcherView,
                0, 0);
        try {
            mShowText = a.getBoolean(R.styleable.TextImageSwitcherView_showText, false);
        } finally {
            // Note that TypedArray objects are a shared resource and must be recycled after use.
            a.recycle();
        }
        init();
    }

    public TextImageSwitcherView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //Inflate xml resource, pass "this" as the parent, we use <merge> tag in xml to avoid
        //redundant parent, otherwise a LinearLayout will be added to this LinearLayout ending up
        //with two view groups
        inflate(getContext(), R.layout.text_image_switcher_view, this);
        mTextView = findViewById(R.id.main_text);
        mImageView = findViewById(R.id.main_image);

        mTextView.setVisibility(GONE);
        mImageView.setVisibility(GONE);
        mTextView.setScaleX(0);
        mTextView.setScaleY(0);
        mImageView.setScaleX(0);
        mImageView.setScaleY(0);


        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                if (mShowText) {
                    mTextView.setText(mTextTitle);
                    mTextView.setVisibility(VISIBLE);
                    ViewCompat.animate(mTextView).scaleX(1).setDuration(mAnimStartDelay);
                    ViewCompat.animate(mTextView).scaleY(1).setDuration(mAnimStartDelay);
                } else {
                    GlideApp.with(getContext())
                            .load(mImageUrl)
                            .placeholder(R.drawable.ic_android_sad)
                            .error(R.drawable.ic_android_happy)
                            .circleCrop()
                            .into(mImageView);
                    mImageView.setVisibility(VISIBLE);
                    ViewCompat.animate(mImageView).scaleX(1).setDuration(mAnimStartDelay);
                    ViewCompat.animate(mImageView).scaleY(1).setDuration(mAnimStartDelay);

                }

                return false;
            }
        });
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
        setupView();
        requestLayout();
    }

    public boolean isShowText() {
        return mShowText;
    }

    //This method is called to switch
    private void setupView() {

        if(mShowText) {
            mImageView.setVisibility(GONE);
            mTextView.setVisibility(VISIBLE);
            mImageView.setScaleX(0);
            mImageView.setScaleY(0);
            ViewCompat.animate(mTextView).setStartDelay(mAnimStartDelay).scaleX(1).setDuration(mAnimStartDelay);
            ViewCompat.animate(mTextView).scaleY(1).setDuration(mAnimStartDelay);


        } else {
            mTextView.setVisibility(GONE);
            mImageView.setVisibility(VISIBLE);
            mTextView.setScaleX(0);
            mTextView.setScaleY(0);
            ViewCompat.animate(mTextView).scaleX(0).setDuration(mAnimStartDelay);
            ViewCompat.animate(mTextView).scaleY(0).setDuration(mAnimStartDelay);
            ViewCompat.animate(mImageView).scaleX(1).setDuration(mAnimStartDelay);
            ViewCompat.animate(mImageView).scaleY(1).setDuration(mAnimStartDelay);

        }
    }
}
