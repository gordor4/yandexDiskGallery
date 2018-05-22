package rus.ru.yandexapp.model;

import android.os.Parcel;

import java.io.Serializable;

public class GalleryPhoto implements Serializable {

    private String urlPreview;
    private String imagePath;
    private String mTitle;
    private String downloadUrl;

    public GalleryPhoto() { }

    protected GalleryPhoto(Parcel in) {
        urlPreview = in.readString();
        mTitle = in.readString();
    }

    public String getPreviewUrl() {
        return urlPreview;
    }

    public void setPreviewUrl(String url) {
        urlPreview = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}