package com.craftybyte.products.android.quicktextfree;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class Activity1 extends ActionBarActivity {
    public static CharSequence[] pop_list = new CharSequence[]{"Select Photo", "Capture Photo"};
    static final int requestCodeX = 100;
    static ImageView img;
    static Bitmap finalImage;
    static EditText inputText ;
    static Button gen;
    static Bitmap fImage;
    static final int requestCodeY = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_activity1);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case requestCodeX:
                if(resultCode == RESULT_OK)
                {
                    Uri imageSelected = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageSelected);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    finalImage = BitmapFactory.decodeStream(imageStream);
                    img.setImageBitmap(finalImage);
                 }
                else
                    Toast.makeText(this,"Failed to set the image",Toast.LENGTH_LONG).show();
                break;

            case requestCodeY:

                if(resultCode == RESULT_OK)
                {
                    File f = new File(Environment.getExternalStorageDirectory(),"Quick Text/.capture.jpeg");
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(f);
                    } catch (FileNotFoundException e) {
                        Log.d(getLocalClassName(),"Error found here Peers"+e.getLocalizedMessage());
                    }
                    finalImage = BitmapFactory.decodeStream(fis);
                    img.setImageBitmap(finalImage);
                }
                else
                    Toast.makeText(this,"Failed to set the image",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity1, container, false);
            img = (ImageView) rootView.findViewById(R.id.qtImageButton);

            Button but=(Button)rootView.findViewById(R.id.qtButton);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectionFragment sf=new SelectionFragment();
                    sf.show(getFragmentManager(),getTag());
                }
            });
            inputText = (EditText) rootView.findViewById(R.id.qtEditText);
            //inputText.getText();

            gen = (Button) rootView.findViewById(R.id.qtButton2);
            gen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = inputText.getText().toString();
                    text = text.toUpperCase();
                    try {
                        fImage = finalImage.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas c = new Canvas(fImage);
                    Paint p = new Paint();
                    // 19 Words Leave it as such!s
                    int len = text.length();
                    String text2="";
                    // For more Larger Texts!!
                    double divFac = 10;
                    if(len > 32)
                        divFac = 12;
                    if(len > 18)
                    {// split the words
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        int arrCount=0;
                        String[] textArr = text.split(" ");
                        Log.d(getTag(),"Text Array!");
                        for(String tt:textArr)
                        Log.d(getTag(),tt);
                        for(String t:textArr)
                        {
                            if(((t.length())+arrCount) <= 19) {
                                arrCount += t.length()+1;
                                sb.append(t);
                                sb.append(" ");
                            }
                            else
                            {
                                arrCount += t.length();
                                sb1.append(t);
                                sb1.append(" ");
                            }
                        }
                        text = sb.toString();
                        text2 = sb1.toString();
                    }
                    Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Rafale BG.otf");
                    p.setTypeface(tf);
                    p.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                    p.setTextSize((float) ((fImage.getHeight()) / divFac));
                    p.setShadowLayer(2.0f, 1.0f, 1.0f, Color.BLACK);
                    p.setColor(Color.WHITE);
                    p.setStyle(Paint.Style.FILL_AND_STROKE);
                    c.drawText(text, (fImage.getWidth() - p.measureText(text)) / 2, (float) ((fImage.getHeight() * (divFac - 1.25)) / divFac), p);
                    c.drawText(text2,(fImage.getWidth() - p.measureText(text2)) / 2,(float) (( fImage.getHeight()*(divFac-0.4) )/divFac),p);
                    File sdCardDirectory = Environment.getExternalStorageDirectory();
                    boolean success = new File(sdCardDirectory,"Quick Text").mkdir();
                    if(success)
                        Log.d(getTag(),"Directory Created");
                    File f = new File(sdCardDirectory, "Quick Text/.tmp.jpeg");
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                    } catch (FileNotFoundException e) {
                        Log.d(getActivity().getLocalClassName(),"Found the error  Peek in here peers!" + e.getLocalizedMessage());
                    }

                    fImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Intent lastActivity = new Intent(getActivity(), Activity2.class);
                    startActivity(lastActivity);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity(),"Choose a photo to continue",Toast.LENGTH_LONG).show();
                        Log.d(getTag(),"Error is "+e.getLocalizedMessage());
                    }
                }
            });
            return rootView;
        }


        public static class SelectionFragment extends DialogFragment {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.pop_title);
                builder.setItems(pop_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Toast.makeText(getActivity(), pop_list[which], Toast.LENGTH_SHORT).show();
                                Intent photoSelectIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                                photoSelectIntent.setType("image/*");
                                getActivity().startActivityForResult(photoSelectIntent, requestCodeX);
                                break;
                            case 1:
                                Toast.makeText(getActivity(), pop_list[which], Toast.LENGTH_SHORT).show();
                                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File sdCardDirectory = Environment.getExternalStorageDirectory();
                                boolean success = new File(sdCardDirectory,"Quick Text").mkdir();
                                if(success)
                                    Log.d(getTag(),"Directory Created");
                                File f = new File(sdCardDirectory, "Quick Text/.capture.jpeg");
                                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
                                if (photoCaptureIntent.resolveActivity(getActivity().getPackageManager()) != null)
                                getActivity().startActivityForResult(photoCaptureIntent,requestCodeY);
                                break;
                            default:
                                break;
                        }
                    }
                });
                return builder.create();
            }

        }

    }
}
