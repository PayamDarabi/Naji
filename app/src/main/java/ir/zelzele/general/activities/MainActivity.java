package ir.zelzele.general.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.DrawerLayout;
import androidx.core.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.DefaultItemAnimator;
import androidx.appcompat.widget.DividerItemDecoration;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ir.zelzele.R;
import ir.zelzele.classes.RecyclerTouchListener;
import ir.zelzele.customview.CustomTextView;
import ir.zelzele.dialogs.UserNameDialog;
import ir.zelzele.general.adapters.ItemsAdapter;
import ir.zelzele.models.Target;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.utils.Tools;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    static boolean showList = true;
    private static boolean doubleBackToExitPressedOnce = false;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 10;
    RecyclerView rw_items;
    private ItemsAdapter mAdapter;
    //private static boolean flashLight = false;
    static Camera camera = null;
    FloatingActionButton fab;
    AVLoadingIndicatorView prg_main;
    DrawerLayout drawer;
    NavigationView navigationView;
     CustomTextView txt_noThing;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Configuration configuration = getResources().getConfiguration();
                configuration.setLayoutDirection(new Locale("fa"));
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
            }

            //     ButterKnife.bind(this);
            rw_items = (RecyclerView) findViewById(R.id.rw_items);
            prg_main = (AVLoadingIndicatorView) findViewById(R.id.prg_main);
            txt_noThing = (CustomTextView) findViewById(R.id.txt_nothing);


            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            //   toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SignalsActivity.class);
                    startActivity(intent);

                    /*if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            try {
                                turnFlashOnOff();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        R.string.cameraRequest,
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            requestPermission();
                        }
                    } else {
                        turnFlashOnOff();
                    }*/
                }
            });

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rw_items.setLayoutManager(mLayoutManager);
            rw_items.setItemAnimator(new DefaultItemAnimator());
            rw_items.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                    LinearLayoutManager.VERTICAL));


            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {

                        //           if (showList) {
           /*                 getData(true);*/
                        //          }
                        //          else
                        getData(false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    //      mSwipeRefreshLayout.setRefreshing(true);
                    try {
                        getData(true);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        txt_noThing.setVisibility(View.VISIBLE);
                    }
                }
            });

         /*   try {
                getData(true);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                txt_noThing.setVisibility(View.VISIBLE);
            }*/
          /*  txt_noThing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (show == 0) {
                            getData(true);
                        }
                        else
                            getData(false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });*/
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());

            String username = Tools.convertEnglishNumbersToPersian(
                    PreferenceManager.getInstance(getApplicationContext()).getUsername());
            final CustomTextView txt_username = (CustomTextView) header.findViewById(R.id.txt_username);
            if (!username.isEmpty()) {
                txt_username.setText(username);
            } else {
                txt_username.setText(R.string.input_number);
                txt_username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserNameDialog cmd = new UserNameDialog(MainActivity.this);
                        cmd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                String username = Tools.convertEnglishNumbersToPersian(
                                        PreferenceManager.getInstance(getApplicationContext()).getUsername());
                                txt_username.setText(username);
                            }
                        });
                        cmd.show();
                    }
                });
            }
        } catch (Exception e0) {
            e0.printStackTrace();
            FirebaseCrash.log(e0.getMessage());
        }
    }

    private void turnFlashOnOff() {
        try {
         /*   if (!flashLight) {
                fab.setImageResource(R.drawable.ic_flash_off);
                turnOnFlash();
                flashLight = true;
            } else {
                fab.setImageResource(R.drawable.ic_flash_on);
                turnOffFlash();
                flashLight = false;
            }*/
        }
        catch (Exception e){
            //camera.release();
            e.printStackTrace();
        }
    }
