package com.fmoreno.countrylistapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fmoreno.countrylistapplication.R;

import utils.Utils;

public class SplashActivity extends AppCompatActivity {

    public static final int segundos = 4;
    public static final int milisegundos = segundos * 1000;
    public static final int delay = 2;

    Animation atg, atgtwo, atgthree, right_in, right_out, zoom_forward_in, zoom_forward_out;

    private ImageView mImageLogo, mImageName, mImageApellido;

    public ProgressBar pbprogreso;

    private TextView footerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
        right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
        zoom_forward_in = AnimationUtils.loadAnimation(this, R.anim.zoom_forward_in);
        zoom_forward_out = AnimationUtils.loadAnimation(this, R.anim.zoom_forward_out);
        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(this, R.anim.atgthree);

        mImageLogo = (ImageView) findViewById(R.id.img_logo);
        mImageName = (ImageView) findViewById(R.id.img_nombre);
        mImageApellido = (ImageView) findViewById(R.id.img_apellido);
        pbprogreso= (ProgressBar) findViewById(R.id.pbprogreso);
        footerInfo = (TextView) findViewById(R.id.footerInfo);
        setFooterInfo();

        startAnimation();
        pbprogreso.setMax(maximumProgress());
        nextActivity();
    }

    private void setFooterInfo() {
        try {
            String currentYear = Utils.getCurrentYear();
            //String footerMessage = "\u00A9" + currentYear + " Fabian Moreno" R.string.str_name_complete;
            String footerMessage = "\u00A9" + currentYear + " | " + Utils.getStringFromResource(R.string.str_name, this);

            footerInfo.setText(footerMessage);
        } catch (Exception e) {
            //FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void startAnimation(){
        try{
            mImageLogo.startAnimation(atg);
            mImageApellido.startAnimation(right_in);
            mImageName.startAnimation(right_in);
            pbprogreso.startAnimation(zoom_forward_in);
        }catch (Exception ex){
            //FirebaseCrashlytics.getInstance().recordException(ex);
        }
    }

    private void nextActivity(){
        try{
            new CountDownTimer(milisegundos,1000){
                @Override
                public void onTick(long millisUntilFinished) {
                }@Override
                public void onFinish() {
                    Intent mainActivity = new Intent(SplashActivity.this, CountryListActivity.class);
                    startActivity(mainActivity);
                    overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                    finish();
                }
            }.start();
        }catch (Exception ex){
            //FirebaseCrashlytics.getInstance().recordException(ex);
        }
    }

    public int maximumProgress(){
        return segundos-delay;
    }
}