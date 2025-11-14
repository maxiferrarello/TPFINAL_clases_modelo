package JSONManagement.Mappers;

import models.ContraseniaHash;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContraseniaHashMapper extends AbstractMapper<ContraseniaHash>{
    @Override
    public JSONObject objectToJSONObject(ContraseniaHash contraseniaHash) {
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();
            jsonObject.put("hash", contraseniaHash.getHash());
            jsonObject.put("salt", contraseniaHash.getSalt());
        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public ContraseniaHash jsonObjectToObject(JSONObject jsonObject) {
        ContraseniaHash contraseniaHash = new ContraseniaHash();

        try {
            contraseniaHash.setHash(jsonObject.getString("hash"));
            contraseniaHash.setSalt(jsonObject.getString("salt"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        return contraseniaHash;
    }

    @Override
    public JSONArray listToJSONArray(List<ContraseniaHash> contraseniaHashes) {
        JSONArray jsonArray = null;

        try {
            jsonArray = new JSONArray();
            for (ContraseniaHash contraseniaHash : contraseniaHashes){
                jsonArray.put(objectToJSONObject(contraseniaHash));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    public List<ContraseniaHash> jsonArrayToList(JSONArray jsonArray) {
        List<ContraseniaHash> contraseniaHashes = new ArrayList<>();

        try {
            for(int i=0; i<jsonArray.length(); i++){
                contraseniaHashes.add(jsonObjectToObject(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return contraseniaHashes;
    }
}
