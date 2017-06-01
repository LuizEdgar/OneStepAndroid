package com.lutzed.servoluntario.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.Image;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

/**
 * TODO: document your custom view class.
 */
public class DataView extends LinearLayout {
    private Context context;

    private String mTitle;
    private String mData;
    private String mSubData;
    private Drawable mPrimaryIcon;

    private Drawable mSecondaryIcon;
    private TextView mDataTextView;
    private TextView mSubDataTextView;
    private ImageView mPrimaryIconImageView;
    private ImageView mSecondaryIconImageView;
    private TextView mTitleTextView;

    public DataView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public DataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public void init(){
        setOrientation(LinearLayout.HORIZONTAL);

        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

        int padding = (int) (16*px);
        setPadding((int) (12*px), 0, padding, 0);
        setMinimumHeight((int) (72*px));

        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.custom_data_view, this, true);

        mTitleTextView = (TextView) findViewById(R.id.title);
        mSubDataTextView = (TextView) findViewById(R.id.sub_data);
        mDataTextView = (TextView) findViewById(R.id.data);
        mPrimaryIconImageView = (ImageView) findViewById(R.id.primary_icon);
        mSecondaryIconImageView = (ImageView) findViewById(R.id.secondary_icon);
    }

     public void init(AttributeSet attrs) {
        init();

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DataView,
                0, 0);

        try {
            mTitle = a.getString(R.styleable.DataView_title);
            mData = a.getString(R.styleable.DataView_data);
            mSubData = a.getString(R.styleable.DataView_sub_data);
            mPrimaryIcon = a.getDrawable(R.styleable.DataView_primary_icon);
            mSecondaryIcon = a.getDrawable(R.styleable.DataView_secondary_icon);


            if (TextUtils.isEmpty(mData)) setVisibility(View.GONE);

            setTitle(mTitle);
            setData(mData);
            setSubData(mSubData);
            setPrimaryIcon(mPrimaryIcon);
            setSecondaryIcon(mSecondaryIcon);

        } finally {
            a.recycle();
        }
    }

    public void setTitle(String title){
        if (TextUtils.isEmpty(title)) return;

        mTitle = title;
        mTitleTextView.setText(title);
        mTitleTextView.setVisibility(View.VISIBLE);
    }

    public void setSubData(String subData) {
        if (TextUtils.isEmpty(subData)) return;

        mSubData = subData;
        mSubDataTextView.setText(subData);
        mSubDataTextView.setVisibility(View.VISIBLE);
    }

    public void setData(String data) {
        if (TextUtils.isEmpty(data)) return;

        mData = data;
        mDataTextView.setText(data);
        setVisibility(View.VISIBLE);
    }

    public void setPrimaryIcon(int primaryIconResource) {
        setPrimaryIcon(ContextCompat.getDrawable(context, primaryIconResource));
    }

    public void setPrimaryIcon(Drawable icon) {
        if (icon == null) return;

        Drawable wrapDrawable = DrawableCompat.wrap(icon);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.iconsDark));

        mPrimaryIcon = icon;
        mPrimaryIconImageView.setImageDrawable(icon);
    }

    public void setPrimaryIcon(Image picture, Transformation transformation) {
        if (picture == null) return;
        setPrimaryIcon(picture.getUrl(), transformation);
    }

    public void setPrimaryIcon(String url, Transformation transformation) {
        if (url == null) return;

        RequestCreator requestCreator = Picasso.with(getContext()).load(url);
        if (transformation!=null){
            requestCreator = requestCreator.transform(transformation);
        }
        requestCreator.into(mPrimaryIconImageView);
    }

    public void setPrimaryIconOnClickListener(OnClickListener listener) {
        mPrimaryIconImageView.setOnClickListener(listener);
    }

    public void setSecondaryIcon(int secondaryIconResource) {
        setSecondaryIcon(ContextCompat.getDrawable(context, secondaryIconResource));
    }

    public void setSecondaryIcon(Drawable icon) {
        if (icon == null) return;

        Drawable wrapDrawable = DrawableCompat.wrap(icon);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getContext(), R.color.iconsDark));

        mSecondaryIcon = icon;
        mSecondaryIconImageView.setImageDrawable(icon);
        mSecondaryIconImageView.setVisibility(View.VISIBLE);
    }

    public void setSecondaryIconOnClickListener(OnClickListener listener) {
        mSecondaryIconImageView.setOnClickListener(listener);
    }
}
