package hackathon.london.tearfunddisasterresponse.questions;

import android.app.Activity;
import android.app.ListActivity;
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

import hackathon.london.tearfunddisasterresponse.R;

public class QuestionsActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private ListView answersView;

    static final String[] questions = {"question 1", "question 2", "question 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        answersView = getListView();

        answersView.setAdapter(new ArrayAdapter<String>(this, R.layout.answer_list_item, questions));
        answersView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
    }
}
