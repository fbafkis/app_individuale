package com.francescobertamini.app_individuale.ui.championship;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubChampionshipsDialog extends DialogFragment {

    public static final String TAG = "sub_championship_dialog";
    private Toolbar toolbar;


    JsonArray championships;
    JsonObject championship;
    String id;

    @BindView(R.id.subChampName)
    TextView _subChampName;
    @BindView(R.id.subChampId)
    TextView _subChampId;
    @BindView(R.id.subChampCar)
    TextView _subChampCar;
    @BindView(R.id.subChampLogo)
    ImageView _subChampLogo;
    @BindView(R.id.subChampCalendarButton)
    Button _subChampCalendarButton;
    @BindView(R.id.subChampRacersButton)
    Button _subChampRacersButton;
    @BindView(R.id.subChampResultsButton)
    Button _subChampResultsButton;

    public static SubChampionshipsDialog display (FragmentManager fragmentManager, String champ_id){

        SubChampionshipsDialog dialog = new SubChampionshipsDialog();
        dialog.id = champ_id;
        dialog.show(fragmentManager, TAG);
        return dialog;

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_SlideUpDown);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.dialog_sub_champ, container, false);
        ButterKnife.bind(this,root);

        toolbar = root.findViewById(R.id.sub_champ_toolbar);


        JsonExtractorChampionships jsonExtractorChampionships = new JsonExtractorChampionships(this.getContext());

        try {
            championships = jsonExtractorChampionships.readJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<championships.size();i++){

            if(championships.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
                championship = championships.get(i).getAsJsonObject();
            }
        }

        _subChampName.setText(championship.get("nome").getAsString());
        _subChampId.setText(championship.get("id").getAsString());
        _subChampCar.setText(championship.get("lista-auto").getAsString());


        String wrong_res_name = championship.get("logo").getAsString();
        wrong_res_name =  wrong_res_name.replaceAll("-","_");

        String logo_res = wrong_res_name.substring(0,wrong_res_name.indexOf("."));

        int logo_drawable_id = getContext().getResources().getIdentifier( logo_res, "drawable", getContext().getPackageName());

        _subChampLogo.setImageDrawable(getContext().getResources().getDrawable(logo_drawable_id));

        return root;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle("Campionato");
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });
    }
}


