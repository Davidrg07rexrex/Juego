package io;

// Guarda la partida en un archivo JSON
public class GuardadorJSON {

    // Guarda la partida completa en JSON
    public boolean guardarPartida(String ruta, DatosPartida datosPartida, Log log) {
        try {
            DatosPartida.guardar(ruta, datosPartida);
            log.registrar("Partida guardada en: " + ruta);
            return true;
        } catch (Exception e) {
            log.registrar("ERROR al guardar partida: " + e.getMessage());
            return false;
        }
    }
}
