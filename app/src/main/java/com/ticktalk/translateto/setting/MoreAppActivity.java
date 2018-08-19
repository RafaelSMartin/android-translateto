package com.ticktalk.translateto.setting;

import android.app.Activity;
import android.content.Intent;

import com.ticktalk.helper.AppUpdateDialog;
import com.ticktalk.helper.Constant;
import com.ticktalk.moreapplib.MoreAppBasedActivity;
import com.ticktalk.moreapplib.MoreAppModel;
import com.ticktalk.translateto.R;

import java.util.ArrayList;
import java.util.List;

public class MoreAppActivity extends MoreAppBasedActivity {



    public static void start(Activity startingActivity)
    {
        Intent intent = new Intent(startingActivity, MoreAppActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public List<MoreAppModel> createMoreApps() {
        List<MoreAppModel> moreAppModels = new ArrayList<>();

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_dictionary)
                .icon(R.drawable.icon_dictionary_with_background)
                .appName(getString(R.string.multi_dictionary_app_name))
                .description(getString(R.string.more_dictionary_sub_text))
                .packageName(Constant.PackageName.DICTIONARY)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_camera_translator)
                .icon(R.drawable.icon_camera_translator)
                .appName(getString(R.string.camera_translator))
                .description(getString(R.string.more_camera_sub_text))
                .packageName(Constant.PackageName.CAMERA_TRANSLATOR)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_multi_language)
                .icon(R.drawable.icon_multi_language)
                .appName(getString(R.string.triple_translator))
                .description(getString(R.string.more_multi_sub_text))
                .packageName(Constant.PackageName.MULTI_TRANSLATOR)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_cam_scanner)
                .icon(R.drawable.icon_cam_scanner)
                .appName(getString(R.string.cam_scanner))
                .description(getString(R.string.more_cam_scanner_sub_text))
                .packageName(Constant.PackageName.CAM_SCANNER)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_pdf_converter)
                .icon(R.drawable.icon_pdf_converter)
                .appName(getString(R.string.pdf_converter))
                .description(getString(R.string.more_pdf_converter_sub_text))
                .packageName(Constant.PackageName.PDF_CONVERTER)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_image_converter)
                .icon(R.drawable.icon_image_converter)
                .appName(getString(R.string.image_converter))
                .description(getString(R.string.more_image_converter_sub_text))
                .packageName(Constant.PackageName.IMAGE_CONVERTER)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_mp3_converter)
                .icon(R.drawable.icon_music_converter)
                .appName(getString(R.string.mp3_converter))
                .description(getString(R.string.more_mp3_converter_sub_text))
                .packageName(Constant.PackageName.MUSIC_CONVERTER)
                .build());

        moreAppModels.add(new MoreAppModel.Builder()
                .background(R.drawable.bg_video_converter)
                .icon(R.drawable.icon_video_converter)
                .appName(getString(R.string.video_converter))
                .description(getString(R.string.more_video_converter_sub_text))
                .packageName(Constant.PackageName.VIDEO_CONVERTER)
                .build());

        return moreAppModels;
    }
}
