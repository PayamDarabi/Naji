package ir.zelzele.general.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import androidx.core.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.Locale;

import ir.zelzele.R;
import ir.zelzele.models.LoginResponse;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppIntro2 {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));

            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
        getSupportActionBar().hide();

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.intro_first_title), getString(R.string.intro_first_desc) +
                        "\n" +
                        getString(R.string.intro_first_desc2),
                R.drawable.earthquake, getResources().getColor(R.color.colorPrimary)));

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.intro_safePlaces_title), getString(R.string.intro_safePlaces_desc),
                R.drawable.safe, getResources().getColor(R.color.colorPrimary)));

        addSlide(AppIntro2Fragment.newInstance(getString(R.string.intro_learn_title), getString(R.string.intro_learn_desc),
                R.drawable.elearn, getResources().getColor(R.color.colorPrimary)));

        PreferenceManager.getInstance(this).setSeeIntro(true);

        // Override bar/separator color
        setBarColor(getResources().getColor(R.color.colorPrimaryDark));

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices

        // setNavBarColor(Color.parseColor("#3F51B5"));

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);
        nextButton.setScaleX(1);


        setProgressButtonEnabled(true);
        setFlowAnimation();
        final String device_name = AppInfoProvider.getDeviceName();
        try {
            ApiMethodCaller.getInstance(false, getApplicationContext()).signIn(
                    "",
                    "",
                    "",
                    device_name,
                    "android",
                    "",
                    new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            try {
                                LoginResponse responses = response.body();
                                PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                                if (responses.getToken().isEmpty())
                                    PreferenceManager.getInstance(getApplicationContext())
                                            .setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                                else
                                    PreferenceManager.getInstance(getApplicationContext()).setToken(responses.getToken());
                            } catch (Exception e) {
                                e.printStackTrace();
                                PreferenceManager.getInstance(getApplicationContext())
                                        .setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            PreferenceManager.getInstance(getApplicationContext()).setUsername("");
                            PreferenceManager.getInstance(getApplicationContext()).setToken("h42nsjk98kkdiwjd7hbw6ha0m75bgf1");
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
     //   if (!PreferenceManager.getInstance(getApplicationContext()).getUsername().equals("") &&
            //    !PreferenceManager.getInstance(getApplicationContext()).getToken().equals("")) {
            startActivity(new Intent(IntroductionActivity.this, MainActivity.class));
            finish();
/*
        } else {
            startActivity(new Intent(IntroductionActivity.this, LoginActivity.class));
            finish();
            // No user is logged-in
        }*/
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        //    if (!PreferenceManager.getInstance(getApplicationContext()).getUsername().equals("") &&
        //          !PreferenceManager.getInstance(getApplicationContext()).getToken().equals("")) {
        startActivity(new Intent(IntroductionActivity.this, MainActivity.class));
        finish();

        // } else {
        //        startActivity(new Intent(IntroductionActivity.this, LoginActivity.class));
        //        finish();
        // No user is logged-in
        //   }
    }
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}