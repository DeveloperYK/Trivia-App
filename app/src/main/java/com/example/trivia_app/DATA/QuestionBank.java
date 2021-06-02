package com.example.trivia_app.DATA;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trivia_app.CONTROLLER.AppController;
import com.example.trivia_app.M0DEL.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    ArrayList<Question> questionArrayList = new ArrayList<>();


    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i< response.length(); i++) {
                            try {
                                Question Question_instance = new Question();
                                Question_instance.setAnswer(response.getJSONArray(i).get(0).toString());
                                Question_instance.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                            //   Log.d("JSON","On Response: "+  response.getJSONArray(i).get(0).toString());

                                // Add question objects to list

                                questionArrayList.add(Question_instance);
//                                Log.d("Hello","On Response: "+ Question_instance);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(null != callBack) {
                            Log.d("Interface", "onResponse: "+ callBack);
                            callBack.processFinished(questionArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","OnResponse: " + error.getMessage());

            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
