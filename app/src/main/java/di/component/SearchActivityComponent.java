package di.component;

import com.prueba.rappi.activities.SearchActivity;

import dagger.Component;
import di.scopes.ActivityScope;

@Component(dependencies = ApplicationComponent.class)
@ActivityScope
public interface SearchActivityComponent {

    void inject(SearchActivity searchActivity);

}
