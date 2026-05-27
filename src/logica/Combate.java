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
        double aleatorio = Math.random();                    // 0.0 a 1.0
        int daño = (int)(atacante.getAtaque() * (aleatorio * 2)) - defensor.getDefensa();
        daño = Math.max(0, daño);                            // mínimo 0

        int nuevaVida = defensor.getVida() - daño;
        if (nuevaVida < 0) nuevaVida = 0;
        defensor.setVida(nuevaVida);

        return !defensor.estaVivo();
    }
}