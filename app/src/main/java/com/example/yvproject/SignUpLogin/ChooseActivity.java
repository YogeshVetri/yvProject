package com.example.yvproject.SignUpLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yvproject.R;
import com.example.yvproject.staff.MainActivity;
import com.example.yvproject.student.StudentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseActivity extends AppCompatActivity {

    TextView tvoption;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        tvoption = findViewById(R.id.textoption);
        if (mAuth.getCurrentUser() != null) {
            checkCategoryandupdateUI();
        }

    }


    private void checkCategoryandupdateUI() {

        updateWelcome();
    }


    private void updateWelcome() {
        String category = tvoption.getText().toString();
        if (category.equals("staff")) {

            Intent staffActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(staffActivity);
            finish();
        } else if (category.equals("student")) {
            Intent studentActivity = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(studentActivity);
            finish();

        } else {

            Toast.makeText(this, "Choose Your Category", Toast.LENGTH_SHORT).show();
        }

    }

    public void staff(View view) {
        Intent intent = new Intent(ChooseActivity.this, LoginActivity.class);
        String option = "staff";
        intent.putExtra("option", option);
        startActivity(intent);
    }

    public void student(View view) {
        Intent intent = new Intent(ChooseActivity.this, LoginActivity.class);
        String option = "student";
        intent.putExtra("option", option);
        startActivity(intent);
    }
}