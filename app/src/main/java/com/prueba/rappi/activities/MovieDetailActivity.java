package com.prueba.rappi.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prueba.rappi.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import di.component.ApplicationComponent;
import di.component.DaggerMovieDetailComponent;
import di.component.MovieDetailComponent;
import di.qualifier.ApplicationContext;
import helpers.Utils;
import interfaces.IServices;
import models.GetMovieDetailResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private Toolbar appbar;
    private ImageView imgToolbar;
    private ImageView imageViewPoster;
    private TextView textTitle;
    private TextView textYear;
    private AppCompatRatingBar ratingMovie;
    private TextView textDescription;

    MovieDetailComponent movieDetailComponent;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    public IServices apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        appbar = (Toolbar) findViewById(R.id.appbar);
        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        imageViewPoster = (ImageView) findViewById(R.id.imageViewPoster);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textYear = (TextView) findViewById(R.id.textYear);
        ratingMovie = (AppCompatRatingBar) findViewById(R.id.ratingMovie);
        textDescription = (TextView) findViewById(R.id.textDescription);

        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int movieid = getIntent().getIntExtra("MovieId", 0);

        ApplicationComponent applicationComponent = MovieApplication.get(this).getApplicationComponent();
        movieDetailComponent = DaggerMovieDetailComponent.builder()
                .applicationComponent(applicationComponent)
                .build();

        movieDetailComponent.inject(this);

        GetMovieDetail(movieid);
    }

    private void GetMovieDetail(final int id) {

        apiInterface.getMovieDetail(String.format("%s",id), Utils.API_KEY, Utils.LANGUAGE)
                .enqueue(new Callback<GetMovieDetailResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieDetailResponseData> call, Response<GetMovieDetailResponseData> response) {

                        GetMovieDetailResponseData item = response.body();

                        if (!item.getPosterPath().isEmpty()){
                            String urlMoviePoster = String.format("https://image.tmdb.org/t/p/w500/%s", item.getPosterPath());
                            Picasso.with(getApplicationContext()).load(urlMoviePoster).into(imageViewPoster);
                        }else {
                            imageViewPoster.setVisibility(View.INVISIBLE);
                        }

                        if (!item.getBackdropPath().isEmpty()){
                            String urlMovieBack = String.format("https://image.tmdb.org/t/p/w500/%s", item.getBackdropPath());
                            Picasso.with(getApplicationContext()).load(urlMovieBack).into(imgToolbar);
                        }

                        if (!item.getTitle().isEmpty()){
                            textTitle.setText(response.body().getTitle());
                        }

                        if (!item.getReleaseDate().isEmpty()){
                            try {
                                textYear.setText(new SimpleDateFormat("yyyy").format(Utils.GetDateFormat("yyyy").parse(item.getReleaseDate())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (!item.getOverview().isEmpty()){
                            textDescription.setText(item.getOverview());
                        }else{
                            textDescription.setText("Sin descripción.");
                        }

                        if (item.getVoteAverage() > 0){
                            ratingMovie.setRating(Float.parseFloat(item.getVoteAverage().toString()) * .5f);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetMovieDetailResponseData> call, Throwable t) {

                    }
                });
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
}
