package di.module;

import dagger.Module;
import dagger.Provides;
import di.qualifier.di.scopes.ApplicationScope;
import helpers.Utils;
import interfaces.IServices;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @Provides
    @ApplicationScope
    IServices getApiInterface(Retrofit retroFit) {
        return retroFit.create(IServices.class);
    }

    @Provides
    @ApplicationScope
    Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Utils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
