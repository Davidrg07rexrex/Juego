package test;

import modelo.Jugador;
import modelo.Posicion;
import modelo.Entidad;
import mundo.Enemigo;
import logica.TurnoManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurnoManagerTest {

    @Test
    public void testOrdenDeTurnos() {
        Jugador jugador = new Jugador("Héroe", 100, 10, 5, 3, new Posicion(0, 0));
        Enemigo goblin = new Enemigo("e1", "Goblin", 30, 10, 2, 1, 1);
        Enemigo orco = new Enemigo("e2", "Orco", 50, 12, 4, 1, 1);

        TurnoManager tm = new TurnoManager(jugador, goblin, orco);

        // Primer turno: debe ser el jugador
        tm.iniciarTurno();
        assertEquals(jugador, tm.getEntidadActual());

        // Finalizar turno del jugador
        tm.finalizarTurno();

        // Segundo turno: primer enemigo (goblin)
        tm.iniciarTurno();
        assertEquals(goblin, tm.getEntidadActual());

        // Finalizar turno del goblin
        tm.finalizarTurno();

        // Tercer turno: segundo enemigo (orco)
        tm.iniciarTurno();
        assertEquals(orco, tm.getEntidadActual());

        // Finalizar turno del orco
        tm.finalizarTurno();

        // Cuarto turno: vuelve a ser el jugador
        tm.iniciarTurno();
        assertEquals(jugador, tm.getEntidadActual());
    }

    @Test
    public void testEliminarEntidad() {
        Jugador jugador = new Jugador("Héroe", 100, 10, 5, 3, new Posicion(0, 0));
        Enemigo goblin = new Enemigo("e1", "Goblin", 30, 10, 2, 1, 1);
        Enemigo orco = new Enemigo("e2", "Orco", 50, 12, 4, 1, 1);

        TurnoManager tm = new TurnoManager(jugador, goblin, orco);

        // Iniciar primer turno (jugador)
        tm.iniciarTurno();
        assertEquals(jugador, tm.getEntidadActual());
        tm.finalizarTurno();

        // Segundo turno: goblin
        tm.iniciarTurno();
        assertEquals(goblin, tm.getEntidadActual());

        // Eliminar al goblin (muere)
        tm.eliminarEntidad(goblin);
        // La entidad actual debe ser null porque la eliminamos
        assertNull(tm.getEntidadActual());

        // Al iniciar siguiente turno, debería ser el orco
        tm.iniciarTurno();
        assertEquals(orco, tm.getEntidadActual());
    }
}