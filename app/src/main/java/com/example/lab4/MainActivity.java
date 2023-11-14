package com.example.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listNoteItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lvNotes;
    TextView lblNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvNotes = findViewById(R.id.lvNotes);

        adapter = new ArrayAdapter<>(this, R.layout.item_list_layout, R.id.tvTitle, this.listNoteItems);
        lvNotes.setAdapter(adapter);
        lblNotes = findViewById(R.id.tvNotes);

        lblNotes.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(i);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_options_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = this.getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
        String lastSavedNote = sharedPref.getString(Constants.NOTE_KEY,"NA" );
        String lastSavedNoteDate = sharedPref.getString(Constants.NOTE_KEY_DATE, "1900-01-01");
        Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, null);

        if (savedSet != null) {
            this.listNoteItems.clear();
            this.listNoteItems.addAll(savedSet);
            this.adapter.notifyDataSetChanged();
        }

        Snackbar.make(lvNotes, String.format("%s: %s", getString(R.string.msg_last_saved_note), lastSavedNote), Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, lastSavedNoteDate, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.add_note) {
            Intent i = new Intent(this, AddNoteActivity.class);
            startActivity(i);
            return true;
        } else if (itemId == R.id.remove_note) {
            Intent u = new Intent(this, DeleteNoteActivity.class);
            startActivity(u);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

