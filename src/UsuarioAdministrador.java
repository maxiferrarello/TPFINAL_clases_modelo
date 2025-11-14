
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UsuarioAdministrador extends Usuario {
    /// atributos
    private PermisosAdmin nivelAdmin;
    private TreeMap<LocalDateTime,String> registroAcciones;

    /// Constructores
    public UsuarioAdministrador() {
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(PermisosAdmin nivelAdmin) { // hago este porque en el uml aparece con el treemap y me resulto raro
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(PermisosAdmin nivelAdmin, TreeMap<LocalDateTime, String> registroAcciones) {
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = registroAcciones;
    }

    public UsuarioAdministrador(String nombre, String hashContrasena, String salt, boolean activo, PermisosAdmin nivelAdmin) // este lo hago por si tambien se necesitaba
    {
        super(nombre, hashContrasena, salt, activo, RolUsuarios.ADMIN);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
    }

    /// getter y setters


    public PermisosAdmin getNivelAdmin() {
        return nivelAdmin;
    }

    public void setNivelAdmin(PermisosAdmin nivelAdmin) {
        this.nivelAdmin = nivelAdmin;
    }

    public TreeMap<LocalDateTime, String> getRegistroAcciones() {
        return registroAcciones;
    }

    public void setRegistroAcciones(TreeMap<LocalDateTime, String> registroAcciones) {
        this.registroAcciones = registroAcciones;
    }


    /// metodos para el registro de las acciones


    public boolean ingresarAccionAlRegistro(String accion) {

        // fecha y la hora sin los nanos

        LocalDateTime fechaHora = LocalDateTime.now().withNano(0);


        while (registroAcciones.containsKey(fechaHora)) { // si existe la misma hora y fehca en el treemap, (poco probable dependiendo el caso pero buenpo), se le agrega 1 seg
            fechaHora = fechaHora.plusSeconds(1);
        }

        registroAcciones.put(fechaHora, accion);

        return true;
    }

    public boolean eliminarAccionDelRegistro(LocalDateTime fechaHora, String accion) {

        LocalDateTime claveAborrar = null;

        for (Map.Entry<LocalDateTime, String> entry : registroAcciones.entrySet()) {

            LocalDateTime fecha = entry.getKey();

            if (fecha.toLocalDate().equals(fechaHora.toLocalDate())
                    && entry.getValue().equals(accion)) {

                claveAborrar = fecha;
                break;
            }
        }

        if (claveAborrar != null) {
            registroAcciones.remove(claveAborrar);
            return true;
        }

        return false;
    }


    public List<String> buscarAcciones(int anio, int mes, int dia)
    {
        List<String> accionEncontrada = new ArrayList<>();

        for (Map.Entry<LocalDateTime, String> entrada : registroAcciones.entrySet())
        {
            LocalDateTime fechaHora = entrada.getKey();

            if (fechaHora.getYear() == anio &&
                    fechaHora.getMonthValue() == mes &&
                    fechaHora.getDayOfMonth() == dia) {

                accionEncontrada.add(entrada.getValue());
            }
        }

        return accionEncontrada;

    }

    @Override
    public String toString() {
        return "UsuarioAdministrador{" +
                "nivelAdmin=" + nivelAdmin +
                ", registroAcciones=" + registroAcciones +
                "} " + super.toString();
    }
}
