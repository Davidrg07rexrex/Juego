package logica;

import controlador.GameControllerModel;
import modelo.*;
import listas.*;
import mundo.*;

public class JuegoReal implements GameControllerModel {

    private TurnoManager turnoManager;
    private HabitacionModelo habitacionActual;
    private Jugador jugador;
    private ListaSimplementeEnlazada<String> log;
    private boolean gameOver;
    private boolean victoria;

    // Atributos para el movimiento con BFS
    private boolean[][] celdasAlcanzables;
    private int turnosRestantes;

    // Grafo y lista de habitaciones (para cambio de sala)
    private Grafo<HabitacionModelo> grafo;
    private ListaSimplementeEnlazada<HabitacionModelo> listaHabitaciones;

    public JuegoReal(Jugador jugador, HabitacionModelo inicial,
                     Grafo<HabitacionModelo> grafo,
                     ListaSimplementeEnlazada<HabitacionModelo> listaHabitaciones,
                     int turnosMaximos) {
        this.jugador = jugador;
        this.habitacionActual = inicial;
        this.grafo = grafo;
        this.listaHabitaciones = listaHabitaciones;
        this.log = new ListaSimplementeEnlazada<>();
        this.gameOver = false;
        this.victoria = false;
        this.turnosRestantes = turnosMaximos;
        this.celdasAlcanzables = new boolean[inicial.getFilas()][inicial.getColumnas()];
    }

    // ---------- Métodos de GameControllerModel ----------
    @Override
    public HabitacionModelo getCurrentRoom() { return habitacionActual; }

    @Override
    public Jugador getPlayer() { return jugador; }

    @Override
    public ListaSimplementeEnlazada<Objeto> getInventory() {
        return jugador.getInventario();
    }

    @Override
    public ListaSimplementeEnlazada<String> getEventLog() { return log; }

    @Override
    public boolean isGameOver() { return gameOver; }

    @Override
    public boolean isVictoria() { return victoria; }

    @Override
    public boolean esTurnoJugador() {
        if (turnoManager == null) return false;
        return turnoManager.getEntidadActual() == jugador;
    }

    @Override
    public boolean movePlayer(Direction dir) {
        if (gameOver || turnoManager == null || !esTurnoJugador()) return false;

        Posicion nueva = jugador.getPosicion().mover(dir);
        if (nueva.getFila() >= 0 && nueva.getFila() < getCurrentRoomRows()
                && nueva.getColumna() >= 0 && nueva.getColumna() < getCurrentRoomCols()
                && habitacionActual.esTransitable(nueva.getFila(), nueva.getColumna())) {
            // Movimiento simple (1 casilla) permitido
            realizarMovimiento(nueva);
            return true;
        }
        log.add("Movimiento inválido hacia " + dir);
        return false;
    }

    @Override
    public boolean[][] getCeldasAlcanzables() {
        return celdasAlcanzables;
    }

    @Override
    public boolean movePlayer(Posicion destino) {
        if (gameOver || turnoManager == null || !esTurnoJugador()) return false;
        if (celdasAlcanzables == null || !celdasAlcanzables[destino.getFila()][destino.getColumna()]) {
            log.add("Esa celda no está dentro de tu rango de movimiento.");
            return false;
        }
        realizarMovimiento(destino);
        return true;
    }

    private void realizarMovimiento(Posicion nueva) {
        jugador.setPosicion(nueva);
        log.add("Movido a " + nueva);

        // Comprobar trampa
        if (habitacionActual.esTrampa(nueva.getFila(), nueva.getColumna())) {
            int danio = habitacionActual.getDanioTrampa(nueva.getFila(), nueva.getColumna());
            jugador.recibirDanio(danio);
            log.add("¡Has caído en una trampa! Recibes " + danio + " de daño.");
            if (!jugador.estaVivo()) {
                gameOver = true;
                log.add("Has muerto por una trampa.");
                return;
            }
        }

        // Intentar cambiar de habitación si es una puerta
        intentarCambiarHabitacion();
    }

