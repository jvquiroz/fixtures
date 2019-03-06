package jose.ideaware.com.fixturesandresults.model;

import java.util.Date;

import jose.ideaware.com.fixturesandresults.adapters.RecyclerItem;


public class Fixture implements RecyclerItem, Comparable<Fixture> {
    private int id;
    private String type;
    private Team homeTeam;
    private Team awayTeam;
    private Date date;
    private CompetitionStage competitionStage;
    private Competition venue;
    private String state;
    private Score score;

    public String getType() {
        return type;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Date getDate() {
        return date;
    }

    public CompetitionStage getCompetitionStage() {
        return competitionStage;
    }

    public Competition getVenue() {
        return venue;
    }

    public String getState() {
        return state;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public int getRecyclerItemType() {
        return TYPE_FIXTURE;
    }

    @Override
    public int compareTo(Fixture fixture) {
        return date.compareTo(fixture.date);
    }
}
