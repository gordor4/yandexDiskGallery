package rus.ru.yandexapp.app;


import javax.inject.Singleton;
import dagger.Component;
import rus.ru.yandexapp.activity.InitActivity;
import rus.ru.yandexapp.activity.auth.AuthPresenter;
import rus.ru.yandexapp.activity.gallery.GalleryPresenter;
import rus.ru.yandexapp.activity.settings.SettingPresenter;
import rus.ru.yandexapp.activity.gallery.viewer.PhotoViewPresenter;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {

    void inject(InitActivity activity);

    void inject(GalleryPresenter presenter);

    void inject(PhotoViewPresenter presenter);

    void inject(SettingPresenter presenter);

    void inject(AuthPresenter presenter);

}