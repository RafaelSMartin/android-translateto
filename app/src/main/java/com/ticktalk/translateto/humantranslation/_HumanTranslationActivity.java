package com.ticktalk.translateto.humantranslation;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ticktalk.translateto.favorite.FavoriteActivity;
import com.ticktalk.translateto.history.HistoryActivity;
import com.ticktalk.translateto.sessionpref.SessionPrefs;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.spinner.CustomAdapter;
import com.ticktalk.translateto.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.spinner.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.spinner.ArraySpinner.countryNames;
import static com.ticktalk.translateto.spinner.ArraySpinner.flags;

/**
 * Created by Rafael S. Martin
 */

public class _HumanTranslationActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        View.OnLayoutChangeListener,
        Toolbar.OnMenuItemClickListener
{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.from_spinner_human_translation)
    Spinner fromSpinner;

    @BindView(R.id.more_spinner_human_translation)
    Spinner toMoreSpinner;

    @BindView(R.id.more_language)
    ImageView moreLanguage;

    @BindView(R.id.less_language)
    ImageView lessLanguage;

    @BindView(R.id.layout_to_spinner)
    LinearLayout layoutToSpinner;

    @BindView(R.id.topic_spinner)
    Spinner topicSpinner;

    @BindView(R.id.tone_spinner)
    Spinner toneSpinner;

    @BindView(R.id.author_gender_spinner)
    Spinner authorGenderSpinner;

    @BindView(R.id.audience_gender_spinner)
    Spinner audienceGenderSpinner;

    @BindView(R.id.submit_order)
    Button submitOrder;


    private CustomAdapter customAdapter;
    private Spinner toSpinnerMore;
    private LinearLayout.LayoutParams layoutToSpinnerParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_human_tranlation_dos);
        ButterKnife.bind(this);

        initToolbar();
        initCustomSpinner();
        initTopicSpinner();
        initToneSpinner();
        initAuthorGenderSpinner();
        initAudienceGenderSpinner();

//        if(layoutToSpinner.getChildCount() == 1){
//            lessLanguage.setVisibility(View.INVISIBLE);
//        }

        layoutToSpinner.addOnLayoutChangeListener(this);
        moreLanguage.setOnClickListener(this);
        lessLanguage.setOnClickListener(this);
        submitOrder.setOnClickListener(this);



    }


    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Human Translation");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    private void initCustomSpinner(){
        customAdapter = new CustomAdapter(getApplicationContext(), flags, countryNames, countryCodes, true);
        layoutToSpinner.setGravity(Gravity.CENTER);
        fromSpinner.setAdapter(customAdapter);
        toMoreSpinner.setAdapter(customAdapter);

        if (SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage() != 0)
            fromSpinner.setSelection(SessionPrefs.get(getApplicationContext()).getPrefNativeLanguage());


        //get resources para las medidas del layout del spinner dean dp
        Resources r = getResources();
        float pxLeftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        float pxTopMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
        float pxRightMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
        float pxBottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());


        layoutToSpinnerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutToSpinnerParams.setMargins(Math.round(pxLeftMargin), Math.round(pxTopMargin), Math.round(pxRightMargin), Math.round(pxBottomMargin));


    }

    private void initTopicSpinner(){
        List<String> topic = new ArrayList<String>();
        topic.add("Select the topic fits best?");
        topic.add("Politics");
        topic.add("Relationsships");
        topic.add("Crafts/Arts");
        topic.add("Parenting");
        topic.add("Technologic");
        topic.add("Sports/Leisure");
        topic.add("Food");
        topic.add("Travel");
        topic.add("Business");
        topic.add("Industry");
        topic.add("Entertainment");
        topic.add("Other");

        ArrayAdapter<String> dataAdapterTopic = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, topic);
        dataAdapterTopic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicSpinner.setAdapter(dataAdapterTopic);

        topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(parent.getContext(), "Android Simple Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                 parent.getItemAtPosition(0);
            }
        });
    }

    private void initToneSpinner(){
        List<String> tone = new ArrayList<String>();
        tone.add("What is the voice/mood?");
        tone.add("Informal");
        tone.add("Friendly");
        tone.add("Business");
        tone.add("Formal");

        ArrayAdapter<String> dataAdapterTone = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tone);

        dataAdapterTone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        toneSpinner.setAdapter(dataAdapterTone);
        toneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

//                Toast.makeText(parent.getContext(), "Android Simple Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });
    }

    private void initAuthorGenderSpinner(){
        List<String> authorGender = new ArrayList<String>();
        authorGender.add("What gender is speaking?");
        authorGender.add("Male");
        authorGender.add("Female");
        authorGender.add("Group");
        authorGender.add("Neutral");

        ArrayAdapter<String> dataAdapterAuthorGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, authorGender);

        dataAdapterAuthorGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        authorGenderSpinner.setAdapter(dataAdapterAuthorGender);
        authorGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

//                Toast.makeText(parent.getContext(), "Android Simple Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });
    }

    private void initAudienceGenderSpinner(){
        List<String> audienceGender = new ArrayList<String>();
        audienceGender.add("What gender is receiving?");
        audienceGender.add("Male");
        audienceGender.add("Female");
        audienceGender.add("Group");
        audienceGender.add("Neutral");

        ArrayAdapter<String> dataAdapterAudienceGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, audienceGender);

        dataAdapterAudienceGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        audienceGenderSpinner.setAdapter(dataAdapterAudienceGender);
        audienceGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

//                Toast.makeText(parent.getContext(), "Android Simple Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.getItemAtPosition(0);
            }
        });
    }

    //Listener para layout buttons
    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldright, int oldBottom) {
        if(layoutToSpinner.getChildCount() > 1){
            lessLanguage.setVisibility(View.VISIBLE);
        } else{
            lessLanguage.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_language:
                toSpinnerMore = new Spinner(this);
                toSpinnerMore.setBackgroundResource(R.drawable.custom_spinner_background);
                toSpinnerMore.setAdapter(customAdapter);
                layoutToSpinner.addView(toSpinnerMore, layoutToSpinnerParams);


//                if(layoutToSpinner.getChildCount() > 1){
//                    lessLanguage.setVisibility(View.VISIBLE);
//                }
                break;

            case R.id.less_language:
                if(layoutToSpinner.getChildCount() > 1){

//                    lessLanguage.setVisibility(View.VISIBLE);
                    int pos = layoutToSpinner.getChildCount() - 1;
                    View view = layoutToSpinner.getChildAt(pos);
                    layoutToSpinner.removeView(view);

                }
//                else if(layoutToSpinner.getChildCount() == 1){
//                    lessLanguage.setVisibility(View.INVISIBLE);
//                }
                break;

            case R.id.submit_order:
                //TODO enviar peticion a la web
                break;


            default:
                break;
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(this);
                break;

            case R.id.history:
                HistoryActivity.startHistoryActivity(this);
                break;

            case R.id.favorite:
                FavoriteActivity.startFavoriteActivity(this);
                break;

        }
        return true;
    }

    /**
     *
     * Spinner
     *
     * **/

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }





}
