package di.module;

import android.support.v7.widget.RecyclerView;

import com.prueba.rappi.activities.MainActivity;
import com.prueba.rappi.activities.MovieDetailActivity;

import javax.inject.Named;

import adapters.MoviesAdapter;
import adapters.TrailersAdapter;
import dagger.Module;
import dagger.Provides;
import di.qualifier.di.scopes.ActivityScope;

@Module(includes = {MainActivityContextModule.class})
public class AdapterModule {

    @Provides
    @Named("TopRated")
    @ActivityScope
    public MoviesAdapter getMoviesLIstTopRated(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @Named("Popular")
    @ActivityScope
    public MoviesAdapter getMoviesLIstPopular(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @Named("Upcoming")
    @ActivityScope
    public MoviesAdapter getMoviesLIstUpcoming(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @Named("Trailers")
    @ActivityScope
    public TrailersAdapter getListTrailers(MoviesAdapter.ClickListener clickListener) {
        return new TrailersAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    public MoviesAdapter.ClickListener getClickListener(MainActivity mainActivity) {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    public TrailersAdapter.ClickListener getClickListener(MovieDetailActivity movieDetailActivity) {
        return movieDetailActivity;
    }
}
