package com.prueba.rappi.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.prueba.rappi.R;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import javax.inject.Inject;

import adapters.FavoriteAdapter;
import di.component.ApplicationComponent;
import di.component.DaggerSearchActivityComponent;
import di.component.SearchActivityComponent;
import di.qualifier.ApplicationContext;
import dmax.dialog.SpotsDialog;
import helpers.Utils;
import interfaces.IServices;
import models.GetMovieResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, FavoriteAdapter.ClickListener {

    @Inject
    public IServices apiInterface;

    @Inject
    @ApplicationContext
    public Context mContext;

    public FavoriteAdapter resultAdapter;

    private Toolbar toolbar;
    private RadioGroup radioSearch;
    private MultiSnapRecyclerView listResults;
    private SearchActivityComponent searchActivityComponent;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(null);

        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        radioSearch = findViewById(R.id.radioSearch);
        listResults = findViewById(R.id.listResults);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        ApplicationComponent applicationComponent = MovieApplication.get(this).getApplicationComponent();

        searchActivityComponent = DaggerSearchActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .build();

        searchActivityComponent.inject(this);

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere por favor....")
                .setCancelable(false)
                .build();

        resultAdapter = new FavoriteAdapter(this);
        LinearLayoutManager layoutManagerResult = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listResults.setLayoutManager(layoutManagerResult);
        listResults.setAdapter(resultAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.performClick();

        mSearchView.setQueryHint("Realizar una busqueda");

        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        if (Utils.isOnline(this)){

            dialog.show();
            apiInterface.searchMovie(Utils.API_KEY, Utils.LANGUAGE, s, Utils.PAGE, false)
                    .enqueue(new Callback<GetMovieResponseData>() {
                        @Override
                        public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response) {

                            if (response.body().getResults().size() > 0){
                                resultAdapter.setData(response.body().getResults());
                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Sin resultados.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<GetMovieResponseData> call, Throwable t) {

                            dialog.dismiss();
                            Snackbar.make(getWindow().getDecorView().getRootView(), "Sin resultados.", Snackbar.LENGTH_LONG)
                                    .setAction("Aceptar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    }).show();
                        }
                    });

        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void launchIntent(int movieId, ImageView movieImage) {

    }
}