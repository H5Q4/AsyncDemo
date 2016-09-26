package com.github.geoffreyhuang.asyncdemo.asynctask;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.geoffreyhuang.asyncdemo.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DownloadImageHeadlessFragment extends Fragment {

    public interface AsyncListener {
        void onPreExecute();
        void onProgressUpdate(Integer... progress);
        void onPostExecute(Bitmap result);
        void onCancelled(Bitmap bitmap);
    }

    private static final String ARG_PARAM_URL = "url";
    private static final String TAG = DownloadImageHeadlessFragment.class.getName();

    private AsyncListener mAsyncListener;
    private DownloadImageTask mDownloadImageTask;


    public DownloadImageHeadlessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param url url string.
     * @return A new instance of fragment DownloadImageHeadlessFragment.
     */
    public static DownloadImageHeadlessFragment newInstance(String url) {
        DownloadImageHeadlessFragment fragment = new DownloadImageHeadlessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            String urlString = getArguments().getString(ARG_PARAM_URL);
            mDownloadImageTask = new DownloadImageTask();
            try {
                URL url = new URL(urlString);
                mDownloadImageTask.execute(url);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Can't new URL", e);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAsyncListener = (AsyncListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAsyncListener = null;
    }

    public void cancel() {
        if (mDownloadImageTask != null) {
            mDownloadImageTask.cancel(false);
        }
    }

    private class DownloadImageTask extends AsyncTask<URL, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            if (mAsyncListener != null) {
                mAsyncListener.onPreExecute();
            }
        }

        @Override
        protected Bitmap doInBackground(URL... params) {
            URL url = params[0];
            Bitmap bitmap = null;
            InputStream inputStream = null;
            BufferedInputStream bfi = null;
            final int totalBytes;
            if (isCancelled()) {
                return null;
            }
            publishProgress(0);
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int code = conn.getResponseCode();
                if (code != HttpURLConnection.HTTP_OK) {
                    throw new Exception("Unsuccessful result code.");
                }
                totalBytes = conn.getContentLength();
                inputStream = conn.getInputStream();
                bfi = new BufferedInputStream(inputStream) {
                    int progress = 0;
                    int downloadedBytes = 0;

                    @Override
                    public synchronized int read(byte[] buffer, int byteOffset, int byteCount)
                            throws IOException {
                        int readBytes = super.read(buffer, byteOffset, byteCount);
                        if (isCancelled()) {
                            return -1;
                        }
                        downloadedBytes += readBytes;
                        int percent = (downloadedBytes * 100) / totalBytes;
                        if (percent > progress) {
                            publishProgress(percent);
                            progress = percent;
                        }
                        return readBytes;
                    }
                };
                if (!isCancelled()) {
                    bitmap = BitmapFactory.decodeStream(bfi);
                }
                bfi.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bfi != null) {
                    try {
                        bfi.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mAsyncListener != null) {
                mAsyncListener.onProgressUpdate(values);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mAsyncListener != null) {
                mAsyncListener.onPostExecute(bitmap);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            if (mAsyncListener != null) {
                mAsyncListener.onCancelled(bitmap);
            }
        }
    }

}

