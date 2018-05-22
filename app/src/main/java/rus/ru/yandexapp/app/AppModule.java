package rus.ru.yandexapp.app;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    protected Context appContext;

    public AppModule(Context appContext){
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return appContext;
    }

}