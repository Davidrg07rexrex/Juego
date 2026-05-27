package test;

import logica.InicializadorJuego;
import logica.JuegoReal;
import modelo.HabitacionModelo;
import modelo.Jugador;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CargaJSONTest {

    @Test
    public void testCargarPartidaDesdeJSON() {
        // Ajusta la ruta si tu archivo está en otro sitio
        JuegoReal juego = InicializadorJuego.cargarDesdeJSON("src/partida.json");

        // Verificar que el juego se cargó correctamente
        assertNotNull(juego, "El juego no debería ser null tras cargar el JSON");

        // Verificar el jugador
        Jugador jugador = juego.getPlayer();
        assertNotNull(jugador, "El jugador no debería ser null");
        assertEquals("Aventurero", jugador.getNombre(), "El nombre del jugador debe coincidir");
        assertEquals(100, jugador.getVida(), "La vida inicial debe ser 100");
        assertTrue(jugador.getAtaque() > 0, "El ataque debe ser positivo");
        assertTrue(jugador.getDefensa() >= 0, "La defensa no debe ser negativa");

        // Verificar la habitación inicial
        HabitacionModelo hab = juego.getCurrentRoom();
        assertNotNull(hab, "La habitación inicial no debería ser null");
        assertTrue(hab.getFilas() > 0, "La habitación debe tener filas");
        assertTrue(hab.getColumnas() > 0, "La habitación debe tener columnas");

        // Verificar que el inventario tiene objetos (según el JSON de prueba)
        assertTrue(juego.getInventory().getTamaño() > 0, "El inventario debería tener al menos un objeto");

        // Verificar que los turnos están bien inicializados
        assertTrue(juego.getTurnosRestantes() > 0, "Deben quedar turnos disponibles");

        System.out.println("Prueba de carga superada. Juego cargado correctamente.");
    }
}