package hackathon.london.tearfunddisasterresponse.photobyintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hackathon.london.tearfunddisasterresponse.ItemReport;
import hackathon.london.tearfunddisasterresponse.LocationActivity;
import hackathon.london.tearfunddisasterresponse.R;
import hackathon.london.tearfunddisasterresponse.amazons3.AmazonUploader;


public class PhotoIntentActivity extends Activity {

	private static final int ACTION_TAKE_PHOTO_S = 2;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private Bitmap mImageBitmap;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	private String category;


	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}


	/*
	 * Get directory for album that photos will be stored in
	 */
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_S:
			File f;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch

        Log.d("mydebugmsg", "startActivityForResult");
		startActivityForResult(takePictureIntent, actionCode);
	}

	Button.OnClickListener mTakeHealthPicOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					category = "health";
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}
			};

	Button.OnClickListener mTakeBuildingPicOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					category = "building";
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}
			};

	Button.OnClickListener mTakeWaterPicOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					category = "water";
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
				}
			};

	Button.OnClickListener mFoodIntentOnClickListener =
			new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					category = "food";
					Intent nextScreen = new Intent(getApplicationContext(), LocationActivity.class);
					ItemReport itemReport = new ItemReport();
					itemReport.setCategory(category);
					itemReport.addPicture("NoPicture");
					nextScreen.putExtra("Category", "Sample Food Question");
					nextScreen.putExtra("Report", itemReport);
					startActivity(nextScreen);
				}
			};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mImageBitmap = null;

		ImageButton picHealthBtn = (ImageButton) findViewById(R.id.btnIntendHealth);
		setBtnListenerOrDisable(
				picHealthBtn,
				mTakeHealthPicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		ImageButton picWaterBtn = (ImageButton) findViewById(R.id.btnIntendWater);
		setBtnListenerOrDisable(
				picWaterBtn,
				mTakeWaterPicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		ImageButton picBuildingBtn = (ImageButton) findViewById(R.id.btnIntendBuilding);
		setBtnListenerOrDisable(
				picBuildingBtn,
				mTakeBuildingPicOnClickListener,
				MediaStore.ACTION_IMAGE_CAPTURE
		);

		ImageButton foodBtn = (ImageButton) findViewById(R.id.btnIntendFood);
		foodBtn.setOnClickListener(mFoodIntentOnClickListener);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("mydebugmsg", "onActivityResult");

        if (requestCode != ACTION_TAKE_PHOTO_S || resultCode != RESULT_OK) {
			Log.d("mydebugmsg", "Activity wasn't an image or wasn't okay.");
			return;
		}

		AmazonUploader uploader = new AmazonUploader(getApplicationContext());
		File file = new File(mCurrentPhotoPath);
		String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
		uploader.uploadPhoto(time, file);

		Intent nextScreen = new Intent(getApplicationContext(), LocationActivity.class);
		ItemReport itemReport = new ItemReport();
		itemReport.setCategory(category);
		itemReport.addPicture(time);
		if(category.equalsIgnoreCase("health")) {
			nextScreen.putExtra("Category", "Sample Health Question");
		} else if(category.equalsIgnoreCase("building")) {
			nextScreen.putExtra("Category", "Building Status");
		} else if(category.equalsIgnoreCase("water")) {
			nextScreen.putExtra("Category", "Sample Water Question");
		}
		nextScreen.putExtra("Report", itemReport);
		startActivity(nextScreen);
	}

	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void setBtnListenerOrDisable( 
			ImageButton btn,
			ImageButton.OnClickListener onClickListener,
			String intentName
	) {
		if (isIntentAvailable(this, intentName)) {
			btn.setOnClickListener(onClickListener);        	
		} else {
			btn.setClickable(false);
		}
	}

}