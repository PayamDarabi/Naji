package ir.zelzele.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.zelzele.R;

/**
 * Created by Payam on 12/28/2017.
 */

public class DownloadTask extends AsyncTask<String, Integer, String> {
    private ProgressDialog mPDialog;
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private File mTargetFile;
    static boolean isFailed= false;

    //Constructor parameters :
    // @context (current Activity)
    // @targetFile (File object to write,it will be overwritten if exist)
    // @dialogMessage (message of the ProgresDialog)
    public DownloadTask(Context context,File targetFile,String dialogMessage) {
        this.mContext = context;
        this.mTargetFile = targetFile;
        mPDialog = new ProgressDialog(context);

        mPDialog.setMessage(dialogMessage);
        mPDialog.setIndeterminate(true);
        mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mPDialog.setCancelable(false);
        // reference to instance to use inside listener
        final DownloadTask me = this;
        mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                me.cancel(true);
            }
        });
   //     Log.i("DownloadTask","Constructor done");
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(sUrl[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                isFailed=true;
                Log.i("DownloadTask","Response " + connection.getResponseCode()+ url.toString());
                return mContext.getString(R.string.fail_connect_server);
            }


            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(mTargetFile,false);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                 //   Log.i("DownloadTask","Cancelled");
                    input.close();
                    isFailed=true;
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            isFailed=true;
            return e.toString();
        } finally {
            try {
                if (output != null && !isFailed)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

        mPDialog.show();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mPDialog.setIndeterminate(false);
        mPDialog.setMax(100);
        mPDialog.setProgress(progress[0]);

    }

    @Override
    protected void onPostExecute(String result) {
        // Log.i("DownloadTask", "Work Done! PostExecute");
        mWakeLock.release();
        mPDialog.dismiss();
        if (isFailed)
            try {
                mTargetFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        if (result != null)
            Toast.makeText(mContext, mContext.getString(R.string.download_fail), Toast.LENGTH_LONG).show();
        else {

            String fileName = mTargetFile.getName().replace(".jpg", "");
            //   String fileName = parts[0];

            Toast.makeText(mContext, mContext.getString(R.string.file) + " "
                            + fileName + " "
                            + mContext.getString(R.string.successDownload)
                    , Toast.LENGTH_LONG).show();
            showPhoto(Uri.fromFile(mTargetFile), mContext);
        }
    }
    private void showPhoto(Uri photoUri,Context context){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        context.startActivity(intent);
    }
}