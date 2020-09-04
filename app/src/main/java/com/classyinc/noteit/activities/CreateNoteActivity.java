package com.classyinc.noteit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.classyinc.noteit.Helper.AudienceNetworkInitializeHelper;
import com.classyinc.noteit.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.provider.MediaStore;
import android.transition.AutoTransition;
import android.transition.TransitionManager;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.classyinc.noteit.database.NotesDatabase;
import com.classyinc.noteit.entities.Note;



import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.facebook.ads.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle , inputNoteSubtitle , InputNoteText;
    private TextView textDateTime,textwebUrl;
    private ImageView imageNote;
    private LinearLayout layout_webUrl;

    private View ViewSubtitleIndicator;
    private String SelectedNoteColor;
    private String SelectedImagePath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private AlertDialog dialog_addUrl;
    private AlertDialog dialog_deleteNote;

    private Note alreadyAvailableNote;

    LinearLayout expandable_color_view;
    ConstraintLayout constraintLayout;
    ScrollView scrollView;
    ImageView ColorDown;


    private AdView adView;


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ShowFBbannerAds();

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            ShowGoogleBannerAds();

        }else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {

            ShowFBbannerAds();
        }


        expandable_color_view = findViewById(R.id.layout_expandable_notecolor_id);
        ColorDown = findViewById(R.id.imageColorDropdown_id);
        constraintLayout = findViewById(R.id.cons_id);
        scrollView = findViewById(R.id.scrollView2);
        ColorDown.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(expandable_color_view.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(constraintLayout,new AutoTransition());
                    expandable_color_view.setVisibility(View.VISIBLE);

                    Misc();
                }else {
                    //TransitionManager.beginDelayedTransition(constraintLayout,new AutoTransition());
                    expandable_color_view.setVisibility(View.GONE);

                }
            }
        });

        ImageView imgback = findViewById(R.id.imgback_id);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputnoteTitle_id);
        inputNoteSubtitle = findViewById(R.id.inputnoteSubTitle_id);
        InputNoteText = findViewById(R.id.inputnote_id);

        textDateTime = findViewById(R.id.txtDateTime_id);
        ViewSubtitleIndicator = findViewById(R.id.viewsubtitleindicator_id);
        imageNote = findViewById(R.id.imageNote_id);
        textwebUrl = findViewById(R.id.txtwebUrl_id);
        layout_webUrl = findViewById(R.id.layout_webUrl_id);

        textDateTime.setText(
                new SimpleDateFormat("EEEE,dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())

        );

        ImageView imglang = findViewById(R.id.imglang_id);
        imglang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddURLDialog();
            }
        });

        ImageView img_add = findViewById(R.id.img_id);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            CreateNoteActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                }else {

                    selectImage();
                }
            }
        });


        ImageView imgDeleteNote = findViewById(R.id.imageDeleteNote_id);
        if(alreadyAvailableNote == null) {
            imgDeleteNote.setVisibility(View.GONE);
        }
        if(alreadyAvailableNote != null){
            imgDeleteNote.setVisibility(View.VISIBLE);
            imgDeleteNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteNoteDialog();
                }
            });
        }

        ImageView imgDone = findViewById(R.id.img_done_id);
        imgDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();
            }
        });

        SelectedNoteColor = "#333333";
        SelectedImagePath = "";

        if(getIntent().getBooleanExtra("isViewOrUpdate",false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        findViewById(R.id.imageRemoveWebURL_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textwebUrl.setText(null);
                layout_webUrl.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.imgRemoveImage_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNote.setImageBitmap(null);
                imageNote.setVisibility(View.GONE);
                findViewById(R.id.imgRemoveImage_id).setVisibility(View.GONE);
                SelectedImagePath="";

            }
        });

        //initMisc();
        setSubtitleIndicator();
    }

    private void ShowFBbannerAds() {


        AudienceNetworkInitializeHelper.initialize(this);

        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, "587129425284136_587255505271528", AdSize.BANNER_HEIGHT_50);
