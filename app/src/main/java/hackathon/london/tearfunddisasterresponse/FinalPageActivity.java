package hackathon.london.tearfunddisasterresponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONObject;

import hackathon.london.tearfunddisasterresponse.photobyintent.PhotoIntentActivity;

public class FinalPageActivity extends Activity {

    Button.OnClickListener startAgainButtonListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToStartPage = new Intent(getApplicationContext(), PhotoIntentActivity.class);
                    startActivity(goToStartPage);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemReport itemReport = (ItemReport) getIntent().getSerializableExtra("Report");
        String category = getIntent().getStringExtra("Category");
        itemReport.setCategory(category);

        setContentView(R.layout.activity_final_page);

        final Button button = (Button) findViewById(R.id.startAgainButton);

        button.setOnClickListener(startAgainButtonListener);

        button.setEnabled(false);

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "xC0N1kQNCRAUFfKOlxQyegPXe3fZoJFllD36FWey", "PaMc1ad9VVmOWWQ2lDaLpfD3x8YO4q82jNqS1K6F");

        ParseObject report = new ParseObject("report");
        JSONObject jsonReport = itemReport.generateJSON();
        report.put("Report", jsonReport);
        report.saveInBackground();
        // wait a bit before changing so flow is not incomprehensible
        Handler handler = new Handler();
        int millisToWait = 1000;
        handler.postDelayed(new Runnable() {
            public void run() {
                TextView progressText = (TextView) findViewById(R.id.progressText);
                progressText.setText("Sending Complete");
//                progressText.setText(Resources.getSystem().getString(R.string.sending_complete));
                button.setEnabled(true);
            }
        }, millisToWait);
    }



}