/*
    public static void turnOnFlash() {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }

    public static void turnOffFlash() {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        camera.release();
    }*/

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, R.string.press_back_again, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
      /*  if (camera != null)
            camera.release();*/
        super.onDestroy();
    }

    public void getData(final boolean isFirstTime) throws UnsupportedEncodingException {
        if (isFirstTime)
            prg_main.show();
        if (Tools.internetConnect()) {
            txt_noThing.setVisibility(View.GONE);
            rw_items.setVisibility(View.VISIBLE);
            ApiMethodCaller.getInstance(false, getApplicationContext()).getTargetList("ir", AppInfoProvider.getDeviceName(), "android",
                    new Callback<List<Target>>() {
                        @Override
                        public void onResponse(Call<List<Target>> call, Response<List<Target>> response) {
                            try {
                                final List<Target> targets = response.body();
                                Collections.sort(targets, new Comparator<Target>() {
                                    @Override
                                    public int compare(Target lhs, Target rhs) {
                                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                        Date a = Tools.convertToDate(lhs.getDate());
                                        Date b = Tools.convertToDate(rhs.getDate());
                                        return b.compareTo(a);
                                    }
                                });
                                mAdapter = new ItemsAdapter(targets);
                                rw_items.setAdapter(mAdapter);
                                if (isFirstTime) {
                                    rw_items.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                                            rw_items, new RecyclerTouchListener.ClickListener() {
                                        @Override
                                        public void onClick(View view, int position) {

                                            Intent intent = new Intent(getBaseContext(), MapActivity.class);
                                            intent.putExtra("lat", targets.get(position).getLatitude());
                                            intent.putExtra("lng", targets.get(position).getLongitude());
                                            intent.putExtra("state", targets.get(position).getState());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onLongClick(View view, int position) {

                                        }

                                    }));
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                                //   mAdapter.notifyAll();
                                if (isFirstTime)
                                    prg_main.hide();
                                txt_noThing.setVisibility(View.GONE);

                            }
                            catch (Exception e){
                         //       showList=true;
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Target>> call, Throwable t) {
                            if (isFirstTime)
                                prg_main.hide();
                        //    showList=true;
                            txt_noThing.setVisibility(View.VISIBLE);
                            rw_items.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
        } else {
            txt_noThing.setVisibility(View.VISIBLE);
            rw_items.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.internet_Fail, Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        try {
            int id = item.getItemId();
            if (id == R.id.nav_tehranSafePlaces) {
                Intent intent = new Intent(MainActivity.this, SafePlacesActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_events) {
            } else if (id == R.id.nav_exit) {
                try {
               /*     PreferenceManager.getInstance(MainActivity.this).setToken("");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);*/
                    finish();
                } catch (Exception e) {
                    finish();
                    e.printStackTrace();
                }
            } else if (id == R.id.nav_naji) {
                Intent intent = new Intent(MainActivity.this, NajiActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_telegram) {
                intentMessageTelegram();
            } /*else if (id == R.id.nav_mostEarthQuicks) {
                Intent webview = new Intent(Intent.ACTION_VIEW, Uri.parse("http://irsc.ut.ac.ir/current_map.php?lang=fa"));
                startActivity(webview);
            }*/
            else if (id == R.id.nav_donate) {
                Intent webview = new Intent(MainActivity.this, DonateActivity.class);
                startActivity(webview);
            } else if (id == R.id.nav_share) {
                try {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            getResources().getString(R.string.share_with_friends_text) +
                                    "\n" +
                                    "https://cafebazaar.ir/app/ir.zelzele/?l=fa");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (id == R.id.leaningTips) {
                Intent intent = new Intent(MainActivity.this, LearningTipsActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_rate) {
                try {
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setData(Uri.parse("http://cafebazaar.ir/app/" + getPackageName() + "/?l=fa"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://cafebazaar.ir/app/" + getPackageName() + "/?l=fa")));
                    //    FirebaseCrash.report(new Exception(e.toString()));
                }
            } else if (id == R.id.nav_refrences) {
                Intent intent = new Intent(MainActivity.this, RefrencesActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    void intentMessageTelegram() {
        Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.telegram)));
        startActivity(telegram);
    }


    private boolean checkPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }

    }

    private void requestPermission() {
        try {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.doubleBackToExitPressedOnce = false;
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkCommented() {
        try {
            boolean isCommented = MainActivity.this.getSharedPreferences("PREFERENCE",
                    Context.MODE_PRIVATE).getBoolean("isCommented", false);
            if (isCommented) {
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    turnFlashOnOff();

                } else {
                    Toast.makeText(this, R.string.cameraRequest, Toast.LENGTH_LONG).show();
                }
            }
            return;
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}