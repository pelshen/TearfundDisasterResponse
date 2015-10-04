package hackathon.london.tearfunddisasterresponse.amazons3;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

public class AmazonUploader {

    private Context context;

    /**
     * 
     * @param context the application context of the activity
     */
    public AmazonUploader(Context context) {
        this.context = context;
    }

    /**
     *
     * @param key the key value to upload with
     * @param file a file object containing the path of the photo to upload
     */
    public void uploadPhoto(String key, File file) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context, // Context
                "eu-west-1:dc733703-2c3e-46af-94a3-77e5ee82a903", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );

        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
        TransferUtility transferUtility = new TransferUtility(s3, context);

        TransferObserver observer = transferUtility.upload(
                "tearfunddr",     /* The bucket to upload to */
                key,    /* The key for the uploaded object */
                file        /* The file where the data to upload exists */
        );
    }
}
