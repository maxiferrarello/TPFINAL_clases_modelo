import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        UsuarioAdministrador usuarioAdministrador = new UsuarioAdministrador("maxi","papa","dsada",true,PermisosAdmin.SOLICITARIO);

        usuarioAdministrador.ingresarAccionAlRegistro("dibujar");

        usuarioAdministrador.ingresarAccionAlRegistro("borrar");

        usuarioAdministrador.ingresarAccionAlRegistro("pintar");

        usuarioAdministrador.ingresarAccionAlRegistro("bordar");

        System.out.println(usuarioAdministrador.getRegistroAcciones());


        List <String> a = new ArrayList<>();

        a = usuarioAdministrador.buscarAcciones(2025,11,14);

        System.out.println(a);

        usuarioAdministrador.eliminarAccionDelRegistro(LocalDateTime.of(2025,11,14,0,0), "dibujar");














































    }
}