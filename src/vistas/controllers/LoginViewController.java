package vistas.controllers;

import controllers.GestorSesion;
import controllers.GestorArchivoUsuario;
import models.Usuario;
import models.enumerators.RolUsuarios;
import models.exceptions.InvalidOrMissingHashPasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasenia;
    @FXML private Label lblError;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    private GestorSesion gestorSesion;
    private GestorArchivoUsuario gestorArchivoUsuario;
    private RolUsuarios rolSeleccionado;
    private String nombreUsuarioLogueado;

    @FXML
    public void initialize() {
        try {
            gestorSesion = new GestorSesion();
            gestorArchivoUsuario = new GestorArchivoUsuario();
            lblError.setVisible(false);
            rolSeleccionado = RolUsuarios.NORMAL;

            txtUsuario.setOnAction(event -> handleIniciarSesion(null));
            txtContrasenia.setOnAction(event -> handleIniciarSesion(null));

            System.out.println("✅ LoginViewController inicializado correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error al inicializar LoginViewController:");
            e.printStackTrace();
            mostrarError("Error al inicializar el sistema: " + e.getMessage());
        }
    }

    @FXML
    private void handleIniciarSesion(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtContrasenia.getText();

        System.out.println("\n🔐 Intentando iniciar sesión:");
        System.out.println("   - Usuario: " + usuario);

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarError("Por favor, complete todos los campos");
            return;
        }

        try {
            // Buscar usuario (ADMIN primero, luego NORMAL)
            Usuario usuarioEncontrado = gestorArchivoUsuario.buscarUsuario(usuario, RolUsuarios.ADMIN);
            if (usuarioEncontrado == null) {
                usuarioEncontrado = gestorArchivoUsuario.buscarUsuario(usuario, RolUsuarios.NORMAL);
            }

            // Verificar si existe
            if (usuarioEncontrado == null) {
                System.err.println("❌ Usuario no encontrado");
                mostrarError("Usuario o contraseña incorrectos");
                limpiarContrasenia();
                return;
            }

            // Verificar si está activo
            if (!usuarioEncontrado.isActivo()) {
                System.err.println("❌ Usuario INACTIVO: " + usuario);
                mostrarError("Tu cuenta está PENDIENTE DE ACTIVACIÓN.\n\nUn administrador debe aprobar tu cuenta para que puedas iniciar sesión.\n\nPor favor, espera a ser activado.");
                limpiarContrasenia();
                return;
            }

            // Intentar login según el rol encontrado
            boolean exito = false;

            if (usuarioEncontrado.getRolUsuarios() == RolUsuarios.ADMIN) {
                System.out.println("🔑 Intentando login como ADMIN...");
                exito = gestorSesion.inicioSesion(usuario, contrasenia, RolUsuarios.ADMIN);
                if (exito) {
                    rolSeleccionado = RolUsuarios.ADMIN;
                    System.out.println("✅ Login exitoso como ADMIN");
                }
            } else {
                System.out.println("🔑 Intentando login como NORMAL...");
                exito = gestorSesion.inicioSesion(usuario, contrasenia, RolUsuarios.NORMAL);
                if (exito) {
                    rolSeleccionado = RolUsuarios.NORMAL;
                    System.out.println("✅ Login exitoso como NORMAL");
                }
            }

            if (exito) {
                lblError.setVisible(false);
                nombreUsuarioLogueado = usuario;
                abrirMenuPrincipal();
            } else {
                System.err.println("❌ Contraseña incorrecta");
                mostrarError("Usuario o contraseña incorrectos");
                limpiarContrasenia();
            }

        } catch (InvalidOrMissingHashPasswordException e) {
            System.err.println("❌ Error en la contraseña:");
            e.printStackTrace();
            mostrarError("Error en la contraseña: " + e.getMessage());
            limpiarContrasenia();
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar sesión:");
            e.printStackTrace();
            mostrarError("Error al iniciar sesión: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/RegisterView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) linkRegistro.getScene().getWindow();
            Scene scene = new Scene(root);

            try {
                scene.getStylesheets().add(getClass().getResource("/resources/styles/login.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("⚠ CSS no encontrado");
            }

            stage.setScene(scene);
            stage.setTitle("Registro - Sistema de Dibujo");

        } catch (IOException e) {
            System.err.println("❌ Error al abrir ventana de registro:");
            e.printStackTrace();
            mostrarError("Error al abrir ventana de registro: " + e.getMessage());
        }
    }

    private void abrirMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/MainMenuView.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setUsuarioLogueado(nombreUsuarioLogueado, rolSeleccionado);

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);

            try {
                scene.getStylesheets().add(getClass().getResource("/resources/styles/main.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("⚠ CSS no encontrado");
            }

            stage.setScene(scene);
            stage.setTitle("Menú Principal - Sistema de Dibujo");
            stage.centerOnScreen();

            System.out.println("✅ Menú principal abierto:");
            System.out.println("   - Usuario: " + nombreUsuarioLogueado);
            System.out.println("   - Rol: " + rolSeleccionado);

        } catch (Exception e) {
            System.err.println("❌ Error al abrir menú principal:");
            e.printStackTrace();
            mostrarError("Error al abrir menú principal: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void limpiarContrasenia() {
        txtContrasenia.clear();
        txtContrasenia.requestFocus();
    }

    public RolUsuarios getRolSeleccionado() {
        return rolSeleccionado;
    }

    public String getNombreUsuarioLogueado() {
        return nombreUsuarioLogueado;
    }
}