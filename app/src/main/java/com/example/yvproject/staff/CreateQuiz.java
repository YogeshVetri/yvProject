package com.example.yvproject.staff;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yvproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateQuiz extends AppCompatActivity implements View.OnClickListener {
    EditText aText, bText, cText, dText, eText, correctAnswer;
    EditText question,explanation;
    TextInputLayout lquestion,lexplanation;
    CheckBox aCheck, bCheck, cCheck, dCheck;
    TextView questionNumber;
    Button Next, clear, Home;
    LinearLayout quizlayout;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    public static final String TAG = "TAG";
    int count = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        question = findViewById(R.id.questionView);
        explanation = findViewById(R.id.explanationView);
        lquestion = findViewById(R.id.lquestionView);
        lexplanation = findViewById(R.id.lexplanationView);
        quizlayout = findViewById(R.id.quizlinear);
        aText = findViewById(R.id.aText);
        bText = findViewById(R.id.bText);
        cText = findViewById(R.id.cText);
        dText = findViewById(R.id.dText);
        eText = findViewById(R.id.explanationView);
        questionNumber = findViewById(R.id.questionNumber);
        clear = findViewById(R.id.clearbtn);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        correctAnswer = findViewById(R.id.correctAnswer);
        Next = findViewById(R.id.nextBtn);
        Home = findViewById(R.id.homeBtn);
        questionNumber.setText(String.valueOf(count));

        aCheck = (CheckBox) findViewById(R.id.aCheck);
        aCheck.setOnClickListener(this);
        bCheck = (CheckBox) findViewById(R.id.bCheck);
        bCheck.setOnClickListener(this);
        cCheck = (CheckBox) findViewById(R.id.cCheck);
        cCheck.setOnClickListener(this);
        dCheck = (CheckBox) findViewById(R.id.dCheck);
        dCheck.setOnClickListener(this);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String questionn = question.getText().toString();
                String Aoption = aText.getText().toString();
                String Boption = bText.getText().toString();
                String Coption = cText.getText().toString();
                String Doption = dText.getText().toString();
                String Explanation = eText.getText().toString();

                if (questionn.isEmpty()) {
                    question.setError("Enter Valid Question");
                } else if (Aoption.isEmpty()) {
                    aText.setError("Enter A option");
                } else if (Boption.isEmpty()) {
                    bText.setError("Enter B option");
                } else if (Coption.isEmpty()) {
                    cText.setError("Enter C option");
                } else if (Doption.isEmpty()) {
                    dText.setError("Enter D option");

                } else if (Explanation.isEmpty()) {
                    eText.setError("Enter Explanation for Correct Option");
                } else if (aCheck.isChecked() || bCheck.isChecked() || cCheck.isChecked() || dCheck.isChecked()) {
                    updateQuestion();

                } else {
                    Toast.makeText(CreateQuiz.this, "Please Choose correct answer", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    @Override

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.aCheck:

                if (aCheck.isChecked())
                    correctAnswer.setText(correctAnswer.getText().toString() + (aCheck.getText().toString()));

                else {
                    String removedans = correctAnswer.getText().toString().replaceAll("[A]", "");
                    correctAnswer.setText(removedans);
                }
            case R.id.bCheck:
                if (bCheck.isChecked())
                    correctAnswer.setText(correctAnswer.getText().toString() + (bCheck.getText().toString()));
                else {
                    String removedans = correctAnswer.getText().toString().replaceAll("[B]", "");
                    correctAnswer.setText(removedans);
                }
            case R.id.cCheck:
                if (cCheck.isChecked())
                    correctAnswer.setText(correctAnswer.getText().toString() + (cCheck.getText().toString()));
                else {
                    String removedans = correctAnswer.getText().toString().replaceAll("[C]", "");
                    correctAnswer.setText(removedans);
                }
            case R.id.dCheck:
                if (dCheck.isChecked())
                    correctAnswer.setText(correctAnswer.getText().toString() + (dCheck.getText().toString()));
                else {
                    String removedans = correctAnswer.getText().toString().replaceAll("[D]", "");
                    correctAnswer.setText(removedans);
                }


        }
    }

    public void updateQuestion() {
        String questionn = question.getText().toString();
        String Aoption = aText.getText().toString();
        String Boption = bText.getText().toString();
        String Coption = cText.getText().toString();
        String Doption = dText.getText().toString();

        String Answer = correctAnswer.getText().toString();
        String Explanation = eText.getText().toString();

        final String test = questionNumber.getText().toString();
        String collectionpath = "Testing";
        DocumentReference documentReference = fStore.collection(collectionpath).document(test);
        Map<String, Object> user = new HashMap<>();
        user.put("Question", questionn);
        user.put("A", Aoption);
        user.put("B", Boption);
        user.put("C", Coption);
        user.put("D", Doption);
        user.put("Answer", Answer);
        user.put("Explanation", Explanation);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "OnSuccess: Quiz added for " + test);
                increasequestioncount();
                clearAll();
            }
        });


    }

    public void clearAll() {

        question.setText("");
        aText.setText("");
        bText.setText("");
        cText.setText("");
        dText.setText("");
        aCheck.setChecked(false);
        bCheck.setChecked(false);
        cCheck.setChecked(false);
        dCheck.setChecked(false);
        eText.setText("");
        correctAnswer.setText("");

    }


    private void increasequestioncount() {
        if (count <= 9) {


            count++;
            questionNumber.setText(String.valueOf(count));

        } else {
            Toast.makeText(this, "Maximum 10 Questions", Toast.LENGTH_SHORT).show();
            quizlayout.setVisibility(View.INVISIBLE);
            clear.setVisibility(View.INVISIBLE);
            Next.setVisibility(View.INVISIBLE);
            Home.setVisibility(View.VISIBLE);
        }

    }

    public void clearfields(View view) {
        clearAll();
    }

    public void GotoHome(View view) {

        Intent intent = new Intent(CreateQuiz.this, CreateQuiz.class);

        startActivity(intent);
    }
}