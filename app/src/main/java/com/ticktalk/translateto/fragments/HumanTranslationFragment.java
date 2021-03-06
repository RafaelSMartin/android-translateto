package com.ticktalk.translateto.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ticktalk.translateto.R;
import com.ticktalk.translateto.database.SessionPrefs;
import com.ticktalk.translateto.activities.SettingActivity;
import com.ticktalk.translateto.adapters.CustomAdapter;
import com.ticktalk.translateto.purchase.PurchaseFragment;
import com.ticktalk.translateto.utils.Utils;
import com.ticktalk.translateto.remote.model.HumanPojo;
import com.ticktalk.translateto.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ticktalk.translateto.utils.ArraySpinner.countryCodes;
import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.ArraySpinner.flags;

/**
 * Created by Rafael S. Martin
 */

public class HumanTranslationFragment extends Fragment implements
//        Response.ErrorListener,
//        Response.Listener<String>,
        AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        View.OnLayoutChangeListener,
        Toolbar.OnMenuItemClickListener

{

    public static final String TAG = "HUMAN_TRANSLATE_FRAGMENT";
    private View view;
    private AppCompatActivity activity;

    @BindView(R.id.contraint_layout_human_translation)
    ConstraintLayout layoutHumanTranslation;

    @BindView(R.id.human_fragment_root_two)
    RelativeLayout layoutFragmentRootTwo;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.from_spinner_human_translation)
    Spinner fromSpinner;

    @BindView(R.id.text_human_tranlation)
    EditText textHuman;

    @BindView(R.id.more_spinner_human_translation0)
    Spinner toMoreSpinner0;

    @BindView(R.id.more_spinner_human_translation1)
    Spinner toMoreSpinner1;

    @BindView(R.id.more_spinner_human_translation2)
    Spinner toMoreSpinner2;

    @BindView(R.id.more_spinner_human_translation3)
    Spinner toMoreSpinner3;

    @BindView(R.id.more_spinner_human_translation4)
    Spinner toMoreSpinner4;

    @BindView(R.id.more_language)
    ImageView moreLanguage;

    @BindView(R.id.less_language)
    ImageView lessLanguage;

    @BindView(R.id.layout_to_spinner)
    LinearLayout layoutToSpinner;

    @BindView(R.id.text_instructions_optional)
    EditText textOptions;

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

    private PurchaseFragment purchaseFragment;

    private CustomAdapter customAdapter;
    private Spinner toSpinnerMore;
    private LinearLayout.LayoutParams layoutToSpinnerParams;
    private HumanPojo model;
    public static String access, email_in, password_in, language_source, language_target,
            numero_language, topic_language, tone_language, author_gender,
            audience_gender, total_precio, total_words;
    public static int language_source_pos, language_target_pos;
    public static String topic_languagePos, tone_languagePos, author_genderPos, audience_genderPos;
    public static String text_translation = "";
    public static String instructions_language = "";

    private ArrayList<String> idiomasDestino = new ArrayList<String>();
    private int cont = 0;

    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_human_translation, container, false);
        ButterKnife.bind(this, view);

        initToolbar();
        initCustomSpinner();
        initTopicSpinner();
        initToneSpinner();
        initAuthorGenderSpinner();
        initAudienceGenderSpinner();

        layoutToSpinner.addOnLayoutChangeListener(this);
        moreLanguage.setOnClickListener(this);
        lessLanguage.setOnClickListener(this);
        submitOrder.setOnClickListener(this);

        submitOrder.setEnabled(false);
        submitOrder.setBackgroundColor(Color.GRAY);

        textHuman.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                int numberLanguages = layoutToSpinner.getChildCount();
                numero_language = String.valueOf(numberLanguages);

                int numberWords = Utils.contardorPalabras(textHuman.getText().toString());

                double precio = (0.1 * numberWords * numberLanguages);
                total_precio = String.valueOf((double)Math.round(precio*100d)/100d);
                total_words = String.valueOf(numberWords);

                // Habilitar el pago si es mas de 1€
                if(precio >= 1){
                    submitOrder.setEnabled(true);
                    submitOrder.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else{
                    submitOrder.setEnabled(false);
                    submitOrder.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        submitOrder.setEnabled(false);
        return view;
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_human);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("Human Translation");
        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
    }

    private void initCustomSpinner(){
        customAdapter = new CustomAdapter(activity, flags, countryNames, countryCodes, true);
        layoutToSpinner.setGravity(Gravity.CENTER);
        fromSpinner.setAdapter(customAdapter);
        toMoreSpinner0.setAdapter(customAdapter);
        toMoreSpinner1.setAdapter(customAdapter);
        toMoreSpinner2.setAdapter(customAdapter);
        toMoreSpinner3.setAdapter(customAdapter);
        toMoreSpinner4.setAdapter(customAdapter);

        if (SessionPrefs.get(activity).getPrefNativeLanguage() != 0)
            fromSpinner.setSelection(SessionPrefs.get(activity).getPrefNativeLanguage());

        idiomasDestino.add(countryNames[toMoreSpinner0.getSelectedItemPosition()]);

        Log.d("idiomasDestinoOO", idiomasDestino.toString());

        toMoreSpinner0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
               idiomasDestino.remove(0);
               idiomasDestino.add(0, countryNames[pos]);
                Log.d("idiomasDestino0", idiomasDestino.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        toMoreSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                idiomasDestino.remove(1);
                idiomasDestino.add(1, countryNames[pos]);
                Log.d("idiomasDestino1", idiomasDestino.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        toMoreSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                idiomasDestino.remove(2);
                idiomasDestino.add(2, countryNames[pos]);
                Log.d("idiomasDestino2", idiomasDestino.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        toMoreSpinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                idiomasDestino.remove(3);
                idiomasDestino.add(3, countryNames[pos]);
                Log.d("idiomasDestino3", idiomasDestino.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        toMoreSpinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                idiomasDestino.remove(4);
                idiomasDestino.add(4, countryNames[pos]);
                Log.d("idiomasDestino4", idiomasDestino.toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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

        ArrayAdapter<String> dataAdapterTopic = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, topic);
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

        ArrayAdapter<String> dataAdapterTone = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, tone);

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

        ArrayAdapter<String> dataAdapterAuthorGender = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, authorGender);

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

        ArrayAdapter<String> dataAdapterAudienceGender = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, audienceGender);

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
//        if(layoutToSpinner.getChildCount() > 1){
//            lessLanguage.setVisibility(View.VISIBLE);
//        } else{
//            lessLanguage.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Boton que genera mas spinner
            case R.id.more_language:
                switch (cont) {
                    case 0:
                        toMoreSpinner1.setVisibility(View.VISIBLE);
                        idiomasDestino.add(1, countryNames[toMoreSpinner1.getSelectedItemPosition()]);
                        cont++;//1
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        toMoreSpinner2.setVisibility(View.VISIBLE);
                        idiomasDestino.add(2, countryNames[toMoreSpinner2.getSelectedItemPosition()]);
                        cont++;//2
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        toMoreSpinner3.setVisibility(View.VISIBLE);
                        idiomasDestino.add(3, countryNames[toMoreSpinner3.getSelectedItemPosition()]);
                        cont++;//3
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        toMoreSpinner4.setVisibility(View.VISIBLE);
                        idiomasDestino.add(4, countryNames[toMoreSpinner4.getSelectedItemPosition()]);
                        cont++;//4
                        moreLanguage.setVisibility(View.GONE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        break;
                }


                /*toSpinnerMore = new Spinner(activity);
                toSpinnerMore.setBackgroundResource(R.drawable.custom_spinner_background);
                toSpinnerMore.setAdapter(customAdapter);
                layoutToSpinner.addView(toSpinnerMore, layoutToSpinnerParams);*/

//                toSpinnerMore.getChildAt()

                /*idiomasDestino.add(countryNames[toSpinnerMore.getSelectedItemPosition()]);*/
//                if(layoutToSpinner.getChildCount() > 1){
//                    lessLanguage.setVisibility(View.VISIBLE);
//                }

                // Obtener los idiomas en un array o string para despues
                /*Log.d("IdiomaListAdd", idiomasDestino.toString());*/



                /*toSpinnerMore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                        for (int i = 0; i<layoutToSpinner.getChildCount(); i++){
                            String position = layoutToSpinner.getChildAt(i).toString();
                            Log.d("con un for", layoutToSpinner.getChildAt(i)+"------------------------");
                        }
//                        idiomasDestino.remove(pos);
//                        idiomasDestino.add(pos, countryNames[toSpinnerMore.getSelectedItemPosition()]);

//                        idiomasDestino.remove(layoutToSpinner.getChildAt(parent.getSelectedItemPosition()));
//                        idiomasDestino.add(layoutToSpinner.getChildAt(parent.getSelectedItemPosition()), countryNames[pos]);

                        Object obj = parent.getItemAtPosition(pos);

                        Log.d("IDIOMAOO", parent.getChildAt(pos)+"");
                        Log.d("IDIOMAOO", view.getId()+"");
                        Log.d("IDIOMAOO", pos+"");
                        Log.d("IDIOMAOO", l+"");
                        Log.d("IDIOMA0", idiomasDestino.toString()+"-----------------------");
                        Log.d("IDIOMA1", parent.getItemAtPosition(pos).toString());
                        Log.d("IDIOMA2", countryNames[pos]);
                        Log.d("IDIOMA3", toSpinnerMore.getCount()+"");
                        Log.d("IDIOMA4", toSpinnerMore.getChildCount()+"");
                        Log.d("IDIOMA5", layoutToSpinner.getChildCount()+"");
                        Log.d("IDIOMA6", layoutToSpinner.getChildAt(parent.getSelectedItemPosition())+"");
                        Log.d("IDIOMA7", parent.getSelectedItem()+"");
                        Log.d("IDIOMA8", parent.getSelectedItemPosition()+"");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });*/
                break;

            case R.id.less_language:
                switch (cont) {
                    case 1:
                        toMoreSpinner1.setVisibility(View.GONE);
                        cont--;//0
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.GONE);
                        idiomasDestino.remove(1);
                        Log.d("idiomasDestino1Remove", idiomasDestino.toString());

                        break;
                    case 2:
                        toMoreSpinner2.setVisibility(View.GONE);
                        cont--;//1
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        idiomasDestino.remove(2);
                        Log.d("idiomasDestino2Remove", idiomasDestino.toString());

                        break;
                    case 3:
                        toMoreSpinner3.setVisibility(View.GONE);
                        cont--;//2
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        idiomasDestino.remove(3);
                        Log.d("idiomasDestino3Remove", idiomasDestino.toString());

                        break;
                    case 4:
                        toMoreSpinner4.setVisibility(View.GONE);
                        cont--;//3
                        moreLanguage.setVisibility(View.VISIBLE);
                        lessLanguage.setVisibility(View.VISIBLE);
                        idiomasDestino.remove(4);
                        Log.d("idiomasDestino4Remove", idiomasDestino.toString());

                        break;

                }
                /*if(layoutToSpinner.getChildCount() > 1){
//                    lessLanguage.setVisibility(View.VISIBLE);
                    int pos = layoutToSpinner.getChildCount() - 1;
                    View view = layoutToSpinner.getChildAt(pos);
                    layoutToSpinner.removeView(view);

                    idiomasDestino.remove(idiomasDestino.size()-1);
                    Log.d("IdiomaListRemove", idiomasDestino.toString());

                }
//                else if(layoutToSpinner.getChildCount() == 1){
//                    lessLanguage.setVisibility(View.INVISIBLE);
//                }*/
                break;

            case R.id.submit_order:
                getElementsToSendHuman();



//                SendHuman sendHuman = new SendHuman(Constant.url, Constant.accessHuman,
//                        email_in, password_in, language_source, language_target,
//                        text_translation, numero_language,instructions_language,
//                        topic_language, tone_language, author_gender, audience_gender,
//                        total_precio, total_words);
//                Request<?> request = sendHuman.getRequest(this, this);
//                App.getInstance().addToRequestQueue(request, App.TAG);



                    if (purchaseFragment == null) {
                        purchaseFragment = new PurchaseFragment();
                    }
                    Bundle args = new Bundle();
                    args.putString(PurchaseFragment.TARGET, language_source);
                    args.putString(PurchaseFragment.TEXT, text_translation);
                    args.putString(PurchaseFragment.SOURCE, language_target);
//                    args.putString(PurchaseFragment.SOURCE_POS, language_target_pos);
                    args.putString(PurchaseFragment.INSTRUCTIONS, instructions_language);
                    args.putString(PurchaseFragment.TOPIC, topic_language);
                    args.putString(PurchaseFragment.TONE, tone_language);
                    args.putString(PurchaseFragment.AUTHOR, author_gender);
                    args.putString(PurchaseFragment.AUDIENCE, audience_gender);
                    args.putString(PurchaseFragment.PRICE, total_precio);
                    args.putString(PurchaseFragment.WORDS, total_words);
                    purchaseFragment.setArguments(args);

                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.human_fragment_root, purchaseFragment,
                                    PurchaseFragment.TAG)
                            .commit();

//                startActivity(new Intent(activity, PurchaseActivity.class)
//                        .putExtra(PurchaseFragment.TEXT, text_translation )
//                        .putExtra(PurchaseFragment.TARGET, language_source)
//                        .putExtra(PurchaseFragment.SOURCE, language_target)
//                        .putExtra(PurchaseFragment.PRICE, total_precio)
//                        .putExtra(PurchaseFragment.WORDS, total_words)
//                );

//                layoutHumanTranslation.setVisibility(View.GONE);
//                layoutFragmentRootTwo.setVisibility(View.VISIBLE);

                break;


            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(activity);
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
//        for(int i = 0; i<layoutToSpinner.getChildCount(); i++){
//            layoutToSpinner.getChildAt(i);
//        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void getElementsToSendHuman(){
        access = Constant.accessHuman;
        //Asumo que esta logueado
        email_in = SessionPrefs.get(activity).getPrefEmail();
        password_in = SessionPrefs.get(activity).getPefPass();

        language_source = countryNames[fromSpinner.getSelectedItemPosition()];
        language_source_pos = fromSpinner.getSelectedItemPosition();

        /*language_target = countryNames[toMoreSpinner.getSelectedItemPosition()];
        language_target_pos = toMoreSpinner.getSelectedItemPosition();*/

        //Idioma de destinos en string
        language_target = idiomasDestino.toString();

//        for(int i = 0; i>layoutToSpinner.getChildCount(); i++){
//            layoutToSpinner.getChildAt(i);
//            Log.d("getElementsToSendHuman1", layoutToSpinner.getChildAt(i)+"");
//
////            language_target += countryNames[toSpinnerMore.getSelectedItemPosition()];
//        }

        if(textHuman.length()>0)
            text_translation = textHuman.getText().toString();

        /*int numberLanguages = layoutToSpinner.getChildCount();
        numero_language = String.valueOf(numberLanguages);*/

        int numberLanguages = cont+1;
        numero_language = String.valueOf(cont+1);

        if(textOptions.length()>0)
            instructions_language = textOptions.getText().toString();

        topic_language = topicSpinner.getSelectedItem().toString();
        topic_languagePos = String.valueOf(topicSpinner.getSelectedItemPosition());

        tone_language = toneSpinner.getSelectedItem().toString();
        tone_languagePos = String.valueOf(toneSpinner.getSelectedItemPosition());

        author_gender = authorGenderSpinner.getSelectedItem().toString();
        author_genderPos = String.valueOf(authorGenderSpinner.getSelectedItemPosition());

        audience_gender = audienceGenderSpinner.getSelectedItem().toString();
        audience_genderPos = String.valueOf(audienceGenderSpinner.getSelectedItemPosition());

        int numberWords = Utils.contardorPalabras(textHuman.getText().toString());

        double precio = (0.1 * numberWords * numberLanguages);
        total_precio = String.valueOf((double)Math.round(precio*100d)/100d);
        total_words = String.valueOf(numberWords);

        Log.d("ElementsToSendHuman1", access);
        Log.d("ElementsToSendHuman2", email_in);
        Log.d("ElementsToSendHuman3", password_in);
        Log.d("ElementsToSendHuman4", language_source);

        Log.d("ElementsToSendHuman5", language_target);
        Log.d("ElementsToSendHuman5", idiomasDestino.toString());

        Log.d("ElementsToSendHuman6", text_translation);
        Log.d("ElementsToSendHuman7", numero_language);
        Log.d("ElementsToSendHuman8", instructions_language);

        Log.d("ElementsToSendHuman9", topic_language);
        Log.d("ElementsToSendHuman9", topic_languagePos);
        Log.d("ElementsToSendHuman10", tone_language);
        Log.d("ElementsToSendHuman10", tone_languagePos);
        Log.d("ElementsToSendHuman11", author_gender);
        Log.d("ElementsToSendHuman11", author_genderPos);
        Log.d("ElementsToSendHuman12", audience_gender);
        Log.d("ElementsToSendHuman12", audience_genderPos);

        Log.d("ElementsToSendHuman13", total_precio);
        Log.d("ElementsToSendHuman14", total_words);

    }


