package com.classyinc.noteit.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.classyinc.noteit.Helper.AudienceNetworkInitializeHelper;
import com.classyinc.noteit.R;
import com.classyinc.noteit.activities.CreateNoteActivity;

import com.classyinc.noteit.adapters.NotesAdapter;
import com.classyinc.noteit.database.NotesDatabase;
import com.classyinc.noteit.entities.Note;
import com.classyinc.noteit.listeners.NotesListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAdListener;


import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.MobileAds;




import java.util.ArrayList;
import java.util.List;


import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotesFragment extends Fragment implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1; //this requestcode is used to add a new note
    public static final int REQUEST_CODE_UPDATE_NOTE = 2; // this requestcode is used to update a note
    public static final int REQUEST_CODE_SHOW_NOTES = 3; //used to display all notes

    private RecyclerView notesrecyclerview;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private int noteClickedPosition = -1;

  //private boolean adLoaded = false;

    int counter = 0;

    com.facebook.ads.InterstitialAd interstitialAd;
    private final String TAG = CreateNoteActivity.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View myview = inflater.inflate(R.layout.fragment_notes , container , false);


        ShowGoogleBannerAds(myview);

        /**
         * MobileAds.initialize(getActivity(), "ca-app-pub-3940256099942544~3347511713");
         * final AdView NotesBannerAD =  myview.findViewById(R.id.notes_ad_id);
         *  NotesBannerAD.loadAd(new AdRequest.Builder().build());
         */

       if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

           ShowFBbannerAds(myview);

       }else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
           ShowGoogleBannerAds(myview);
       }


/***
        Intent intent = null;

        try {
            intent = Intent.getIntent("SwitchON");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        boolean switchChecked = Objects.requireNonNull(intent).getBooleanExtra("SwitchON",true);


        if(switchChecked) {
              ShowFBbannerAds(myview);
        }
***/

        ImageView imgEdtNote = myview.findViewById(R.id.imageaddcontent_id);
        imgEdtNote.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //since we ve started "createNoteActivity" for the result,we need to handle the result in "onActivityResult" method to
                //update the note list after adding a note from "CreateNoteActivity"

                counter++;
                if (counter == 6) {
                    counter = 0;
                    ShowGoogleInterstitialAd();

                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

                        ShowFBInterstitialAd();

                    }else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                        ShowGoogleInterstitialAd();
                    }

                }


              /*  if (counter == 10) {
                    counter = 0;
                    ShowFBInterstitialAd();

                } */

                startActivityForResult(new Intent(getActivity(), CreateNoteActivity.class),REQUEST_CODE_ADD_NOTE);
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        notesrecyclerview = myview.findViewById(R.id.notesrecycler_id);
        notesrecyclerview.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );

        noteList = new ArrayList<>();
        //notesAdapter = new NotesAdapter(getContext(), (ArrayList<Note>) noteList);
        notesAdapter = new NotesAdapter(noteList,this);
        notesrecyclerview.setAdapter(notesAdapter);

        //request code * it means we are displaying all notes from database & therefore as a paramater inNoteDeleted we r passing false
        getNotes(REQUEST_CODE_SHOW_NOTES,false); //this getnotes() method is called from onCreate() method of an activity.it means the application
        //is just started and we need to display all notes from the database & thats why we r passing requestCodeshownotes to that method

        EditText inputSearch = myview.findViewById(R.id.inputsearch_id);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(noteList.size() != 0) {
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });

        return myview;
    }

    private void ShowFBInterstitialAd() {

        interstitialAd = new com.facebook.ads.InterstitialAd(requireActivity(), "587129425284136_587830075214071"); //IMG_16_9_APP_INSTALL#

        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();


    }

    private void ShowGoogleInterstitialAd() {
        MobileAds.initialize(getActivity(), "ca-app-pub-4710955483788759~8187686092");

        final InterstitialAd interstitialAd = new InterstitialAd(requireActivity());
        interstitialAd.setAdUnitId("ca-app-pub-4710955483788759/2935359416");
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }

        });

    }

    private void ShowFBbannerAds(View myview) {


        AudienceNetworkInitializeHelper.initialize(getActivity());

        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(requireActivity(), "587129425284136_587255505271528", AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer =  myview.findViewById(R.id.fb_banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

    }

    private void ShowGoogleBannerAds(View myview) {

        MobileAds.initialize(getActivity(), "ca-app-pub-4710955483788759~8187686092");

        final AdView NotesBannerAD =  myview.findViewById(R.id.notes_ad_id);

        NotesBannerAD.loadAd(new AdRequest.Builder().build());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE,false);
            //this getnotes() method is called from onActivityresult() method of an activity & we checked the current requestcode
            //is for add note and the result is RESULTOK.it means a new note is added from createnoteactivity and its result is sent back to this
            //activity thats why we are passing REQUESTCODEADDNOTE to that method
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if(data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false));
            }
        }
    }

    private void getNotes(final int requestCode,final boolean isNoteDeleted) {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void,Void,List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getDatabase(getActivity()).noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                //we are adding all notes from database to noteList and notify adapter about the new dataset
                if(requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else if(requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesrecyclerview.smoothScrollToPosition(0);
                }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                    //removing a note from clicked position & adding the latest updated note from same position from database
                    //and notify adapter for item changed at that position
                    noteList.remove(noteClickedPosition);


                    if (isNoteDeleted) {
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    }else {
                        noteList.add(noteClickedPosition,notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);

                    }
                }

                //we checked if the note is empty it means the app is just started since we ve declared it as a global variable,
                //in this case we are adding all notes from database to this note list and notify the adapter about the new dataset
                //in another case if the note list is not empty then it means notes are already loaded from the database so we are just
                //adding only the latest note to the notelist and notify adapter about new note inserted.And last we Scrolled our recycler view to top
               /* if(noteList.size() == 0 ) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }else {
                    noteList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }
                notesrecyclerview.smoothScrollToPosition(0); */
            }
        }
        new GetNotesTask().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNoteClicked(Note note, int position) {
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        noteClickedPosition = position;
        Intent intent = new Intent(getActivity(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",note);
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE);

        counter++;
        if(counter == 6) {
            counter = 0;
            ShowGoogleInterstitialAd();
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

                ShowFBInterstitialAd();

            }else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                ShowGoogleInterstitialAd();
            }

        }

        /*if(counter == 11) {

            counter = 0;
            ShowFBInterstitialAd();

        } */

    }

}
//RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("37C4E06175E171563B22691FAF8C6F07")
//List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
//RequestConfiguration configuration =
//    new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
//MobileAds.setRequestConfiguration(configuration);