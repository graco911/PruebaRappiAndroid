package di.module;

import com.prueba.rappi.activities.FavoritesActivity;
import com.prueba.rappi.activities.MovieDetailActivity;

import javax.inject.Named;

import adapters.FavoriteAdapter;
import adapters.MoviesAdapter;
import dagger.Module;
import dagger.Provides;
import di.scopes.ActivityScope;

@Module(includes = FavoriteContextModule.class)
public class AdapterFavoritesModule {

    @Provides
    @ActivityScope
    @Named("Favorite")
    public FavoriteAdapter getMoviesLIstUpcoming(FavoriteAdapter.ClickListener clickListener) {
        return new FavoriteAdapter(clickListener);
    }

    @Provides
    @ActivityScope
    public FavoriteAdapter.ClickListener getClickListenerMovie(FavoritesActivity favoritesActivity) {
        return favoritesActivity;
    }

}
