package com.bdc.ociney.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.bdc.ociney.R;
import com.bdc.ociney.modele.Movie.Movie;
import com.bdc.ociney.task.LoadMoviesTask;
import com.bdc.ociney.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by devandroid on 24/04/14.
 */
public class SplashScreenActivity extends Activity implements LoadMoviesTask.LoadMoviesTaskCallBack {

    boolean chargementFinit = false;
    boolean erreurReseau = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.splash_screen);

        //commencer a chercher la localisation
        new LocationUtils(this, null).chercherLocation();

        new LoadMoviesTask(this).execute();

        tournerRoulette();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLoadMoviesTaskCallBack(ArrayList<List<Movie>> movies) {
        AppFragmentActivity.moviesNowShowing = movies.get(0);
        AppFragmentActivity.moviesCommingSoon = movies.get(1);

        chargementFinit = true;
    }

    @Override
    public void onErreurReseau() {
        erreurReseau = true;
        Toast.makeText(getApplicationContext(),R.string.erreur_reseau,Toast.LENGTH_SHORT).show();
        new View(this).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    public void lancerAppli() {
        Intent intent = new Intent(SplashScreenActivity.this, AppFragmentActivity.class);
        startActivity(intent);
        //overridePendingTransition(0, 0);
        finish();
    }

    protected void tournerRoulette() {
        int previousDegrees = 0;
        int degrees = 360;
        final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setDuration(1500);//Set the duration of the animation to 1 sec.
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (chargementFinit)
                    lancerAppli();
                else {
                    if (!erreurReseau)
                        findViewById(R.id.placeholder_image).startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.placeholder_image).startAnimation(animation);
    }
}
