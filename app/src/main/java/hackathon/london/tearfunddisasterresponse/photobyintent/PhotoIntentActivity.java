package hackathon.london.tearfunddisasterresponse.photobyintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

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

	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final int ACTION_TAKE_PHOTO_S = 2;
	private static final int ACTION_TAKE_VIDEO = 3;

	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private Bitmap mImageBitmap;

	private static final String VIDEO_STORAGE_KEY = "viewvideo";
	private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
	private VideoView mVideoView;
	private Uri mVideoUri;

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	private String category;


	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}


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
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}

	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = 100;
		int targetH = 100;

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mVideoUri = null;
	}

	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
			File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		switch(actionCode) {
		case ACTION_TAKE_PHOTO_S:
			File f = null;
			
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
			break;

		default:
			break;			
		} // switch

        Log.d("mydebugmsg", "startActivityForResult");
		startActivityForResult(takePictureIntent, actionCode);
	}

	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
	}

	private void handleSmallCameraPhoto(Intent intent) {
		Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		mVideoUri = null;
	}

	private void handleBigCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}

	}

	private void handleCameraVideo(Intent intent) {
		mVideoUri = intent.getData();
		mVideoView.setVideoURI(mVideoUri);
		mImageBitmap = null;
		mVideoView.setVisibility(View.VISIBLE);
	}

	Button.OnClickListener mTakePicOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};

	Button.OnClickListener mTakePicSOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
		}
	};

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
					String time = String.valueOf(Calendar.getInstance().getTimeInMillis());
					Intent nextScreen = new Intent(getApplicationContext(), LocationActivity.class);
					ItemReport itemReport = new ItemReport();
					itemReport.setCategory(category);
					itemReport.addPicture("NoPicture");
					nextScreen.putExtra("Category", "Sample Food Question");
					nextScreen.putExtra("Report", itemReport);
					startActivity(nextScreen);
				}
			};

	Button.OnClickListener mTakeVidOnClickListener = 
		new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakeVideoIntent();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mImageBitmap = null;
		mVideoUri = null;

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