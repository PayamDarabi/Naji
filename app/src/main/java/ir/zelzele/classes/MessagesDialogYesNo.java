package ir.zelzele.classes;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import ir.zelzele.R;
import ir.zelzele.customview.CustomTextView;

/**
 * Created by Payam on 1/7/2018.
 */

public class MessagesDialogYesNo extends Dialog {

    private final Context ctx;
    CustomTextView txtMessage, txtAccept, txtCancel;
    private int okText = R.string.yes;

    private int cancelText = R.string.no;

    String methodName;
    String message_txt;
    int message;
    public MessagesDialogButtons interfaceDialogButtons;


    public MessagesDialogYesNo(Context context, String message) {
        super(context, R.style.Theme_Dialog);


        this.message_txt = message;
        this.ctx = context;

    }


    public MessagesDialogYesNo(Context context, String message, int okText, int cancelText) {
        super(context, R.style.Theme_Dialog);


        this.message_txt = message;
        this.ctx = context;
        this.okText = okText;
        this.cancelText = cancelText;

    }


    public void setMessageDialogButton(MessagesDialogButtons listener) {
        this.interfaceDialogButtons = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message_yes_no);
        // getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();

    }

    private void initView() {


        txtMessage = (CustomTextView) findViewById(R.id.txtMessage);
        txtMessage.setLineSpacing(1.5f, 1.5f);
        if (message_txt != null)
            txtMessage.setText(message_txt);
        else
            txtMessage.setText(message);

        txtAccept = (CustomTextView) findViewById(R.id.txtTryAgain);
        txtAccept.setText(okText);
        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interfaceDialogButtons!=null)
                    interfaceDialogButtons.AcceptButtonClicked();
                dismiss();
            }
        });

        txtCancel = (CustomTextView) findViewById(R.id.txtCancel);
        txtCancel.setText(cancelText);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaceDialogButtons.CancleButtonClicked();
                dismiss();
            }
        });
        txtCancel.setVisibility(cancelButtonVisibility);
    }
    int cancelButtonVisibility;
    public void setCancelButtonVisibility(int cancelButtonVisibility) {
        this.cancelButtonVisibility = cancelButtonVisibility;
    }


    public interface MessagesDialogButtons {

        public void AcceptButtonClicked();

        public void CancleButtonClicked();

    }

}
