package io;

/**
 * Clase que se encarga de guardar el estado de la partida en un archivo JSON.
 * Llama al metodo generico DatosPartida.guardar() y registra el resultado
 * en el log para que el jugador sepa si se guardo correctamente.
 */
public class GuardadorJSON {

    /**
     * Guarda la partida completa en un archivo JSON.
     * Llama a DatosPartida.guardar() para hacer la serializacion.
     * Si funciona, lo registra en el log y devuelve true.
     * Si falla, registra el error y devuelve false.
     *
     * @param ruta         ruta del archivo donde se guardara la partida
     * @param datosPartida objeto con todos los datos de la partida
     * @param log          registro donde se anotan los mensajes
     * @return true si se guardo bien, false si hubo un error
     */
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
