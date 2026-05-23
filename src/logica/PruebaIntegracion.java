package logica;

import modelo.*;
import mundo.*;
import listas.*;

public class PruebaIntegracion {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE INTEGRACIÓN DEL SISTEMA DE JUEGO ===\n");

        // 1. Crear habitación de prueba
        HabitacionMock hab = new HabitacionMock("Mazmorra", 5, 5);

        // 2. Crear jugador en posición (2,2)
        Jugador jugador = new Jugador("Héroe", 100, 15, 5, new Posicion(2, 2));

        // 3. Crear enemigos
        Enemigo goblin = new Enemigo("e1", "Goblin", 30, 10, 2, 1);
        goblin.setPosicion(new Posicion(2, 3));  // adyacente al jugador
        Enemigo orco = new Enemigo("e2", "Orco", 50, 12, 4, 1);
        orco.setPosicion(new Posicion(0, 0));

        // Colocar enemigos en el mock
        hab.colocarEnemigo(goblin);
        hab.colocarEnemigo(orco);

        // 4. Inicializar TurnoManager
        TurnoManager tm = new TurnoManager(jugador, goblin, orco);

        // 5. Crear JuegoReal
        JuegoReal juego = new JuegoReal(jugador, hab);
        juego.setTurnoManager(tm);

        // 6. Mostrar estado inicial
        imprimirEstado(juego, hab);

        // 7. Simular varios turnos
        for (int i = 0; i < 6 && !juego.isGameOver(); i++) {
            System.out.println("--- Turno " + (i + 1) + " ---");
            juego.iniciarNuevoTurno();

            if (juego.isGameOver()) break;

            Entidad actual = tm.getEntidadActual();
            if (actual instanceof Jugador) {
                // Simulamos que el jugador ataca al goblin si está adyacente
                Posicion posGoblin = goblin.getPosicion();
                if (posGoblin != null && estanAdyacentes(jugador.getPosicion(), posGoblin)) {
                    System.out.println("(Jugador decide atacar a Goblin)");
                    juego.attack(posGoblin);
                } else {
                    System.out.println("(Jugador no tiene enemigo adyacente, turno sin acción)");
                }
                tm.finalizarTurno();  // Finaliza el turno del jugador manualmente
            }
            // Los turnos de enemigos ya se ejecutan automáticamente en iniciarNuevoTurno()

            imprimirEstado(juego, hab);
            System.out.println();
        }

        if (juego.isGameOver()) {
            System.out.println("¡JUEGO TERMINADO!");
        } else {
            System.out.println("Fin de la simulación (sin game over).");
        }
    }

    private static boolean estanAdyacentes(Posicion a, Posicion b) {
        return Math.abs(a.getFila() - b.getFila()) + Math.abs(a.getColumna() - b.getColumna()) == 1;
    }

    private static void imprimirEstado(JuegoReal juego, HabitacionMock hab) {
        System.out.println("Jugador: " + juego.getPlayer().getNombre()
                + " | Vida: " + juego.getPlayer().getVida()
                + " | Pos: " + juego.getPlayer().getPosicion());
        System.out.print("Enemigos: ");
        ListaSimplementeEnlazada<Enemigo> enemigos = hab.getEnemigos();
        for (int i = 0; i < enemigos.getTamaño(); i++) {
            Enemigo e = enemigos.getDatoEn(i);
            System.out.print(e.getNombre() + "(V:" + e.getVida() + " P:" + e.getPosicion() + ") ");
        }
        System.out.println("\nLog:");
        ListaSimplementeEnlazada<String> log = juego.getEventLog();
        for (int i = 0; i < log.getTamaño(); i++) {
            System.out.println("  - " + log.getDatoEn(i));
        }
    }
}
