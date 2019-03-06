package jose.ideaware.com.fixturesandresults.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jose.ideaware.com.fixturesandresults.R;
import jose.ideaware.com.fixturesandresults.model.Fixture;
import jose.ideaware.com.fixturesandresults.model.MonthHeader;

public class FixturesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Fixture> fixtures;
    private List<RecyclerItem> fixturesByMonth;
    private HashMap<Integer, String> competitionMap;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View viewItem;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if(viewType == RecyclerItem.TYPE_FIXTURE) {
            viewItem = inflater.inflate(R.layout.fixture_item, viewGroup, false);
            return new FixtureViewHolder(viewItem);
        } else {
            viewItem = inflater.inflate(R.layout.fixture_header, viewGroup, false);
            return new HeaderViewHolder(viewItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Context context = viewHolder.itemView.getContext();
        if (viewHolder.getItemViewType() == RecyclerItem.TYPE_FIXTURE) {
            Fixture fixture = (Fixture) fixturesByMonth.get(i);
            FixtureViewHolder fixtureviewHolder = (FixtureViewHolder)viewHolder;
            fixtureviewHolder.league.setText(fixture.getCompetitionStage().getCompetition().getName());
            fixtureviewHolder.venue.setText(context.getString(R.string.fixture_venue,fixture.getVenue().getName()));
            fixtureviewHolder.date.setText(new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm").format(fixture.getDate()));
            fixtureviewHolder.homeTeam.setText(fixture.getHomeTeam().getName());
            fixtureviewHolder.awayTeam.setText(fixture.getAwayTeam().getName());
            fixtureviewHolder.dayNumber.setText(new SimpleDateFormat("EEE").format(fixture.getDate()));
            fixtureviewHolder.dayName.setText(new SimpleDateFormat("dd").format(fixture.getDate()));

            fixtureviewHolder.dayNumber.setVisibility(View.VISIBLE);
            fixtureviewHolder.dayName.setVisibility(View.VISIBLE);
            fixtureviewHolder.status.setVisibility(View.GONE);
            fixtureviewHolder.scoreHome.setVisibility(View.GONE);
            fixtureviewHolder.scoreAway.setVisibility(View.GONE);
            fixtureviewHolder.date.setTextColor(context.getResources().getColor(R.color.dividerGray));
            fixtureviewHolder.scoreHome.setTextColor(context.getResources().getColor(R.color.textDarkPurple));
            fixtureviewHolder.scoreAway.setTextColor(context.getResources().getColor(R.color.textDarkPurple));

            if (!TextUtils.isEmpty(fixture.getState()) && fixture.getState().equals("postponed")) {
                fixtureviewHolder.status.setText(fixture.getState());
                fixtureviewHolder.status.setVisibility(View.VISIBLE);
                fixtureviewHolder.date.setTextColor(context.getResources().getColor(R.color.orange));
            } else if (!TextUtils.isEmpty(fixture.getState()) && fixture.getState().equals("finished")) {
                fixtureviewHolder.dayNumber.setVisibility(View.GONE);
                fixtureviewHolder.dayName.setVisibility(View.GONE);
                fixtureviewHolder.scoreHome.setVisibility(View.VISIBLE);
                fixtureviewHolder.scoreAway.setVisibility(View.VISIBLE);
                fixtureviewHolder.scoreHome.setText(String.valueOf(fixture.getScore().getHome()));
                fixtureviewHolder.scoreAway.setText(String.valueOf(fixture.getScore().getAway()));
                if (fixture.getScore().getAway() > fixture.getScore().getHome()) {
                    fixtureviewHolder.scoreAway.setTextColor(context.getResources().getColor(R.color.blueWinner));
                } else if (fixture.getScore().getAway() < fixture.getScore().getHome()) {
                    fixtureviewHolder.scoreHome.setTextColor(context.getResources().getColor(R.color.blueWinner));
                }
            }
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            headerViewHolder.title.setText(((MonthHeader)fixturesByMonth.get(i)).getHeaderTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return fixturesByMonth.get(position).getRecyclerItemType();
    }

    @Override
    public int getItemCount() {
        return fixturesByMonth != null ? fixturesByMonth.size() : 0;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
        processData(null);
    }

    private void processData(CharSequence filter) {
        Collections.sort(fixtures);
        HashMap<String, List<Fixture>> hashMap = new HashMap<>();
        competitionMap = new HashMap<>();
        fixturesByMonth = new ArrayList<>();
        for (Fixture fixture : fixtures) {
            if (filter == null || (filter != null && fixture.getCompetitionStage().getCompetition().getName().equals(filter))) {
                String key = new SimpleDateFormat("MMMM yyyy").format(fixture.getDate());
                if (hashMap.containsKey(key)) {
                    hashMap.get(key).add(fixture);
                } else {
                    List<Fixture> list = new ArrayList<>(Arrays.asList(fixture));
                    hashMap.put(key, list);
                }
            }

            int competitionKey = fixture.getCompetitionStage().getCompetition().getId();
            if (!competitionMap.containsKey(competitionKey)) {
                competitionMap.put(competitionKey, fixture.getCompetitionStage().getCompetition().getName());
            }
        }

        for (String title : hashMap.keySet()) {
            fixturesByMonth.add(new MonthHeader(title));
            fixturesByMonth.addAll(hashMap.get(title));
        }
        notifyDataSetChanged();
    }

    public Collection<String> getCompetitions() {
        if (competitionMap != null) {
            return competitionMap.values();
        } else {
            return new ArrayList<>();
        }
    }

    public void filterResults(CharSequence filter) {
        processData(filter);
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder {

        private TextView league;
        private TextView venue;
        private TextView date;
        private TextView homeTeam;
        private TextView awayTeam;
        private TextView dayNumber;
        private TextView dayName;
        private TextView status;
        private TextView scoreHome;
        private TextView scoreAway;


        public FixtureViewHolder(@NonNull View itemView) {
            super(itemView);
            league = itemView.findViewById(R.id.league_tv);
            venue = itemView.findViewById(R.id.venue_tv);
            date = itemView.findViewById(R.id.date_tv);
            homeTeam = itemView.findViewById(R.id.home_team_tv);
            awayTeam = itemView.findViewById(R.id.away_team_tv);
            dayName = itemView.findViewById(R.id.day_number_tv);
            dayNumber = itemView.findViewById(R.id.day_name_tv);
            status = itemView.findViewById(R.id.status_tv);
            scoreAway = itemView.findViewById(R.id.away_score_tv);
            scoreHome = itemView.findViewById(R.id.home_score_tv);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView;
        }
    }
}
