package rus.ru.yandexapp.activity.gallery.viewer;

import java.util.List;

import retrofit2.Callback;
import rus.ru.yandexapp.base.AbstractInterface;
import rus.ru.yandexapp.model.FileURL;
import rus.ru.yandexapp.model.GalleryPhoto;

public interface PhotoViewInterface extends AbstractInterface {

    interface View extends AbstractInterface.View<PhotoViewInterface.Presenter> {
        void setUrlList(List<GalleryPhoto> photoList);

        void addUrl(GalleryPhoto photo);
    }

    interface Presenter extends AbstractInterface.Presenter {
        void onDownloadFile();

        String getToken();

        int getAllPhotoCount();

        void setCurrentIndex(int index);

        int getCurrentIndex();

        void getUrl(final GalleryPhoto photo, Callback<FileURL> callback);

    }
}