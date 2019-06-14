package di.component;

import android.content.Context;

import com.prueba.rappi.activities.FavoritesActivity;

import dagger.Component;
import di.module.AdapterFavoritesModule;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@ActivityScope
@Component(modules = AdapterFavoritesModule.class, dependencies = ApplicationComponent.class)
public interface FavoriteActivityComponent {

    @ActivityContext
    Context getContext();

    void inject(FavoritesActivity favoritesActivity);
}
