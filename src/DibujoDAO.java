import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class DibujoDAO implements ITraductorDAO<Dibujo>{

    @Override
    public void objectTOfile (String nombreArch, Dibujo d){
        OpLectoEscritura.grabar(nombreArch, DibujoMapper.serializarDibujo(d));
    }

    @Override
    public void arrayTOfile (String nombreArch, List<Dibujo> d){
        OpLectoEscritura.grabar(nombreArch, DibujoMapper.serilizarListDibujo(d));
    }

    @Override
    public ArrayList<Dibujo> fileTOarray (String nombreArch){
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        ArrayList<Dibujo> d= new ArrayList<>();
        try {
            d = DibujoMapper.deserilizarListDibujo(new JSONArray(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    public Dibujo fileTOobject (String nombreArch){
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        Dibujo d = new Dibujo();
        try{
            d=DibujoMapper.deserializarDibujo(new JSONObject(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return d;
    }

}
