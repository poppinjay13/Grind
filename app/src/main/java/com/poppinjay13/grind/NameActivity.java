package com.poppinjay13.grind;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    ImageView back, save;
    EditText username;
    Preferences preferences = new Preferences();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        preferences.prefConfig(NameActivity.this);

        back = findViewById(R.id.back);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save);

        username.setHint(preferences.readName());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity createActivity = new CreateActivity();
                if(createActivity.check(username)) {
                    preferences.writeName(username.getText().toString());
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
