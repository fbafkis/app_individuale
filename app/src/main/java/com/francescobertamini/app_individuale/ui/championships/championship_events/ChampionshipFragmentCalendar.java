package com.francescobertamini.app_individuale.ui.championships.championship_events;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonParser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChampionshipFragmentCalendar extends Fragment {

    JsonObject championship;
    JsonArray events;
    CalendarView calendarView;
    CalendarEventAdapter adapter;

    @BindView(R.id.champCalendarViewDay)
    CalendarView _champCalendarViewDay;
    @BindView(R.id.champCalendarViewNight)
    CalendarView _champCalendarViewNight;
    @BindView(R.id.champEventsCalRecycler)
    RecyclerView _eventsRecyclerView;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_champ_events_calendar, null);

        ButterKnife.bind(this, root);

        calendarView=_champCalendarViewDay;

        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

            _champCalendarViewDay.setVisibility(View.GONE);
            calendarView=_champCalendarViewNight;
            calendarView.setVisibility(View.VISIBLE);
        }


        Bundle args = getArguments();
        championship = (JsonObject) JsonParser.parseString(args.getString("championship"));

        Log.e("Campionato", championship.toString());

        events = championship.get("calendario").getAsJsonArray();

        List<EventDay> eventDayList = new ArrayList<EventDay>();

        for (int i = 0; i < events.size(); i++) {

            JsonObject event = events.get(i).getAsJsonObject();
            String dateString = event.get("data").getAsString();

            Log.e("Date", dateString);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            EventDay eventDay = new EventDay(calendar, R.drawable.ic_baseline_directions_car_24);

            eventDayList.add(eventDay);

        }



        calendarView.setEvents(eventDayList);




        calendarView.setOnDayClickListener(eventDay -> {

            SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = DateFor.format(eventDay.getCalendar().getTime());

            JsonArray todayEvents = new JsonArray();

            for (int i = 0; i < events.size(); i++) {

                Log.e("Date", events.get(i).toString());

                if (events.get(i).getAsJsonObject().get("data").getAsString().equals(stringDate)) {

                    todayEvents.add(events.get(i).getAsJsonObject());

                }

            }



            adapter = new CalendarEventAdapter(todayEvents, getActivity());

            _eventsRecyclerView.setAdapter(adapter);
            _eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        });


        return root;
    }


}
