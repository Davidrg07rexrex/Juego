package logica;

import modelo.Entidad;
import modelo.Jugador;
import listas.Cola;

public class TurnoManager {
    private Cola<Entidad> cola;
    private Entidad entidadActual;

    // Constructor: recibe jugador y un array de enemigos (o MiListaEnlazada)
    public TurnoManager(Jugador jugador, Entidad... enemigos) {
        cola = new Cola<>();
        cola.encolar(jugador);
        for (Entidad enemigo : enemigos) {
            cola.encolar(enemigo);
        }
        // No iniciamos turno automáticamente; el juego llamará a iniciarTurno() cuando todo esté listo
        entidadActual = null;
    }

    /**
     * Saca la siguiente entidad de la cola y la convierte en la entidad activa.
     * Llamar al principio de cada turno.
     */
    public void iniciarTurno() {
        if (entidadActual != null) {
            throw new IllegalStateException("Ya hay un turno en curso");
        }
        if (cola.estaVacia()) {
            entidadActual = null; // no hay más turnos (juego terminado)
            return;
        }
        entidadActual = cola.desencolar();
    }

    /** Devuelve quién debe jugar ahora, o null si no hay turno activo. */
    public Entidad getEntidadActual() {
        return entidadActual;
    }

    /**
     * Finaliza el turno de la entidad actual y la vuelve a encolar.
     * Después de llamar a este método, la entidad actual será null hasta que se llame a iniciarTurno().
     */
    public void finalizarTurno() {
        if (entidadActual == null) {
            throw new IllegalStateException("No hay turno en curso");
        }
        cola.encolar(entidadActual);
        entidadActual = null;
    }

    /** Indica si hay una entidad esperando para jugar (turno activo). */
    public boolean hayTurnoActivo() {
        return entidadActual != null;
    }

    /**
     * Elimina una entidad de la cola (si está esperando) y de la entidad actual si es ella.
     * Útil cuando un enemigo muere.
     */
    public void eliminarEntidad(Entidad e) {
        if (entidadActual == e) {
            entidadActual = null; // no se reencolará
        } else {
            cola.eliminar(e); // necesitas este método en MiCola (ver abajo)
        }
    }
}