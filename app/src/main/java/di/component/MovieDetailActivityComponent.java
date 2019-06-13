package di.component;

import android.content.Context;

import com.prueba.rappi.activities.MovieDetailActivity;

import dagger.Component;
import di.module.AdapterMovieModule;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@ActivityScope
@Component(modules = AdapterMovieModule.class, dependencies = ApplicationComponent.class)
public interface MovieDetailActivityComponent {

    @ActivityContext
    Context getContext();

    void inject (MovieDetailActivity mainActivity);
}
