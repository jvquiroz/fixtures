package jose.ideaware.com.fixturesandresults.model;

import jose.ideaware.com.fixturesandresults.adapters.RecyclerItem;

public class MonthHeader implements RecyclerItem {
    private String headerTitle;

    public MonthHeader(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    @Override
    public int getRecyclerItemType() {
        return TYPE_HEADER;
    }
}
