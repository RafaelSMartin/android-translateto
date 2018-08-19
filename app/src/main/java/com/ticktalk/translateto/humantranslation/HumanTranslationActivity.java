package com.ticktalk.translateto.humantranslation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ticktalk.translateto.R;

import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class HumanTranslationActivity extends AppCompatActivity
{

    HumanTranslationFragment humanTranslationFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_human);
        ButterKnife.bind(this);


        humanTranslationFragment = (HumanTranslationFragment)
                getSupportFragmentManager().findFragmentByTag(HumanTranslationFragment.TAG);

        if(humanTranslationFragment == null) {
            humanTranslationFragment = new HumanTranslationFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold,
                            R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.human_fragment_root, humanTranslationFragment,
                            HumanTranslationFragment.TAG)
                    .commit();
        }


    }






}
