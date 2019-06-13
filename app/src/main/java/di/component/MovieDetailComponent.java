package di.component;

import com.prueba.rappi.activities.MovieDetailActivity;

import dagger.Component;
import di.qualifier.di.scopes.ActivityScope;

@Component(dependencies = ApplicationComponent.class)
@ActivityScope
public interface MovieDetailComponent {
    void inject(MovieDetailActivity detailActivity);
}
