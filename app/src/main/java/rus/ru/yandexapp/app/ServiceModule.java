package rus.ru.yandexapp.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rus.ru.yandexapp.service.YandexDiskService;
import rus.ru.yandexapp.service.YandexDiskServiceImpl;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public YandexDiskService provideYandexDiskService(Context context) {

        return new YandexDiskServiceImpl(context);
    }


}
