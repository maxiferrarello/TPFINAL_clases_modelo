package controllers;

import models.JSONManagement.DataAccessObjects.UsuarioAdministradorDAO;
import models.JSONManagement.DataAccessObjects.UsuarioNormalDAO;
import models.JSONManagement.ReadWriteOperations;
import models.enumerators.PermisosAdmin;
import models.enumerators.RolUsuarios;
import models.ContraseniaHash;
import models.Usuario;
import models.UsuarioAdministrador;
import models.UsuarioNormal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

public class GestorArchivoUsuario {
    private static final String NAME_FILE_ADMINS = "UsuariosAdministradores.json";
    private static final String NAME_FILE_USERS = "UsuariosNormales.json";

    private final UsuarioAdministradorDAO usuarioAdministradorDAO = new UsuarioAdministradorDAO();
    private final UsuarioNormalDAO usuarioNormalDAO = new UsuarioNormalDAO();
    private ArrayList<UsuarioAdministrador> administradores = new ArrayList<>();
    private ArrayList<UsuarioNormal> normales = new ArrayList<>();
    private HashSet<Usuario> usuariosTotales = new HashSet<>();

    private GestorArchivoDibujo gestorArchivoDibujo = new GestorArchivoDibujo();

    public GestorArchivoUsuario(){
        if(ReadWriteOperations.archivoExiste(NAME_FILE_ADMINS)) {
            actualizarListaAdmins();
        }

        if(ReadWriteOperations.archivoExiste(NAME_FILE_USERS)){
            actualizarListaUsuariosNormales();
        }
    }

    public boolean crearUsuarioNormal(String nombre, String contrasenia, boolean activo, RolUsuarios rolUsuarios, boolean puedeCrear){
        if(nombreUsuarioUnico(nombre) && rolUsuarios.equals(RolUsuarios.NORMAL)){
            ContraseniaHash contraseniaHash = GestorContrasenia.generarHashContrasenia(contrasenia);
            normales.add(new UsuarioNormal(
                    generarIdUsuario(),
                    nombre,
                    contraseniaHash.getSalt(),
                    contraseniaHash.getHash(),
                    activo,
                    rolUsuarios,
                    puedeCrear)
            );

            guardarCambios();
            actualizarListas();
            return true;
        }

        return false;
    }

    public boolean crearUsuarioAdmin(String nombre, String contrasenia, boolean activo, RolUsuarios rolUsuarios, PermisosAdmin permisoAdmin){
        if(nombreUsuarioUnico(nombre) && rolUsuarios.equals(RolUsuarios.ADMIN)){
            ContraseniaHash contraseniaHash = GestorContrasenia.generarHashContrasenia(contrasenia);
            administradores.add(new UsuarioAdministrador(
                    generarIdUsuario(),
                    nombre,
                    contraseniaHash.getSalt(),
                    contraseniaHash.getHash(),
                    activo,
                    rolUsuarios,
                    permisoAdmin)
            );

            guardarCambios();
            actualizarListas();
            return true;
        }

        return false;
    }

    public void modificarUsuarioAdmin(UsuarioAdministrador administradorModificado){
        UsuarioAdministrador administradorAModificar = buscarUsuarioAdmin(administradorModificado.getIdUsuario());

        if(administradorAModificar != null){
            administradores.remove(administradorAModificar);
            administradores.add(administradorModificado);
            guardarCambios();
            actualizarListas();
        }
    }

