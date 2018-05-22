package rus.ru.yandexapp.activity.gallery;


import java.util.List;

import rus.ru.yandexapp.base.AbstractInterface;
import rus.ru.yandexapp.model.GalleryPhoto;

public interface GalleryInterface extends AbstractInterface {

    interface View extends AbstractInterface.View<Presenter> {
        void setImages(List<GalleryPhoto> photoList);

        void stopRefreshing();
    }

    interface Presenter extends AbstractInterface.Presenter {
        String getToken();

    }
}
