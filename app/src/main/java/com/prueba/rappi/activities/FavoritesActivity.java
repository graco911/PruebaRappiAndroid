package com.prueba.rappi.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.prueba.rappi.R;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import javax.inject.Inject;
import javax.inject.Named;

import adapters.FavoriteAdapter;
import adapters.MoviesAdapter;
import di.component.ApplicationComponent;
import di.component.DaggerFavoriteActivityComponent;
import di.component.FavoriteActivityComponent;
import di.module.FavoriteContextModule;
import di.qualifier.ActivityContext;
import di.qualifier.ApplicationContext;
import dmax.dialog.SpotsDialog;
import helpers.Utils;
import interfaces.IServices;
import models.GetFavoritesMoviesResponseData;
import models.GetMovieResponseData;
import models.GetResponseUserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.ClickListener {

    @Inject
    @Named("Favorite")
    public FavoriteAdapter favoriteAdapter;

    @Inject
    public IServices apiInterface;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;
    private FavoriteActivityComponent favoriteActivityComponent;

    private MultiSnapRecyclerView listFavorites;
    private Gson gson;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favoritos");

        listFavorites = (MultiSnapRecyclerView) findViewById(R.id.listFavorites);

        Utils.GetPrefsInstance(this);

        ApplicationComponent applicationComponent = MovieApplication.get(this).getApplicationComponent();
        favoriteActivityComponent = DaggerFavoriteActivityComponent.builder()
                .favoriteContextModule(new FavoriteContextModule(this))
                .applicationComponent(applicationComponent)
                .build();

        favoriteActivityComponent.inject(this);
        gson = new Gson();
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere por favor....")
                .setCancelable(false)
                .build();

        LinearLayoutManager layoutManagerFavorites = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listFavorites.setLayoutManager(layoutManagerFavorites);
        listFavorites.setAdapter(favoriteAdapter);

        GetFavorites();
    }

    private void GetFavorites()
    {
        dialog.show();
        if (!Prefs.getString("UserData", "").isEmpty())
        {
            GetResponseUserData userData = gson.fromJson(Prefs.getString("UserData", ""), GetResponseUserData.class);
            String sessionId = Prefs.getString("UserSession", "");

            apiInterface.getFavorites(String.valueOf(userData.getId()), Utils.API_KEY, sessionId, Utils.LANGUAGE, Utils.SORT_BY_ASC)
                    .enqueue(new Callback<GetMovieResponseData>() {
                        @Override
                        public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response) {
                            favoriteAdapter.setData(response.body().getResults());
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<GetMovieResponseData> call, Throwable t)
                        {
                            dialog.dismiss();
                        }
                    });
        }

    }

    @Override
    public void onBackPressed()
    {
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void launchIntent(int movieId, ImageView movieImage) {

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MovieId", movieId);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, movieImage, "movieDetail");
        startActivity(intent, options.toBundle());

    }
}
