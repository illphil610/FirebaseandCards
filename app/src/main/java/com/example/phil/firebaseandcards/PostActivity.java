package com.example.phil.firebaseandcards;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private ImageButton imageButton;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }

    public void imageButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageButton = (ImageButton) findViewById(R.id.imageButton);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageButton.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        /*
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Log.v("tag", "Hey I am here!!");
            uri = data.getData();
            String pathName = uri.toString();
            Bitmap bmp = BitmapFactory.decodeFile(pathName);
            imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setImageBitmap(bmp);
            //Picasso.with(imageButton.getContext()).load(uri).fit().centerCrop().into(imageButton);
        }
        */

        }
    }
}
