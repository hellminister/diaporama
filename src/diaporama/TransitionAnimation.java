package diaporama;


public interface TransitionAnimation<T> {
    void start();

    void changeImage() throws InterruptedException;

    T getView();
}
