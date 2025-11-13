package modelo;
import java.util.List;

public interface ITraductorDAO <T> {

    void objectTOfile (String nombreArch, T t);
    void arrayTOfile (String nombreArch, List<T> t);
    List<T> fileTOarray (String nombreArch);
    T fileTOobject (String nombreArch);

}
