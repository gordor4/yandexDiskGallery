package rus.ru.yandexapp.activity.auth;

import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;

import butterknife.BindView;
import rus.ru.yandexapp.R;
import rus.ru.yandexapp.activity.gallery.GalleryActivity;
import rus.ru.yandexapp.base.AbstractFragment;

public class AuthFragment extends AbstractFragment<AuthInterface.Presenter> implements AuthInterface.View {

    private String authString = "https://oauth.yandex.ru/authorize?response_type=token&client_id=f9b58b519a9c49e8bcd2640d5300466d";
    @BindView(R.id.auth_web_view) WebView authWebView;


    public AuthFragment() {}

    public static AuthFragment newInstance() { return new AuthFragment(); }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.auth_fragment, container, false);

        uiBind(root);

        boolean forceNeToken = presenter.isForceNewToken();

        if(forceNeToken) {
            authString += "&force_confirm=yes";
        }

        authWebView.loadUrl(authString);

        authWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                if(url.contains("access_token")) {
                    String token = "";

                    String[] strings = url.split("=");

                    for(int i = 0; i < strings.length; i++) {
                        if(strings[i].contains("token_type")) {
                            String[] values = strings[i].split("&");
                            token = values[0];
                        }
                    }

                    presenter.setToken(token);

                    if(!token.equals("")) {
                        Intent galleryIntent = new Intent(getActivity(), GalleryActivity.class);
                        startActivity(galleryIntent);
                    }
                } else {
                    view.loadUrl(url);
                }

                return false;
            }
        });


        return root;
    }
}
