package di.component;

import android.content.Context;

import com.prueba.rappi.activities.MovieApplication;

import dagger.Component;
import di.module.ContextModule;
import di.module.RetrofitModule;
import di.qualifier.ApplicationContext;
import di.qualifier.di.scopes.ApplicationScope;
import interfaces.IServices;

@ApplicationScope
@Component(modules = {ContextModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    public IServices getApiInterface();

    @ApplicationContext
    public Context getContext();

    public void injectApplication(MovieApplication myApplication);
}