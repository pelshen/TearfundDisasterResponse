package hackathon.london.tearfunddisasterresponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hackathon.london.tearfunddisasterresponse.photobyintent.PhotoIntentActivity;

/**
 * Created by Sam on 10/10/2015.
 */
public class Notes extends Activity{

    private ItemReport itemReport;
    EditText mEdit;

    Button.OnClickListener notesDoneButtonListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToFinalPage = new Intent(getApplicationContext(), FinalPageActivity.class);
                    itemReport.setNotes(mEdit.getText().toString());
                    goToFinalPage.putExtra("Report", itemReport);
                    startActivity(goToFinalPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemReport = (ItemReport) getIntent().getSerializableExtra("Report");

        setContentView(R.layout.notes);

        Button button = (Button) findViewById(R.id.notesDone);
        mEdit   = (EditText)findViewById(R.id.notesInfo);

        button.setOnClickListener(notesDoneButtonListener);
    }
}
