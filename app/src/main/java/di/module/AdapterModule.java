package di.module;

import com.prueba.rappi.activities.MainActivity;

import javax.inject.Named;

import adapters.MoviesAdapter;
import dagger.Module;
import dagger.Provides;
import di.scopes.ActivityScope;

@Module(includes = {MainActivityContextModule.class})
public class AdapterModule {

    @Provides
    @ActivityScope
    @Named("TopRated")
    public MoviesAdapter getMoviesLIstTopRated(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    @Named("Popular")
    public MoviesAdapter getMoviesLIstPopular(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    @Named("Upcoming")
    public MoviesAdapter getMoviesLIstUpcoming(MoviesAdapter.ClickListener clickListener) {
        return new MoviesAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    public MoviesAdapter.ClickListener getClickListenerMovie(MainActivity mainActivity) {
        return mainActivity;
    }
}
