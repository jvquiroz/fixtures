package jose.ideaware.com.fixturesandresults.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import jose.ideaware.com.fixturesandresults.R;
import jose.ideaware.com.fixturesandresults.adapters.FixturesAdapter;
import jose.ideaware.com.fixturesandresults.model.Fixture;
import jose.ideaware.com.fixturesandresults.network.FixturesAPI;
import jose.ideaware.com.fixturesandresults.network.FixturesClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixtureFragment extends Fragment{

    public static final int FIXTURES = 0;
    public static final int RESULTS = 1;

    private static final String KEY_CONTENT_TYPE = "type";

    private int type;
    private FixturesAPI fixturesAPI;
    private View loadingView;
    private View errorView;
    private RecyclerView recyclerView;
    private FixturesAdapter adapter;
    private int selectedFilterOption = -1;

    public FixtureFragment() {

    }

    public static final FixtureFragment newInstance(@ContentType int type) {
        FixtureFragment fragment = new FixtureFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(KEY_CONTENT_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(KEY_CONTENT_TYPE);
        fixturesAPI = FixturesClient.getClient().create(FixturesAPI.class);
    }

    private void loadData() {
        loadingView.setVisibility(View.VISIBLE);
        if (type == FIXTURES) {
            fixturesAPI.loadFixtures().enqueue(answerCallback);
        } else {
            fixturesAPI.loadResults().enqueue(answerCallback);
        }
    }

    Callback<List<Fixture>> answerCallback = new Callback<List<Fixture>>() {
        @Override
        public void onResponse(Call<List<Fixture>> call, Response<List<Fixture>> response) {
            loadingView.setVisibility(View.GONE);
            if (response.isSuccessful()) {
                adapter.setFixtures(response.body());
            } else {
                displayError();
            }
        }

        @Override
        public void onFailure(Call<List<Fixture>> call, Throwable t) {
            loadingView.setVisibility(View.GONE);
            displayError();
        }
    };

    private void displayError() {
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new FixturesAdapter();
        loadingView = rootView.findViewById(R.id.loading_view);
        errorView = rootView.findViewById(R.id.error_view);
        rootView.findViewById(R.id.error_action_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorView.setVisibility(View.GONE);
                loadData();
            }
        });
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = adapter.getCompetitions().toArray(new CharSequence[adapter.getCompetitions().size()]);
                final AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),  R.style.MyAlertDialogStyle);
                builder.setTitle(R.string.filter_title);
                builder.setSingleChoiceItems(options, selectedFilterOption,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectedFilterOption = i;
                                dialogInterface.dismiss();
                                adapter.filterResults(options[i]);
                            }
                        });
                builder.setNegativeButton(R.string.filter_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.clear_filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        selectedFilterOption = -1;
                        adapter.filterResults(null);
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        loadData();
        return rootView;
    }

    @IntDef({FIXTURES, RESULTS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentType {}
}
