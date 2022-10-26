package com.example.android.myalarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myalarm.adapter.AlarmAdapter;
import com.example.android.myalarm.data.Contract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView listView;
    FloatingActionButton add_Alarm;
    AlarmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_Alarm = findViewById(R.id.fab);
        listView = findViewById(R.id.listview);

        mAdapter = new AlarmAdapter(this,null);
        listView.setAdapter(mAdapter);
        LoaderManager.getInstance(this).initLoader(0,null,this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView milliSeconds = view.findViewById(R.id.textView7);
                String millis = milliSeconds.getText().toString();

                Intent intent = new Intent(MainActivity.this,Set_New_Alarm.class);
                intent.putExtra("id",id);
                intent.putExtra("millis",millis);
                intent.putExtra("mode","update");
                startActivity(intent);
            }
        });

        add_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Set_New_Alarm.class);
                intent.putExtra("mode","add");
                startActivity(intent);
            }
        });
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,Contract.Alarm.CONTENT_URI,null,null,null,Contract.Alarm.COLUMN_MILLIS);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}