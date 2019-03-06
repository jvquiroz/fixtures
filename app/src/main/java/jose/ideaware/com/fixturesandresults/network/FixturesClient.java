package jose.ideaware.com.fixturesandresults.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FixturesClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if(retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            retrofit =  new Retrofit.Builder()
                    .baseUrl(FixturesAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
