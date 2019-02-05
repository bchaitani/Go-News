package com.bassel.statefullayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.helpbit.statefullayout.R;

/**
 * Created by basselchaitani on 5/10/18
 */
public class StatefulLayout extends LinearLayout {

    private static final String MSG_ONE_CHILD = "StatefulLayout must have one child!";

    private static final boolean DEFAULT_ANIM_ENABLED = true;
    private static final int DEFAULT_IN_ANIM = R.anim.fade_in;
    private static final int DEFAULT_OUT_ANIM = R.anim.fade_out;

    /**
     * Indicates whether to place the animation on state changes
     */
    private boolean animationEnabled;

    /**
     * Animation started begin of state change
     */
    private Animation inAnimation;

    /**
     * Animation started end of state change
     */
    private Animation outAnimation;

    /**
     * To synchronize transaction animations when animation duration shorter than request of state change
     */
    private int animCounter;

    private View content;
    private LinearLayout mContainer;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private TextView mTextView;
    private Button mButton;

    public StatefulLayout(Context context) {
        this(context, null);
    }

    public StatefulLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatefulLayout, 0, 0);
        animationEnabled = array.getBoolean(R.styleable.StatefulLayout_stf_AnimationEnabled, DEFAULT_ANIM_ENABLED);
        inAnimation = anim(array.getResourceId(R.styleable.StatefulLayout_stf_InAnimation, DEFAULT_IN_ANIM));
        outAnimation = anim(array.getResourceId(R.styleable.StatefulLayout_stf_OutAnimation, DEFAULT_OUT_ANIM));
        array.recycle();
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }

    public Animation getInAnimation() {
        return inAnimation;
    }

    public void setInAnimation(Animation animation) {
        inAnimation = animation;
    }

    public void setInAnimation(@AnimRes int anim) {
        inAnimation = anim(anim);
    }

    public Animation getOutAnimation() {
        return outAnimation;
    }

    public void setOutAnimation(Animation animation) {
        outAnimation = animation;
    }

    public void setOutAnimation(@AnimRes int anim) {
        outAnimation = anim(anim);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1)
            throw new IllegalStateException(MSG_ONE_CHILD);

        // Hide State Views in Designer
        if (isInEditMode())
            return;

        setOrientation(VERTICAL);
        content = getChildAt(0);
        LayoutInflater.from(getContext()).inflate(R.layout.stateful_layout, this, true);
        mContainer = findViewById(R.id.container);
        mProgressBar = findViewById(R.id.progress);
        mImageView = findViewById(R.id.image);
        mTextView = findViewById(R.id.message);
        mButton = findViewById(R.id.button);

        mProgressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void showContent() {
        if (isAnimationEnabled()) {
            mContainer.clearAnimation();
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (mContainer.getVisibility() == View.VISIBLE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounter != animCounterCopy) return;

                        mContainer.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        content.startAnimation(inAnimation);
                    }
                });

                mContainer.startAnimation(outAnimation);
            }
        } else {
            mContainer.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(@StringRes int resId) {
        showLoading(str(resId));
    }

    public void showLoading(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .loading());
    }

    // empty //

    public void showEmpty() {
        showEmpty(R.string.empty_message);
    }

    public void showEmpty(@StringRes int resId) {
        showEmpty(str(resId));
    }

    public void showEmpty(String message) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.ic_no_connection));
    }

    // error //

    public void showError(OnClickListener clickListener) {
        showError(R.string.error_message, clickListener);
    }

    public void showError(@StringRes int resId, OnClickListener clickListener) {
        showError(str(resId), clickListener);
    }

    public void showError(String message, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.ic_no_connection)
                .buttonText(str(R.string.retry))
                .buttonClickListener(clickListener));
    }

    public void showNotLoggedError(OnClickListener clickListener) {
        showNotLoggedError(R.string.msg_not_logged_in, clickListener);
    }

    public void showNotLoggedError(@StringRes int resId, OnClickListener clickListener) {
        showNotLoggedError(str(resId), clickListener);
    }

    public void showNotLoggedError(String message, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.ic_not_logged_in)
                .buttonText(str(R.string.login))
                .buttonClickListener(clickListener));
    }

    // offline

    public void showOffline(OnClickListener clickListener) {
        showOffline(R.string.offline_message, clickListener);
    }

    public void showOffline(@StringRes int resId, OnClickListener clickListener) {
        showOffline(str(resId), clickListener);
    }

    public void showOffline(String message, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.ic_no_connection)
                .buttonText(str(R.string.retry))
                .buttonClickListener(clickListener));
    }

    // location off //

    public void showLocationOff(OnClickListener clickListener) {
        showLocationOff(R.string.location_off_message, clickListener);
    }

    public void showLocationOff(@StringRes int resId, OnClickListener clickListener) {
        showLocationOff(str(resId), clickListener);
    }

    public void showLocationOff(String message, OnClickListener clickListener) {
        showCustom(new CustomStateOptions()
                .message(message)
                .image(R.drawable.ic_no_connection)
                .buttonText(str(R.string.retry))
                .buttonClickListener(clickListener));
    }

    /**
     * Shows custom state for given options. If you do not set buttonClickListener, the button will not be shown
     *
     * @param options customization options
     * @see CustomStateOptions
     */
    public void showCustom(final CustomStateOptions options) {
        if (isAnimationEnabled()) {
            mContainer.clearAnimation();
            content.clearAnimation();
            final int animCounterCopy = ++animCounter;
            if (mContainer.getVisibility() == GONE) {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter) return;
                        content.setVisibility(GONE);
                        mContainer.setVisibility(VISIBLE);
                        mContainer.startAnimation(inAnimation);
                    }
                });
                content.startAnimation(outAnimation);
                state(options);
            } else {
                outAnimation.setAnimationListener(new CustomAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animCounterCopy != animCounter)
                            return;
                        state(options);
                        mContainer.startAnimation(inAnimation);
                    }
                });
                mContainer.startAnimation(outAnimation);
            }
        } else {
            content.setVisibility(GONE);
            mContainer.setVisibility(VISIBLE);
            state(options);
        }
    }

    private void state(CustomStateOptions options) {
        if (!TextUtils.isEmpty(options.getMessage())) {
            mTextView.setVisibility(VISIBLE);
            mTextView.setText(options.getMessage());
        } else {
            mTextView.setVisibility(GONE);
        }

        if (options.isLoading()) {
            mProgressBar.setVisibility(VISIBLE);
            mImageView.setVisibility(GONE);
            mButton.setVisibility(GONE);
        } else {
            mProgressBar.setVisibility(GONE);
            if (options.getImageRes() != 0) {
                mImageView.setVisibility(VISIBLE);
                mImageView.setImageResource(options.getImageRes());
            } else {
                mImageView.setVisibility(GONE);
            }

            if (options.getClickListener() != null) {
                mButton.setVisibility(VISIBLE);
                mButton.setOnClickListener(options.getClickListener());
                if (!TextUtils.isEmpty(options.getButtonText())) {
                    mButton.setText(options.getButtonText());
                }
            } else {
                mButton.setVisibility(GONE);
            }
        }
    }

    private String str(@StringRes int resId) {
        return getContext().getString(resId);
    }

    private Animation anim(@AnimRes int resId) {
        return AnimationUtils.loadAnimation(getContext(), resId);
    }
}
