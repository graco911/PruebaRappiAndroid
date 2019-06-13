package di.module;

import android.content.Context;

import com.prueba.rappi.activities.MovieDetailActivity;

import dagger.Module;
import dagger.Provides;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@Module
public class MovieDetailContextModule {
    private MovieDetailActivity movieDetailActivity;

    public Context context;

    public MovieDetailContextModule(MovieDetailActivity movieDetailActivity) {
        this.movieDetailActivity = movieDetailActivity;
        context = movieDetailActivity;
    }

    @Provides
    @ActivityScope
    public MovieDetailActivity providesMovideDetailActivity() {
        return movieDetailActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }
}
