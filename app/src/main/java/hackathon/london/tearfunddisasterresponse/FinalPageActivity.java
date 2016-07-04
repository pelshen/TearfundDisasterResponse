package hackathon.london.tearfunddisasterresponse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import org.json.JSONObject;

import java.util.Random;

import hackathon.london.tearfunddisasterresponse.photobyintent.PhotoIntentActivity;

public class FinalPageActivity extends Activity {

    private static final String LOGTAG = "FinalPageActivity";

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

        setContentView(R.layout.activity_final_page);

        final Button button = (Button) findViewById(R.id.startAgainButton);

        button.setOnClickListener(startAgainButtonListener);

        button.setEnabled(false);

        String deviceId = getDeviceId();
        Log.d(LOGTAG, "deviceId: " + deviceId);
        itemReport.setDeviceId(deviceId);
        // if id is phone number, set phone number
        if (isNumeric(deviceId)) {
            itemReport.setPhoneNumber(deviceId);
        }

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), "xC0N1kQNCRAUFfKOlxQyegPXe3fZoJFllD36FWey", "PaMc1ad9VVmOWWQ2lDaLpfD3x8YO4q82jNqS1K6F");

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

    protected String getDeviceId() {
        Log.d(LOGTAG, "entering getDeviceId");

        TelephonyManager telephonyManager = (TelephonyManager)
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        String returnString = null;

        if (telephonyManager == null) {
            Log.w(LOGTAG, "telephonyManager is null");

        } else {
            returnString = telephonyManager.getLine1Number();

            // if the device has no phone number, return device Id instead
            if (TextUtils.isEmpty(returnString) || returnString.matches("\\s")) {
                Log.d(LOGTAG, "phone number not found, returning device Id instead");
                returnString = "DeviceId " + telephonyManager.getDeviceId();
            }
        }

        if (TextUtils.isEmpty(returnString) || returnString.matches("\\s")) {
            Log.d(LOGTAG, "deviceId is null, empty, or whitespace; returning random number");
            long randomNumber = new Random().nextLong();
            returnString = "RAND " + Long.toString(randomNumber);
        }

        Log.d(LOGTAG, "deviceId: " + returnString);
        return returnString;
    }

    // borrowed from http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

}
