package models;

import models.enumerators.PermisosAdmin;
import models.enumerators.RolUsuarios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UsuarioAdministrador extends Usuario {

    /// atributos

    private PermisosAdmin nivelAdmin;
    private TreeMap<LocalDateTime,String> registroAcciones;



    /// Constructor

    public UsuarioAdministrador() {
        super();
        this.nivelAdmin = PermisosAdmin.VISUALIZANTE;
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(PermisosAdmin nivelAdmin) {
        super();
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(PermisosAdmin nivelAdmin, TreeMap<LocalDateTime, String> registroAcciones) {
        super();
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>(registroAcciones);
    }

    public UsuarioAdministrador(int idUsuario, String nombre, String salt, String hash, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin nivelAdmin)
    {
        super(idUsuario, nombre, salt, hash, activo, rolUsuarios);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(int idUsuario, String nombre, String salt, String hash, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin nivelAdmin, TreeMap<LocalDateTime, String> registroAcciones)
    {
        super(idUsuario, nombre, salt, hash, activo, rolUsuarios);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>(registroAcciones);
    }

    public UsuarioAdministrador(int idUsuario, String nombre, ContraseniaHash hashContrasenia, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin nivelAdmin)
    {
        super(idUsuario, nombre, hashContrasenia, activo, rolUsuarios);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
    }

    public UsuarioAdministrador(int idUsuario, String nombre, ContraseniaHash hashContrasenia, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin nivelAdmin,TreeMap<LocalDateTime, String> registroAcciones)
    {
        super(idUsuario, nombre, hashContrasenia, activo, rolUsuarios);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>(registroAcciones);
    }



    /// getters y setters

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

    public void ingresarAccionAlRegistro(String accion) // Correcciones de Maxi
    {
        LocalDateTime fechaHora = LocalDateTime.now().withNano(0);

        if (registroAcciones.containsKey(fechaHora))
        {
            fechaHora = fechaHora.plusSeconds(1);
        }

        registroAcciones.put(fechaHora, accion);
    }

    public boolean eliminarAccionDelRegistro(LocalDateTime fechaHora, String accion)
    {
        LocalDateTime claveAborrar = null;

        for (Map.Entry<LocalDateTime, String> entry : registroAcciones.entrySet()) {
            if (entry.getKey().equals(fechaHora) && entry.getValue().equals(accion)) {  // Corrección: se debe comprobar la fecha completa
                claveAborrar = entry.getKey();
                break;
            }
        }

        if (claveAborrar != null) {
            registroAcciones.remove(claveAborrar);
            return true;
        }

        return false;
    }

    public void eliminarAccionDelRegistro(LocalDateTime fechaHora) {
        registroAcciones.remove(fechaHora);
    }

    public List<String> buscarAccionesDelDia(LocalDateTime fechaIngresada)  // Se lo ajustó a una búsqueda más precisa
    {
        LocalDateTime inicioDia = fechaIngresada.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        Map<LocalDateTime, String> accionesEncontradas = registroAcciones.subMap(inicioDia, finDia);

        return new ArrayList<>(accionesEncontradas.values());
    }

    @Override
    public String toString() {
        return super.toString() + "\nUsuarioAdministrador{" +
                "nivelAdmin=" + nivelAdmin +
                ", registroAcciones=" + registroAcciones +
                '}';
    }
}
