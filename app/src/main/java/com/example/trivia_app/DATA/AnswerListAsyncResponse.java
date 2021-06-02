package com.example.trivia_app.DATA;

import com.example.trivia_app.M0DEL.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);


}
