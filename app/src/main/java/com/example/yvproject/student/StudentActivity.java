package com.example.yvproject.student;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.yvproject.R;
import com.example.yvproject.TaskActvity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Objects;

public class StudentActivity extends AppCompatActivity {

    Dialog dialog;

    public EditText userMail, userPassword, userName, userPhone, userUid;
    TextInputLayout name, emailid;
    FirebaseAuth fAuth;
    Button upd;
    FirebaseFirestore fStore;
    DatabaseReference reference;
    FirebaseUser currentUser;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        dialog = new Dialog(this);
        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        userName = findViewById(R.id.regName);
        name = findViewById(R.id.name);
        reference = FirebaseDatabase.getInstance().getReference().child("users");

        userName = findViewById(R.id.regName);
        userMail = findViewById(R.id.regEmail);
        userPassword = findViewById(R.id.regPassword);
        userPhone = findViewById(R.id.regPhone);
        upd = findViewById(R.id.btnUpdate);
        userUid = findViewById(R.id.reguseruid);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        name = findViewById(R.id.name);
        emailid = findViewById(R.id.email);


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(StudentActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                userPhone.setText(documentSnapshot.getString("phone"));
                userName.setText(documentSnapshot.getString("Name"));
                userMail.setText(documentSnapshot.getString("email"));

                userUid.setText(documentSnapshot.getString("UID"));
            }
        });


    }


    @Override
    public void onBackPressed() {

        dialog.setContentView(R.layout.activity_open_confirm_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnYes = dialog.findViewById(R.id.btnYes);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                StudentActivity.this.finish();

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }


    public void startQuiz(View view) {
        Intent intent = new Intent(StudentActivity.this, AttendQuiz.class);

        startActivity(intent);
    }


}



