package ir.zelzele.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ir.zelzele.R;


public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    String fontName = "parastoo.ttf";

    public CustomTextView(Context context) {
        super(context);
        setFont();

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewWithCustomFont);
        if(a.hasValue(0)) {
            fontName = a.getString(0);
        }
        setFont();
    }


    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewWithCustomFont);
        if(a.hasValue(0)) {
            fontName = a.getString(0);
        }
        setFont();
    }

    private void setFont() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
        if (typeface != null)
            setTypeface(typeface);
    }
}
