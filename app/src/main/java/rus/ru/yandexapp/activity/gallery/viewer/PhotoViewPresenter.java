package rus.ru.yandexapp.activity.gallery.viewer;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rus.ru.yandexapp.base.BasePresenter;
import rus.ru.yandexapp.model.FileURL;
import rus.ru.yandexapp.model.GalleryPhoto;
import rus.ru.yandexapp.model.PhotoList;
import rus.ru.yandexapp.service.YandexDiskService;
import rus.ru.yandexapp.utils.DownloadTask;

public class PhotoViewPresenter extends BasePresenter<PhotoViewInterface.View> implements PhotoViewInterface.Presenter {

    private PhotoList photoList;
    private Context context;

    private int count;
    private int index;
    private int callbackCount = 0;

    @Inject
    YandexDiskService yandexDiskService;


    public PhotoViewPresenter(PhotoViewInterface.View view, PhotoList photoList, PhotoViewActivity context, int index, int count) {
        super(view);

        this.photoList = photoList;
        this.context = context;
        this.index = index;
        this.count = count;
    }

    @Override
    protected void onStart() {

        final List<GalleryPhoto> galleryPhotoList = photoList.getPhotoList();

        for(final GalleryPhoto photo : galleryPhotoList) {

            yandexDiskService.getFileUrl(photo.getImagePath(), new Callback<FileURL>() {
                @Override
                public void onResponse(Call<FileURL> call, Response<FileURL> response) {
                    FileURL result = response.body();
                    if (result != null) {
                        photo.setDownloadUrl(result.getHref());

                        callbackCount++;

                        if(callbackCount == galleryPhotoList.size()) {
                            mView.setUrlList(galleryPhotoList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<FileURL> call, Throwable t) { }
            });
        }
    }



    @Override
    public void getUrl(final GalleryPhoto photo, Callback<FileURL> callback) {
        yandexDiskService.getFileUrl(photo.getImagePath(), callback);
    }

    @Override
    public void onDownloadFile() {
        GalleryPhoto photo = photoList.getPhotoList().get(index);

        String downloadUrl = photo.getDownloadUrl();
        String title = photo.getTitle();
        if(downloadUrl != null) {
            new DownloadTask(context, downloadUrl, title);
        } else {
            mView.showNetworkError();
        }
    }

    @Override
    public void setCurrentIndex(int index) {
        this.index = index;
    }

    @Override
    public int getCurrentIndex() {
        return index;
    }

    @Override
    public String getToken() {
        return yandexDiskService.getToken();
    }

    @Override
    public int getAllPhotoCount() {
        return count;
    }


}
