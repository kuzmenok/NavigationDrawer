package com.example.sok.navigationdrawer;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WearableListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    private TextView mAuthor;
    private TextView mText;

    private final float mFadedTextAlpha;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mFadedTextAlpha = getResources().getInteger(R.integer.action_text_faded_alpha) / 100f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAuthor = (TextView) findViewById(R.id.author);
        mText = (TextView) findViewById(R.id.text);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mAuthor.setAlpha(1f);
        mText.setAlpha(1f);
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        mAuthor.setAlpha(mFadedTextAlpha);
        mText.setAlpha(mFadedTextAlpha);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}