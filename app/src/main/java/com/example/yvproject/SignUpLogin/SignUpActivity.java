package com.example.yvproject.SignUpLogin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.example.yvproject.R;
import com.example.yvproject.staff.MainActivity;
import com.example.yvproject.student.StudentActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    float v = 0;
    public static final String TAG = "TAG";
    ProgressBar loginProgress;

    Button btnSignup;
    TextView logotext, tvoption, point;
    ImageView logoimage;
    TextInputLayout lemail, lname, lphone, lpassword;
    EditText eemail, ename, ephone, epassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;
    ProgressBar ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        loginProgress = findViewById(R.id.login_progress);

        Sprite wave = new Wave();
        loginProgress.setIndeterminateDrawable(wave);

        btnSignup = findViewById(R.id.usersignup);
        logotext = findViewById(R.id.logo_name);
        logoimage = findViewById(R.id.logo_image);

        tvoption = findViewById(R.id.textoption);

        lemail = findViewById(R.id.useremail);
        lpassword = findViewById(R.id.userpassword);
        lname = findViewById(R.id.username);
        lphone = findViewById(R.id.userphone);

        eemail = findViewById(R.id.regMail);
        ename = findViewById(R.id.regName);
        ephone = findViewById(R.id.regPhone);
        epassword = findViewById(R.id.regPassword);
        point = findViewById(R.id.points);
        mAuth = FirebaseAuth.getInstance();
        point = findViewById(R.id.points);
        fStore = FirebaseFirestore.getInstance();

        String data = Objects.requireNonNull(getIntent().getExtras()).getString("option");
        tvoption.setText(data);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (validateEmail() && validatePhone() && validateName() && validatePassword()) {

                    final String email = eemail.getText().toString();
                    final String name = ename.getText().toString();
                    final String category = tvoption.getText().toString();
                    final String phone = ephone.getText().toString();
                    final String password = epassword.getText().toString();
                    final String points = point.getText().toString();
                    btnSignup.setVisibility(View.INVISIBLE);
                    loginProgress.setVisibility(View.VISIBLE);

                    CreateUserAccount(email, password, name, phone, category, points);


                } else {
                    Toast.makeText(SignUpActivity.this, "Failure", Toast.LENGTH_SHORT).show();

                }
            }
        });

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
        } else {

            Intent studentActivity = new Intent(getApplicationContext(), StudentActivity.class);
            startActivity(studentActivity);
            finish();
        }

    }

    private void CreateUserAccount(final String email, final String name, final String points, final String phone, final String password, final String category) {


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final String email = eemail.getText().toString();
                    final String name = ename.getText().toString();
                    final String category = tvoption.getText().toString();
                    final String phone = ephone.getText().toString();
                    final String password = epassword.getText().toString();
                    final String points = point.getText().toString();
                    userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", name);
                    user.put("email", email);
                    user.put("password", password);
                    user.put("phone", phone);
                    user.put("priviledge", category);
                    user.put("points", points);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "OnSuccess: user Profile is created for " + userID);
                        }
                    });
                    showMessage("Register Complete");
                    updateUI();


                } else {


                    Toast.makeText(SignUpActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateName() {
        String name = ename.getText().toString();
        String noWhiteSpace = "(?=\\s+$)";
        if (name.isEmpty()) {
            ename.setError("Name Cannot be empty");
            return false;
        } else if (name.length() <= 5) {
            ename.setError("Name too short");
            return false;
        } else if (name.matches(noWhiteSpace)) {
            ename.setError("No Space allowed");
            return false;
        } else {
            ename.setError(null);
            //ename.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String phone = ephone.getText().toString();
        String noWhiteSpace = "(?=\\s+$)";
        if (phone.isEmpty()) {
            ephone.setError("Phone Cannot be empty");
            return false;
        } else if (phone.length() <= 8) {
            ephone.setError("Enter Valid Mobile Number");
            return false;
        } else if (phone.matches(noWhiteSpace)) {
            ephone.setError("No Space allowed in phone");
            return false;
        } else {
            ephone.setError(null);
            //ename.setErrorEnabled(false);
            return true;
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


    private void updateUI() {

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

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }
}
