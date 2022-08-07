package ir.zelzele.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.Locale;

import ir.zelzele.R;
import ir.zelzele.customview.CustomButtonView;
import ir.zelzele.models.LoginResponse;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Payam on 1/20/2018.
 */

public class UserNameDialog extends Dialog

{

    public Activity c;
    public Dialog d;
    EditText et_login;

//    public Button no;

    public UserNameDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = c.getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("fa"));
            c.getResources().updateConfiguration(configuration, c.getResources().getDisplayMetrics());
        }

        try {

            final CustomButtonView btn_login = (CustomButtonView) findViewById(R.id.btn_register);
            et_login = (EditText) findViewById(R.id.et_username);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                et_login.setOnEditorActionListener(new EditText.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if (i == EditorInfo.IME_ACTION_NEXT) {
                            btn_login.performClick();
                            return true;
                        }
                        return false;
                    }
                });
            }
            et_login.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 11) {
                        btn_login.performClick();

                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

          /*  et_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b)
                    {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });*/
            //    et_login.requestFocus();
        //    final AVLoadingIndicatorView prg_login = (AVLoadingIndicatorView) findViewById(R.id.prg_login);
          //  prg_login.hide();

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_login.getWindowToken(), 0);
                    final String username = et_login.getText().toString();
                    if (checkMobileNumber(username)) {
//                        prg_login.show();
                        final String device_name = AppInfoProvider.getDeviceName();
                        ApiMethodCaller.getInstance(false, c).signIn(
                                username,
                                "",
                                "",
                                device_name,
                                "android",
                                "",
                                new Callback<LoginResponse>() {
                                    @Override
                                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                        LoginResponse responses = response.body();
                                   //     prg_login.hide();
                                        PreferenceManager.getInstance(c).setUsername(username);
                                        PreferenceManager.getInstance(c).setToken(responses.getToken());
                                        dismiss();

                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                              //          prg_login.hide();
                                        PreferenceManager.getInstance(c).setUsername(username);
                                        PreferenceManager.getInstance(c).setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                       dismiss();

                                    }
                                });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkMobileNumber(String username) {
        et_login.setError(null);
        String name = username;
        boolean noError = true;
        if (TextUtils.isEmpty(name)) {
            et_login.setError(c.getString(R.string.create_error_no_name));
            et_login.requestFocus();
            noError = false;
            et_login.setEnabled(true);
        }
        name = name.trim();
        if (!(name.startsWith("09") && name.length() == 11)) {
            et_login.setError(c.getString(R.string.mobile_number_invalid));
            et_login.requestFocus();
            noError = false;
            et_login.setEnabled(true);
        }
        return noError;
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
}