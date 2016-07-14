package com.customkeyboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

   // private EditText et;
    private static int RESULT_LOAD_IMAGE =1;
    private  boolean isTemp;
    static MainActivity mainActivity;
    private Bitmap yourSelectedImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mainActivity=this;

        //String str=getIntent().getStringExtra("from");
        if(getIntent().getExtras()!=null){
            System.out.println("not null");
            doChangeKeyBoardBackground();
        }else{
            System.out.println("null");
        }


    }

    public static MainActivity returnInstance(){
        return mainActivity;
    }

    @Override
    protected void onResume(){
        super.onResume();
        //finish();
    }

    // change default_theme background images
    public void doChangeKeyBoardBackground(){

        Intent intent;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        }
        intent.putExtra("return-data", true);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == RESULT_LOAD_IMAGE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    getPathFromURI(selectedImageUri);
                    // finish();
                     MyKeyboardService.setBackGroundImage(yourSelectedImage);
                     finish();

                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            yourSelectedImage = BitmapFactory.decodeFile(res);
        }
        cursor.close();
        return res;
    }


}
