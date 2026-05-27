package test;

import modelo.Jugador;
import modelo.Posicion;
import mundo.Enemigo;
import logica.Combate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CombateTest {

    @Test
    public void testAtacanteMataDefensor() {
        Jugador jugador = new Jugador("Héroe", 100, 50, 5, 3, new Posicion(0, 0));
        Enemigo enemigo = new Enemigo("e1", "Goblin", 10, 5, 2, 1, 1); // 7 args
        enemigo.setPosicion(new Posicion(1, 0));

        boolean muerto = Combate.resolver(jugador, enemigo);
        assertTrue(muerto || enemigo.getVida() <= 0, "El enemigo debería haber muerto");
    }

    @Test
    public void testDanioMinimo() {
        Jugador jugador = new Jugador("Héroe", 100, 1, 5, 3, new Posicion(0, 0));
        Enemigo enemigo = new Enemigo("e1", "Goblin", 30, 5, 2, 1, 1);
        enemigo.setPosicion(new Posicion(1, 0));

        int vidaAntes = enemigo.getVida();
        Combate.resolver(jugador, enemigo);
        assertTrue(enemigo.getVida() <= vidaAntes);
    }

    @Test
    public void testDefensorNoMuereSiDefensaAlta() {
        Jugador jugador = new Jugador("Héroe", 100, 5, 5, 3, new Posicion(0, 0));
        Enemigo enemigo = new Enemigo("e1", "Goblin", 50, 5, 100, 1, 1); // defensa 100
        enemigo.setPosicion(new Posicion(1, 0));

        boolean muerto = Combate.resolver(jugador, enemigo);
        assertFalse(muerto);
        assertEquals(50, enemigo.getVida());
    }
}
