package com.ticktalk.translateto.spinner;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ticktalk.translateto.R;

/**
 * Created by Indogroup02 on 23/11/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    int flags[];
    String[] countryNames;
    String[] countryCodes;
    LayoutInflater inflter;
    boolean spinnerLeft;
    LinearLayout llderecha;
    LinearLayout llizquierda;

    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames, String[] countryCodes, Boolean spinnerLeft) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        this.countryCodes = countryCodes;
        this.spinnerLeft = spinnerLeft;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
//            if (spinnerLeft == false) {
                //spinner derecha
//                view = inflter.inflate(R.layout.custom_spinner_item_right, null);
//                llderecha = (LinearLayout) view;
//            } else {
                view = inflter.inflate(R.layout.custom_spinner_items, null);
//                llizquierda = (LinearLayout) view;
//
//            }
        }

        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        TextView codes = (TextView) view.findViewById(R.id.textViewCod);

        icon.setImageResource(flags[position]);
        names.setText(countryNames[position]);
        codes.setText(countryCodes[position]);

//        if (spinnerLeft == false) {
//            llderecha.setGravity(Gravity.LEFT);
//        } else{
//            llizquierda.setGravity(Gravity.RIGHT);
//        }


        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        LinearLayout ll = (LinearLayout) view;

        ImageView icon = (ImageView) ll.findViewById(R.id.imageView);
        TextView names = (TextView) ll.findViewById(R.id.textView);
        TextView codes = (TextView) ll.findViewById(R.id.textViewCod);

        if (spinnerLeft == false) {
            //spinner derecha
//            ll.setGravity(Gravity.RIGHT);
        } else {
//            ll.setGravity(Gravity.LEFT);
        }
//        names.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return view;
    }

}
