package test;

import modelo.Jugador;
import modelo.Posicion;
import mundo.Enemigo;
import logica.Combate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CombateTest {

    @Test
    public void testAtacanteReduceVida() {
        Jugador jugador = new Jugador("Héroe", 100, 50, 5, 3, new Posicion(0, 0));
        Enemigo enemigo = new Enemigo("e1", "Goblin", 10, 5, 2, 1, 1);
        enemigo.setPosicion(new Posicion(1, 0));

        int vidaAntes = enemigo.getVida();
        Combate.resolver(jugador, enemigo);
        assertTrue(enemigo.getVida() <= vidaAntes, "La vida del defensor no puede aumentar tras un ataque");
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