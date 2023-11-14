package com.example.lab4;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DeleteNoteActivity extends AppCompatActivity {

    private Spinner spinnerNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_delete_note);

        spinnerNotes = findViewById(R.id.spinnerNotes);
        Button btnDeleteNote = findViewById(R.id.btnDeleteNote);

        List<String> noteTitles = noteTitlesForSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, noteTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNotes.setAdapter(adapter);

        btnDeleteNote.setOnClickListener(view -> {
            String selectedNoteTitle = spinnerNotes.getSelectedItem().toString();

            removeNoteFromSharedPreferences(selectedNoteTitle);

            noteTitles.remove(selectedNoteTitle);
            adapter.notifyDataSetChanged();

            Toast.makeText(DeleteNoteActivity.this, "Note deleted: " + selectedNoteTitle, Toast.LENGTH_SHORT).show();
        });
    }

    private List<String> noteTitlesForSpinner() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
        Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, new HashSet<>());
        return new ArrayList<>(savedSet);
    }

    @SuppressLint("MutatingSharedPrefs")
    private void removeNoteFromSharedPreferences(String noteTitle) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
        Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, new HashSet<>());

        savedSet.remove(noteTitle);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Set<String> updatedSet = new HashSet<>(savedSet);

        editor.putStringSet(Constants.NOTES_ARRAY_KEY, updatedSet);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