    @Override
    public boolean attack(Direction dir) {
        if (gameOver || turnoManager == null || !esTurnoJugador()) return false;

        Posicion objetivo = jugador.getPosicion().mover(dir);
        if (objetivo.getFila() < 0 || objetivo.getFila() >= getCurrentRoomRows()
                || objetivo.getColumna() < 0 || objetivo.getColumna() >= getCurrentRoomCols()
                || !habitacionActual.esTransitable(objetivo.getFila(), objetivo.getColumna())) {
            log.add("No puedes atacar en esa dirección.");
            return false;
        }

        Entidad defensor = habitacionActual.getEnemigoEn(objetivo.getFila(), objetivo.getColumna());
        if (defensor == null) {
            log.add("No hay enemigo en esa dirección.");
            return false;
        }

        boolean muerto = Combate.resolver(jugador, defensor);
        log.add("Atacas a " + defensor.getNombre() + " causando "
                + (muerto ? "la muerte" : "daño")
                + " (vida restante: " + defensor.getVida() + ")");

        if (muerto) {
            habitacionActual.eliminarEnemigo(objetivo.getFila(), objetivo.getColumna());
            turnoManager.eliminarEntidad(defensor);
            log.add(defensor.getNombre() + " ha muerto");
        }

        return true;
    }

    @Override
    public boolean attackNearbyEnemy() {
        if (attack(Direction.UP)) return true;
        if (attack(Direction.DOWN)) return true;
        if (attack(Direction.LEFT)) return true;
        if (attack(Direction.RIGHT)) return true;
        log.add("No hay enemigo cerca");
        return false;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        if (gameOver || turnoManager == null || !esTurnoJugador()) return false;

        Objeto obj = habitacionActual.getObjetoEn(pos.getFila(), pos.getColumna());
        if (obj == null) {
            log.add("No hay ningún objeto en " + pos);
            return false;
        }

        // Verificar que el jugador está adyacente (o en la misma celda, según invariante)
        Posicion pJugador = jugador.getPosicion();
        int dist = Math.abs(pJugador.getFila() - pos.getFila()) + Math.abs(pJugador.getColumna() - pos.getColumna());
        if (dist > 1) {
            log.add("Estás demasiado lejos para recoger el objeto.");
            return false;
        }

        jugador.agregarAlInventario(obj);
        habitacionActual.eliminarObjeto(pos.getFila(), pos.getColumna());
        log.add("Has recogido " + obj.getNombre());

        // Si es una llave, guardarla en el llavero
        if (obj.getTipo().equals("llave")) {
            jugador.agregarLlave(obj.getId());
        }
        return true;
    }

    @Override
    public boolean useItem(Objeto item) {
        if (gameOver || turnoManager == null || !esTurnoJugador()) return false;

        // Buscar el objeto en el inventario
        ListaSimplementeEnlazada<Objeto> inventario = jugador.getInventario();
        boolean encontrado = false;
        for (int i = 0; i < inventario.getTamaño(); i++) {
            if (inventario.getDatoEn(i).equals(item)) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            log.add("No tienes ese objeto en el inventario.");
            return false;
        }

        String tipo = item.getTipo();
        switch (tipo) {
            case "pocion":
                int curacion = 20; // Podrías leerlo del objeto real (Pocion)
                if (item instanceof Pocion) {
                    curacion = ((Pocion) item).getBonusVida();
                }
                int nuevaVida = jugador.getVida() + curacion;
                if (nuevaVida > jugador.getVidaMaxima()) nuevaVida = jugador.getVidaMaxima();
                jugador.setVida(nuevaVida);
                log.add("Usas " + item.getNombre() + " y recuperas " + curacion + " puntos de vida.");
                break;
            case "arma":
                // Equipamiento simple: aumenta ataque temporalmente
                int bonus = 5; // idealmente obtenerlo de Arma
                if (item instanceof Arma) {
                    bonus = ((Arma) item).getBonusAtaque();
                }
                jugador.setAtaque(jugador.getAtaque() + bonus);
                log.add("Equipas " + item.getNombre() + " (Ataque +" + bonus + ").");
                return true; // no se consume
            default:
                log.add("No puedes usar ese objeto.");
                return false;
        }

        // Eliminar el objeto del inventario (para consumibles)
        inventario.remove(item);
        return true;
    }

    @Override
    public void saveGame(String file) {
        log.add("Guardar partida en " + file + " (pendiente)");
    }

