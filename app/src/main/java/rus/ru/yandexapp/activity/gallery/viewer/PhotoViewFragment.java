package rus.ru.yandexapp.activity.gallery.viewer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.base.AbstractActivity;
import rus.ru.yandexapp.base.AbstractFragment;
import rus.ru.yandexapp.model.GalleryPhoto;
import rus.ru.yandexapp.model.GlideUrlCustomCacheKey;

public class PhotoViewFragment extends AbstractFragment<PhotoViewInterface.Presenter> implements PhotoViewInterface.View {

    class PhotoViewPagerAdapter extends FragmentPagerAdapter {
        private List<GalleryPhoto> photoList;
        private String token;

        public PhotoViewPagerAdapter(FragmentManager fragmentManager, String token) {
            super(fragmentManager);

            this.token = token;
            photoList = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Fragment getItem(int position) {
            final GalleryPhoto photo = photoList.get(position);

            return ArrayListFragment.newInstance(photo, token);
        }

        public void setPhotoList(List<GalleryPhoto> photoList) {
            this.photoList = photoList;

            notifyDataSetChanged();
        }

        public void addPhoto(GalleryPhoto photo) {
            photoList.add(photo);

            notifyDataSetChanged();
        }
    }

    public static class ArrayListFragment extends ListFragment {
        @BindView(R.id.photo_view) ImageView photoView;
        @BindView(R.id.progress_bar) ProgressBar progressBar;

        private GalleryPhoto photo;
        private String token;
        public static final String IMAGE_DOWNLOAD_FOLDER = "/gallery/";

        static ArrayListFragment newInstance(GalleryPhoto photo, String token) {
            ArrayListFragment arrayListFragment = new ArrayListFragment();

            arrayListFragment.setPhoto(photo);

            Bundle args = new Bundle();
            args.putSerializable("photo", photo);
            args.putString("token", token);
            arrayListFragment.setArguments(args);

            return arrayListFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            photo = getArguments() != null ? (GalleryPhoto) getArguments().getSerializable("photo") : null;
            token = getArguments() != null ? getArguments().getString("token") : "";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.photo_pager_item, container, false);

            ButterKnife.bind(this, root);

            final PhotoViewActivity activity = (PhotoViewActivity) getActivity();

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity != null) {
                        activity.toggleToolbarVisibility();
                        activity.restartHideScheduler();
                    }
                }
            });

            //изменим тактику кэширования и добавим токер авторизации
            GlideUrlCustomCacheKey glideUrl = new GlideUrlCustomCacheKey(photo.getDownloadUrl(), photo.getImagePath(), new Headers() {
                @Override
                public Map<String, String> getHeaders() {
                    Map headers = new HashMap<>();
                    headers.put("Authorization", token);

                    return headers;
                }
            });

            Glide.with(activity)
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .fitCenter()
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(photoView);

            return root;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1));
        }

        private void setPhoto(GalleryPhoto photo) {
            this.photo = photo;
        }

        public boolean isImageDownloaded() {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + IMAGE_DOWNLOAD_FOLDER + photo.getTitle());

            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }
    }


    private PhotoViewPagerAdapter pagerAdapter;

    @BindView(R.id.image_pager) ViewPager imagePager;

    public PhotoViewFragment() { }

    public static PhotoViewFragment newInstance() {
        return new PhotoViewFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.photo_view_fragment, container, false);

        uiBind(root);

        final PhotoViewActivity activity = (PhotoViewActivity) getActivity();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        pagerAdapter = new PhotoViewPagerAdapter(fragmentManager, presenter.getToken());
        imagePager.setAdapter(pagerAdapter);

        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                setTitleCounter(position + 1);

                setCheckDownloadMark(position);

                activity.restartHideScheduler();

                presenter.setCurrentIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        return root;
    }

    private void setCheckDownloadMark(int position) {
        PhotoViewActivity activity = (PhotoViewActivity) getActivity();

        ArrayListFragment fragment = (ArrayListFragment) pagerAdapter.getItem(position);

        if (fragment.isImageDownloaded()) {
            activity.setCheckDownloadMark(true);
        } else {
            activity.setCheckDownloadMark(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        uiUnbind();
    }

    @Override
    public void setUrlList(List<GalleryPhoto> photoList) {
        pagerAdapter.setPhotoList(photoList);

        imagePager.setCurrentItem(presenter.getCurrentIndex(), false);
    }

    @Override
    public void addUrl(GalleryPhoto photo) {
        pagerAdapter.addPhoto(photo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }

        if (id == R.id.action_bar_download) {
            presenter.onDownloadFile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTitleCounter(int i) {
        AbstractActivity activity = (AbstractActivity) getActivity();

        if(activity != null) {
            activity.setActionBarTitle(getString(R.string.image_count_holder, i, presenter.getAllPhotoCount()));
        }
    }
}
