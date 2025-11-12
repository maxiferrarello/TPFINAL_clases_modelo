package models;

import enumerators.PermisosAdmin;
import enumerators.RolUsuarios;
import exceptions.MissingKeyOrValueException;

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
        this.registroAcciones = registroAcciones;
    }

    public UsuarioAdministrador(int idUsuario, String nombre, String salt, String hash, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin nivelAdmin)
    {
        super(idUsuario, nombre, salt, hash, activo, rolUsuarios);
        this.nivelAdmin = nivelAdmin;
        this.registroAcciones = new TreeMap<>();
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

    public void ingresarAccionAlRegistro(String accion) throws MissingKeyOrValueException
    {
        LocalDateTime fechaHora = LocalDateTime.now();

        if (registroAcciones.containsKey(fechaHora))
        {
          throw new MissingKeyOrValueException("La fecha ingresada ya esta registrada");
        }

        registroAcciones.put(fechaHora, accion);
    }

    public boolean eliminarAccionDelRegistro(LocalDateTime fechaHora, String accion)
    {
       return registroAcciones.remove(fechaHora,accion);
    }

    public List<String> buscarAcciones(LocalDateTime fechaIngresada)
    {
        List<String> accionEncontrada = new ArrayList<>();

        for (Map.Entry<LocalDateTime, String> entrada : registroAcciones.entrySet())
        {
            LocalDateTime fechaHora = entrada.getKey();

            if (fechaHora.equals(fechaIngresada)) {
                accionEncontrada.add(entrada.getValue());
            }
        }
        return accionEncontrada;
    }

    @Override
    public String toString() {
        return super.toString() + "\nUsuarioAdministrador{" +
                "nivelAdmin=" + nivelAdmin +
                ", registroAcciones=" + registroAcciones +
                '}';
    }
}