//    @Override
//    public void onErrorResponse(VolleyError error) {
//        if (error instanceof AuthFailureError){
//            Log.e("ErrorResponse", "Error credentials" + error.getMessage());
//        } else if(error instanceof NetworkError){
//            Log.e("ErrorResponse", "Error network" + error.getMessage());
//        } else if (error instanceof NoConnectionError){
//            Log.e("ErrorResponse", "Error no conection" + error.getMessage());
//        } else if (error instanceof ParseError){
//            Log.e("ErrorResponse", "Error no process the response" + error.getMessage());
//        } else if (error instanceof ServerError){
//            Log.e("ErrorResponse", "Error form server" + error.getMessage());
//        } else if (error instanceof TimeoutError){
//            Log.e( "ErrorResponse", "Error out of time" + error.getMessage());
//        }
//        Toast.makeText(activity, "Something has happened, try again please.", Toast.LENGTH_SHORT).show();
//    }
//
//    //Recibimos un tipo string y deberemos convertirlo a objeto
//    @Override
//    public void onResponse(String response) {
//
//        GsonBuilder builder = new GsonBuilder();
//        builder.setExclusionStrategies(new DefaultExclusionStrategy());
//
//        //Creacion de objeto gson con builder
//        Gson gson = builder.create();
//        Gson gsonSynonym = builder.create();
//
//        Log.d("MainSend", response+"");
//
//
//        // Recogemos la respuesta del WebService
//        model = gson.fromJson(response, HumanPojo.class);
//
//
//        String keyTokenFromServer = model.getKeyToken();
//        Log.d("MainSendToken", keyTokenFromServer+"");
//
//
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
    }



}