//IMG_16_9_APP_INSTALL#
        // Find the Ad Container
        LinearLayout adContainer = findViewById(R.id.fb_banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

    }


    private void ShowGoogleBannerAds() {

        MobileAds.initialize(this, "ca-app-pub-4710955483788759~8187686092");

        final com.google.android.gms.ads.AdView NotesBannerAD =  findViewById(R.id.notes_ad_id);

        NotesBannerAD.loadAd(new AdRequest.Builder().build());


    }

    private void showDeleteNoteDialog() {

        if(dialog_deleteNote == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layout_deleteNote_container_id)
            );
            builder.setView(view);
            dialog_deleteNote = builder.create();
            if(dialog_deleteNote.getWindow() != null) {
                dialog_deleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.txtDeleteNote_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak" )
                    class DeleteNoteTask extends AsyncTask<Void,Void,Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.txtCancelDeleteNote_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_deleteNote.dismiss();
                }
            });
        }

        dialog_deleteNote.show();
    }

    private void setViewOrUpdateNote() {

        ImageView imgDeleteNote = findViewById(R.id.imageDeleteNote_id);
        imgDeleteNote.setVisibility(View.VISIBLE);
        imgDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteNoteDialog();
            }
        });

        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        InputNoteText.setText(alreadyAvailableNote.getNotetxt());
        textDateTime.setText(alreadyAvailableNote.getDate_time());

        if(alreadyAvailableNote.getImagepath() != null && !alreadyAvailableNote.getImagepath().trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagepath()));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imgRemoveImage_id).setVisibility(View.VISIBLE);
            SelectedImagePath = alreadyAvailableNote.getImagepath();
        }

        if(alreadyAvailableNote.getWeblink() != null && !alreadyAvailableNote.getWeblink().trim().isEmpty()) {
            textwebUrl.setText(alreadyAvailableNote.getWeblink());
            layout_webUrl.setVisibility(View.VISIBLE);
        }

    }

    private void saveNote() {

        if(inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(CreateNoteActivity.this, "Set a Title", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(inputNoteSubtitle.getText().toString().trim().isEmpty() && InputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(CreateNoteActivity.this, "Describe your note", Toast.LENGTH_SHORT).show();
            return;
        }

        //preparing note object to save in database
        final Note note  = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNotetxt(InputNoteText.getText().toString());
        note.setDate_time(textDateTime.getText().toString());
        note.setColor(SelectedNoteColor);
        note.setImagepath(SelectedImagePath);

        //we ve checked if "layoutweburl" is visible or not if its visible it means weburl is added.since
        //we ve made it visible only while adding web url from addurl dialog.
        if(layout_webUrl.getVisibility() == View.VISIBLE) {
            note.setWeblink(textwebUrl.getText().toString());
        }

        //here we are setting id of new note from an alreadyavailablenote.
        //since we have set onConflictStrategy to "REPLACE" in NoteDao.
        //this means if id of new note is already available in the database then it will be replaced with new note and our note get updated
        if(alreadyAvailableNote != null) {

            note.setId(alreadyAvailableNote.getId());
        }

        //Room doesnt allow database operation on Main thread thats why we are using async task to save note
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    private void Misc() {

        final LinearLayout color = findViewById(R.id.layout_expandable_notecolor_id);

        final ImageView imgColor1 = color.findViewById(R.id.imagecolor1);
        final ImageView imgColor2 = color.findViewById(R.id.imagecolor2);
        final ImageView imgColor3 = color.findViewById(R.id.imagecolor3);
        final ImageView imgColor4 = color.findViewById(R.id.imagecolor4);
        final ImageView imgColor5 = color.findViewById(R.id.imagecolor5);
        final ImageView imgColor6 = color.findViewById(R.id.imagecolor6);
        final ImageView imgColor7 = color.findViewById(R.id.imagecolor7);

        color.findViewById(R.id.viewcolor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#333333";
                imgColor1.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });


        color.findViewById(R.id.viewcolor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FDBE3B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.ic_done);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        color.findViewById(R.id.viewcolor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FF4842";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.ic_done);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        color.findViewById(R.id.viewcolor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#3A52Fc";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.ic_done);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        color.findViewById(R.id.viewcolor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#000000";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(R.drawable.ic_done);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        color.findViewById(R.id.viewcolor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#9C27B0";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(R.drawable.ic_done);
                imgColor7.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        color.findViewById(R.id.viewcolor7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FFFFFF";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                imgColor6.setImageResource(0);
                imgColor7.setImageResource(R.drawable.ic_done);



                setSubtitleIndicator();
            }
        });

        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {

            switch (alreadyAvailableNote.getColor()) {

                case "#FDBE3B":
                    color.findViewById(R.id.viewcolor2).performClick();
                    break;
                case "#FF4842":
                    color.findViewById(R.id.viewcolor3).performClick();
                    break;
                case "#3A52Fc":
                    color.findViewById(R.id.viewcolor4).performClick();
                    break;
                case "#000000":
                    color.findViewById(R.id.viewcolor5).performClick();
                    break;
                case "#9C27B0":
                    color.findViewById(R.id.viewcolor6).performClick();
                    break;
                case  "#FFFFFF":
                    color.findViewById(R.id.viewcolor7).performClick();
                    break;
            }
        }

    }

   /* private void  initMisc() {

        final LinearLayout layoutMisc = findViewById(R.id.layout_misc_id);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMisc);
        layoutMisc.findViewById(R.id.textMisc_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imgColor1 = layoutMisc.findViewById(R.id.imagecolor1);
        final ImageView imgColor2 = layoutMisc.findViewById(R.id.imagecolor2);
        final ImageView imgColor3 = layoutMisc.findViewById(R.id.imagecolor3);
        final ImageView imgColor4 = layoutMisc.findViewById(R.id.imagecolor4);
        final ImageView imgColor5 = layoutMisc.findViewById(R.id.imagecolor5);

        layoutMisc.findViewById(R.id.viewcolor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#333333";
                imgColor1.setImageResource(R.drawable.ic_done);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });


        layoutMisc.findViewById(R.id.viewcolor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FDBE3B";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(R.drawable.ic_done);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMisc.findViewById(R.id.viewcolor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#FF4842";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(R.drawable.ic_done);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMisc.findViewById(R.id.viewcolor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#3A52Fc";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(R.drawable.ic_done);
                imgColor5.setImageResource(0);
                setSubtitleIndicator();
            }
        });

        layoutMisc.findViewById(R.id.viewcolor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNoteColor = "#000000";
                imgColor1.setImageResource(0);
                imgColor2.setImageResource(0);
                imgColor3.setImageResource(0);
                imgColor4.setImageResource(0);
                imgColor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicator();
            }
        });

        if(alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {

            switch (alreadyAvailableNote.getColor()) {

                case "#FDBE3B":
                    layoutMisc.findViewById(R.id.viewcolor2).performClick();
                    break;
                case "#FF4842":
                    layoutMisc.findViewById(R.id.viewcolor3).performClick();
                    break;
                case "#3A52Fc":
                    layoutMisc.findViewById(R.id.viewcolor4).performClick();
                    break;
                case "#000000":
                    layoutMisc.findViewById(R.id.viewcolor5).performClick();
                    break;
            }
        }

        layoutMisc.findViewById(R.id.layout_addimg_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                 if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                         != PackageManager.PERMISSION_GRANTED) {

                     ActivityCompat.requestPermissions(
                             CreateNoteActivity.this,
                             new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                             REQUEST_CODE_STORAGE_PERMISSION
                     );
                 }else {

                     selectImage();
                 }

            }
        });

        layoutMisc.findViewById(R.id.layout_addurl_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLDialog();
            }
        });
    } */

    private void setSubtitleIndicator() {

        GradientDrawable gradientDrawable = (GradientDrawable) ViewSubtitleIndicator.getBackground();
        gradientDrawable.setAlpha(Color.parseColor(SelectedNoteColor));

    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null ) {
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handling permission result
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }else {
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handling result for selected image
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data != null) {
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imgRemoveImage_id).setVisibility(View.VISIBLE);

                        SelectedImagePath = getPathFromUri(selectedImageUri);

                    }catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri,null,null,null,null);
        if(cursor == null) {
            filePath = contentUri.getPath();

        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void showAddURLDialog() {

        if(dialog_addUrl == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url,
                    (ViewGroup) findViewById(R.id.layout_addurlContainer_id)
            );
            builder.setView(view);
            dialog_addUrl = builder.create();
            if(dialog_addUrl.getWindow() != null) {
                dialog_addUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputUrl_id);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputURL.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    }else if(!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                        Toast.makeText(CreateNoteActivity.this, "Enter a Valid URL", Toast.LENGTH_SHORT).show();
                    }else {
                        textwebUrl.setText(inputURL.getText().toString());
                        layout_webUrl.setVisibility(View.VISIBLE);
                        dialog_addUrl.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_addUrl.dismiss();
                }
            });
        }
        dialog_addUrl.show();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}