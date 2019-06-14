package com.prueba.rappi.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.prueba.rappi.R;
import com.squareup.picasso.Picasso;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.inject.Named;

import adapters.MoviesAdapter;
import adapters.TrailersAdapter;
import di.component.ApplicationComponent;
import di.component.DaggerMovieDetailActivityComponent;
import di.component.MovieDetailActivityComponent;
import di.module.MovieDetailContextModule;
import di.qualifier.ActivityContext;
import di.qualifier.ApplicationContext;
import helpers.Utils;
import interfaces.IServices;
import models.GetAddFavoritesResponseData;
import models.GetMovieDetailResponseData;
import models.GetMovieResponseData;
import models.GetResponseUserData;
import models.GetTrailersResponseData;
import models.SetFavoriteMovieData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.ClickListener, MoviesAdapter.ClickListener {

    private Toolbar appbar;
    private ImageView imgToolbar;
    private ImageView imageViewPoster;
    private TextView textTitle;
    private TextView textYear;
    private TextView textGenres;
    private AppCompatRatingBar ratingMovie;
    private TextView textDescription;
    private MultiSnapRecyclerView listTrailers, listSimilar;
    private FloatingActionButton btnFab;

    @Inject
    @Named("Similar")
    public MoviesAdapter similarAdapter;

    @Inject
    @Named("Trailers")
    public TrailersAdapter trailersAdapter;

    @Inject
    public IServices apiInterface;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;

    private MovieDetailActivityComponent movieDetailActivityComponent;
    private int page = 1;
    private boolean isbBusy;
    private LinearLayoutManager layoutManagerSimilar;
    private int movieid;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        imageViewPoster = (ImageView) findViewById(R.id.imageViewPoster);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textYear = (TextView) findViewById(R.id.textYear);
        ratingMovie = (AppCompatRatingBar) findViewById(R.id.ratingMovie);
        textDescription = (TextView) findViewById(R.id.textDescription);
        listTrailers = (MultiSnapRecyclerView) findViewById(R.id.listTrailers);
        listSimilar = (MultiSnapRecyclerView) findViewById(R.id.listSimilars);
        btnFab = (FloatingActionButton) findViewById(R.id.btnFab);
        textGenres = (TextView) findViewById(R.id.textGenres);

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        movieid = getIntent().getIntExtra("MovieId", 0);

        ApplicationComponent applicationComponent = MovieApplication.get(this).getApplicationComponent();
        movieDetailActivityComponent = DaggerMovieDetailActivityComponent.builder()
                .movieDetailContextModule(new MovieDetailContextModule(this))
                .applicationComponent(applicationComponent).build();

        movieDetailActivityComponent.inject(this);

        Utils.GetPrefsInstance(this);
        gson = new Gson();

        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (!Prefs.getString("UserData", "").isEmpty()){

                    GetResponseUserData userData = gson.fromJson(Prefs.getString("UserData", ""), GetResponseUserData.class);
                    String sessionId = Prefs.getString("UserSession", "");

                    SetFavoriteMovieData movieData = new SetFavoriteMovieData();
                    movieData.setFavorite(true);
                    movieData.setMediaId(movieid);
                    movieData.setMediaType("movie");
                    apiInterface.setFavorite(String.valueOf(userData.getId()), Utils.API_KEY, sessionId, movieData)
                            .enqueue(new Callback<GetAddFavoritesResponseData>() {
                                @Override
                                public void onResponse(Call<GetAddFavoritesResponseData> call, Response<GetAddFavoritesResponseData> response)
                                {
                                    Snackbar.make(view, response.body().getStatusMessage(), Snackbar.LENGTH_LONG)
                                            .setAction("Aceptar", null).show();
                                }

                                @Override
                                public void onFailure(Call<GetAddFavoritesResponseData> call, Throwable t)
                                {
                                    if (t.getMessage().isEmpty())
                                    {

                                    }

                                }
                            });
                }
            }
        });

        LinearLayoutManager layoutManagerTrailers = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listTrailers.setLayoutManager(layoutManagerTrailers);
        listTrailers.setAdapter(trailersAdapter);

        layoutManagerSimilar = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listSimilar = findViewById(R.id.listSimilars);
        listSimilar.setLayoutManager(layoutManagerSimilar);
        listSimilar.setAdapter(similarAdapter);

        SetScrollListener();

        GetMovieDetail(movieid);

    }

    private void SetScrollListener() {

        listSimilar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = layoutManagerSimilar.getItemCount();
                int visibleItemCount = layoutManagerSimilar.getChildCount();
                int firstVisibleItem = layoutManagerSimilar.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isbBusy) {
                        GetSimilarMovies(page + 1);
                    }
                }
            }
        });

    }

    private void GetSimilarMovies(int page) {

        isbBusy = true;

        apiInterface.getSimilars(String.valueOf(movieid), Utils.API_KEY, Utils.LANGUAGE, page)
                .enqueue(new Callback<GetMovieResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response)
                    {
                        similarAdapter.setData(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<GetMovieResponseData> call, Throwable t)
                    {

                    }
                });

    }

    private void GetMovieSimilar(int movieid) {

        isbBusy = true;

        apiInterface.getSimilars(String.valueOf(movieid), Utils.API_KEY, Utils.LANGUAGE, page)
                .enqueue(new Callback<GetMovieResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response)
                    {
                        similarAdapter.setData(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<GetMovieResponseData> call, Throwable t)
                    {

                    }
                });

    }

    private void GetTrailers(final int id)
    {

        apiInterface.getTrailers(String.format("%s", id), Utils.API_KEY, "en-US")
                .enqueue(new Callback<GetTrailersResponseData>() {
                    @Override
                    public void onResponse(Call<GetTrailersResponseData> call, Response<GetTrailersResponseData> response)
                    {
                        if (response.body().getResults().size() > 0){
                            trailersAdapter.setData(response.body().getResults());
                        }

                        GetMovieSimilar(id);

                    }

                    @Override
                    public void onFailure(Call<GetTrailersResponseData> call, Throwable t) {

                    }
                });

    }

    private void GetMovieDetail(final int id)
    {

        apiInterface.getMovieDetail(String.format("%s", id), Utils.API_KEY, Utils.LANGUAGE)
                .enqueue(new Callback<GetMovieDetailResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieDetailResponseData> call, Response<GetMovieDetailResponseData> response) {

                        GetMovieDetailResponseData item = response.body();

                        getSupportActionBar().setTitle(item.getTitle());

                        if (!item.getPosterPath().isEmpty()) {
                            String urlMoviePoster = String.format("https://image.tmdb.org/t/p/w500/%s", item.getPosterPath());
                            Picasso.with(getApplicationContext()).load(urlMoviePoster).into(imageViewPoster);
                        } else {
                            imageViewPoster.setVisibility(View.INVISIBLE);
                        }

                        if (!item.getBackdropPath().isEmpty()) {
                            String urlMovieBack = String.format("https://image.tmdb.org/t/p/w500/%s", item.getBackdropPath());
                            Picasso.with(getApplicationContext()).load(urlMovieBack).into(imgToolbar);
                        }

                        if (!item.getTitle().isEmpty()) {
                            textTitle.setText(response.body().getTitle());
                        }

                        if (!item.getReleaseDate().isEmpty()) {
                            try {
                                textYear.setText(new SimpleDateFormat("yyyy").format(Utils.GetDateFormat("yyyy").parse(item.getReleaseDate())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (!item.getOverview().isEmpty()) {
                            textDescription.setText(item.getOverview());
                        } else {
                            textDescription.setText("Sin descripción.");
                        }

                        if (item.getVoteAverage() > 0) {
                            ratingMovie.setRating(Float.parseFloat(item.getVoteAverage().toString()) * .5f);
                        }
                        if (item.getGenres().size() > 0){
                            StringBuilder genres = new StringBuilder("");

                            int count = 0;

                            for (GetMovieDetailResponseData.Genre genre: item.getGenres())
                            {
                                count++;
                                genres.append(genre.getName());

                                if (count < item.getGenres().size()){
                                    genres.append(",");
                                }
                            }

                            textGenres.setText(genres);
                        }

                        GetTrailers(item.getId());
                    }

                    @Override
                    public void onFailure(Call<GetMovieDetailResponseData> call, Throwable t)
                    {

                    }
                });
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void launchIntent(String movieKey) {

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("https://www.youtube.com/watch?v=%s", movieKey)));
        try
        {
            MovieDetailActivity.this.startActivity(webIntent);
        } catch (ActivityNotFoundException ex)
        {

        }
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
