package ir.zelzele.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import ir.zelzele.R;


public class CustomButtonView extends androidx.appcompat.widget.AppCompatButton {
    String fontName = "parastoo.ttf";

    public CustomButtonView(Context context) {
        super(context);
        setFont();

    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextViewWithCustomFont);
        if(a.hasValue(0)) {
            fontName = a.getString(0);
        }
        setFont();
    }


    public CustomButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
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
