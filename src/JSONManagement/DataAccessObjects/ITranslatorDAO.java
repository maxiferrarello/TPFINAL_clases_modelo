package JSONManagement.DataAccessObjects;

import java.util.List;

public interface ITranslatorDAO<T> {
    void objectToFile(T t, String fileName);
    void listToFile(List<T> t, String fileName);
    T fileToObject(String fileName);
    List<T> fileToList(String fileName);
}
