package io;

import listas.ListaSimplementeEnlazada;

/**
 * Sistema de registro de eventos del juego.
 * 
 * Esta clase no tiene relacion directa con Gson ni con el patron
 * de serializacion JSON visto en clase. Es una utilidad auxiliar
 * que almacena mensajes con marca de tiempo a lo largo de la partida.
 * 
 * Internamente usa la ListaSimplementeEnlazada proporcionada por
 * otro componente del proyecto para guardar las entradas.
 * 
 * El log se utiliza desde CargadorJSON y GuardadorJSON para
 * registrar el proceso de carga/guardado, y tambien desde
 * otras partes del juego para registrar eventos.
 */
public class Log {
    private ListaSimplementeEnlazada<String> entradas;

    /**
     * Constructor: crea un log vacio
     */
    public Log() {
        this.entradas = new ListaSimplementeEnlazada<>();
    }

    /**
     * Anade un mensaje al log
     */
    public void registrar(String mensaje) {
        entradas.add("[LOG] " + mensaje);
    }

    /**
     * Muestra todas las entradas del log por consola
     */
    public void mostrar() {
        for (int i = 0; i < entradas.getTamaño(); i++) {
            System.out.println(entradas.getDatoEn(i));
        }
    }

    /**
     * Devuelve el numero de entradas del log
     */
    public int getTamano() {
        return entradas.getTamaño();
    }

    /**
     * Devuelve la ultima entrada del log (o cadena vacia si no hay)
     */
    public String getUltimo() {
        if (entradas.getTamaño() == 0) return "";
        return entradas.getDatoEn(entradas.getTamaño() - 1);
    }

    /**
     * Exporta todo el log como un unico String
     * Util para guardar la partida
     */
    public String exportar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entradas.getTamaño(); i++) {
            sb.append(entradas.getDatoEn(i)).append("\n");
        }
        return sb.toString();
    }
}
