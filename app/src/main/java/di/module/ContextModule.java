package di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import di.qualifier.ApplicationContext;
import di.qualifier.di.scopes.ApplicationScope;

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    public Context provideContext() {
        return context;
    }
}
