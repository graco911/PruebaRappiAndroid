package di.component;

import android.content.Context;

import com.prueba.rappi.activities.MainActivity;

import dagger.Component;
import di.module.AdapterModule;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@ActivityScope
@Component(modules = AdapterModule.class, dependencies = ApplicationComponent.class)
public interface MainActivityComponent {

    @ActivityContext
    Context getContext();

    void injectMainActivity(MainActivity mainActivity);
}