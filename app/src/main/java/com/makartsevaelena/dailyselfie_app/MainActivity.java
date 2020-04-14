package com.makartsevaelena.dailyselfie_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_IMAGE = 1;
    private static final String TAG = "myLogs";
    private static final String PROVIDER = "com.makartsevaelena.dailyselfie_app.provider";
    private static final long INTERVAL_TWO_MINUTES = 2 * 60 * 1000L;
    private ArrayList<Selfie> selfiesList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private String absolutePathCurrentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selfiesList = new ArrayList<Selfie>();
        recyclerViewAdapter = new RecyclerViewAdapter(this, selfiesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        scheduleRepeatingRTCNotification(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.camera) {
            getImageFromCamera();
        }
        if (id == R.id.delete_all) {
            if (!getStorageDirlistFiles().isEmpty()) {
                for (File file : getStorageDirlistFiles()) {
                    file.delete();
                }
            }
            selfiesList.clear();
            recyclerViewAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<File> getStorageDirlistFiles() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ArrayList<File> storageDirlistFiles = new ArrayList<>();
        if (storageDir != null) {
            File[] listFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String name) {
                    return name.endsWith(".jpg");
                }
            });
            if (listFiles != null) {
                storageDirlistFiles.addAll(Arrays.asList(listFiles));
            }
        }
        return storageDirlistFiles;
    }

    public static void scheduleRepeatingRTCNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + INTERVAL_TWO_MINUTES);
        calendar.add(Calendar.SECOND, 5);
        Intent intent = new Intent(context, Receiver.class);
        PendingIntent alarmIntentRTC = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManagerRTC = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManagerRTC != null) {
            alarmManagerRTC.setRepeating(AlarmManager.RTC,
                    calendar.getTimeInMillis(), INTERVAL_TWO_MINUTES, alarmIntentRTC);
        }
    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = FileProvider.getUriForFile(this, PROVIDER, getCurrentFile());
        if (imageUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else Log.d(TAG, "Uri = null");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_IMAGE);
        } else {
            Log.d(TAG, "Intent = null");
        }

    }

    private File getCurrentFile() {
        String imageFileName = "SELFIE_";
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    getStorageDir()      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageFile != null) {
            absolutePathCurrentFile = imageFile.getAbsolutePath();
        } else {
            Log.d(TAG, "imageFile = null");
        }
        return imageFile;
    }

    private File getStorageDir() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            if (!storageDir.exists()) {
                storageDir.mkdir();
            } else {
                Log.d(TAG, "Directory is exists");
            }
        }
        return storageDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                Log.d(TAG, "onActivityResult data = null");
            } else {
                Uri uri = null;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                    uri = Uri.fromFile(new File(absolutePathCurrentFile));
                } else {
                    uri = FileProvider.getUriForFile(this, PROVIDER, new File(absolutePathCurrentFile));
                }
                Log.d(TAG, "Uri is " + uri);

            }
        } else {
            Log.d(TAG, "Canceled");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selfiesList.clear();
        for (File file : getStorageDirlistFiles()) {
            selfiesList.add(new Selfie(file.getAbsolutePath()));
        }
        recyclerViewAdapter.notifyDataSetChanged();

    }

}
