package com.example.yvproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.yvproject.staff.MainActivity;

public class TaskActvity extends AppCompatActivity {
    int count = 0;
    LottieAnimationView box1, box2, box3, box4, box5, box6, box7, box8, box9, box10, box11, box12, box13, box14;
    TextView next, task1, task2, task3, task4, task5, task6, task7, task8, task9, task10, task11, task12, task13, task14, textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_actvity);

        next = findViewById(R.id.nextCount);
        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);
        box6 = findViewById(R.id.box6);
        box7 = findViewById(R.id.box7);
        box8 = findViewById(R.id.box8);
        box9 = findViewById(R.id.box9);
        box10 = findViewById(R.id.box10);
        box11 = findViewById(R.id.box11);
        box12 = findViewById(R.id.box12);
        box13 = findViewById(R.id.box13);
        box14 = findViewById(R.id.box14);

        task1 = findViewById(R.id.task1);
        task2 = findViewById(R.id.task2);
        task3 = findViewById(R.id.task3);
        task4 = findViewById(R.id.task4);
        task5 = findViewById(R.id.task5);
        task6 = findViewById(R.id.task6);
        task7 = findViewById(R.id.task7);
        task8 = findViewById(R.id.task8);
        task9 = findViewById(R.id.task9);
        task10 = findViewById(R.id.task10);
        task11 = findViewById(R.id.task11);
        task12 = findViewById(R.id.task12);
        task13 = findViewById(R.id.task13);
        task14 = findViewById(R.id.task14);

        loadincrement();
    }

    private void loadincrement() {
        Thread t = new Thread() {


            @Override
            public void run() {


                while (!isInterrupted()) {

                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                count++;
                                next.setText(String.valueOf(count));
                                displaytick();
                            }

                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        t.start();

    }


    private void displaytick() {

        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);
        box6 = findViewById(R.id.box6);
        box7 = findViewById(R.id.box7);
        box8 = findViewById(R.id.box8);
        box9 = findViewById(R.id.box9);
        box10 = findViewById(R.id.box10);
        box11 = findViewById(R.id.box11);
        box12 = findViewById(R.id.box12);
        box13 = findViewById(R.id.box13);
        box14 = findViewById(R.id.box14);
        int id = Integer.parseInt(next.getText().toString());
        if (id == 1) {
            box1.setVisibility(View.VISIBLE);

            task1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task1.setSelected(true);
        } else if (id == 2) {

            box2.setVisibility(View.VISIBLE);

            task2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task2.setSelected(true);
        } else if (id == 3) {

            box3.setVisibility(View.VISIBLE);

            task3.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task3.setSelected(true);
        } else if (id == 4) {

            box4.setVisibility(View.VISIBLE);

            task4.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task4.setSelected(true);
        } else if (id == 5) {

            box5.setVisibility(View.VISIBLE);

            task5.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task5.setSelected(true);
        } else if (id == 6) {

            box6.setVisibility(View.VISIBLE);

            task6.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task6.setSelected(true);
        } else if (id == 7) {

            box7.setVisibility(View.VISIBLE);

            task7.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task7.setSelected(true);
        } else if (id == 8) {

            box8.setVisibility(View.VISIBLE);

            task8.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task8.setSelected(true);
        } else if (id == 9) {

            box9.setVisibility(View.VISIBLE);

            task9.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task9.setSelected(true);
        } else if (id == 10) {

            box10.setVisibility(View.VISIBLE);

            task10.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task10.setSelected(true);
        } else if (id == 11) {

            box11.setVisibility(View.VISIBLE);

            task11.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task11.setSelected(true);
        } else if (id == 12) {

            box12.setVisibility(View.VISIBLE);

            task12.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task12.setSelected(true);
        } else if (id == 13) {

            box13.setVisibility(View.VISIBLE);

            task13.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task13.setSelected(true);
        } else {

            box14.setVisibility(View.VISIBLE);

            task14.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            task14.setSelected(true);
        }
    }

    public void GoToHome(View view) {

        Intent HomeActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(HomeActivity);
        finish();
    }
}

