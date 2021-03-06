package com.alexia.callbutton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class QuestionnaireSurveyActivity extends AppCompatActivity {

    TextView questionTextView;
    TextView questionIDTextView;

    FloatingActionButton yesButton;
    FloatingActionButton noButton;

    public static String url = "http://192.168.43.186:9090/api/tests/questions/?id=";

    static int currentId = 1;
    int pointSum = 0;

    private String TAG = QuestionnaireSurveyActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_survey_layout);
        yesButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonYes);
        noButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonNo);

        new GetQuestion().execute();
        questionTextView = (TextView) findViewById(R.id.question);
        questionIDTextView = (TextView) findViewById(R.id.question_id);
        if (getIntent().hasExtra("bundle") && savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras().getBundle("bundle");
        }

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetQuestion().execute();
                pointSum++;
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetQuestion().execute();

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_help);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {

                            case R.id.action_map: {
                                Intent intent3 = new Intent(QuestionnaireSurveyActivity.this, MapsActivity.class);
                                QuestionnaireSurveyActivity.this.startActivity(intent3);
                            }
                            break;

                            case R.id.action_sos: {
                                Intent intent2 = new Intent(QuestionnaireSurveyActivity.this, MainActivity.class);
                                QuestionnaireSurveyActivity.this.startActivity(intent2);
                            }
                            break;
                            case R.id.action_settings: {
                                Intent intent2 = new Intent(QuestionnaireSurveyActivity.this, SettingsActivity.class);
                                QuestionnaireSurveyActivity.this.startActivity(intent2);
                            }
                            break;
                        }
                        return true;
                    }
                });
    }


    @SuppressLint("StaticFieldLeak")
    private class GetQuestion extends AsyncTask<Void, Void, Void> {
        Question question;
        String error = "0";

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String questionUrl = url + String.valueOf(currentId);
            String jsonStr = sh.makeServiceCall(questionUrl);
            if (jsonStr != null) {
                try {
                    currentId += 1;
                    JSONObject c = new JSONObject(jsonStr);
                    question = new Question(c);

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
                this.error = "500";

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (this.error != "500") {
                questionIDTextView.setText(String.valueOf(question.idQuestion));
                questionTextView.setText(String.valueOf(question.question));
            } else {
//                --- For passing data of "pointSum"
                Intent intent = new Intent(getApplicationContext(), QuestionnaireInstructionActivity.class);
                intent.putExtra("ARG_POINT_SUM", pointSum);
                startActivity(intent);
            }
        }

    }
}


