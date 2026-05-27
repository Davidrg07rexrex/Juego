package logica;

import modelo.*;
import mundo.Objeto;
import java.util.Scanner;

public class MainJuego {

    public static void main(String[] args) {
        // Carga el juego desde el JSON de configuración
        JuegoReal juego = InicializadorJuego.cargarDesdeJSON("Juego/src/partida.json");
        if (juego == null) {
            System.out.println("No se pudo cargar la partida. Revisa que partida.json esté en la raíz del proyecto.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        while (!juego.isGameOver() && !juego.isVictoria()) {
            mostrarEstado(juego);
            // Mientras no sea el turno del jugador, ejecutar turnos de enemigos automáticamente
            while (!juego.isGameOver() && !juego.isVictoria() && !juego.esTurnoJugador()) {
                juego.iniciarNuevoTurno();
            }

            // Si no es el turno del jugador, los enemigos se mueven automáticamente
            if (!juego.esTurnoJugador()) {
                juego.iniciarNuevoTurno();
                continue;
            }

            System.out.println("\n--- TU TURNO ---");
            System.out.println("[M]over  [A]tacar  [R]ecoger  [U]sar objeto  [P]asar turno");
            String op = sc.nextLine().trim().toLowerCase();

            switch (op) {
                case "m":
                    System.out.print("Dirección (w/a/s/d): ");
                    String dir = sc.nextLine().trim().toLowerCase();
                    Direction d = switch (dir) {
                        case "w" -> Direction.UP;
                        case "s" -> Direction.DOWN;
                        case "a" -> Direction.LEFT;
                        case "d" -> Direction.RIGHT;
                        default -> null;
                    };
                    if (d != null) {
                        boolean movido = juego.movePlayer(d);
                        if (movido) {
                            juego.finalizarTurnoJugador();   // <-- automático tras mover
                        }
                    } else {
                        System.out.println("Dirección no válida.");
                    }
                    break;

                case "a":
                    System.out.print("Dirección de ataque (w/a/s/d): ");
                    String atkDir = sc.nextLine().trim().toLowerCase();
                    Direction atkD = switch (atkDir) {
                        case "w" -> Direction.UP;
                        case "s" -> Direction.DOWN;
                        case "a" -> Direction.LEFT;
                        case "d" -> Direction.RIGHT;
                        default -> null;
                    };
                    if (atkD != null) {
                        boolean atacado = juego.attack(atkD);
                        if (atacado) {
                            juego.finalizarTurnoJugador();   // <-- automático tras atacar
                        }
                    } else {
                        System.out.println("Dirección no válida.");
                    }
                    break;

                case "r":
                    Posicion posJugador = juego.getPlayer().getPosicion();
                    boolean recogido = juego.pickItem(posJugador);
                    if (recogido) {
                        System.out.println("Objeto recogido.");
                        juego.finalizarTurnoJugador();        // <-- automático tras recoger
                    } else {
                        System.out.println("No hay objeto aquí o no puedes recogerlo.");
                    }
                    break;

                case "u":
                    System.out.println("Inventario:");
                    for (int i = 0; i < juego.getInventory().getTamaño(); i++) {
                        System.out.println(i + ": " + juego.getInventory().getDatoEn(i).getNombre());
                    }
                    System.out.print("Índice del objeto a usar: ");
                    int idx = sc.nextInt();
                    sc.nextLine();
                    if (idx >= 0 && idx < juego.getInventory().getTamaño()) {
                        Objeto obj = juego.getInventory().getDatoEn(idx);
                        boolean usado = juego.useItem(obj);
                        if (usado) {
                            juego.finalizarTurnoJugador();    // <-- automático tras usar
                        }
                    } else {
                        System.out.println("Índice no válido.");
                    }
                    break;

                case "p":
                    juego.finalizarTurnoJugador();
                    System.out.println("Turno pasado. Los enemigos actúan...");
                    break;

                default:
                    System.out.println("Opción no reconocida.");
            }
        }

        // Fin del juego
        if (juego.isVictoria()) {
            System.out.println("\n¡HAS ESCAPADO! Victoria.");
        } else if (juego.isGameOver()) {
            System.out.println("\nHas muerto o se agotaron los turnos. Fin del juego.");
        }

        sc.close();
    }

    private static void mostrarEstado(JuegoReal juego) {
        System.out.println("\n--- " + juego.getCurrentRoom().getId() + " ---");
        System.out.println("Vida: " + juego.getPlayer().getVida()
                + " | Turnos restantes: " + juego.getTurnosRestantes());
        System.out.print("Mapa:\n");
        for (int i = 0; i < juego.getCurrentRoomRows(); i++) {
            for (int j = 0; j < juego.getCurrentRoomCols(); j++) {
                String simbolo = juego.getCellSymbol(i, j);
                // Si la celda es la posición del jugador, mostramos J
                if (juego.getPlayer().getPosicion().getFila() == i
                        && juego.getPlayer().getPosicion().getColumna() == j) {
                    System.out.print("J ");
                } else {
                    System.out.print(simbolo + " ");
                }
            }
            System.out.println();
        }
        // Mostrar últimas entradas del log
        int total = juego.getEventLog().getTamaño();
        for (int i = Math.max(0, total - 3); i < total; i++) {
            System.out.println("  " + juego.getEventLog().getDatoEn(i));
        }
    }
}