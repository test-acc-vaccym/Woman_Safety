package com.alexia.callbutton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 14/12/2017.
 */
class QuestionChoose {
    String chooseTestDescription;

    QuestionChoose(JSONObject json) throws JSONException {
        chooseTestDescription = json.getString("survey");
    }

    @Override
    public String toString() {
        return "survey: " + chooseTestDescription;
    }
}

public class QuestionnaireChooseActivity extends AppCompatActivity {
    TextView chooseTestDescriptionTextView;

    public static String url = "http://192.168.43.186:9090/api/tests/info";

    private String TAG = QuestionnaireChooseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_choose_layout);
        new GetChooseTestDescription().execute();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_help);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_help: {
                                Intent intent1 = new Intent(QuestionnaireChooseActivity.this, QuestionnaireActivity.class);
                                QuestionnaireChooseActivity.this.startActivity(intent1);
                            }
                            break;

                            case R.id.action_map: {
                                Intent intent3 = new Intent(QuestionnaireChooseActivity.this, MapsActivity.class);
                                QuestionnaireChooseActivity.this.startActivity(intent3);
                            }
                            break;

                            case R.id.action_sos: {
                                Intent intent2 = new Intent(QuestionnaireChooseActivity.this, MainActivity.class);
                                QuestionnaireChooseActivity.this.startActivity(intent2);
                            }
                            break;
                            case R.id.action_settings: {
                                Intent intent2 = new Intent(QuestionnaireChooseActivity.this, SettingsActivity.class);
                                QuestionnaireChooseActivity.this.startActivity(intent2);
                            }
                            break;
                        }
                        return true;
                    }
                });
    }

    public void onClickFirstSurvey(View view) {
        startActivity(new Intent(QuestionnaireChooseActivity.this, QuestionnaireStartActivity.class));
    }

    @SuppressLint("StaticFieldLeak")
    private class GetChooseTestDescription extends AsyncTask<Void, Void, Void> {
        QuestionChoose chooseTestDescription;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chooseTestDescriptionTextView = (TextView) findViewById(R.id.survey_text_1);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String chooseTestDescriptionUrl = url;
            String jsonStr = sh.makeServiceCall(chooseTestDescriptionUrl);
            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    chooseTestDescription = new QuestionChoose(jsonObject);
                    String.valueOf(jsonObject.getString("survey"));
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();}
                        });
                 }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();}
                    });
                }
                return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            chooseTestDescriptionTextView.setText((String.valueOf(chooseTestDescription.chooseTestDescription)));
        }
    }
}