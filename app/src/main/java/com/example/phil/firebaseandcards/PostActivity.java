package com.example.phil.firebaseandcards;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import  android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 2;
    private ImageButton imageButton;
    private Uri uri = null;
    private EditText editName;
    private EditText editDesc;

    // Reference to Firebase storage stuff waz-aaaaaaa
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Get the actual instance of the reference
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getInstance().getReference().child("CardApp");

        editName = (EditText) findViewById(R.id.edit_name);
        editDesc = (EditText) findViewById(R.id.enter_desc);
    }

    public void imageButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Make sure it's legit
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageButton = (ImageButton) findViewById(R.id.imageButton);
            uri = data.getData();
            Bitmap bitmap;

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageButton.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // THINK FAST!
                e.printStackTrace();
            }
        }
    }

    public void submitButtonClicked(View view) {
        final String nameFromEditText = editName.getText().toString().trim();
        final String descFromEditText = editDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(nameFromEditText) && !TextUtils.isEmpty(descFromEditText)) {
            StorageReference filePath = storageReference.child("PostImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(PostActivity.this, "Upload Complete!", Toast.LENGTH_LONG).show();
                    DatabaseReference newPost = databaseReference.push();
                    newPost.child("title").setValue(nameFromEditText);
                    newPost.child("description").setValue(descFromEditText);
                    newPost.child("image").setValue(downloadurl.toString());
                }
            });

            Intent intent = new Intent(PostActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
