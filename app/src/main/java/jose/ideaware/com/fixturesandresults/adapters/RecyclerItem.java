package jose.ideaware.com.fixturesandresults.adapters;

public interface RecyclerItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FIXTURE = 1;

    int getRecyclerItemType();
}
