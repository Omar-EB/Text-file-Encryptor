import java.util.NoSuchElementException;

public interface Iterator<E> {

    boolean hasNext();
    E next();

}
