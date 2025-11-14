package models;

import enumerators.RolUsuarios;

import java.util.Objects;

public abstract class  Usuario {

    /// Atributos

    private int idUsuario;
    private String nombre;
    private ContraseniaHash hashContrasena;
    private boolean activo;
    private RolUsuarios rolUsuarios;



    /// Constructor

    public Usuario() {
        this.idUsuario = 0;
        this.nombre = "";
        this.hashContrasena = new ContraseniaHash();
        this.activo = false;
        this.rolUsuarios = RolUsuarios.NORMAL;
    }

    public Usuario(int idUsuario, String nombre, String salt, String hash, boolean activo, RolUsuarios rolUsuarios) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.hashContrasena = new ContraseniaHash(hash, salt);
        this.activo = activo;
        this.rolUsuarios = rolUsuarios;
    }



    /// getter y setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ContraseniaHash getHashContrasena() {
        return hashContrasena;
    }

    public void setHashContrasena(ContraseniaHash hashContrasena) {
        this.hashContrasena = hashContrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public RolUsuarios getRolUsuarios() {
        return rolUsuarios;
    }

    public void setRolUsuarios(RolUsuarios rolUsuarios) {
        this.rolUsuarios = rolUsuarios;
    }




    /// Hash y equals

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return idUsuario == usuario.idUsuario && activo == usuario.activo && Objects.equals(nombre, usuario.nombre) && Objects.equals(hashContrasena, usuario.hashContrasena) && rolUsuarios == usuario.rolUsuarios;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombre, hashContrasena, activo, rolUsuarios);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", hashContrasena='" + hashContrasena + '\'' +
                ", activo=" + activo +
                ", rolUsuarios=" + rolUsuarios +
                '}';
    }
}
