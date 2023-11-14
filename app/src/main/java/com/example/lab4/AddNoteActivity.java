package com.example.lab4;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class AddNoteActivity extends AppCompatActivity {
    EditText edTitle;
    EditText edNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_note);

        this.edTitle = findViewById(R.id.edTitle);
        this.edNote = findViewById(R.id.edNote);
        edTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void onBtnSaveClick(View view) {
      try {
          String titleToAdd = this.edTitle.getText().toString();
          String noteToAdd = this.edNote.getText().toString();

          if (titleToAdd.isEmpty() || noteToAdd.isEmpty()) {
              Toast.makeText(this, "Title and Text are required.", Toast.LENGTH_SHORT).show();
          } else {
              Date c = Calendar.getInstance().getTime();
              System.out.println("Current time => " + c);

              SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
              String formattedDate = df.format(c);

              SharedPreferences sharedPref = this.getSharedPreferences(Constants.NOTES_FILE, MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPref.edit();

              Set<String> savedSet = sharedPref.getStringSet(Constants.NOTES_ARRAY_KEY, null);
              Set<String> newSet = new HashSet<>();

              if (savedSet != null) {
                  newSet.addAll(savedSet);
              }

              String fullNote = titleToAdd + "\n\n" + noteToAdd;
              newSet.add(fullNote);

              editor.putString(Constants.NOTE_KEY, fullNote);
              editor.putString(Constants.NOTE_KEY_DATE, formattedDate);
              editor.putStringSet(Constants.NOTES_ARRAY_KEY, newSet);
              editor.apply();
              finish();
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
      }
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