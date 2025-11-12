package controllers;

import models.ContraseniaHash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class GestorContrasenia {

    // Atributos estaticos
    private static final int TAMANIO_SALT = 16;
    private static final String ALGORITMO_CRPT = "SHA-256";


    // Validaciones y generadores

    public static ContraseniaHash generarHashContrasenia(String contrasenia){
        byte[] saltBytes = generarSalt();
        byte[] hashBytes = hashearContrasenia(contrasenia, saltBytes);

        if(hashBytes == null) return null;

        String saltStr = Base64.getEncoder().encodeToString(saltBytes);
        String hashStr = Base64.getEncoder().encodeToString(hashBytes);

        return new ContraseniaHash(hashStr, saltStr);
    }

    public boolean verificarContraseniaIngresada(String contraseniaIngresada, String saltAlmacenada, String hashAlmacenado){
        try {
            byte[] saltBytes = Base64.getDecoder().decode(saltAlmacenada);
            byte[] hashContrasniaBytes = hashearContrasenia(contraseniaIngresada, saltBytes);
            String hashContraseniaStr = Base64.getEncoder().encodeToString(hashContrasniaBytes);

            return hashContraseniaStr.equals(hashAlmacenado);
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    private static byte[] generarSalt(){
        byte[] nuevaSalt = new byte[TAMANIO_SALT];
        SecureRandom random = new SecureRandom();

        random.nextBytes(nuevaSalt);     // Rellenar el array de bytes con valores seguros

        return nuevaSalt;
    }

    private static byte[] hashearContrasenia(String contrasenia, byte[] salt){
        Objects.requireNonNull(contrasenia, "La contrasenia no puede ser nula.");
        Objects.requireNonNull(salt, "La salt no puede ser nula.");

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(contrasenia.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error faltal: Algoritmo de hash " + ALGORITMO_CRPT+ " no encontrado.", e);
        }
    }
}
