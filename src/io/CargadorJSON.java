package io;

import io.DatosPartida;
import io.DatosPartida.DatosObjeto;
import io.DatosPartida.DatosEnemigoPlantilla;
import io.DatosPartida.DatosHabitacionCompleta;
import io.DatosPartida.DatosConexion;
import io.Log;

public class CargadorJSON {

    /**
     * Carga el JSON de partida y devuelve el POJO directamente.
     * No convierte a objetos del juego, solo carga y muestra informacion.
     * 
     * @param ruta Ruta al archivo partida.json
     * @return DatosPartida con todos los datos, o null si hay error
     */
    public static DatosPartida cargarCompleto(String ruta) {
        Log log = new Log();
        log.registrar("Cargando partida desde: " + ruta);

        // Cargar el JSON a POJO usando Gson (como en clase con Usuario)
        DatosPartida datos = DatosPartida.cargar(ruta, DatosPartida.class);

        if (datos == null) {
            log.registrar("ERROR: No se pudo cargar el archivo");
            return null;
        }

        // Calcular cuantas conexiones hay en el grafo
        int numConexiones = 0;
        if (datos.grafo != null) {
            numConexiones = datos.grafo.length;
        }

        // Mostrar resumen por pantalla
        System.out.println("===== RESUMEN DE PARTIDA =====");
        System.out.println("Objetos disponibles: " + contarArray(datos.objetosDisponibles));
        System.out.println("Plantillas de enemigos: " + contarArray(datos.enemigosDisponibles));
        System.out.println("Habitaciones: " + contarArray(datos.habitaciones));
        System.out.println("Conexiones en el grafo: " + numConexiones);
        if (datos.jugador != null) {
            System.out.println("Jugador: " + datos.jugador.nombre + " (Vida: " + datos.jugador.vida + ")");
        }
        if (datos.partida != null) {
            System.out.println("Habitacion actual: " + datos.partida.habitacionActual);
            System.out.println("Turnos: " + datos.partida.turnosRestantes + " / " + datos.partida.turnosMaximos);
        }
        System.out.println("==============================");

        log.registrar("Partida cargada correctamente");
        return datos;
    }

    /**
     * Cuenta cuantos elementos tiene un array, devuelve 0 si es null.
     */
    private static int contarArray(Object[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }
}
