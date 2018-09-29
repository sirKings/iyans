package com.iyans.imagefilter;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.iyans.R;
import com.iyans.imagefilter.EditImageFragment.EditImageFragmentListener;
import com.iyans.imagefilter.FiltersListFragment.FiltersListFragmentListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageShow extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener {
    public static final String IMAGE_NAME = "dowmload.jpg";
    public static final int SELECT_GALLERY_IMAGE = 101;
    private static final String TAG = ImageShow.class.getSimpleName();
    int brightnessFinal = 0;
    Bitmap compressedImage;
    float contrastFinal = 1.0f;
    CoordinatorLayout coordinatorLayout;
    EditImageFragment editImageFragment;
    Bitmap filteredImage;
    FiltersListFragment filtersListFragment;
    Bitmap finalImage;
    Uri imageBytes;
    ImageView imagePreview;
    Bitmap originalImage;
    float saturationFinal = 1.0f;
    TabLayout tabLayout;
    ViewPager viewPager;

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragmentList.get(position);
        }

        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) this.mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_main));
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        imagePreview = (ImageView) findViewById(R.id.image_preview);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        imageBytes = Uri.parse(getIntent().getStringExtra("byteimage"));
        try {
            originalImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        originalImage.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append(".jpg");
        File file = new File(Environment.getExternalStorageDirectory(), stringBuilder.toString());
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        loadImage();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("byteimage", getIntent().getStringExtra("byteimage"));
        this.filtersListFragment = new FiltersListFragment();
        this.filtersListFragment.setArguments(bundle);
        this.filtersListFragment.setListener(this);
        this.editImageFragment = new EditImageFragment();
        this.editImageFragment.setListener(this);
        adapter.addFragment(this.filtersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(this.editImageFragment, getString(R.string.tab_edit));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControls();
        this.filteredImage = this.originalImage.copy(Config.ARGB_8888, true);
        this.imagePreview.setImageBitmap(filter.processFilter(this.filteredImage));
        this.finalImage = this.filteredImage.copy(Config.ARGB_8888, true);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        this.brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        this.imagePreview.setImageBitmap(myFilter.processFilter(this.finalImage.copy(Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        this.saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        this.imagePreview.setImageBitmap(myFilter.processFilter(this.finalImage.copy(Config.ARGB_8888, true)));
    }

    @Override
    public void onContrastChanged(float contrast) {
        this.contrastFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        this.imagePreview.setImageBitmap(myFilter.processFilter(this.finalImage.copy(Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {
    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = this.filteredImage.copy(Config.ARGB_8888, true);
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(this.brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(this.contrastFinal));
        myFilter.addSubFilter(new SaturationSubfilter(this.saturationFinal));
        this.finalImage = myFilter.processFilter(bitmap);
    }

    private void resetControls() {
        if (this.editImageFragment != null) {
            this.editImageFragment.resetControls();
        }
        this.brightnessFinal = 0;
        this.saturationFinal = 1.0f;
        this.contrastFinal = 1.0f;
    }

    private void loadImage() {
        this.filteredImage = this.originalImage.copy(Config.ARGB_8888, true);
        this.finalImage = this.originalImage.copy(Config.ARGB_8888, true);
        this.imagePreview.setImageBitmap(this.originalImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_save) {
            return super.onOptionsItemSelected(item);
        }
        saveImageToGallery();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == 101) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);
            this.originalImage.recycle();
            this.finalImage.recycle();
            this.finalImage.recycle();
            this.originalImage = bitmap.copy(Config.ARGB_8888, true);
            this.filteredImage = this.originalImage.copy(Config.ARGB_8888, true);
            this.finalImage = this.originalImage.copy(Config.ARGB_8888, true);
            this.imagePreview.setImageBitmap(this.originalImage);
            bitmap.recycle();
            this.filtersListFragment.prepareThumbnail(this.originalImage);
        }
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent intent = new Intent("android.intent.action.PICK");
                    intent.setType("image/*");
                    ImageShow.this.startActivityForResult(intent, 101);
                    return;
                }
                Toast.makeText(ImageShow.this.getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void saveImageToGallery() {
        Dexter.withActivity(this).withPermissions("android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Bitmap bitmap = ImageShow.this.finalImage;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(System.currentTimeMillis());
                    stringBuilder.append("_.jpg");
                    final String path = BitmapUtils.insertImage(getApplicationContext().getContentResolver(), bitmap, stringBuilder.toString(), null);
                    if (TextUtils.isEmpty(path)) {
                        Snackbar.make(ImageShow.this.coordinatorLayout, (CharSequence) "Unable to save image!", 0).show();
                    } else {
                        Snackbar.make(ImageShow.this.coordinatorLayout, (CharSequence) "Image saved to gallery!", 0).setAction((CharSequence) "OPEN", new OnClickListener() {
                            public void onClick(View view) {
                                ImageShow.this.openImage(path);
                            }
                        }).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("imagepath", path);
                        ImageShow.this.setResult(-1, returnIntent);
                        ImageShow.this.finish();
                    }
                    return;
                }
                Toast.makeText(ImageShow.this.getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

    private Bitmap compressImage(Uri imageUri) {
        int scale = 1;
        Cursor cursor = new CursorLoader(this, imageUri, new String[]{"_data"}, null, null, null).loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        while ((options.outWidth / scale) / 2 >= Callback.DEFAULT_DRAG_ANIMATION_DURATION && (options.outHeight / scale) / 2 >= Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(selectedImagePath, options);
    }
}
