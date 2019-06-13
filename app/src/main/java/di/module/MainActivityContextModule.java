package di.module;

import android.content.Context;

import com.prueba.rappi.activities.MainActivity;

import dagger.Module;
import dagger.Provides;
import di.qualifier.ActivityContext;
import di.scopes.ActivityScope;

@Module
public class MainActivityContextModule {
    private MainActivity mainActivity;

    public Context context;

    public MainActivityContextModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        context = mainActivity;
    }

    @Provides
    @ActivityScope
    public MainActivity providesMainActivity() {
        return mainActivity;
    }

    @Provides
    @ActivityScope
    @ActivityContext
    public Context provideContext() {
        return context;
    }

}