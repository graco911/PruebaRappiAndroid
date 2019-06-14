package di.module;

import com.prueba.rappi.activities.MovieDetailActivity;

import javax.inject.Named;

import adapters.MoviesAdapter;
import adapters.TrailersAdapter;
import dagger.Module;
import dagger.Provides;
import di.scopes.ActivityScope;

@Module(includes = {MovieDetailContextModule.class})
public class AdapterMovieModule {
    @Provides
    @ActivityScope
    @Named("Trailers")
    public TrailersAdapter getListTrailers(TrailersAdapter.ClickListener clickListener) {
        return new TrailersAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    public TrailersAdapter.ClickListener getClickListenerTrailer(MovieDetailActivity movieDetailActivity) {
        return movieDetailActivity;
    }

    @Provides
    @ActivityScope
    @Named("Similar")
    public MoviesAdapter getMoviesLIstUpcoming(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    public MoviesAdapter.ClickListener getClickListenerMovie(MovieDetailActivity movieDetailActivity) {
        return movieDetailActivity;
    }
}