    public void modificarUsuarioNormal(UsuarioNormal normalModificado){
        UsuarioNormal normalAModificar = buscarUsuarioNormal(normalModificado.getIdUsuario());

        if(normalAModificar != null){
            normales.remove(normalAModificar);
            normales.add(normalModificado);
            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarUsuarioAdmin(int idUsuario){
        UsuarioAdministrador administradorAEliminar = buscarUsuarioAdmin(idUsuario);

        if(administradorAEliminar != null){
            administradores.remove(administradorAEliminar);
            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarUsuarioNormal(int idUsuario){
        UsuarioNormal normalAEliminar = buscarUsuarioNormal(idUsuario);

        if(normalAEliminar != null){
            normales.remove(normalAEliminar);
            guardarCambios();
            actualizarListas();
        }
    }

    public Usuario buscarUsuario(int idUsuario){
        for(Usuario usuario : usuariosTotales){
            if(usuario.getIdUsuario() == idUsuario){
                return usuario;
            }
        }
        return null;
    }

    public Usuario buscarUsuario(String nombre){
        for(Usuario usuario : usuariosTotales){
            if(usuario.getNombre().equals(nombre)){
                return usuario;
            }
        }
        return null;
    }

    public UsuarioNormal buscarUsuarioNormal(int idUsuario){
        for (UsuarioNormal normal : normales) {
            if (normal.getIdUsuario() == idUsuario) {
                return normal;
            }
        }

        return null;
    }

    public UsuarioAdministrador buscarUsuarioAdmin(int idUsuario){
        for (UsuarioAdministrador administrador : administradores) {
            if (administrador.getIdUsuario() == idUsuario) {
                return administrador;
            }
        }

        return null;
    }

    public UsuarioNormal buscarUsuarioNormal(String nombre){
        for (UsuarioNormal normal : normales) {
            if (normal.getNombre().equals(nombre)) {
                return normal;
            }
        }

        return null;
    }

    public UsuarioAdministrador buscarUsuarioAdmin(String nombre){
        for (UsuarioAdministrador administrador : administradores) {
            if (administrador.getNombre().equals(nombre)) {
                return administrador;
            }
        }


        return null;
    }



    // Gestion de dibujos pintados y creados

    public void agregarDibujoPintado(int idPropietario, int idDibujo){
        UsuarioNormal usuarioNormal = buscarUsuarioNormal(idPropietario);

        if(usuarioNormal != null && gestorArchivoDibujo.buscarDibujoEnLista(idDibujo) != null){
            usuarioNormal.ingresarIdDibujoPintado(idDibujo);

            guardarCambios();
            actualizarListas();
        }
    }

    public void agregarDibujoCreado(int idPropietario, int idDibujo){
        UsuarioNormal usuarioNormal = buscarUsuarioNormal(idPropietario);

        if(usuarioNormal != null){
            usuarioNormal.ingresarIdDibujoCreado(idDibujo);

            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarDibujoPintado(int idPropietario, int idDibujo){
        UsuarioNormal usuarioNormal = buscarUsuarioNormal(idPropietario);

        if(usuarioNormal != null && gestorArchivoDibujo.buscarDibujoEnLista(idDibujo) != null){
            usuarioNormal.eliminarDibujoPintado(idDibujo);

            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarDibujoCreado(int idPropietario, int idDibujo){
        UsuarioNormal usuarioNormal = buscarUsuarioNormal(idPropietario);

        if(usuarioNormal != null && gestorArchivoDibujo.buscarDibujoEnLista(idDibujo) != null){
            usuarioNormal.eliminarDibujoCreado(idDibujo);

            guardarCambios();
            actualizarListas();
        }
    }


    // Gestion de acciones

    public void agregarAccion(int idAdmin, String accion){
        UsuarioAdministrador administrador = buscarUsuarioAdmin(idAdmin);

        if(administrador != null){
            administrador.ingresarAccionAlRegistro(accion);
        }
    }

    public void eliminarAccion(int idAdmin, LocalDateTime idAccion, String accion){
        UsuarioAdministrador administrador = buscarUsuarioAdmin(idAdmin);

        if(administrador != null){
            administrador.eliminarAccionDelRegistro(idAccion, accion);
        }
    }



    // Validadores y generadores

    public boolean nombreUsuarioUnico(String nombreUsuario){
        for(Usuario usuario : usuariosTotales){
            if(usuario.getNombre().equals(nombreUsuario)) return false;
        }

        return true;
    }

    private int generarIdUsuario(){
        return usuariosTotales.size() + 1;
    }

    private void guardarCambios(){
        if(administradores == null) this.administradores = new ArrayList<>();
        if(normales == null) this.normales = new ArrayList<>();

        usuarioAdministradorDAO.listToFile(administradores, NAME_FILE_ADMINS);
        usuarioNormalDAO.listToFile(normales, NAME_FILE_USERS);
    }

    private void actualizarListas(){
        this.usuariosTotales.clear();

        if(administradores != null && !administradores.isEmpty()) this.usuariosTotales.addAll(administradores);
        if(normales != null && !normales.isEmpty()) this.usuariosTotales.addAll(normales);
    }

    private void actualizarListaAdmins(){
        this.administradores = (ArrayList<UsuarioAdministrador>) usuarioAdministradorDAO.fileToList(NAME_FILE_ADMINS);
        actualizarListas();
    }

    private void actualizarListaUsuariosNormales(){
        this.normales = (ArrayList<UsuarioNormal>) usuarioNormalDAO.fileToList(NAME_FILE_USERS);
        actualizarListas();
    }
}
