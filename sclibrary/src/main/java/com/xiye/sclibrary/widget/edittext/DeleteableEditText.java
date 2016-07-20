package com.xiye.sclibrary.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.xiye.sclibrary.R;
import com.xiye.sclibrary.utils.Tools;


/**
 * TODO setEnable false 的时候 应该隐藏X...true的时候，如果有内容，应该显示X
 * Created by xqq on 2016/4/22.
 */
public class DeleteableEditText extends EditText {

    private static final int STATE_SHOW_EMPTY = 0;
    private static final int STATE_SHOW_DELETE = 1;

    private Drawable mDeleteDrawable;
    private Drawable mEmptyDrawable;
    private int mUpY, mUpX;
    private int state = STATE_SHOW_EMPTY;

    public DeleteableEditText(Context context) {
        super(context);
        init(context);
    }

    public DeleteableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttribute(context, attrs);
        init(context);
    }

    public DeleteableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttribute(context, attrs);
        init(context);
    }

    private void parseAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DeleteableEditText);
        mDeleteDrawable = ta.getDrawable(R.styleable.DeleteableEditText_deleteDrawable);
    }

    private void init(Context context) {
        setPadding(0, 0, Tools.dip2px(context, 10), 0);
        setCompoundDrawablePadding(Tools.dip2px(context, 10));
        if (mDeleteDrawable == null) {
            mDeleteDrawable = context.getResources().getDrawable(R.drawable.ic_edit_del_normal);
        }
        mEmptyDrawable = createEmptyDrawable();
        setDrawable();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDrawable();
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(null, null, mEmptyDrawable, null);
                }
            }
        });
    }

    private void setDrawable() {
        if (length() > 0 && isFocused()) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, mDeleteDrawable, null);
            state = STATE_SHOW_DELETE;
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, mEmptyDrawable, null);
            state = STATE_SHOW_EMPTY;
        }
    }

    private Drawable createEmptyDrawable() {
        int width = mDeleteDrawable.getIntrinsicWidth();
        int height = mDeleteDrawable.getIntrinsicHeight();
        if (width == -1 || height == -1) {
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        return new BitmapDrawable(bmp);
    }

    private boolean isClickDelete() {
        int vH = getHeight();
        int vW = getWidth();
        int padding = Tools.dip2px(getContext(), 10);
        int clickBorder = Tools.dip2px(getContext(), 5);
        int dH = mDeleteDrawable.getIntrinsicHeight();
        int dW = mDeleteDrawable.getIntrinsicWidth();
        if (mUpY > ((vH - dH) / 2 - clickBorder)
                && mUpY < ((vH + dH) / 2 + clickBorder)
                && mUpX > (vW - padding - dW - clickBorder)
                && mUpX < (vW - padding + clickBorder)) {
            setText("");
            return true;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        boolean isClickDelete = isClickDelete();
        if (isClickDelete) {
            return true;
        } else {
            return super.performClick();
        }
    }

    public void setDeleteDrawable(Drawable drawable) {
        mDeleteDrawable = drawable;
        mEmptyDrawable = createEmptyDrawable();
        setDrawable();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mUpX = (int) event.getX();
            mUpY = (int) event.getY();
        }
        return super.onTouchEvent(event);
    }


}
