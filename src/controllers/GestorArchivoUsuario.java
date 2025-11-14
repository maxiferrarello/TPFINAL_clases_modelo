package controllers;

import JSONManagement.DataAccessObjects.UsuarioAdministradorDAO;
import JSONManagement.DataAccessObjects.UsuarioNormalDAO;
import enumerators.PermisosAdmin;
import enumerators.RolUsuarios;
import models.ContraseniaHash;
import models.Usuario;
import models.UsuarioAdministrador;
import models.UsuarioNormal;

import java.util.ArrayList;
import java.util.HashSet;

public class GestorArchivoUsuario {
    private static final String NAME_FILE_ADMINS = "UsuariosAdministradores.json";
    private static final String NAME_FILE_USERS = "UsuariosNormales.json";

    private final UsuarioAdministradorDAO usuarioAdministradorDAO = new UsuarioAdministradorDAO();
    private final UsuarioNormalDAO usuarioNormalDAO = new UsuarioNormalDAO();
    private ArrayList<UsuarioAdministrador> administradores;
    private ArrayList<UsuarioNormal> normales;
    private HashSet<Usuario> usuariosTotales = new HashSet<>();

    public GestorArchivoUsuario(){
        actualizarListas();
    }

    public boolean crearUsuarioNormal(String nombre, String contrasenia, boolean activo, RolUsuarios rolUsuarios, boolean puedeCrear){
        if(rolUsuarios.equals(RolUsuarios.NORMAL)){
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
        if(rolUsuarios.equals(RolUsuarios.ADMIN)){
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
        UsuarioAdministrador administradorAModificar = (UsuarioAdministrador) buscarUsuario(administradorModificado.getIdUsuario());

        if(administradorAModificar != null){
            administradores.remove(administradorAModificar);
            administradores.add(administradorModificado);
            guardarCambios();
            actualizarListas();
        }
    }

    public void modificarUsuarioNormal(UsuarioNormal normalModificado){
        UsuarioNormal normalAModificar = (UsuarioNormal) buscarUsuario(normalModificado.getIdUsuario());

        if(normalAModificar != null){
            normales.remove(normalAModificar);
            normales.add(normalModificado);
            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarUsuarioAdmin(int idUsuario){
        UsuarioAdministrador administradorAEliminar = (UsuarioAdministrador) buscarUsuario(idUsuario);

        if(administradorAEliminar != null){
            administradores.remove(administradorAEliminar);
            guardarCambios();
            actualizarListas();
        }
    }

    public void eliminarUsuarioNormal(int idUsuario){
        UsuarioNormal normalAEliminar = (UsuarioNormal) buscarUsuario(idUsuario);

        if(normalAEliminar != null){
            normales.remove(normalAEliminar);
            guardarCambios();
            actualizarListas();
        }
    }

    public Usuario buscarUsuario(int idUsuario){
        RolUsuarios rolUsuarioBuscado = null;

        for(Usuario usuario : usuariosTotales){
            if(usuario.getIdUsuario() == idUsuario){
                rolUsuarioBuscado = usuario.getRolUsuarios();
                break;
            }
        }

        switch (rolUsuarioBuscado){
            case RolUsuarios.NORMAL -> {
                for(UsuarioNormal normal : normales){
                    if(normal.getIdUsuario() == idUsuario) {
                        return normal;
                    }
                }
            }
            case RolUsuarios.ADMIN -> {
                for(UsuarioAdministrador administrador : administradores){
                    if(administrador.getIdUsuario() == idUsuario){
                        return administrador;
                    }
                }
            }
            case null, default -> {
                return null;
            }
        }

        return null;
    }

    public Usuario buscarUsuario(String nombre){
        RolUsuarios rolUsuarioBuscado = null;

        for(Usuario usuario : usuariosTotales){
            if(usuario.getNombre().equals(nombre)){
                rolUsuarioBuscado = usuario.getRolUsuarios();
                break;
            }
        }

        switch (rolUsuarioBuscado){
            case RolUsuarios.NORMAL -> {
                for(UsuarioNormal normal : normales){
                    if(normal.getNombre().equals(nombre)) {
                        return normal;
                    }
                }
            }
            case RolUsuarios.ADMIN -> {
                for(UsuarioAdministrador administrador : administradores){
                    if(administrador.getNombre().equals(nombre)){
                        return administrador;
                    }
                }
            }
            case null, default -> {
                return null;
            }
        }

        return null;
    }

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
        usuarioAdministradorDAO.listToFile(administradores, NAME_FILE_ADMINS);
        usuarioNormalDAO.listToFile(normales, NAME_FILE_USERS);
    }

    private void actualizarListas(){
        this.administradores = (ArrayList<UsuarioAdministrador>) usuarioAdministradorDAO.fileToList(NAME_FILE_ADMINS);
        this.normales = (ArrayList<UsuarioNormal>) usuarioNormalDAO.fileToList(NAME_FILE_USERS);

        this.usuariosTotales.clear();

        this.usuariosTotales.addAll(administradores);
        this.usuariosTotales.addAll(normales);
    }
}
