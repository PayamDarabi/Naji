package ir.zelzele.general.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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

public class LoginActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    EditText et_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("fa"));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }

        try {

            final CustomButtonView btn_login = (CustomButtonView) findViewById(R.id.btn_login);
            et_login = (EditText) findViewById(R.id.et_login_username);

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
            final AVLoadingIndicatorView prg_login = (AVLoadingIndicatorView) findViewById(R.id.prg_login);
            prg_login.hide();

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
               //     imm.hideSoftInputFromWindow(et_login.getWindowToken(), 0);
                    final String username = et_login.getText().toString();
                    if (checkMobileNumber(username)) {
                        prg_login.show();
                        final String device_name = AppInfoProvider.getDeviceName();
                        ApiMethodCaller.getInstance(false, getApplicationContext()).signIn(
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
                                        prg_login.hide();
                                        PreferenceManager.getInstance(getApplicationContext()).setUsername(username);
                                        PreferenceManager.getInstance(getApplicationContext()).setToken(responses.getToken());
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();

                                    }

                                    @Override
                                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                                        prg_login.hide();
                                        PreferenceManager.getInstance(getApplicationContext()).setUsername(username);
                                        PreferenceManager.getInstance(getApplicationContext()).setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();

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
            et_login.setError(getString(R.string.create_error_no_name));
            et_login.requestFocus();
            noError = false;
            et_login.setEnabled(true);
        }
        name = name.trim();
        if (!(name.startsWith("09") && name.length() == 11)) {
            et_login.setError(getString(R.string.mobile_number_invalid));
            et_login.requestFocus();
            noError = false;
            et_login.setEnabled(true);
        }
        return noError;
    }

    @Override
    protected void onDestroy() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_login.getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_login.getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onStop();
    }
}