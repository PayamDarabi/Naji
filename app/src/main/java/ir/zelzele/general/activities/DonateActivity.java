package ir.zelzele.general.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import ir.zelzele.R;
import ir.zelzele.customview.CustomButtonView;
import ir.zelzele.util.IabHelper;
import ir.zelzele.util.IabResult;
import ir.zelzele.util.Inventory;
import ir.zelzele.util.Purchase;


public class DonateActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private RadioGroup radioSlcthm;
    CustomButtonView btn_submit;
    static final String TAG = "bazar";
    static String SKU_PREMIUM = "1";
    boolean mIsPremium = false;
    static final int RC_REQUEST = 124;
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        getSupportActionBar().setTitle(getString(R.string.donat));
        radioSlcthm = (RadioGroup) findViewById(R.id.radiohm);


        try {
            String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDWcdomC61KAq3GxtgOG0ylb2pl86uiQzqNZ2yICuHkcWYeHdT61uAsj" +
                    "e3P3N+3LZarjgFJnttBZ+sGY+0M3XHZ3BlLHjvGboJnTK0WGQWBqYZ0thibzs2Eb4uJ/02V8vd/XJuOJtnIC11I3H6ddg11WoUPaViNnJ9uYXOW8It4Eza7" +
                    "MHxv6WGKUKzOnI9OlEURDr8g6UgWqU08aFGCHBo6ZFkWgv+Oaaj0qbcMGmECAwEAAQ==";
            mHelper = new IabHelper(getApplicationContext(), base64EncodedPublicKey);

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                    }
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            });
            btn_submit = (CustomButtonView) findViewById(R.id.btnDisplay);
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId = radioSlcthm.getCheckedRadioButtonId();
                    View radioButton = radioSlcthm.findViewById(selectedId);
                    int idx = radioSlcthm.indexOfChild(radioButton);
                    if (idx == 0) {
                        SKU_PREMIUM = "1";
                    }
                    if (idx == 1) {
                        SKU_PREMIUM = "2";
                    }
                    if (idx == 2) {
                        SKU_PREMIUM = "5";
                    }

                    if (mHelper != null) {
                        try {
                            mHelper.flagEndAsync();
                            mHelper.launchPurchaseFlow(DonateActivity.this,
                                    SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "payload-string");
                        } catch (IllegalStateException ex) {
                            Toast.makeText(getApplicationContext(), R.string.tryAgaain,
                                    Toast.LENGTH_LONG).show();
                       /* mHelper.flagEndAsync();
                        mHelper.launchPurchaseFlow(DonateActivity.this,
                                SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, "payload-string");*/
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            try {
                if (result.isFailure()) {
                    return;
                } else {
                    mIsPremium = inventory.hasPurchase(SKU_PREMIUM);
                    if (mIsPremium)
                        mHelper.consumeAsync(inventory.getPurchase(SKU_PREMIUM),
                                mConsumeFinishedListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            try {
                if (result.isFailure()) {
                    return;
                } else if (purchase.getSku().equals(SKU_PREMIUM)) {
                    Toast.makeText(getApplicationContext(), R.string.donatesuccess, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    try {
                        if (result.isSuccess()) {
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mHelper != null) mHelper.dispose();
            mHelper = null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}