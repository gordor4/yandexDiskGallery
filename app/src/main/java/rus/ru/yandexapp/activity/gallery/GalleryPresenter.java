package rus.ru.yandexapp.activity.gallery;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.Resource;
import com.yandex.disk.rest.json.ResourceList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rus.ru.yandexapp.R;
import rus.ru.yandexapp.base.BasePresenter;
import rus.ru.yandexapp.model.GalleryPhoto;
import rus.ru.yandexapp.service.YandexDiskService;

public class GalleryPresenter extends BasePresenter<GalleryInterface.View> implements GalleryInterface.Presenter {

    @Inject
    YandexDiskService yandexDiskService;

    @Inject
    Context context;

    private FileTask task;

    public GalleryPresenter(GalleryInterface.View view) {
        super(view);
    }

    @Override
    protected void onStart() {
        if(task == null) {
            task = new FileTask();
            task.execute("disk:/images");

        } else {
            task.cancel(false);
            task = new FileTask();
            task.execute("disk:/images");
        }
    }

    @Override
    public String getToken() {
        return yandexDiskService.getToken();
    }


    class FileTask extends AsyncTask<String, Void, ResourceList> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResourceList doInBackground(String... param) {
            ResourceList info = null;

            try {
                info = yandexDiskService.getFile(param.toString());
            } catch (IOException e) {
                mView.showNetworkError();

            } catch (ServerException e) {
                mView.showServerError();

            }

            return info;
        }



        @Override
        protected void onPostExecute(ResourceList result) {
            List<GalleryPhoto> photoList = new ArrayList<>();

            if(result != null) {

                List<Resource> items = result.getItems();

                for(Resource res : items) {
                    String title = res.getName();
                    String previewURL = res.getPreview();
                    String imagePath = res.getPath().getPath();

                    GalleryPhoto photo = new GalleryPhoto();
                    photo.setTitle(title);
                    photo.setImagePath(imagePath);
                    photo.setPreviewUrl(previewURL);

                    photoList.add(photo);
                }
            }

            if(!photoList.isEmpty()) {
                mView.setImages(photoList);
            }

            mView.stopRefreshing();
        }
    }
}
