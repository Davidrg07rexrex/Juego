package logica;

import modelo.Entidad;
import java.util.Random;

public class Combate {

    private static final Random dado = new Random();

    /**
     * Resuelve un ataque de 'atacante' contra 'defensor'.
     * Aplica el daño al defensor y devuelve true si el defensor muere.
     */
    public static boolean resolver(Entidad atacante, Entidad defensor) {
        int tirada = dado.nextInt(6) + 1;   // 1..6
        int daño = atacante.getAtaque() - defensor.getDefensa() + tirada;
        if (daño < 1) daño = 1;            // daño mínimo

        int nuevaVida = defensor.getVida() - daño;
        defensor.setVida(nuevaVida);

        return !defensor.estaVivo();       // true si ha muerto
    }
}