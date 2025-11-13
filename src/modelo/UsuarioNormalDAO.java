import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioNormalDAO implements ITraductorDAO <UsuarioNormal>{

    @Override
    public void objectTOfile (String nombreArch, UsuarioNormal un) {
        OpLectoEscritura.grabar(nombreArch, UsuarioNormalMapper.serializarUsuarioNormal(un));
    }

    @Override
    public void arrayTOfile (String nombreArch, List<UsuarioNormal> uns) {
        OpLectoEscritura.grabar(nombreArch, UsuarioNormalMapper.serializarListaUsNormal(uns));
    }

    @Override
    public List<UsuarioNormal> fileTOarray (String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        List<UsuarioNormal> uns = new ArrayList<>();
        try {
            uns=UsuarioNormalMapper.deserializarListaUsNormal(new JSONArray(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uns;
    }

    @Override
    public UsuarioNormal fileTOobject(String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        UsuarioNormal un = new UsuarioNormal();
        try{
            un=UsuarioNormalMapper.deserializarUsuarioNormal(new JSONObject(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return un;
    }

}
