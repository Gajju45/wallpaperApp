package com.android.gajju45.pexelapiApp;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class FullScreenWallpaperActivity extends AppCompatActivity {
    String originalUrl = " ";

    String mediumUrl = " ";
    PhotoView photoView;
    CircularProgressButton setWallpaperButton, downloadWallpaper;
    ImageView shareBtn, backBytton;
    ImageView constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);
        Intent intent = getIntent();
        originalUrl = intent.getStringExtra("originalUrl");
        mediumUrl = intent.getStringExtra("mediumlUrl");


        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        shareBtn = findViewById(R.id.shareImg);
        backBytton = findViewById(R.id.backButton);

        backBytton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FullScreenWallpaperActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


        constraintLayout=findViewById(R.id.medium_photo);
        Glide.with(this).load(mediumUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                }).into(constraintLayout);




        photoView = findViewById(R.id.photoView);
        Glide.with(this).load(originalUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(photoView);

        setWallpaperButton = (CircularProgressButton) findViewById(R.id.buttonSetWallpaper);
        setWallpaperButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                AsyncTask<String, String, String> demosetWallpaper = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenWallpaperActivity.this);
                            Bitmap bitmap2 = ((BitmapDrawable) photoView.getDrawable()).getBitmap();

                            try {
                                wallpaperManager.setBitmap(bitmap2);
                                Toast.makeText(FullScreenWallpaperActivity.this, "Set Wllpaper succesfully ", Toast.LENGTH_SHORT).show();
                                Bitmap bm = drawableToBitmap(getResources().getDrawable(R.drawable.ic_baseline_done_24));
                                // setWallpaperButton.doneLoadingAnimation(R.color.purple_200,bm);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                };
                // setWallpaperButton.startAnimation();
                demosetWallpaper.execute();

            }
        });
        //Download
        downloadWallpaper = (CircularProgressButton) findViewById(R.id.buttonDownloadWallpaper);
        downloadWallpaper.setOnClickListener(new View.OnClickListener() {

            // @SuppressLint("StaticFieldLeake")
            @Override
            public void onClick(View v) {
                new StyleableToast
                        .Builder(FullScreenWallpaperActivity.this)
                        .text("Downloading Start")
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.WHITE)
                        .iconEnd(R.drawable.ic_baseline_download_for_offline_24)
                        .show();
                @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> demosetWallpaper = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        try {

                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "done";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if (s.equals("done")) {

                            AltexImageDownloader.writeToDisk(FullScreenWallpaperActivity.this, originalUrl, "pexels");

                            Bitmap bitmap2 = drawableToBitmap(getResources().getDrawable(R.drawable.ic_baseline_done_24));
                            //  downloadWallpaper.doneLoadingAnimation(R.color.purple_200, bitmap2);
                           // downloadWallpaper.setText("Succesfully Download");
                            new StyleableToast
                                    .Builder(FullScreenWallpaperActivity.this)
                                    .text("Downloading Succesfully")
                                    .textColor(Color.WHITE)
                                    .iconEnd(R.drawable.ic_baseline_done_24)
                                    .backgroundColor(Color.GREEN)
                                    .show();
                        }

                    }
                };
                //  downloadWallpaper.startAnimation();
                demosetWallpaper.execute();

            }
        });

        //share button
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = photoView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                try {
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + "image.jpeg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");

                    startActivity(Intent.createChooser(intent, "Share image via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap2 = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap2 = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap2);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap2;
    }
}