import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class DibujoMapper {

    public static JSONObject serializarDibujo(Dibujo dibujo){
        JSONObject object= null;
        try {
            object = new JSONObject();
            object.put("idDibujo", dibujo.getIdDibujo());
            object.put("idPropietario", dibujo.getIdPropietario());
            object.put("nombreDibujo", dibujo.getNombreDibujo());
            object.put("activo", dibujo.isActivo());
            object.put("anchoCuadricula",  dibujo.getAnchoCuadricula());

            JSONArray array = new JSONArray();
            for (Map.Entry<Integer, String> entry : dibujo.getClavesColores().entrySet()) {
                JSONObject json = new JSONObject();
                json.put("idColor", entry.getKey());
                json.put("color", entry.getValue());
                array.put(json);
            }
            object.put("clavesColores", array);

            JSONArray array2 = new JSONArray();
            for (Cuadricula c :  dibujo.getCuadriculas()) {
                array2.put(c);
            }
            object.put("cuadriculas", array2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static Dibujo deserializarDibujo(JSONObject object){
        Dibujo dibujo = new Dibujo();
        try {
            dibujo.setIdDibujo(object.getInt("idDibujo"));
            dibujo.setIdPropietario(object.getInt("idPropietario"));
            dibujo.setNombreDibujo(object.getString("nombreDibujo"));
            dibujo.setActivo(object.getBoolean("activo"));
            dibujo.setAnchoCuadricula(object.getInt("anchoCuadricula"));
            JSONArray array = object.getJSONArray("clavesColores");
            TreeMap<Integer, String> arbol = new TreeMap<>();
            for(int i = 0; i < array.length(); i++){
                JSONObject json = array.getJSONObject(i);
                int key = json.getInt("idColor");
                String color = json.getString("color");
                arbol.put(key, color);
            }
            dibujo.setClavesColores(arbol);
            JSONArray array2 = object.getJSONArray("cuadriculas");
            HashSet<Cuadricula> cuadriculas = new HashSet<>();
            for(int i = 0; i < array2.length(); i++) {
                JSONObject json = array2.getJSONObject(i);
                Cuadricula c = CuadriculaMapper.deserializarCuadricula(json);
                cuadriculas.add(c);
            }
            dibujo.setCuadriculas(cuadriculas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dibujo;
    }

    public static JSONArray serilizarListDibujo (List<Dibujo> ds){
        JSONArray array = null;
        try {
            array = new JSONArray();
            for (Dibujo d : ds) {
                array.put(serializarDibujo(d));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public static ArrayList<Dibujo> deserilizarListDibujo (JSONArray array){
        ArrayList<Dibujo> ds = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                ds.add(deserializarDibujo(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ds;
    }

}
