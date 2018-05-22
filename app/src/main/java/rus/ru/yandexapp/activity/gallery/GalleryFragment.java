package rus.ru.yandexapp.activity.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Headers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.activity.gallery.viewer.PhotoViewActivity;
import rus.ru.yandexapp.base.AbstractFragment;
import rus.ru.yandexapp.model.GalleryPhoto;
import rus.ru.yandexapp.model.GlideUrlCustomCacheKey;
import rus.ru.yandexapp.model.PhotoList;


public class GalleryFragment extends AbstractFragment<GalleryInterface.Presenter> implements GalleryInterface.View {

    public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {

        @Override
        public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.gallery_item, parent, false);

            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ImageView imageView = holder.mPhotoImageView;

            GalleryPhoto photo = imagePhotos.get(position);

            GlideUrlCustomCacheKey glideUrl = new GlideUrlCustomCacheKey(photo.getPreviewUrl(), photo.getImagePath(), new Headers() {
                @Override
                public Map<String, String> getHeaders() {
                    Map headers = new HashMap<>();
                    headers.put("Authorization", presenter.getToken());

                    return headers;
                }
            });

            Glide.with(mContext)
                    .load(glideUrl)
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .centerCrop()
                    .into(imageView);

            holder.imageTitle.setText(photo.getTitle());
        }

        @Override
        public int getItemCount() {
            return imagePhotos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.iv_photo) ImageView mPhotoImageView;
            @BindView(R.id.image_title) TextView imageTitle;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                ButterKnife.bind(this, itemView);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION) {

                    PhotoList photoList = new PhotoList();
                    photoList.setPhotoList(imagePhotos);

                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                    intent.putExtra(PhotoViewActivity.GALLERY_PHOTO_LIST, photoList);
                    intent.putExtra(PhotoViewActivity.PHOTO_INDEX, position);
                    intent.putExtra(PhotoViewActivity.ALL_PHOTO_COUNT, getItemCount());

                    startActivity(intent);
                }
            }
        }

        private List<GalleryPhoto> imagePhotos;
        private Context mContext;

        public ImageGalleryAdapter(Context context) {
            mContext = context;
            imagePhotos = new ArrayList<>();
        }

        public void setPhotos(List<GalleryPhoto> photoList) {
            imagePhotos = photoList;

            notifyDataSetChanged();
        }
    }

    private ImageGalleryAdapter adapter;

    @BindView(R.id.image_gallery) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance() { return new GalleryFragment(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ImageGalleryAdapter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        uiBind(root);

        RecyclerView.LayoutManager layoutManager;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.start();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        uiUnbind();
    }

    @Override
    public void setImages(List<GalleryPhoto> photoList) {
        adapter.setPhotos(photoList);

    }

    @Override
    public void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

}
