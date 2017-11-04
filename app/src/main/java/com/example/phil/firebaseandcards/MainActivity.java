package com.example.phil.firebaseandcards;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CardApp");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter <EventReport, EventViewHolder> FBRA = new FirebaseRecyclerAdapter<EventReport, EventViewHolder>(
                EventReport.class, R.layout.card_row, EventViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, EventReport model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };
        mRecyclerView.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_icon) {
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
        }

        if (id == R.id.log_out) {
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public EventViewHolder(View itemView) {
            super(itemView);
            View mView = itemView;
        }

        public void setTitle(String title) {
            TextView event_title = itemView.findViewById(R.id.report_title);
            event_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView event_desc = itemView.findViewById(R.id.report_desc);
            event_desc.setText(desc);
        }

        public void setImage(Context context, String image) {
            ImageView event_image = itemView.findViewById(R.id.report_card);
            Picasso.with(context).load(image).into(event_image);
        }
    }
}
