package test;

import modelo.Posicion;
import mundo.HabitacionMock;
import logica.Movimiento;
import mundo.Trampa;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovimientoTest {

    @Test
    public void testCeldasAlcanzablesUnPaso() {
        HabitacionMock hab = new HabitacionMock("sala1", "Sala", 3, 3);
        boolean[][] alcanzables = Movimiento.celdasAlcanzables(hab, 1, 1, 1);

        // Desde (1,1) con 1 paso: debe poder ir a (0,1), (2,1), (1,0), (1,2)
        assertTrue(alcanzables[0][1], "Debería alcanzar (0,1)");
        assertTrue(alcanzables[2][1], "Debería alcanzar (2,1)");
        assertTrue(alcanzables[1][0], "Debería alcanzar (1,0)");
        assertTrue(alcanzables[1][2], "Debería alcanzar (1,2)");
        // La propia posición también está marcada
        assertTrue(alcanzables[1][1], "La posición inicial debe estar marcada");
        // No debería alcanzar esquinas opuestas
        assertFalse(alcanzables[0][0], "No debería alcanzar (0,0)");
        assertFalse(alcanzables[2][2], "No debería alcanzar (2,2)");
    }

    @Test
    public void testCeldasAlcanzablesDosPasos() {
        HabitacionMock hab = new HabitacionMock("sala1", "Sala", 3, 3);
        boolean[][] alcanzables = Movimiento.celdasAlcanzables(hab, 0, 0, 2);

        // Con 2 pasos desde (0,0) se alcanzan todas las celdas con distancia Manhattan <= 2
        // (0,0) sí (distancia 0)
        assertTrue(alcanzables[0][0], "Debe alcanzar (0,0)");
        // (0,1) distancia 1
        assertTrue(alcanzables[0][1], "Debe alcanzar (0,1)");
        // (1,0) distancia 1
        assertTrue(alcanzables[1][0], "Debe alcanzar (1,0)");
        // (0,2) distancia 2
        assertTrue(alcanzables[0][2], "Debe alcanzar (0,2)");
        // (1,1) distancia 2
        assertTrue(alcanzables[1][1], "Debe alcanzar (1,1)");
        // (2,0) distancia 2
        assertTrue(alcanzables[2][0], "Debe alcanzar (2,0)");

        // Las de distancia 3 no se alcanzan
        assertFalse(alcanzables[2][1], "No debe alcanzar (2,1) (distancia 3)");
        assertFalse(alcanzables[1][2], "No debe alcanzar (1,2) (distancia 3)");
        assertFalse(alcanzables[2][2], "No debe alcanzar (2,2) (distancia 4)");
    }

    @Test
    public void testCeldasAlcanzablesConObstaculo() {
        HabitacionMock hab = new HabitacionMock("sala1", "Sala", 3, 3);
        // Colocamos una trampa en (1,1) para que sea intransitable
        hab.colocarTrampa(1, 1, new Trampa(10));  // necesitas importar mundo.Trampa

        boolean[][] alcanzables = Movimiento.celdasAlcanzables(hab, 0, 0, 3);

        // La celda (1,1) no debe ser alcanzable
        assertFalse(alcanzables[1][1], "No debe alcanzar (1,1) por el obstáculo");

        // Algunas celdas accesibles
        assertTrue(alcanzables[0][0], "Debe alcanzar (0,0)");
        assertTrue(alcanzables[0][1], "Debe alcanzar (0,1)");
        assertTrue(alcanzables[1][0], "Debe alcanzar (1,0)");
        // (2,0) podría ser alcanzable dependiendo de la ruta (0,0)->(1,0)->(2,0) no pasa por (1,1)
        assertTrue(alcanzables[2][0], "Debe alcanzar (2,0)");
    }
}