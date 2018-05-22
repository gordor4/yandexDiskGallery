package rus.ru.yandexapp.model;

import java.io.Serializable;
import java.util.List;

public class PhotoList implements Serializable {

    private List<GalleryPhoto> photoList;

    public List<GalleryPhoto> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<GalleryPhoto> photoList) {
        this.photoList = photoList;
    }
}
