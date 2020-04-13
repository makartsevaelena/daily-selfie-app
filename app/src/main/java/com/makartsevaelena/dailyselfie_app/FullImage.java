package com.makartsevaelena.dailyselfie_app;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.Serializable;

public class FullImage extends AppCompatActivity implements Serializable {
    private ImageView imageViewFull;
    private File file;
    private static final String PROVIDER = "com.makartsevaelena.dailyselfie_app.provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageViewFull = (ImageView) findViewById(R.id.imageFull);
        setDataFromIntent();
    }

    private void setDataFromIntent() {
        String selfiePath = (String) getIntent().getSerializableExtra("selfiePath");
        if (selfiePath != null) {
            file = new File(selfiePath);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(this, PROVIDER, file);
        }
        imageViewFull.setImageURI(uri);
    }
}
