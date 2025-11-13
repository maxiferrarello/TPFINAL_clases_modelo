package modelo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class CuadriculaDAO implements ITraductorDAO<Cuadricula> {

    @Override
    public void objectTOfile (String nombreArch, Cuadricula c){
        OpLectoEscritura.grabar(nombreArch, CuadriculaMapper.serializarCuadricula(c));
    }

    @Override
    public void arrayTOfile (String nombreArch, List<Cuadricula> c){
        OpLectoEscritura.grabar(nombreArch, CuadriculaMapper.serializarListaCuadricula(c));
    }

    @Override
    public Cuadricula fileTOobject (String nombreArch){
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        Cuadricula c = new Cuadricula();
        try {
            c=CuadriculaMapper.deserializarCuadricula(new JSONObject(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public ArrayList<Cuadricula> fileTOarray (String nombreArch){
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        ArrayList<Cuadricula> c = new ArrayList<>();
        try {
            c=CuadriculaMapper.deserializarListaCuadricula(new JSONArray(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return c;
    }

}
