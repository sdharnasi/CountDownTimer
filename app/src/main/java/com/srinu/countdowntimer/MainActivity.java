package com.srinu.countdowntimer;

import java.util.concurrent.TimeUnit;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    private long timeCountInMilliSeconds = 1 * 60000;
    private enum TimerStatus{
        STARTED,
        STOPPED
    }
    private TimerStatus timerStatus = TimerStatus.STOPPED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init views
        initViews();

        //init listeners
        initListeners();
    }

    private void initViews(){
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        editTextMinute = (EditText) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewReset = (ImageView) findViewById(R.id.imageViewReset);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);

    }
    private void initListeners(){
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                startStop();
                break;

        }
    }
    /**
     * method to reset count down timer
     */
    private void reset(){
        stopCountDownTimer();
        startCountDownTimer();
    }
    /**
     * method to start and stop count down timer
     */
    public void startStop(){
        if(timerStatus == TimerStatus.STOPPED){
            // start timer
            setTimerValues();
            setProgressBarValues();
            imageViewReset.setVisibility(View.VISIBLE);
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
            editTextMinute.setEnabled(false);
            timerStatus = TimerStatus.STARTED;
            startCountDownTimer();
        }else{
            // stop timer
            imageViewReset.setVisibility(View.GONE);
            imageViewStartStop.setImageResource(R.drawable.icon_start);
            editTextMinute.setEnabled(true);
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }

    }
    // Set values from minute text box.
    private void setTimerValues(){
        int time = 0;
        if(!editTextMinute.getText().toString().isEmpty()){
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.message_minute),Toast.LENGTH_LONG).show();
        }
        timeCountInMilliSeconds = time * 60 * 1000;
    }
    // Start timer
    private void startCountDownTimer(){
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                setProgressBarValues();
                imageViewReset.setVisibility(View.GONE);
                imageViewStartStop.setImageResource(R.drawable.icon_start);
                editTextMinute.setEnabled(true);
                timerStatus = TimerStatus.STOPPED;
                stopCountDownTimer();
            }
        }.start();
        countDownTimer.start();
    }
    // Stop timer
    private void stopCountDownTimer(){
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues(){
        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);

    }
    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds){
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }
}
