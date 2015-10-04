package hackathon.london.tearfunddisasterresponse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hackathon.london.tearfunddisasterresponse.photobyintent.PhotoIntentActivity;
import hackathon.london.tearfunddisasterresponse.questions.QuestionsActivity;

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

        setContentView(R.layout.activity_final_page);

        final Button button = (Button) findViewById(R.id.startAgainButton);

        button.setOnClickListener(startAgainButtonListener);

        button.setEnabled(false);

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
