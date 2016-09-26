package com.github.geoffreyhuang.asyncdemo.asynctask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.geoffreyhuang.asyncdemo.R;

public class ShowPuppyWithHeadlessActivity extends AppCompatActivity
        implements DownloadImageHeadlessFragment.AsyncListener {

    private static final String DOWNLOAD_PHOTO_FRAG = "download_photo_frag";

    private DownloadImageHeadlessFragment mDownloadFragment;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_puppy_with_headless);
        Button showPuppyBtn = (Button) findViewById(R.id.btn_show_puppy_with_headless);
        showPuppyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                mDownloadFragment
                        = (DownloadImageHeadlessFragment) fm.findFragmentByTag(DOWNLOAD_PHOTO_FRAG);
                if (mDownloadFragment == null) {
                    mDownloadFragment = DownloadImageHeadlessFragment
                            .newInstance(
                                    "https://hd.unsplash.com/photo-1444212477490-ca407925329e"
                            );
                    fm.beginTransaction().add(mDownloadFragment, DOWNLOAD_PHOTO_FRAG).commit();
                }
            }
        });
    }

    @Override
    public void onPreExecute() {
        if (mProgressDialog == null) {
            prepareProgressDialog();
        }
    }

    private void prepareProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("downloading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDownloadFragment.cancel();
            }
        });
        mProgressDialog.show();
    }

    @Override
    public void onProgressUpdate(Integer... progress) {
        if (mProgressDialog == null) {
            prepareProgressDialog();
        }
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    public void onPostExecute(Bitmap result) {
        if (result != null) {
            ImageView imageView = (ImageView) findViewById(R.id.iv_puppy);
            imageView.setImageBitmap(result);
        }
        cleanUp();
    }

    private void cleanUp() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(DOWNLOAD_PHOTO_FRAG);
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onCancelled(Bitmap bitmap) {
        cleanUp();
    }
}
