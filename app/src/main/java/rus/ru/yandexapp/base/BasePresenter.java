package rus.ru.yandexapp.base;

public abstract class BasePresenter<T extends AbstractInterface.View> implements AbstractInterface.Presenter {

    protected final T mView;

    @SuppressWarnings("unchecked")
    protected BasePresenter(T view) {
        this.mView = view;

        this.mView.setPresenter(this);
    }

    public void start() {
        onStart();
    }

    protected abstract void onStart();

}
