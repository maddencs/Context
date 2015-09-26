package com.example.cory.annotate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cory.annotate.R;

public class Note extends AppCompatActivity {

    private String mTitle;
    private String mBody;
    private String[] mTags;
    private long mNetworkId;
    private String mDateCreated;
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        TextView title = (TextView) findViewById(R.id.noteTitle);
        TextView body = (TextView) findViewById(R.id.noteBody);
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        body.setText(intent.getStringExtra("body"));
    }

    public Note(String title, String body, String[] tags, String dateCreated) {
        mTitle = title;
        mBody = body;
        mTags = tags;
        mDateCreated = dateCreated;
    }
    public Note() {}

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getNoteTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String[] getTags() {
        return mTags;
    }

    public void setTags(String[] tags) {
        mTags = tags;
    }

    public long getNetworkId() {
        return mNetworkId;
    }

    public void setNetworkId(long networkId) {
        mNetworkId = networkId;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public long getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
