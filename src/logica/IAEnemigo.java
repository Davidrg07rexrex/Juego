package logica;

import modelo.*;
import listas.ListaSimplementeEnlazada;

public class IAEnemigo {

    /**
     * Ejecuta el turno de un enemigo: si está adyacente al jugador, lo ataca;
     * si no, intenta moverse un paso hacia el jugador.
     */
    public static void ejecutar(Entidad enemigo, Jugador jugador, HabitacionModelo habitacion,
                                TurnoManager turnoManager, ListaSimplementeEnlazada<String> log) {

        Posicion posEnemigo = enemigo.getPosicion();
        Posicion posJugador = jugador.getPosicion();

        // Calcular diferencias
        int difFila = posJugador.getFila() - posEnemigo.getFila();
        int difCol = posJugador.getColumna() - posEnemigo.getColumna();
        int distManhattan = Math.abs(difFila) + Math.abs(difCol);

        // Si está adyacente (distancia 1), atacar al jugador
        if (distManhattan == 1) {
            int daño = calcularDaño(enemigo, jugador);
            jugador.setVida(jugador.getVida() - daño);
            log.add(enemigo.getNombre() + " ataca a " + jugador.getNombre()
                    + " causando " + daño + " de daño (vida restante: " + jugador.getVida() + ")");
            if (!jugador.estaVivo()) {
                log.add("Has muerto... Fin del juego.");
                // gameOver se pondrá en JuegoReal
            }
            return;
        }

        // Si no, intentar moverse un paso hacia el jugador
        // Priorizar dirección que más reduzca distancia en el eje con mayor diferencia
        int nuevaFila = posEnemigo.getFila();
        int nuevaCol = posEnemigo.getColumna();

        if (Math.abs(difFila) >= Math.abs(difCol)) {
            // Moverse en vertical
            nuevaFila += Integer.signum(difFila);
        } else {
            // Moverse en horizontal
            nuevaCol += Integer.signum(difCol);
        }

        // Verificar que la nueva casilla es transitable y no hay otro enemigo
        if (habitacion.esTransitable(nuevaFila, nuevaCol)) {
            Entidad otro = habitacion.getEnemigoEn(nuevaFila, nuevaCol);
            if (otro == null || otro == enemigo) {
                habitacion.moverEnemigo(enemigo, nuevaFila, nuevaCol);
                enemigo.setPosicion(new Posicion(nuevaFila, nuevaCol));
                log.add(enemigo.getNombre() + " se mueve a (" + nuevaFila + ", " + nuevaCol + ")");
                return;
            }
        }

        // Si no pudo moverse en la dirección principal, intentar la otra
        if (Math.abs(difFila) >= Math.abs(difCol)) {
            nuevaCol += Integer.signum(difCol);
            nuevaFila = posEnemigo.getFila(); // resetear fila
        } else {
            nuevaFila += Integer.signum(difFila);
            nuevaCol = posEnemigo.getColumna();
        }

        if (habitacion.esTransitable(nuevaFila, nuevaCol)) {
            Entidad otro = habitacion.getEnemigoEn(nuevaFila, nuevaCol);
            if (otro == null || otro == enemigo) {
                habitacion.moverEnemigo(enemigo, nuevaFila, nuevaCol);
                enemigo.setPosicion(new Posicion(nuevaFila, nuevaCol));
                log.add(enemigo.getNombre() + " se mueve a (" + nuevaFila + ", " + nuevaCol + ")");
                return;
            }
        }

        // Si no puede moverse en ninguna dirección, se queda quieto
        log.add(enemigo.getNombre() + " no puede moverse y se queda quieto.");
    }

    private static int calcularDaño(Entidad atacante, Entidad defensor) {
        int tirada = (int)(Math.random() * 6) + 1;
        int daño = atacante.getAtaque() - defensor.getDefensa() + tirada;
        return Math.max(1, daño);
    }
}
