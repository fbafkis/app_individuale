package com.francescobertamini.app_individuale.ui.championships;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorStandings;
import com.francescobertamini.app_individuale.ui.championships.championship_events.ChampionshipEventsActivity;
import com.francescobertamini.app_individuale.ui.championships.championship_partecipants.ChampionshipPartecipantsActivity;
import com.francescobertamini.app_individuale.ui.championships.championship_standings.ChampionshipStandingsActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipsDialog extends DialogFragment {
    public static final String TAG = "unsub_championship_dialog";
    private Toolbar toolbar;
    JsonArray championships;
    JsonObject championship;
    String id;
    Boolean sub;

    @BindView(R.id.champName)
    TextView _champName;
    @BindView(R.id.champId)
    TextView _champId;
    @BindView(R.id.champCar)
    TextView _champCar;
    @BindView(R.id.champFlags)
    TextView _champFlags;
    @BindView(R.id.champFuel)
    TextView _champFuel;
    @BindView(R.id.champTires)
    TextView _champTires;
    @BindView(R.id.champHelps)
    TextView _champHelps;
    @BindView(R.id.champLogo)
    ImageView _champLogo;
    @BindView(R.id.champCalendarButton)
    Button _champCalendarButton;
    @BindView(R.id.champRacersButton)
    Button _champRacersButton;
    @BindView(R.id.champResultsButton)
    Button _champResultsButton;
    @BindView(R.id.champStateChip)
    Chip _champStateChip;
    @BindView(R.id.champSubUnsubButton)
    FloatingActionButton _champSubUnsubButton;
    @BindView(R.id.champSubUnsubText)
    TextView _champSubUnsubText;

    public static ChampionshipsDialog display(FragmentManager fragmentManager, String champ_id, boolean sub) {
        ChampionshipsDialog dialog = new ChampionshipsDialog();
        dialog.id = champ_id;
        dialog.sub = sub;
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
        View root = inflater.inflate(R.layout.dialog_champ, container, false);
        ButterKnife.bind(this, root);
        toolbar = root.findViewById(R.id.champToolbar);
        JsonExtractorChampionships jsonExtractorChampionships = new JsonExtractorChampionships(this.getContext());
        try {
            championships = jsonExtractorChampionships.readJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < championships.size(); i++) {
            if (championships.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
                championship = championships.get(i).getAsJsonObject();
            }
        }
        _champName.setText(championship.get("nome").getAsString());
        _champId.setText(championship.get("id").getAsString());
        _champCar.setText(championship.get("lista-auto").getAsString());
        if (sub) {
            changeToSub();
        } else if (!sub) {
            changeToUnsub();
        }
        JsonArray settings = championship.get("impostazioni-gioco").getAsJsonArray();
        _champFlags.setText(settings.get(0).getAsJsonObject().get("valore").getAsString());
        _champFuel.setText(settings.get(1).getAsJsonObject().get("valore").getAsString());
        _champTires.setText(settings.get(2).getAsJsonObject().get("valore").getAsString());
        _champHelps.setText(settings.get(3).getAsJsonObject().get("valore").getAsString());
        String wrong_res_name = championship.get("logo").getAsString();
        wrong_res_name = wrong_res_name.replaceAll("-", "_");
        String logo_res = wrong_res_name.substring(0, wrong_res_name.indexOf("."));
        int logo_drawable_id = getContext().getResources().getIdentifier(logo_res, "drawable", getContext().getPackageName());
        _champLogo.setImageDrawable(getContext().getResources().getDrawable(logo_drawable_id));
        _champCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChampionshipEventsActivity.class);
                intent.putExtra("championship", championship.toString());
                startActivity(intent);
            }
        });
        _champRacersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChampionshipPartecipantsActivity.class);
                intent.putExtra("championship", championship.toString());
                startActivity(intent);
            }
        });
        _champResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject championshipStandings = null;
                JsonArray championshipsStandings = null;
                try {
                    championshipsStandings = new JsonExtractorStandings(getContext()).readJson();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < championshipsStandings.size(); i++) {
                    if (championshipsStandings.get(i).getAsJsonObject().get("id").getAsString().equals(id)) {
                        championshipStandings = championshipsStandings.get(i).getAsJsonObject();
                    }
                }
                Intent intent = new Intent(getContext(), ChampionshipStandingsActivity.class);
                intent.putExtra("championship", championshipStandings.toString());
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

    public void changeToSub() {
        _champStateChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#61ba22")));
        _champStateChip.setText("ISCRITTO");
        _champSubUnsubText.setText("Disiscriviti dal campionato");
        _champSubUnsubButton.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_outline_delete_forever_24, null));
        _champSubUnsubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unsub logic
            }
        });
    }

    public void changeToUnsub() {
        _champStateChip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#C33535")));
        _champStateChip.setText("NON ISCRITTO");
        _champSubUnsubText.setText("Iscriviti al campionato");
        _champSubUnsubButton.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.ic_baseline_add_24, null));
        _champSubUnsubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sub logic
            }
        });
    }
}


