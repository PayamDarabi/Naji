package ir.zelzele.general.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ir.zelzele.R;

public class SignalsActivity extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST_CODE = 10;
    private static boolean torchLight_offOn = false;
    private static boolean flashLight_offOn = false;
    MediaPlayer mplayer;

    static Vibrator vibartor;

    private static boolean led_flash = false;
    private static boolean play_noise = false;
    private static boolean vibrate=false;

    float oldBrightNess = 0;
    float newBrightNess = 1.0f;
    static Camera cam;
    Camera.Parameters p;

    Handler handler;
    ImageView imgv_flash, imgv_led, imgv_noise, imgv_torch, img_vibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signals);
      //  LinearLayout lay_flash = (LinearLayout) findViewById(R.id.lay_flashlight);
     //   LinearLayout lay_ledLight = (LinearLayout) findViewById(R.id.lay_ledlight);
        LinearLayout lay_noise = (LinearLayout) findViewById(R.id.lay_noise);
        LinearLayout lay_torch = (LinearLayout) findViewById(R.id.lay_torch);
        LinearLayout lay_vibrate = (LinearLayout) findViewById(R.id.lay_vibrate);
  //      imgv_flash = (ImageView) findViewById(R.id.imgv_flash);
  ///      imgv_led = (ImageView) findViewById(R.id.imgv_ledlight);
        imgv_noise = (ImageView) findViewById(R.id.imgv_noise);
        imgv_torch = (ImageView) findViewById(R.id.imgv_torch);
        img_vibrate = (ImageView) findViewById(R.id.imgv_vibrate);

        vibartor = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        lay_torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (checkPermission()) {
                            try {
                                turnTorchOnOff();
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
                        turnTorchOnOff();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

      /*  lay_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lay_ledLight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    if (!led_flash) {
                        imgv_led.setImageResource(R.drawable.ic_phone_android_accent);
                        led_flash = true;
                    } else {
                        imgv_led.setImageResource(R.drawable.ic_phone_android_black);
                        led_flash = false;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
        lay_noise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!play_noise) {
                    imgv_noise.setImageResource(R.drawable.ic_volume_off_accent);
                    startPlay(view);
                    play_noise = true;
                } else {
                    imgv_noise.setImageResource(R.drawable.ic_volume_up_black);
                    stopPlay(view);
                    play_noise = false;
                }
            }
        });

        lay_vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!vibrate) {
                    img_vibrate.setImageResource(R.drawable.ic_vibration_accent);
                    long[] pattern = {0, 100, 100};
                    vibartor.vibrate(pattern, 0);
                    vibrate = true;
                } else {
                    img_vibrate.setImageResource(R.drawable.ic_vibration);
                    // stopPlay(view);
                    vibartor.cancel();
                    vibrate = false;
                }
            }
        });
    }

    private void turnFlashOnOff() {
        if (!flashLight_offOn) {
            imgv_flash.setImageResource(R.drawable.ic_flash_off_accent);
            flashSpeedTest();
            //      flashLight_offOn = true;
        } else {
            imgv_flash.setImageResource(R.drawable.ic_flash_on_black);
            turnOffTorch();
            flashLight_offOn = false;
        }
    }

    private void turnTorchOnOff() {
        try {
            if (!torchLight_offOn) {
                imgv_torch.setImageResource(R.drawable.ic_torch_accent);
                turnOnTorch();
                torchLight_offOn = true;
            } else {
                imgv_torch.setImageResource(R.drawable.ic_torch);
                turnOffTorch();
                torchLight_offOn = false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void turnOnTorch() {
        if (cam == null) {
            prepareCamera();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
        }

    }

    public void turnOffTorch() {
        if (cam != null) {
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam=null;
        }
    }

    public void prepareCamera() {
        if (cam == null) {
            try {
                cam = Camera.open();
                p = cam.getParameters();
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                    cam.startPreview();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), R.string.fail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void flashSpeedTest() {
        final int[] x = {0};
        prepareCamera();
        while (!flashLight_offOn) {
            if (x[0] % 2 == 0)
                turnOnTorch();
            else
                turnOffTorch();
            x[0]++;
        }
    }


    public void startPlay(View view) {
        try {
            stopPlaying();
            mplayer = MediaPlayer.create(this, R.raw.nws_alert);
            mplayer.setLooping(true);
            mplayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (mplayer != null)
                mplayer.pause();
            stopPlaying();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void stopPlaying() {
        try {
            if (mplayer != null) {
                mplayer.stop();
                mplayer.release();
                mplayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            ActivityCompat.requestPermissions(SignalsActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaying();
        if(vibartor!=null)
            vibartor.cancel();
        if(cam!=null)
            cam.release();
    }
}/*
    public static void turnOnTorch()
    {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }

    public static void turnOffTorch()
    {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        camera.release();
    }
    public static void turnOnFlash()
    {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_RED_EYE);
        camera.setParameters(p);
        camera.startPreview();
    }

    public static void turnOffFlash()
    {
        camera = Camera.open();
        Camera.Parameters p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        camera.release();
    }*/