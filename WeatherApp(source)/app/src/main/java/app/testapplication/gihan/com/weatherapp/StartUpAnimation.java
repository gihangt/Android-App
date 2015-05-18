package app.testapplication.gihan.com.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 *This class handle StartUp Animation
 */

public class StartUpAnimation extends Activity{

    //sound variable and other variable
    MediaPlayer ourSong;
    ImageView sun_image;
    Animation rotating;

    //Initialize animation attribute ,sound attribute
    //and display startup page.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide action bar and notification bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.startup_page);

        //Initialize image
        sun_image  =(ImageView)findViewById(R.id.sun_img_ani);

        //Image rotate
        rotating = AnimationUtils.loadAnimation(StartUpAnimation.this,R.anim.rotate);
        sun_image.startAnimation(rotating);

        //Initialize sound track
        ourSong = MediaPlayer.create(StartUpAnimation.this, R.raw.water_drip);
        ourSong.start();


        //Create thread to keep screen 3 second
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                    StartUpAnimation.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent openStartingPoint = new Intent("app.testapplication.gihan.com.weatherap.MainActivity");
                            startActivity(openStartingPoint);
                        }
                    });
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    try{
                        sleep(5000);
                    }catch (Exception e){
                        //Do nothing.
                    }
                    finish();
                }
            }
        };
        timer.start();
    }

    //Stop sound track.
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        ourSong.release();
        rotating.cancel();
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //sun_image.startAnimation(rotating);
        }
    }







}
