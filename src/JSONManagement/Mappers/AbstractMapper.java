package JSONManagement.Mappers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public abstract class AbstractMapper<T> {
    abstract JSONObject objectToJSONObject(T t);
    abstract T jsonObjectToObject(JSONObject jsonObject);
    abstract JSONArray listToJSONArray(List<T> collection);
    abstract List<T> jsonArrayToList(JSONArray jsonArray);
}
