package com.iyans.dashboard.dashfragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iyans.R;
import com.iyans.model.FeedDetailModel;
import com.iyans.model.UserModel;
import com.iyans.utility.Utility;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class TelePostFragment extends Fragment implements OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static String[] ClickButton = new String[]{"LIVE NOW", "TIME CAPSULE", "CANCEL"};
    private static final int GALLERY_VIDEO_REQUEST_CODE = 210;

    private DatabaseReference userRef;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    public static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int SELECT_PICTURE = 1;
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private static String TAG = TelePostFragment.class.getSimpleName();
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    public static String[] choose_from = new String[]{"Gallery", "Camera"};
    private final int CAMERA_CODE = 10;
    private final int GALLERY_CODE = 11;
    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    String UploadVideo = null;
    Button btnSend;
    String dateTime = "";
    EditText edtMessage;
    EditText edtTitle;
    private Uri fileUri;
    byte[] imageBytes;
    ImageView imgViewAudio;
    ImageView imgViewCamera;
    ImageView imgViewProfile;
    private int mDay;
    private int mHour;
    private OnFragmentInteractionListener mListener;
    private int mMinute;
    private int mMonth;
    private String mParam1;
    private String mParam2;
    private int mYear;
    private String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private SharedPreferences permissionStatus;
    String[] permissionsRequired = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"};
    public int position = 0;
    Uri selectedMediaUri;
    SwitchDateTimeDialogFragment dateTimeFragment;
    private boolean sentToSettings = false;
    Bitmap thumbnail;
    String type;
    String videoUri;
    private String whichContent = "text";

    private DatabaseReference feedRef;
    private StorageReference  imageRef;
    private StorageReference  videoRef;
    private String imageUrl;
    private String videoUrl;
    private UserModel curentUser;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Android File Upload", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        Log.v("Android File Upload", "storing at " + mediaFile.getAbsolutePath());
        return mediaFile;
    }

    public static TelePostFragment newInstance(String param1, String param2) {
        TelePostFragment fragment = new TelePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }

        feedRef = FirebaseDatabase.getInstance().getReference("feeds");
        imageRef = FirebaseStorage.getInstance().getReference("images");
        videoRef = FirebaseStorage.getInstance().getReference("videos");

        getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tele_post, container, false);
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", 0);
        imgViewProfile = (ImageView) v.findViewById(R.id.imgViewProfile);
        imgViewCamera = (ImageView) v.findViewById(R.id.imgViewCamera);
        imgViewAudio = (ImageView) v.findViewById(R.id.imgViewAudio);
        edtTitle = (EditText) v.findViewById(R.id.edtTitle);
        edtMessage = (EditText) v.findViewById(R.id.edtMessage);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        imgViewAudio.setOnClickListener(this);
        imgViewCamera.setOnClickListener(this);
        dateTimeFragment = (SwitchDateTimeDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker),
                    getString(R.string.clean));
        }
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault());
        dateTimeFragment.set24HoursMode(false);
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(2015, 0, 1).getTime());
        dateTimeFragment.setMaximumDateTime(new GregorianCalendar(2025, 11, 31).getTime());
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            public void onPositiveButtonClick(Date date) {
                dateTime = myDateFormat.format(date);
                SendTelepostMethod();
            }

            public void onNegativeButtonClick(Date date) {
            }

            public void onNeutralButtonClick(Date date) {
                dateTime = "";
            }
        });
        return v;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSend) {
            SelectSendType(ClickButton);
        } else if (id == R.id.imgViewAudio) {
            type = "video";
            permissions();
        } else if (id == R.id.imgViewCamera) {
            type = "image";
            permissions();
        }
    }

    private void SelectSendType(final String[] clickButton) {
        Builder singlechoicedialog = new Builder(getActivity());
        singlechoicedialog.setSingleChoiceItems((CharSequence[]) clickButton, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                position = item;
                System.out.println(clickButton[item]);
                if (clickButton[item].equalsIgnoreCase("LIVE NOW")) {
                    dialog.cancel();
                    LiveNow();
                } else if (clickButton[item].equalsIgnoreCase("TIME CAPSULE")) {
                    dialog.cancel();
                    TimeCapsule();
                } else if (clickButton[item].equalsIgnoreCase("CANCEL")) {
                    dialog.cancel();
                }
            }
        });
        AlertDialog alert_dialog = singlechoicedialog.create();
        alert_dialog.show();
        alert_dialog.getListView().setItemChecked(position, true);
    }

    private void TimeCapsule() {
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(2017, 2, 4, 15, 20).getTime());
        dateTimeFragment.show(getActivity().getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
    }

    private void LiveNow() {
        if (edtTitle.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Title Can't Be Blank!", Toast.LENGTH_LONG).show();
        } else if (edtMessage.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Massage Can't Be Blank!", Toast.LENGTH_LONG).show();
        } else {
            SendTelepostMethod();
        }
    }

    public void showDialog(final String[] item_array, final String type) {
        Builder singlechoicedialog = new Builder(getActivity());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Choose ");
        stringBuilder.append(type);
        stringBuilder.append(" From");
        singlechoicedialog.setTitle(stringBuilder.toString());
        singlechoicedialog.setSingleChoiceItems((CharSequence[]) item_array, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                position = item;
                System.out.println(item_array[item]);
                if (type.equalsIgnoreCase("image")) {
                    whichContent = "image";
                    if (item_array[item].equalsIgnoreCase("Gallery")) {
                        dialog.cancel();
                        galleryimage();
                    } else if (item_array[item].equalsIgnoreCase("Camera")) {
                        dialog.cancel();
                        cameraIntent();
                    }
                } else if (type.equalsIgnoreCase("video")) {
                    whichContent = "video";
                    if (item_array[item].equalsIgnoreCase("Gallery")) {
                        dialog.cancel();
                        galleryVideo();
                    } else if (item_array[item].equalsIgnoreCase("Camera")) {
                        dialog.cancel();
                        recordVideo();
                    }
                }
            }
        });
        singlechoicedialog.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert_dialog = singlechoicedialog.create();
        alert_dialog.show();
        alert_dialog.getListView().setItemChecked(position, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("onActivityResult() called with: requestCode = [");
//        stringBuilder.append(requestCode);
//        stringBuilder.append("], resultCode = [");
//        stringBuilder.append(resultCode);
//        stringBuilder.append("], data = [");
//        stringBuilder.append(data);
//        stringBuilder.append("]");
//        Log.e(TAG, stringBuilder.toString());

        if (resultCode == -1) {
            Uri imageUri;
            Intent inCamera;
            switch (requestCode) {
                case 0:
                    try {
                        imageUri = getImageUri(getActivity(), (Bitmap) data.getExtras().get("data"));

                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(imageUri.toString());
                        stringBuilder2.append(" path");
                        Log.e("bitmap............", stringBuilder2.toString());

//                        inCamera = new Intent(getActivity(), ImageShow.class);
//                        inCamera.putExtra("byteimage", imageUri.toString());
//                        startActivityForResult(inCamera, 10);
                        uploadImageToFirebase(imageUri);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                case 1:
                    try {
                        imageUri = data.getData();

                        Log.e("imageUri", imageUri.toString());
//                        inCamera = new Intent(getActivity(), ImageShow.class);
//                        inCamera.putExtra("byteimage", imageUri.toString());
//                        startActivityForResult(inCamera, 11);
                        uploadImageToFirebase(imageUri);
                        break;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        break;
                    }
                case 10:
                    imageUri = Uri.parse(data.getStringExtra("imagepath"));
                    try {
                        imageBytes = getBytes(getActivity().getContentResolver().openInputStream(imageUri));
                        //uploadImageToFirebase(imageBytes);
                        break;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        break;
                    }
                case 11:
                    try {
                        imageBytes = getBytes(getActivity().getContentResolver().openInputStream(Uri.parse(data.getStringExtra("imagepath"))));
                        //uploadImageToFirebase(imageBytes);
                    } catch (Exception e32) {
                        e32.printStackTrace();
                    }
                    break;
                case 101:
                    if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) == 0) {
                        proceedAfterPermission();
                        break;
                    }
                    break;
                case 200:
                    videoUri = fileUri.getPath();
                    uploadVideoToFirebase(fileUri);
                    break;
                case GALLERY_VIDEO_REQUEST_CODE /*210*/:
                    selectedMediaUri = data.getData();
                    videoUri = getRealPathFromURI(selectedMediaUri);
                    uploadVideoToFirebase(selectedMediaUri);
                    break;
                default:
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SendTelepostMethod() {
        //Utility.showProgressHUD(getActivity());

        HashMap<String, String> hm = new HashMap<>();
        hm.put("feed_origin_user_id", FirebaseAuth.getInstance().getUid());

        if (whichContent.equalsIgnoreCase("video")) {

            hm.put("feed_media_type", "video");

        } else if (whichContent.equalsIgnoreCase("image")) {
            hm.put("feed_media_type", "image");
        } else {
            hm.put("feed_media_type", "text");
        }

        hm.put("feed_timestamp", dateTime);
        hm.put("feed_description", edtMessage.getText().toString());

        if (whichContent.equalsIgnoreCase("video")) {
            if (videoUri != null) {
                File file = new File(videoUri);
                hm.put("name", file.getName());

            }
        } else if (whichContent.equalsIgnoreCase("image")) {
            hm.put("name", "title.jpg");

        }


        //Log.e("Lastname", curentUser.getLastName());

        if(curentUser != null){
            FeedDetailModel feed = new FeedDetailModel();
            feed.setCommentCount(0);
            feed.setDescription(edtMessage.getText().toString());
            feed.setUserId(FirebaseAuth.getInstance().getUid());
            feed.setFeedId(System.currentTimeMillis() + FirebaseAuth.getInstance().getUid());
            feed.setFirstName(curentUser.getFirstName());
            feed.setLastName(curentUser.getLastName());
            feed.setTitle(edtTitle.getText().toString());
            feed.setViewCount(0);
            feed.setId(System.currentTimeMillis() + Utility.getUUID());
            feed.setLikeCount(0);
            feed.setStatus("active");
            feed.setUpdatedAt(Utility.getDBCurrentDate());
            feed.setCreatedAt(Utility.getDBCurrentDate());
            feed.setLikeStatus("unlike");
            feed.setUserImage(curentUser.getImage());
            //feed.setImage(imageUrl);

            if (whichContent.equalsIgnoreCase("video")) {
                if (videoUri != null) {
                    File file = new File(videoUri);
                    hm.put("name", file.getName());
                    feed.setImageType("video");
                    feed.setImage(videoUrl);

                }
            } else  {
                hm.put("name", "title.jpg");
                feed.setImage(imageUrl);
                feed.setImageType("image");

            }

            feedRef.child(feed.getFeedId()).setValue(feed).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getContext(), "Feed posted successfully", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            getCurrentUser();
            Toast.makeText(getContext(), "Not connected to the internet, try again", Toast.LENGTH_SHORT).show();
        }



    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = 0;
        while (true) {
            int read = is.read(buff);
            len = read;
            if (read == -1) {
                return byteBuff.toByteArray();
            }
            byteBuff.write(buff, 0, len);
        }
    }

    private void recordVideo() {
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        intent.putExtra("android.intent.extra.videoQuality", 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 200);
    }

    public void galleryVideo() {
        fileUri = getOutputMediaFileUri(2);
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), GALLERY_VIDEO_REQUEST_CODE);
    }

    public void galleryimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), 1);
    }

    private void cameraIntent() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 0);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        Cursor cursor = getActivity().getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
        if (cursor.moveToFirst()) {
            res = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        }
        cursor.close();
        return res;
    }

    public Uri getOutputMediaFileUri(int type) {
        //Uri.fromFile(getOutputMediaFile(type));
        return FileProvider.getUriForFile(getContext(), "com.iyans.provider", getOutputMediaFile(type));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void proceedAfterPermission() {
        if (type.equalsIgnoreCase("image")) {
            showDialog(choose_from, "Image");
        } else if (type.equalsIgnoreCase("video")) {
            showDialog(choose_from, "Video");
        }
    }

    private void permissions() {
        Builder builder;
        Editor editor;
        if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) == 0 && ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) == 0 && ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[2]) == 0) {
            if (ContextCompat.checkSelfPermission(getActivity(), permissionsRequired[3]) == 0) {
                proceedAfterPermission();
                return;
            }
        }
        if (!(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1]) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2]))) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[3])) {
                if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    builder = new Builder(getActivity());
                    builder.setTitle((CharSequence) "Need Multiple Permissions");
                    builder.setMessage((CharSequence) "This app needs Camera, Audio and Storage permissions.");
                    builder.setPositiveButton((CharSequence) "Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                            startActivityForResult(intent, 101);
                            Toast.makeText(getActivity(), "Go to Permissions to Grant Camera, Audio and Storage", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, 100);
                }
                editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            }
        }
        builder = new Builder(getActivity());
        builder.setTitle((CharSequence) "Need Multiple Permissions");
        builder.setMessage((CharSequence) "This app needs Camera, Audio and Storage permissions.");
        builder.setPositiveButton((CharSequence) "Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(getActivity(), permissionsRequired, 100);
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        editor = permissionStatus.edit();
        editor.putBoolean(permissionsRequired[0], true);
        editor.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            boolean allgranted = false;
            int i = 0;
            while (i < grantResults.length) {
                if (grantResults[i] != 0) {
                    allgranted = false;
                    break;
                } else {
                    allgranted = true;
                    i++;
                }
            }
            if (allgranted) {
                proceedAfterPermission();
                return;
            }
            if (!(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0]) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1]))) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])) {
                    Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Builder builder = new Builder(getActivity());
            builder.setTitle((CharSequence) "Need Multiple Permissions");
            builder.setMessage((CharSequence) "This app needs Camera, Audio and Storage permissions.");
            builder.setPositiveButton((CharSequence) "Grant", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, 100);
                }
            });
            builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public void uploadImageToFirebase(Uri data){
        Utility.showProgressHUD(getContext(), "Uploading Image, please wait");
        final StorageReference fileRef = imageRef.child(System.currentTimeMillis() + FirebaseAuth.getInstance().getUid());

        UploadTask uploadTask = fileRef.putFile(data);


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Utility.hideProgressHud();
                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    Utility.hideProgressHud();
                    Log.e("downloadUrl", downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public void uploadVideoToFirebase(Uri data){
        Utility.showProgressHUD(getContext(), "Uploading video, please wait..");
        final StorageReference fileRef = videoRef.child(System.currentTimeMillis() + FirebaseAuth.getInstance().getUid()+".3gp");


        UploadTask uploadTask = fileRef.putFile(data);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Long progress = taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount() * 100;
                Log.e("Progress", progress.toString());
                //Utility.showProgressHUD(getContext(), "Uploading video, "+progress+"%");
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Utility.hideProgressHud();
                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    throw task.getException();

                }

                // Continue with the task to get the download URL

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    videoUrl = downloadUri.toString();
                    Utility.hideProgressHud();
                    Log.e("downloadUrl", downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    private void getCurrentUser(){
        String userId = FirebaseAuth.getInstance().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    curentUser = dataSnapshot.getValue(UserModel.class);
                    Log.e("User", curentUser.getDob());
                    Log.e("User", curentUser.getLastName());
                    Log.e("User", curentUser.getFirstName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
