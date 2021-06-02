package com.example.trivia_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia_app.DATA.AnswerListAsyncResponse;
import com.example.trivia_app.DATA.QuestionBank;
import com.example.trivia_app.M0DEL.Question;
import com.example.trivia_app.util.Prefs;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private TextView ScoreText;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private List<Question> questionList;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.Question_textView);
        questionCounterTextView = findViewById(R.id.Counter_TextView);
        ScoreText = findViewById(R.id.ScoreID);

        trueButton = findViewById(R.id.True_Button);
        falseButton = findViewById(R.id.False_Button);

        nextButton = findViewById(R.id.Next_Button);
        prevButton = findViewById(R.id.Previous_Button);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);


        prefs = new Prefs(MainActivity.this);
        Log.d("Second", "onCreate: " + prefs.getHighestScore());
        ScoreText.setText("Score: "+ prefs.getHighestScore());
        score = prefs.getHighestScore();



        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex + " / " + questionArrayList.size());
                Log.d("Inside", "processFinished: " + questionArrayList);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.True_Button:
                checkAnswer(true);
                updateQuestion();
                break;

            case R.id.False_Button:
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.Next_Button:
                nextQuestion();
                break;

            case R.id.Previous_Button:

                previousQuestion();
                break;


        }
    }
    private void updateQuestion() {
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());

    }

    private void nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
        updateCounter();
    }

    private void previousQuestion() {
        if(currentQuestionIndex > 0) {
            currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
            questionTextView.setText(questionList.get(currentQuestionIndex).getAnswer());
            updateCounter();
        }

    }

    private void updateCounter() {
        questionCounterTextView.setText(currentQuestionIndex + " / " + questionList.size());
    }

    private void checkAnswer(boolean userChoice){
        boolean correctAnswer = questionList.get(currentQuestionIndex).getAnswerTrue();
        int ToastMessageID = 0;
        if(correctAnswer == userChoice) {
            fadeView();
            ToastMessageID = R.string.correct_answer;
            score = score + 1;
            updateScore();

        }
        else {
            shakeAnimation();
            ToastMessageID = R.string.wrong_answer;
            if(score>0) {
                score = score - 1;
                updateScore();
            }
        }
        Toast.makeText(MainActivity.this, ToastMessageID, Toast.LENGTH_SHORT).show();
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void updateScore() {


        ScoreText.setText("Score: " + score);


    }

    @Override
    protected void onPause() {
        prefs.saveHighestScore(score);
        super.onPause();
    }
}