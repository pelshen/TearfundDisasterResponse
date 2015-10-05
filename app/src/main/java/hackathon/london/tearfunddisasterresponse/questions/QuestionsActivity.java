package hackathon.london.tearfunddisasterresponse.questions;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hackathon.london.tearfunddisasterresponse.FinalPageActivity;
import hackathon.london.tearfunddisasterresponse.Question;
import hackathon.london.tearfunddisasterresponse.Questions;
import hackathon.london.tearfunddisasterresponse.R;

public class QuestionsActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private ListView answersView;

    static final String[] questions = {"question 1", "question 2", "question 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Questions questionsClass = new Questions();
        Question question = questionsClass.getNextQuestion(getIntent().getStringExtra("Category"));
        if(question != null) {
            ArrayList<String> answers = question.getAnswers();
            String theQuestion = question.getQuestion();

            final TextView textViewToChange = (TextView) findViewById(R.id.questionTitle);
            textViewToChange.setText(
                    theQuestion);

            String[] answersArray = new String[answers.size()];
            for (int i = 0; i < answers.size(); i++) {
                answersArray[i] = answers.get(i);
            }

            answersView = getListView();

            answersView.setAdapter(new ArrayAdapter<String>(this, R.layout.answer_list_item, answersArray));
            answersView.setOnItemClickListener(this);
        } else {
            Intent nextScreen = new Intent(getApplicationContext(), FinalPageActivity.class);
//            nextScreen.putExtra("Category", ((TextView) view).getText());
            startActivity(nextScreen);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//                Toast.LENGTH_SHORT).show();
//        Intent nextQuestionIntent = new Intent()

            Intent nextScreen = new Intent(getApplicationContext(), QuestionsActivity.class);
            nextScreen.putExtra("Category", ((TextView) view).getText());
            startActivity(nextScreen);
    }
}
