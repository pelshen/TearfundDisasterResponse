package hackathon.london.tearfunddisasterresponse.questions;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import hackathon.london.tearfunddisasterresponse.FinalPageActivity;
import hackathon.london.tearfunddisasterresponse.ItemReport;
import hackathon.london.tearfunddisasterresponse.Question;
import hackathon.london.tearfunddisasterresponse.Questions;
import hackathon.london.tearfunddisasterresponse.R;

public class QuestionsActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private static final String LOGTAG = "QuestionsActivity";

    private ItemReport itemReport;
    private String currentQuestionString;
    private ArrayList<String> answers;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        itemReport = (ItemReport) getIntent().getSerializableExtra("Report");
        if (itemReport == null) {
            Log.d(LOGTAG, "itemReport is null");
        } else {
            Log.d(LOGTAG, "itemReport is not null");
        }
        Questions questionsClass = new Questions(getApplicationContext());
        question = questionsClass.getNextQuestion(getIntent().getStringExtra("Category"));
        if(question != null) {
            answers = question.answers();
            currentQuestionString = question.getQuestionText();

            final TextView textViewToChange = (TextView) findViewById(R.id.questionTitle);
            textViewToChange.setText(currentQuestionString);

            String[] answersArray = new String[answers.size()];
            for (int i = 0; i < answers.size(); i++) {
                answersArray[i] = answers.get(i);
            }

            ListView answersView = getListView();

            answersView.setAdapter(new ArrayAdapter<String>(this, R.layout.answer_list_item, answersArray));
            answersView.setOnItemClickListener(this);

        } else {
            Intent nextScreen = new Intent(getApplicationContext(), FinalPageActivity.class);
            nextScreen.putExtra("Report", itemReport);
            nextScreen.putExtra("Category", getIntent().getStringExtra("Category"));
            startActivity(nextScreen);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            itemReport.answerQuestion(currentQuestionString, answers.get(position));
            Intent nextScreen = new Intent(getApplicationContext(), QuestionsActivity.class);
            nextScreen.putExtra("Category", question.nextQuestion(answers.get(position)));
            nextScreen.putExtra("Report", itemReport);
            startActivity(nextScreen);
    }
}
