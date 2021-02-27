package com.example.yvproject.student;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yvproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AttendQuiz extends AppCompatActivity {
    TextView question, aText, bText, cText, dText, correctAnswer, counttimerText, explanation, questionNumber, point, timer, commarks, comtime, stopwatch;

    Button Next, Home;
    LinearLayout quizlayout, completedlayout;
    FirebaseAuth mAuth;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser currentUser;
    String userID;
    EditText Time;


    Timer counttimer;
    TimerTask counttimerTask;
    Double time = 0.0;
    Dialog dialog;

    public static final String TAG = "TAG";
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_quiz);
        question = findViewById(R.id.questionView);
        explanation = findViewById(R.id.explanationView);
        quizlayout = findViewById(R.id.quizlinear);
        completedlayout = findViewById(R.id.quizlinear2);
        point = findViewById(R.id.points);
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        Time = findViewById(R.id.fixedtime);
        questionNumber = findViewById(R.id.questionNumber);
        timer = findViewById(R.id.timerView);
        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        fStore = FirebaseFirestore.getInstance();
        correctAnswer = findViewById(R.id.correctAnswer);
        Next = findViewById(R.id.nextBtn);
        Home = findViewById(R.id.homeBtn);
        questionNumber.setText(String.valueOf(count));
        aText = findViewById(R.id.aText);
        bText = findViewById(R.id.bText);
        cText = findViewById(R.id.cText);
        dText = findViewById(R.id.dText);


        counttimerText = (TextView) findViewById(R.id.counttimerText);
        counttimer = new Timer();


        loadQuizes();
        loadpoint();

        startTimer();


        aText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String answer = correctAnswer.getText().toString();
                if (answer.contains("A")) {

                    displayandaddpoint();
                } else {
                    displayallanswers();
                }
            }
        });


        bText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String answer = correctAnswer.getText().toString();

                if (answer.contains("B")) {
                    Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
                    bText.setBackgroundResource(R.drawable.correctoption);
                    displayandaddpoint();
                } else {
                    displayallanswers();
                }
            }
        });


        cText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String answer = correctAnswer.getText().toString();

                if (answer.contains("C")) {
                    Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
                    cText.setBackgroundResource(R.drawable.correctoption);
                    displayandaddpoint();
                } else {
                    displayallanswers();
                }
            }
        });


        dText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String answer = correctAnswer.getText().toString();

                if (answer.contains("D")) {
                    Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
                    dText.setBackgroundResource(R.drawable.correctoption);
                    displayandaddpoint();
                } else {
                    displayallanswers();
                }
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
                AttendQuiz.this.finish();

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

    private void loadpoint() {


        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(AttendQuiz.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                point.setText(documentSnapshot.getString("points"));
            }
        });


    }

    private void displayandaddpoint() {
        int marks = Integer.parseInt(point.getText().toString());
        if (marks == 0) {

            int updatedpoints = 1;
            point.setText(Integer.toString(updatedpoints));

            displayallanswers();
        } else {
            int updatedpoints = marks + 1;
            point.setText(Integer.toString(updatedpoints));
            String pnt = point.getText().toString();
            String tym = counttimerText.getText().toString();
            userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            HashMap hashMap = new HashMap();
            hashMap.put("points", pnt);

            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                 //   Toast.makeText(AttendQuiz.this, "Correct Answer", Toast.LENGTH_SHORT).show();
                }


            });


            displayallanswers();
        }
    }


    private void displayallanswers() {
        final String answer = correctAnswer.getText().toString();

        String collectionpath = "Category";

        final String test = questionNumber.getText().toString();

        DocumentReference documentReference = fStore.collection(collectionpath).document(test);
        documentReference.addSnapshotListener(AttendQuiz.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                explanation.setText(documentSnapshot.getString("explanation"));


            }
        });


        if (answer.contains("A")) {
            Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
            aText.setBackgroundResource(R.drawable.correctoption);
            aText.setEnabled(false);
        } else {
            aText.setBackgroundResource(R.drawable.wrongoption);
            aText.setEnabled(false);
        }
        if (answer.contains("B")) {
            Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
            bText.setBackgroundResource(R.drawable.correctoption);
            bText.setEnabled(false);
        } else {
            bText.setBackgroundResource(R.drawable.wrongoption);
            bText.setEnabled(false);
        }
        if (answer.contains("C")) {
            Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
            cText.setBackgroundResource(R.drawable.correctoption);
            cText.setEnabled(false);
        } else {
            cText.setBackgroundResource(R.drawable.wrongoption);
            cText.setEnabled(false);
        }


        if (answer.contains("D")) {
            Toast.makeText(AttendQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
            dText.setBackgroundResource(R.drawable.correctoption);
            dText.setEnabled(false);
        } else {
            dText.setBackgroundResource(R.drawable.wrongoption);
            dText.setEnabled(false);
        }

    }

    private void loadQuizes() {
        aText.setBackgroundResource(R.drawable.nooption);
        bText.setBackgroundResource(R.drawable.nooption);
        cText.setBackgroundResource(R.drawable.nooption);
        dText.setBackgroundResource(R.drawable.nooption);
        final String test = questionNumber.getText().toString();
        String collectionpath = "Category";

        DocumentReference documentReference = fStore.collection(collectionpath).document(test);
        documentReference.addSnapshotListener(AttendQuiz.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                question.setText(documentSnapshot.getString("Question"));
                aText.setText(documentSnapshot.getString("A"));
                bText.setText(documentSnapshot.getString("B"));
                cText.setText(documentSnapshot.getString("C"));
                dText.setText(documentSnapshot.getString("D"));
                correctAnswer.setText(documentSnapshot.getString("Answer"));
                Time.setText(documentSnapshot.getString("timetocomplete"));
                RunTimer();

            }
        });


    }

    private void RunTimer() {
        final String test = questionNumber.getText().toString();

        if (test.equals("1")) {

            int minutes = Integer.parseInt(Time.getText().toString());
            new CountDownTimer(minutes, 1000) {
                public void onTick(long millisUntilFinished) {
                    NumberFormat f = new DecimalFormat("00");
                    long min = (millisUntilFinished / 60000) % 60;
                    long sec = (millisUntilFinished / 1000) % 60;
                    timer.setText(f.format(min) + ":" + f.format(sec));
                }

                public void onFinish() {
                    timer.setText("00:00");
                    Toast.makeText(AttendQuiz.this, "Quiz Time Over", Toast.LENGTH_SHORT).show();
                    counttimerTask.cancel();

                }
            }.start();


        }

    }

    private void startTimer() {
        counttimerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        counttimerText.setText(getTimerText());
                    }
                });
            }

        };
        counttimer.scheduleAtFixedRate(counttimerTask, 0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }


    private void increasequestioncount() {
        if (count <= 9) {


            count++;
            questionNumber.setText(String.valueOf(count));

        } else {
            quizlayout.setVisibility(View.INVISIBLE);
            completedlayout.setVisibility(View.VISIBLE);
            Next.setVisibility(View.INVISIBLE);
            Home.setVisibility(View.VISIBLE);
            updatemarktime();

            String tym = counttimerText.getText().toString();
            userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            HashMap hashMap = new HashMap();
            hashMap.put("time", tym);

            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.update(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                }


            });


        }

    }

    private void updatemarktime() {
        commarks = findViewById(R.id.securedmarks);
        comtime = findViewById(R.id.totaltimetaken);

        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(AttendQuiz.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;
                commarks.setText(documentSnapshot.getString("points"));
                comtime.setText(documentSnapshot.getString("time"));
            }
        });

    }

    public void NextQuestion(View view) {

        increasequestioncount();
        clearAll();
        loadQuizes();

        aText.setEnabled(true);
        bText.setEnabled(true);
        cText.setEnabled(true);
        dText.setEnabled(true);
    }


    private void clearAll() {

        question.setText("");
        aText.setText("");
        bText.setText("");
        cText.setText("");
        dText.setText("");
        correctAnswer.setText("");


    }


    public void GoToHome(View view) {
        Intent HomeActivity = new Intent(getApplicationContext(), StudentActivity.class);
        startActivity(HomeActivity);
        finish();

    }
}