import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdministradorDAO implements ITraductorDAO<UsuarioAdministrador> {

    @Override
    public void objectTOfile (String nombreArch, UsuarioAdministrador ua) {
        OpLectoEscritura.grabar(nombreArch, UsuarioAdministradorMapper.serilizarUsAdmin(ua));
    }

    @Override
    public void arrayTOfile (String nombreArch, List<UsuarioAdministrador> uas) {
        OpLectoEscritura.grabar(nombreArch, UsuarioAdministradorMapper.serializarListaUsAdmin(uas));
    }

    @Override
    public List<UsuarioAdministrador> fileTOarray (String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        List<UsuarioAdministrador> uas = new ArrayList<>();
        try {
            uas = UsuarioAdministradorMapper.deserializarListaUsAdmin(new JSONArray(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uas;
    }

    @Override
    public UsuarioAdministrador fileTOobject(String nombreArch) {
        JSONTokener token = OpLectoEscritura.leer(nombreArch);
        UsuarioAdministrador ua = new UsuarioAdministrador();
        try{
            ua=UsuarioAdministradorMapper.deserializarUsAdmin(new JSONObject(token));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ua;
    }
}