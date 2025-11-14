package controllers;

import JSONManagement.DataAccessObjects.DibujoDAO;
import models.Dibujo;

import java.util.ArrayList;

public class GestorArchivoDibujo {
    private static final String NAME_FILE = "Dibujos.json";

    private final DibujoDAO dibujoDAO = new DibujoDAO();
    private ArrayList<Dibujo> dibusjosGuardados;

    public GestorArchivoDibujo(){
        actualizarLista();
    }

    public void crearDibujo(int idPropietario, String nombreDibujo, boolean activo, int anchoCuadricula){
        dibusjosGuardados.add(new Dibujo(generarIdUnico(), idPropietario, nombreDibujo, activo, anchoCuadricula));
        guardarCambios();
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
