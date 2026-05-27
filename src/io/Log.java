package io;

import listas.ListaSimplementeEnlazada;

// Registro de eventos del juego
public class Log {
    private ListaSimplementeEnlazada<String> entradas;

    // Constructor: log vacio
    public Log() {
        this.entradas = new ListaSimplementeEnlazada<>();
    }

    // Anade un mensaje al log
    public void registrar(String mensaje) {
        entradas.add("[LOG] " + mensaje);
    }

    // Muestra el log por consola
    public void mostrar() {
        for (int i = 0; i < entradas.getTamaño(); i++) {
            System.out.println(entradas.getDatoEn(i));
        }
    }

    // Numero de entradas del log
    public int getTamano() {
        return entradas.getTamaño();
    }

    // Devuelve la ultima entrada
    public String getUltimo() {
        if (entradas.getTamaño() == 0) return "";
        return entradas.getDatoEn(entradas.getTamaño() - 1);
    }

    // Exporta el log como String
    public String exportar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entradas.getTamaño(); i++) {
            sb.append(entradas.getDatoEn(i)).append("\n");
        }
        return sb.toString();
    }
}