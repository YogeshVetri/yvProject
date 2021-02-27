package com.example.yvproject.SignUpLogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.yvproject.R;
import com.example.yvproject.staff.MainActivity;
import com.example.yvproject.student.StudentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Button btnSignup, btnForget;
    float v = 0;

    TextView logotext, btnLogin, tvoption;
    ImageView logoimage;
    TextInputLayout email, password;
    CardView cardView;
    ProgressBar loginProgress;
    EditText eemail, epassword;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginProgress = findViewById(R.id.progress);
        btnLogin = findViewById(R.id.userlogin);
        btnSignup = findViewById(R.id.usersignup);
        btnForget = findViewById(R.id.userforget);
        logotext = findViewById(R.id.logo_name);
        logoimage = findViewById(R.id.logo_image);
        email = findViewById(R.id.useremail);
        password = findViewById(R.id.userpassword);
        cardView = findViewById(R.id.cardView);


        email = findViewById(R.id.useremail);
        password = findViewById(R.id.userpassword);

        eemail = findViewById(R.id.regMail);
        epassword = findViewById(R.id.regPassword);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        tvoption = findViewById(R.id.textoption);
        String data = Objects.requireNonNull(getIntent().getExtras()).getString("option");
        tvoption.setText(data);
        cardView.setTranslationY(300);
        cardView.setAlpha(v);
        cardView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        if (mAuth.getCurrentUser() != null) {
            checkCategoryandupdateUI();
        }

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(btnLogin, "btn_login_trans");
                pairs[1] = new Pair<View, String>(btnSignup, "btn_signup_trans");
                pairs[2] = new Pair<View, String>(logotext, "logo_name_trans");
                pairs[3] = new Pair<View, String>(logoimage, "logo");
                pairs[4] = new Pair<View, String>(email, "email_box_trans");
                pairs[5] = new Pair<View, String>(password, "password_box_trans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                String option = tvoption.getText().toString();
                intent.putExtra("option", option);
                startActivity(intent, options.toBundle());
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = eemail.getText().toString();
                final String password = epassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify All Field");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                } else {

                    loginProgress.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.INVISIBLE);
                    signIn(mail, password);
                }


            }
        });

    }


    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    checkCategoryandupdateUI();

                } else {
                    showMessage(Objects.requireNonNull(task.getException()).getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }


            }
        });


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
        } else {

            Intent studentActivity = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(studentActivity);
            finish();
        }

    }


    private Boolean validatePassword() {
        String password = epassword.getText().toString();
        String noWhiteSpace = "(?=\\s+$)";
        if (password.isEmpty()) {
            epassword.setError("password Cannot be empty");
            return false;
        } else if (password.length() <= 7) {
            epassword.setError("Password too short");
            return false;
        } else if (password.matches(noWhiteSpace)) {
            epassword.setError("No Space allowed in phone");
            return false;
        } else {
            epassword.setError(null);
            //ename.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String email = eemail.getText().toString();
        String noWhiteSpace = "(?=\\s+$)";
        String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()) {
            eemail.setError("Email Cannot be empty");
            return false;
        } else if (email.length() <= 5) {
            eemail.setError("Email too short");
            return false;
        } else if (email.matches(noWhiteSpace)) {
            eemail.setError("No Space allowed in email");
            return false;
        } else if (!email.matches(emailPattern)) {
            eemail.setError("Invalid email ID");
            return false;
        } else {
            eemail.setError(null);
            //ename.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        if (!validateEmail() | !validatePassword()) {
            return;

        }
    }


    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }


}