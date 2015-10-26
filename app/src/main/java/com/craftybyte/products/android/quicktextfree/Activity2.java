package com.craftybyte.products.android.quicktextfree;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Activity2 extends ActionBarActivity {
    static Bitmap recvImg;
    static File f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity2, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider mShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri imageURI = Uri.fromFile(f);
        shareIntent.putExtra(Intent.EXTRA_STREAM,imageURI);
        mShare.setShareIntent(shareIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_share:
               // Intent to Share the Image with other Apps
//                startActivity(Intent.createChooser(shareIntent, "Share Image via"));
                break;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity2, container, false);
            ImageView img2 = (ImageView)rootView.findViewById(R.id.qtImage2);
            f = new File(Environment.getExternalStorageDirectory(),"Quick Text/.tmp.jpeg");
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                Log.d(getTag(),"Error found here Peers"+e.getLocalizedMessage());
            }
            recvImg = BitmapFactory.decodeStream(fis);
            img2.setImageBitmap(recvImg);

            Button saveButton = (Button) rootView.findViewById(R.id.qtSaveButton);
            saveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "QuickText_"+timeStamp;
                    f = new File(Environment.getExternalStorageDirectory(),"Quick Text/"+imageFileName);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                    } catch (FileNotFoundException e) {
                        Log.d(getTag(),"Error is "+ e.getLocalizedMessage());
                    }
                    recvImg.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    Toast.makeText(getActivity()," Image Saved ",Toast.LENGTH_LONG).show();
                }
            });
            return rootView;
        }
    }
}
