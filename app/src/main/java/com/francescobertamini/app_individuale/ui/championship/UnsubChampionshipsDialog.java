package com.francescobertamini.app_individuale.ui.championship;

import android.app.Dialog;
import android.content.Intent;
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
import com.francescobertamini.app_individuale.data_managing.JsonExtractor;
import com.francescobertamini.app_individuale.ui.championship.champ_calendar.ChampionshipEventsActivity;
import com.francescobertamini.app_individuale.ui.championship.champ_partecipants.ChampionshipPartecipantsActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnsubChampionshipsDialog extends DialogFragment {

    public static final String TAG = "unsub_championship_dialog";
    private Toolbar toolbar;


    JsonArray championships;
    JsonObject championship;
    String id;

    @BindView(R.id.unsubChampName)
    TextView _unsubChampName;
    @BindView(R.id.unsubChampId)
    TextView _unsubChampId;
    @BindView(R.id.unsubChampCar)
    TextView _unsubChampCar;
    @BindView(R.id.unsubDialogFlags)
    TextView _unsubDialogFlags;
    @BindView(R.id.unsubDialogFuel)
    TextView _unsubDialogFuel;
    @BindView(R.id.unsubDialogTires)
    TextView _unsubDialogTires;
        @BindView(R.id.unsubDialogHelps)
    TextView _unsubDialogHelps;
    @BindView(R.id.unsubChampLogo)
    ImageView _unsubChampLogo;
    @BindView(R.id.unsubChampCalendarButton)
    Button _unsubChampCalendarButton;
    @BindView(R.id.unsubChampRacersButton)
    Button _unsubChampRacersButton;
    @BindView(R.id.unsubChampResultsButton)
    Button _unsubChampResultsButton;

    public static UnsubChampionshipsDialog display (FragmentManager fragmentManager, String champ_id){

        UnsubChampionshipsDialog dialog = new UnsubChampionshipsDialog();
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


        View root = inflater.inflate(R.layout.dialog_unsub_champ, container, false);
        ButterKnife.bind(this,root);

        toolbar = root.findViewById(R.id.unsub_champ_toolbar);


        JsonExtractor jsonExtractor = new JsonExtractor(this.getContext());

        try {
            championships = jsonExtractor.readJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i<championships.size();i++){

            if(championships.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
                championship = championships.get(i).getAsJsonObject();
            }
        }

        _unsubChampName.setText(championship.get("nome").getAsString());

        _unsubChampId.setText(championship.get("id").getAsString());

        _unsubChampCar.setText(championship.get("lista-auto").getAsString());

        JsonArray settings = championship.get("impostazioni-gioco").getAsJsonArray();

        _unsubDialogFlags.setText(settings.get(0).getAsJsonObject().get("valore").getAsString());
        _unsubDialogFuel.setText(settings.get(1).getAsJsonObject().get("valore").getAsString());
        _unsubDialogTires.setText(settings.get(2).getAsJsonObject().get("valore").getAsString());
        _unsubDialogHelps.setText(settings.get(3).getAsJsonObject().get("valore").getAsString());


        String wrong_res_name = championship.get("logo").getAsString();
        wrong_res_name =  wrong_res_name.replaceAll("-","_");

        String logo_res = wrong_res_name.substring(0,wrong_res_name.indexOf("."));

        int logo_drawable_id = getContext().getResources().getIdentifier( logo_res, "drawable", getContext().getPackageName());

        _unsubChampLogo.setImageDrawable(getContext().getResources().getDrawable(logo_drawable_id));

        _unsubChampCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChampionshipEventsActivity.class);
                intent.putExtra("championship", championship.toString());
                startActivity(intent);

            }
        });

        _unsubChampRacersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChampionshipPartecipantsActivity.class);
                intent.putExtra("championship", championship.toString());
                startActivity(intent);
            }
        });


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


