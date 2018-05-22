package rus.ru.yandexapp;

import android.app.Application;

import rus.ru.yandexapp.app.AppComponent;
import rus.ru.yandexapp.app.AppModule;
import rus.ru.yandexapp.app.DaggerAppComponent;
import rus.ru.yandexapp.app.ServiceModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
