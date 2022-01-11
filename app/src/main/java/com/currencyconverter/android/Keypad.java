package com.currencyconverter.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.currencyconverterdf.android.R;

public class Keypad implements View.OnClickListener
{
    private final Button buttonPad0;
    private final Button buttonPad1;
    private final Button buttonPad2;
    private final Button buttonPad3;
    private final Button buttonPad4;
    private final Button buttonPad5;
    private final Button buttonPad6;
    private final Button buttonPad7;
    private final Button buttonPad8;
    private final Button buttonPad9;
    private final Button buttonPadD;
    private final ImageButton buttonPadB;

    private final KeypadListener listener;

    public Keypad(Context context, ViewGroup vg, KeypadListener listener)
    {
        this.listener = listener;

        View view = LayoutInflater.from(context).inflate(R.layout.content_keypad, null);

        vg.removeAllViews();
        vg.addView(view);

        buttonPad0 = (Button)view.findViewById(R.id.buttonPad0);
        buttonPad1 = (Button)view.findViewById(R.id.buttonPad1);
        buttonPad2 = (Button)view.findViewById(R.id.buttonPad2);
        buttonPad3 = (Button)view.findViewById(R.id.buttonPad3);
        buttonPad4 = (Button)view.findViewById(R.id.buttonPad4);
        buttonPad5 = (Button)view.findViewById(R.id.buttonPad5);
        buttonPad6 = (Button)view.findViewById(R.id.buttonPad6);
        buttonPad7 = (Button)view.findViewById(R.id.buttonPad7);
        buttonPad8 = (Button)view.findViewById(R.id.buttonPad8);
        buttonPad9 = (Button)view.findViewById(R.id.buttonPad9);
        buttonPadD = (Button)view.findViewById(R.id.buttonPadD);
        buttonPadB = (ImageButton)view.findViewById(R.id.buttonPadB);

        buttonPad0.setOnClickListener(this);
        buttonPad1.setOnClickListener(this);
        buttonPad2.setOnClickListener(this);
        buttonPad3.setOnClickListener(this);
        buttonPad4.setOnClickListener(this);
        buttonPad5.setOnClickListener(this);
        buttonPad6.setOnClickListener(this);
        buttonPad7.setOnClickListener(this);
        buttonPad8.setOnClickListener(this);
        buttonPad9.setOnClickListener(this);
        buttonPadD.setOnClickListener(this);
        buttonPadB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {


        if(view == buttonPadB)
        {
            bkspc();
        }
        else if(view == buttonPadD)
        {
            key(".");
        }
        else if(view == buttonPad0)
        {
            key("0");
        }
        else if(view == buttonPad1)
        {
            key("1");
        }
        else if(view == buttonPad2)
        {
            key("2");
        }
        else if(view == buttonPad3)
        {
            key("3");
        }
        else if(view == buttonPad4)
        {
            key("4");
        }
        else if(view == buttonPad5)
        {
            key("5");
        }
        else if(view == buttonPad6)
        {
            key("6");
        }
        else if(view == buttonPad7)
        {
            key("7");
        }
        else if(view == buttonPad8)
        {
            key("8");
        }
        else if(view == buttonPad9)
        {
            key("9");
        }
    }

    private void key(String key)
    {
        if(listener != null) listener.onKeypadKey(key);
    }

    private void bkspc()
    {
        if(listener != null) listener.onKeypadBack();
    }

    public interface KeypadListener
    {
        public void onKeypadBack();
        public void onKeypadKey(String key);
    }
}
