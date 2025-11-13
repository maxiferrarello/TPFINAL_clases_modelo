package modelo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements ITraductorDAO<Usuario>{

    @Override
    public void objectTOfile (String nombreArch, Usuario usuario) {
        JSONObject user=UsuarioMapper.serializarUsuario(usuario);
        OpLectoEscritura.grabar(nombreArch, user);
    }

    @Override
    public void arrayTOfile (String nombreArch, List<Usuario> usuarios) {
        OpLectoEscritura.grabar(nombreArch, UsuarioMapper.sereliazarLista(usuarios));
    }

    public Usuario fileTOobject (String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        Usuario usuario = null;
        try {
            usuario = UsuarioMapper.deserializarUsuario(new JSONObject(token));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return usuario;
    }

    @Override
    public ArrayList<Usuario> fileTOarray (String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            usuarios = UsuarioMapper.deserializarLista(new JSONArray(token));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return usuarios;
    }

}
