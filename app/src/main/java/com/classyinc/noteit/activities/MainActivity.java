package com.classyinc.noteit.activities;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import android.widget.Toast;

import com.classyinc.noteit.BuildConfig;
import com.classyinc.noteit.R;

import com.classyinc.noteit.fragments.NotesFragment;

import com.google.android.material.navigation.NavigationView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //navigation view
        drawerLayout = findViewById(R.id.drawer_layout_id);

        navigationView = findViewById(R.id.nav_view_id);

    /**
        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.classyFam);
        MenuItem support = menu.findItem(R.id.Support_id);
        SpannableString s = new SpannableString(tools.getTitle());
        SpannableString s1 = new SpannableString(support.getTitle());
        s.setSpan(new TextAppearanceSpan(this,R.style.groupColor),0,s.length(),0);
        s1.setSpan(new TextAppearanceSpan(this,R.style.groupColor),0,s1.length(),0);
        support.setTitle(s1);
        tools.setTitle(s); ***/


        navigationView.setNavigationItemSelectedListener(this);

        ImageView img_gmail = findViewById(R.id.gmail_id);
        ImageView img_insta = findViewById(R.id.insta_id);
        ImageView img_linkedin = findViewById(R.id.twitter_id);

        img_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Intent.ACTION_SEND);
                String[] recipients = {"krishnansaihari@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL,recipients);
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent,"Send mail"));
            }
        });

        img_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.instagram.com/_u/classy_introvert_/");
                Intent Insta = new Intent(Intent.ACTION_VIEW,uri);
                Insta.setPackage("com.instagram.android");

                try{
                    startActivity(Insta);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/classy_introvert_")));
                }
            }
        });


        img_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String profile_url = "https://www.linkedin.com/in/sai-hari-krishnan-b55492180";
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(profile_url));
                    intent.setPackage("com.linkedin.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(profile_url)));
                }

            }
        });

        //Toolbar
        toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.my_notes));

        //navigation drawer menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //showing default fragment
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fragmentcontainer_id,
                    new NotesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_note_id);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_note_id:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragmentcontainer_id,
                        new NotesFragment()).commit();
                break;

          /*  case R.id.nav_todo_id:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_list_id:
                Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();
                break; */

            case R.id.nav_share_id:
                try{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Share");
                    String message = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n" +
                    "Hey I am using this amazing app to keep my notes."+"\n"
                            +"Give it a try!";
                    intent.putExtra(Intent.EXTRA_TEXT,message);
                    startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(this, "Couldn't Share", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_settings_id :

                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
//  case R.id.classytreasurer_id:
//
//                String profile_url = "http://play.google.com/store/apps/details?id=com.classyinc.classytreasurer";
//                try{
//                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(profile_url));
//                    intent.setPackage("com.classyinc.classytreasurer");
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                } catch (Exception e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(profile_url)));
//                }
//
//             /***   try {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("market://details?id=" + "com.classyinc.classytreasurer"))); //market://details?id=" + "com.classyinc.classytreasurer
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
//                }  ***/
//                break;