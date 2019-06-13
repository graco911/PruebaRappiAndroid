package com.prueba.rappi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.prueba.rappi.R;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;

import javax.inject.Inject;
import javax.inject.Named;

import adapters.MoviesAdapter;
import di.component.ApplicationComponent;
import di.component.DaggerMainActivityComponent;
import di.component.MainActivityComponent;
import di.module.MainActivityContextModule;
import di.qualifier.ActivityContext;
import di.qualifier.ApplicationContext;
import enumerators.EMovieType;
import helpers.Utils;
import interfaces.IServices;
import models.GetMovieResponseData;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MoviesAdapter.ClickListener {

    @Inject
    @Named("TopRated")
    public MoviesAdapter TopRatedAdapter;

    @Inject
    @Named("Popular")
    public MoviesAdapter PopularAdapter;

    @Inject
    @Named("Upcoming")
    public MoviesAdapter UpcomingAdapter;

    @Inject
    public IServices apiInterface;

    @Inject
    @ApplicationContext
    public Context mContext;

    @Inject
    @ActivityContext
    public Context activityContext;

    private boolean isbBusy;
    private int currentPageTopRated = 1;
    private int currentPageUpcoming = 1;
    private int currentPagePopular = 1;

    MainActivityComponent mainActivityComponent;

    private MultiSnapRecyclerView listPopular, listUpcoming, listTopRated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        ApplicationComponent applicationComponent = MovieApplication.get(this).getApplicationComponent();
        mainActivityComponent = DaggerMainActivityComponent.builder()
                .mainActivityContextModule(new MainActivityContextModule(this))
                .applicationComponent(applicationComponent)
                .build();

        mainActivityComponent.injectMainActivity(this);

        LinearLayoutManager layoutManagerPopular = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerTopRated = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerUpcoming = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        listPopular = findViewById(R.id.listPopularSnap);
        listPopular.setLayoutManager(layoutManagerPopular);

        listUpcoming = findViewById(R.id.listUpcomingSnap);
        listUpcoming.setLayoutManager(layoutManagerUpcoming);

        listTopRated = findViewById(R.id.listTopRatedSnap);
        listTopRated.setLayoutManager(layoutManagerTopRated);

        GetMovies(EMovieType.upcoming, UpcomingAdapter);
        GetMovies(EMovieType.popular, PopularAdapter);
        GetMovies(EMovieType.top_rated, TopRatedAdapter);

        listTopRated.setAdapter(TopRatedAdapter);
        listUpcoming.setAdapter(UpcomingAdapter);
        listPopular.setAdapter(PopularAdapter);

        SetScrollListener(listTopRated, layoutManagerTopRated, EMovieType.top_rated, TopRatedAdapter, currentPageTopRated);
        SetScrollListener(listPopular, layoutManagerPopular, EMovieType.popular, PopularAdapter, currentPagePopular);
        SetScrollListener(listUpcoming, layoutManagerUpcoming, EMovieType.upcoming, UpcomingAdapter, currentPageUpcoming);

    }

    private void SetScrollListener(MultiSnapRecyclerView list,
                                   final LinearLayoutManager lm,
                                   final EMovieType movietype,
                                   final MoviesAdapter adapter,
                                   final int pageList) {

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = lm.getItemCount();
                int visibleItemCount = lm.getChildCount();
                int firstVisibleItem = lm.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isbBusy) {
                        getMovies(pageList + 1, movietype, adapter);
                    }
                }

            }
        });

    }

    private void getMovies(final int page, EMovieType movietype, final MoviesAdapter adapt) {

        isbBusy = true;

        apiInterface.getMovies(movietype.toString(), Utils.API_KEY, Utils.LANGUAGE, page)
                .enqueue(new Callback<GetMovieResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response)
                    {
                        adapt.setData(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<GetMovieResponseData> call, Throwable t)
                    {

                    }
                });

    }

    private void GetMovies(EMovieType movieType, final MoviesAdapter adapter) {

        apiInterface.getMovies(movieType.toString(), Utils.API_KEY, Utils.LANGUAGE, Utils.PAGE)
                .enqueue(new Callback<GetMovieResponseData>() {
                    @Override
                    public void onResponse(Call<GetMovieResponseData> call, Response<GetMovieResponseData> response)
                    {
                        adapter.setData(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<GetMovieResponseData> call, Throwable t)
                    {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void launchIntent(int movieId)
    {
        Toast.makeText(mContext, "RecyclerView Row selected", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(activityContext, MovieDetailActivity.class).putExtra("MovieId", movieId));
    }
}
