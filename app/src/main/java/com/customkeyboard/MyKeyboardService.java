package com.customkeyboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by arvind on 8/7/16.
 */
public class MyKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private static  KeyboardView kv;
    private Keyboard keyboard;
    private ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    Drawable d;
    public static  LinearLayout linearLayout;
    private boolean caps = false;
    private String themes="default";
    private static Drawable drawable;




    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        System.out.println("onKeyPressed.");

        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);

        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    @Override
    public void onPress(int primaryCode) {
        System.out.println("onPress");
    }

    @Override
    public void onRelease(int primaryCode) {
        System.out.println("onRelease");
    }

    @Override
    public void onText(CharSequence text) {
        System.out.println("onText");
    }

    @Override
    public void swipeDown() {
        System.out.println("onSwipeDown");
    }

    @Override
    public void swipeLeft() {
        System.out.println("onSwipeLeft");
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    @Override public void onCreate() {
        super.onCreate();
    }


    @Override
    public View onCreateInputView() {
        System.out.println("onCreateInputView.........");
        View view = null;


        if (themes != null && themes.equals("one")) {


            System.out.println("theme one....");

            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater1.inflate(R.layout.themes1, null);
            view.invalidate();

            imageView = (ImageView) view.findViewById(R.id.menu_item);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionsMenu(v);
                }
            });
            kv = (KeyboardView) view.findViewById(R.id.keyboard1);
            //kv.setBackgroundResource(R.drawable.images);
            keyboard = new Keyboard(this, R.xml.qwerty);

            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);

           kv.setBackground(drawable);



            return view;

        }else if (themes != null && themes.equals("two")) {


            System.out.println("theme two....");

            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater1.inflate(R.layout.themes2, null);
            view.invalidate();

            imageView = (ImageView) view.findViewById(R.id.menu_item);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionsMenu(v);
                }
            });
            kv = (KeyboardView) view.findViewById(R.id.keyboard2);
            //kv.setBackgroundResource(R.drawable.images);
            keyboard = new Keyboard(this, R.xml.qwerty);

            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);

            kv.setBackground(drawable);



            return view;

        } else if (themes != null && themes.equals("default")) {
            System.out.println("theme default....");

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.default_theme, null);


            imageView = (ImageView) view.findViewById(R.id.menu_item);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOptionsMenu(v);
                }
            });
            kv = (KeyboardView) view.findViewById(R.id.keyboard);
            //kv.setBackgroundResource(R.drawable.images);
            keyboard = new Keyboard(this, R.xml.qwerty);

            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);

            kv.setBackground(drawable);
            return view;

        }else {
            return  null;
        }



}

    // This function open options menu
    private void openOptionsMenu(View view){
        /** Instantiating PopupMenu class */
        PopupMenu popup = new PopupMenu(getBaseContext(), view);

        /** Adding menu items to the popumenu */
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        /** Defining menu item click listener for the popup menu */
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "Click Item  " + item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getTitle().equals("Keyboard Background Image")) {


                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("from", "service");
                    getApplicationContext().startActivity(intent);

                }else if(item.getTitle().equals("Theme one")){
                    themes="one";
                    setInputView(onCreateInputView());

                }else if(item.getTitle().equals("Theme two")){
                    themes="two";
                    setInputView(onCreateInputView());

                } else if(item.getTitle().equals("Theme Default")){

                    themes="default";
                    setInputView(onCreateInputView());
                }
                return true;
            }
        });

        /** Showing the popup menu */
        popup.show();
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case -1:
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }


public static  void setBackGroundImage(Bitmap bitmap){

    try {

        drawable=new BitmapDrawable(Resources.getSystem(),bitmap);
        kv.setBackground(drawable);
    }catch (Exception e){

    }

}



}