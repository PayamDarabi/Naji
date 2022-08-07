package ir.zelzele.general.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.zelzele.R;
import ir.zelzele.general.adapters.ExpandableListAdapter;
import ir.zelzele.utils.Constants;
import ir.zelzele.utils.Tools;
import ir.zelzele.webservices.DownloadTask;

public class SafePlacesActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 47;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    static HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_places);
        getSupportActionBar().setTitle(R.string.tehransafes);
        expListView = (ExpandableListView) findViewById(R.id.lvExp_safePlaces);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(5);
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        expListView.setOnChildClickListener(new OnChildClickListener() {


            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String parent_id = listDataHeader.get(groupPosition).replace("منطقه", "").trim();
                parent_id = Tools.converNumberPersianToEnglish(parent_id);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()) {
                        try {
                            download(Constants.DownloadBaseUrl + (parent_id) + "/" +
                                            Uri.encode(listDataChild.get(
                                                    listDataHeader.get(groupPosition)).get(
                                                    childPosition) + ".jpg"),
                                    listDataChild.get(
                                            listDataHeader.get(groupPosition)).get(
                                            childPosition));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        requestPermission();
                    }
                } else {
                    download(Constants.DownloadBaseUrl + (parent_id) + "/" +
                                    Uri.encode(listDataChild.get(
                                            listDataHeader.get(groupPosition)).get(
                                            childPosition) + ".jpg"),
                            listDataChild.get(
                                    listDataHeader.get(groupPosition)).get(
                                    childPosition));
                }

                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SafePlacesActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, R.string.ext_mem_permission, Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
    }

    private void requestPermission() {

        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SafePlacesActivity.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //          Toast.makeText(SafePlacesActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(SafePlacesActivity.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        InputStream inputStream = getResources().openRawResource(R.raw.regions2);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jArray = new JSONArray(
                    byteArrayOutputStream.toString());
            String cat_Id = "";
            JSONArray cat_name = null;
            for (int i = 0; i < jArray.length(); i++) {
                cat_Id = jArray.getJSONObject(i).getString("id");
                String header = getString(R.string.regions) + " " + Tools.convertEnglishNumbersToPersian(cat_Id);
                listDataHeader.add(header);
                cat_name = jArray.getJSONObject(i).getJSONArray("data");
                List<String> names = new ArrayList<String>();
                for (int j = 0; j < cat_name.length(); j++) {
                    names.add(cat_name.get(j).toString().trim());
                }
                listDataChild.put(listDataHeader.get(i), names);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
            throw new RuntimeException("This is a crash");
        }
    }

    private void download(final String address, final String name) {
        try {
            String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/naji/safePlaces/";
            File path = new File(PATH);
            path.mkdirs();
            File file = new File(PATH + name + ".jpg");
            if (!file.exists())
                new DownloadTask(this, file, getString(R.string.downloading) + " " + name).execute(address);
            else {
                showPhoto(Uri.fromFile(file), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("This is a crash");
        }
    }

    private void showPhoto(Uri photoUri, Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(photoUri, "image/jpg");
            context.startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("This is a crash");
        }
    }
}