    @Override
    public void loadGame(String file) {
        log.add("Cargar partida desde " + file + " (pendiente)");
    }

    @Override
    public int getCurrentRoomRows() { return habitacionActual.getFilas(); }

    @Override
    public int getCurrentRoomCols() { return habitacionActual.getColumnas(); }

    @Override
    public String getCellSymbol(int row, int col) {
        Posicion pJugador = jugador.getPosicion();
        if (pJugador.getFila() == row && pJugador.getColumna() == col) {
            return "J";
        }
        return habitacionActual.getSimbolo(row, col);
    }

    // ---------- Métodos internos ----------
    public void iniciarNuevoTurno() {
        if (turnoManager == null || gameOver) return;
        turnoManager.iniciarTurno();
        Entidad actual = turnoManager.getEntidadActual();

        if (actual == null) {
            gameOver = true;
            log.add("No quedan entidades. Fin del juego.");
            return;
        }

        if (actual instanceof Jugador) {
            turnosRestantes--;
            if (turnosRestantes <= 0) {
                gameOver = true;
                log.add("Se acabaron los turnos. Has perdido.");
                return;
            }
            // Calcular celdas alcanzables para el jugador en este turno
            Posicion p = jugador.getPosicion();
            celdasAlcanzables = Movimiento.celdasAlcanzables(
                    habitacionActual,
                    p.getFila(),
                    p.getColumna(),
                    jugador.getVelocidad()
            );
        } else {
            // Es un enemigo, ejecutar su IA
            ejecutarTurnoIA();
        }
    }

    @Override
    public void finalizarTurnoJugador() {
        if (turnoManager == null || gameOver) return;
        if (esTurnoJugador()) {
            turnoManager.finalizarTurno();
        }
        // Ejecutar turnos de enemigos automáticamente hasta que vuelva el jugador
        while (!gameOver && turnoManager.getEntidadActual() != null && !(turnoManager.getEntidadActual() instanceof Jugador)) {
            iniciarNuevoTurno();
            if (gameOver) break;
            if (turnoManager.getEntidadActual() == null) {
                turnoManager.iniciarTurno(); // volver a empezar el ciclo
                break;
            }
        }
    }

    private void ejecutarTurnoIA() {
        if (turnoManager == null || gameOver) return;
        Entidad actual = turnoManager.getEntidadActual();
        if (actual == null || actual instanceof Jugador) return;

        IAEnemigo.ejecutar(actual, jugador, habitacionActual, turnoManager, log);

        if (!jugador.estaVivo()) {
            gameOver = true;
            log.add("Fin del juego. Has sido derrotado.");
        }

        turnoManager.finalizarTurno();
    }

    private void intentarCambiarHabitacion() {
        Posicion p = jugador.getPosicion();
        String destino = habitacionActual.getDestinoPuerta(p.getFila(), p.getColumna());
        if (destino != null) {
            if (habitacionActual.puertaNecesitaLlave(p.getFila(), p.getColumna())) {
                String idLlave = habitacionActual.getIdLlavePuerta(p.getFila(), p.getColumna());
                if (!jugador.tieneLlave(idLlave)) {
                    log.add("La puerta necesita la llave: " + idLlave);
                    return;
                }
            }
            HabitacionModelo nuevaHab = buscarHabitacionPorId(destino);
            if (nuevaHab != null) {
                habitacionActual = nuevaHab;
                log.add("Atraviesas la puerta hacia " + nuevaHab.getId());
                if (nuevaHab instanceof Habitacion && ((Habitacion)nuevaHab).esSalida()) {
                    gameOver = true;
                    victoria = true;
                    log.add("¡Has escapado! ¡Victoria!");
                }
            } else {
                log.add("La puerta no lleva a ningún sitio.");
            }
        }
    }

    private HabitacionModelo buscarHabitacionPorId(String id) {
        for (int i = 0; i < listaHabitaciones.getTamaño(); i++) {
            HabitacionModelo hab = listaHabitaciones.getDatoEn(i);
            if (hab.getId().equals(id)) return hab;
        }
        return null;
    }

    public void setTurnoManager(TurnoManager tm) { this.turnoManager = tm; }

    public int getTurnosRestantes() { return turnosRestantes; }
}