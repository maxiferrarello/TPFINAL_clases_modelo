package controllers;

import models.JSONManagement.DataAccessObjects.DibujoDAO;
import models.JSONManagement.ReadWriteOperations;
import models.Dibujo;

import java.util.ArrayList;

public class GestorArchivoDibujo {
    private static final String NAME_FILE = "Dibujos.json";
    private static GestorArchivoUsuario gestorArchivoUsuario = new GestorArchivoUsuario();

    private final DibujoDAO dibujoDAO = new DibujoDAO();
    private ArrayList<Dibujo> dibusjosGuardados = new ArrayList<>();

    public GestorArchivoDibujo(){
        if(ReadWriteOperations.archivoExiste(NAME_FILE)) actualizarLista();
    }

    public void crearDibujo(int idPropietario, String nombreDibujo, boolean activo, int anchoCuadricula){
        int idGenerado = generarIdUnico();

        if(gestorArchivoUsuario.buscarUsuario(idPropietario) != null) {
            dibusjosGuardados.add(new Dibujo(idGenerado, idPropietario, nombreDibujo, activo, anchoCuadricula));

            guardarCambios();
            gestorArchivoUsuario.agregarDibujoCreado(idPropietario, idGenerado);
        }
    }

    public void modificarDibujo(Dibujo dibujoModificado){
        Dibujo dibujoAModificar = buscarDibujoEnLista(dibujoModificado.getIdDibujo());

        if(dibujoAModificar != null){
            dibusjosGuardados.remove(dibujoAModificar);
            dibusjosGuardados.add(dibujoModificado);
            guardarCambios();
        }
    }

    public boolean eliminarDibujo(int idDibujo){
        Dibujo dibujoAEliminar = buscarDibujoEnLista(idDibujo);

        if(dibujoAEliminar != null){
            dibusjosGuardados.remove(dibujoAEliminar);
            guardarCambios();
            return true;
        }

        return false;
    }

    public Dibujo buscarDibujoEnLista(int idDibujo){
        for(Dibujo dibujo : dibusjosGuardados){
            if(dibujo.getIdDibujo() == idDibujo) return dibujo;
        }
        return null;
    }



    // Validadores y generadores

    private int generarIdUnico(){
        return dibusjosGuardados.size() + 1;
    }

    private void guardarCambios(){
        dibujoDAO.listToFile(dibusjosGuardados, NAME_FILE);
    }

    private void actualizarLista(){
        this.dibusjosGuardados = (ArrayList<Dibujo>) dibujoDAO.fileToList(NAME_FILE);
    }
}
