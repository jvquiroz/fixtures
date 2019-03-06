package jose.ideaware.com.fixturesandresults.network;

import java.util.List;

import jose.ideaware.com.fixturesandresults.model.Fixture;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FixturesAPI {

    String BASE_URL = "http://storage.googleapis.com/";

    @GET("cdn-og-test-api/test-task/results.json")
    Call<List<Fixture>> loadResults();

    @GET("cdn-og-test-api/test-task/fixtures.json")
    Call<List<Fixture>> loadFixtures();
}
