package modelo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UsuarioNormalMapper {

    public static JSONObject serializarUsuarioNormal(UsuarioNormal un) {
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj=UsuarioMapper.serializarUsuario(un);
            obj.put("puedeCrear", un.isPuedeCrear());
            JSONArray arr = new JSONArray();
            for (Integer x : un.getDibujosCreados()) {
                arr.put(x);
            }
            obj.put("dibujosCreados", arr);
            JSONArray arrP = new JSONArray();
            for (Integer x : un.getDibujosPintados()) {
                arrP.put(x);
            }
            obj.put("dibujosPintados", arrP);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static UsuarioNormal deserializarUsuarioNormal(JSONObject obj) {
        UsuarioNormal un = new UsuarioNormal();
        try {
            UsuarioMapper.deserializarUsuario(un, obj);
            un.setPuedeCrear(obj.getBoolean("puedeCrear"));
            JSONArray arr=obj.getJSONArray("dibujosCreados");
            HashSet<Integer> hs=new HashSet<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject objP = arr.getJSONObject(i);
                hs.add(objP.getInt("id"));
            }
            un.setDibujosCreados(hs);
            JSONArray arrP=obj.getJSONArray("dibujosPintados");
            HashSet<Integer> hsP=new HashSet<>();
            for (int i = 0; i < arrP.length(); i++) {
                JSONObject objP = arrP.getJSONObject(i);
                hsP.add(objP.getInt("id"));
            }
            un.setDibujosPintados(hsP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return un;
    }

    public static JSONArray serializarListaUsNormal(List<UsuarioNormal> uns) {
        JSONArray arr = null;
        try {
            arr = new JSONArray();
            for (UsuarioNormal un : uns) {
                arr.put(serializarUsuarioNormal(un));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static List<UsuarioNormal> deserializarListaUsNormal(JSONArray arr) {
        List<UsuarioNormal> uns = new ArrayList<>();
        try {
            for (int i = 0; i < arr.length(); i++) {
                uns.add(deserializarUsuarioNormal(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uns;
    }

}
