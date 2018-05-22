package rus.ru.yandexapp.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import static dagger.internal.Preconditions.checkNotNull;

public class ActivityUtils {

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId) {
        addFragment(fragmentManager, fragment, frameId, false);
    }

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull View container) {
        addFragment(fragmentManager, fragment, container, false);
    }

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @NonNull View container, boolean append) {
        checkNotNull(container);

        addFragment(fragmentManager, fragment, container.getId(), append);
    }

    public static void addFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, boolean append) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (append) {
            transaction.add(frameId, fragment);
        } else {
            transaction.replace(frameId, fragment);
        }
        transaction.commit();
    }
}
