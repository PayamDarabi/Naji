package ir.zelzele.customview;

import android.app.ProgressDialog;
import android.content.Context;


public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }
}
