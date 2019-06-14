package di.module;

import android.content.Context;

import com.prueba.rappi.activities.FavoritesActivity;

import dagger.Module;
import dagger.Provides;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@Module
public class FavoriteContextModule {

    private FavoritesActivity favoritesActivity;

    public Context context;

    public FavoriteContextModule(FavoritesActivity favoritesActivity) {
        this.favoritesActivity = favoritesActivity;
        context = favoritesActivity;
    }

    @Provides
    @ActivityScope
    public FavoritesActivity providesFavorityActivity() {
        return favoritesActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}
