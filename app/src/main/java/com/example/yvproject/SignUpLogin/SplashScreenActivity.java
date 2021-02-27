package com.example.yvproject.SignUpLogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yvproject.R;
import com.example.yvproject.staff.MainActivity;
import com.example.yvproject.student.StudentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView image;

    TextView tvoption;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        tvoption = findViewById(R.id.textoption);


        image = findViewById(R.id.logo_image);
        logo = findViewById(R.id.logo_name);

        if (mAuth.getCurrentUser() != null) {
            checkCategoryandupdateUI();
        } else {
            int SPLASH_SCREEN = 3550;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, ChooseActivity.class);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreenActivity.this);
                        startActivity(intent, options.toBundle());
                        finish();

                    }

                }
            }, SPLASH_SCREEN);

        }
    }

    private void checkCategoryandupdateUI() {
        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(SplashScreenActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                tvoption.setText(documentSnapshot.getString("priviledge"));
                updateWelcome();

            }
        });


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
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }

    }


}