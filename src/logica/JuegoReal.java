package logica;

import controlador.GameControllerModel;
import modelo.*;
import listas.*;
import modelo.Posicion;
import mundo.*;

public class JuegoReal implements GameControllerModel {

    private Grafo<HabitacionModelo> grafo;
    private ListaSimplementeEnlazada<HabitacionModelo> listaHabitaciones; // en lugar de Map
    private TurnoManager turnoManager;
    private HabitacionModelo habitacionActual;   // la implementación real de Persona 2
    private Jugador jugador;
    private ListaSimplementeEnlazada<String> log;
    private boolean gameOver;

    public JuegoReal(Jugador jugador, HabitacionModelo inicial,
                     Grafo<HabitacionModelo> grafo,
                     ListaSimplementeEnlazada<HabitacionModelo> listaHabitaciones) {
        this.jugador = jugador;
        this.habitacionActual = inicial;
        this.grafo = grafo;
        this.listaHabitaciones = listaHabitaciones;
        this.log = new ListaSimplementeEnlazada<>();
        this.gameOver = false;
    }

    // ---------- Métodos de GameControllerModel ----------
    @Override
    public HabitacionModelo getCurrentRoom() {
        return habitacionActual;
    }

    @Override
    public Jugador getPlayer() {
        return jugador;
    }

    @Override
    public ListaSimplementeEnlazada<Objeto> getInventory() {
        // Suponiendo que Jugador tenga un inventario con tu lista enlazada
        return jugador.getInventario(); // deberás añadir este método a Jugador
    }

    @Override
    public ListaSimplementeEnlazada<String> getEventLog() {
        return log;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public boolean movePlayer(Direction dir) {
        if (gameOver || turnoManager == null) return false;
        if (turnoManager.getEntidadActual() != jugador) return false;

        Posicion nueva = jugador.getPosicion().mover(dir);
        if (nueva.getFila() >= 0 && nueva.getFila() < getCurrentRoomRows()
                && nueva.getColumna() >= 0 && nueva.getColumna() < getCurrentRoomCols()
                && habitacionActual.esTransitable(nueva.getFila(), nueva.getColumna())) {

            jugador.setPosicion(nueva);
            log.add("Movido a " + nueva);

            // Daño por trampa
            if (habitacionActual.esTrampa(nueva.getFila(), nueva.getColumna())) {
                int danio = habitacionActual.getDanioTrampa(nueva.getFila(), nueva.getColumna());
                jugador.recibirDanio(danio);
                log.add("¡Has caído en una trampa! Recibes " + danio + " de daño.");
                if (!jugador.estaVivo()) {
                    gameOver = true;
                    log.add("Has muerto por una trampa.");
                }
            }

            intentarCambiarHabitacion();   // <--- CAMBIO DE SALA
            return true;
        }
        log.add("Movimiento inválido hacia " + dir);
        return false;
    }

    @Override
    public boolean pickItem(Posicion pos) {
        if (gameOver || turnoManager == null) return false;
        if (turnoManager.getEntidadActual() != jugador) return false;

        Objeto obj = habitacionActual.getObjetoEn(pos.getFila(), pos.getColumna());
        if (obj == null) {
            log.add("No hay ningún objeto en " + pos);
            return false;
        }

        // Verificar que el jugador está adyacente (opcional, según diseño; asumimos que sí)
        Posicion pJugador = jugador.getPosicion();
        int dist = Math.abs(pJugador.getFila() - pos.getFila()) + Math.abs(pJugador.getColumna() - pos.getColumna());
        if (dist > 1) {
            log.add("Estás demasiado lejos para recoger el objeto.");
            return false;
        }

        jugador.agregarAlInventario(obj);
        habitacionActual.eliminarObjeto(pos.getFila(), pos.getColumna());
        log.add("Has recogido " + obj.getNombre());
        return true;
    }

    @Override
    public boolean useItem(Objeto item) {
        if (gameOver || turnoManager == null) return false;
        if (turnoManager.getEntidadActual() != jugador) return false;

        // Buscar el objeto en el inventario del jugador
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
                int curacion = 20; // podrías leerlo de algún atributo extra si lo tuvieras
                int nuevaVida = jugador.getVida() + curacion;
                if (nuevaVida > 100) nuevaVida = 100;
                jugador.setVida(nuevaVida);
                log.add("Usas " + item.getNombre() + " y recuperas " + curacion + " puntos de vida.");
                break;
            case "arma":
                // Equipamiento simple: aumenta ataque temporalmente
                int bonus = 5; // idealmente obtenerlo de item (ej. ((Arma)item).getBonusAtaque())
                jugador.setAtaque(jugador.getAtaque() + bonus);
                log.add("Equipas " + item.getNombre() + " (Ataque +" + bonus + ").");
                // No eliminamos el arma del inventario (se equipa)
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
        // Delegar en Persona 2 (PersistenciaJSON)
        log.add("Guardar partida en " + file + " (pendiente)");
    }

    @Override
    public void loadGame(String file) {
        // Delegar en Persona 2
        log.add("Cargar partida desde " + file + " (pendiente)");
    }

    @Override
    public int getCurrentRoomRows() {
        return habitacionActual.getFilas();
    }

    @Override
    public int getCurrentRoomCols() {
        return habitacionActual.getColumnas();
    }

    @Override
    public String getCellSymbol(int row, int col) {
        // Prioridad: jugador > enemigo > objeto > puerta > vacío
        Posicion pJugador = jugador.getPosicion();
        if (pJugador.getFila() == row && pJugador.getColumna() == col) {
            return "J";
        }
        // El resto de la consulta se delega en Habitacion (Persona 2)
        return habitacionActual.getSimbolo(row, col); // Persona 2 debe crear este método
    }
    private HabitacionModelo buscarHabitacionPorId(String id) {
        for (int i = 0; i < listaHabitaciones.getTamaño(); i++) {
            HabitacionModelo hab = listaHabitaciones.getDatoEn(i);
            if (hab.getId().equals(id)) {
                return hab;
            }
        }
        return null;
    }
    private void intentarCambiarHabitacion() {
        Posicion p = jugador.getPosicion();
        String destino = habitacionActual.getDestinoPuerta(p.getFila(), p.getColumna());
        if (destino != null) {
            // Verificar llave si es necesaria
            if (habitacionActual.puertaNecesitaLlave(p.getFila(), p.getColumna())) {
                String idLlave = habitacionActual.getIdLlavePuerta(p.getFila(), p.getColumna());
                if (!jugador.tieneLlave(idLlave)) {
                    log.add("La puerta necesita la llave: " + idLlave);
                    return;
                }
            }
            HabitacionModelo nuevaHab = buscarHabitacionPorId(destino);
            if (nuevaHab != null) {
                this.habitacionActual = nuevaHab;
                log.add("Atraviesas la puerta hacia " + nuevaHab.getId());
            } else {
                log.add("La puerta no lleva a ningún sitio.");
            }
        }
    }

    @Override
    public boolean attack(Direction dir) {
        if (gameOver || turnoManager == null) return false;
        if (turnoManager.getEntidadActual() != jugador) return false;

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

        if (!(actual instanceof Jugador)) {
            // Si es un enemigo, ejecutar su IA inmediatamente
            ejecutarTurnoIA();
        }
        // Si es el jugador, esperará acciones del usuario a través de la UI.
    }

    public void ejecutarTurnoIA() {
        if (turnoManager == null || gameOver) return;
        Entidad actual = turnoManager.getEntidadActual();
        if (actual == null || actual instanceof Jugador) return;

        // Es un enemigo, ejecutar su IA
        IAEnemigo.ejecutar(actual, jugador, habitacionActual, turnoManager, log);

        // Comprobar si el jugador murió en este ataque
        if (!jugador.estaVivo()) {
            gameOver = true;
            log.add("Fin del juego. Has sido derrotado.");
        }

        // Finalizar el turno del enemigo
        turnoManager.finalizarTurno();
    }
    public void setTurnoManager(TurnoManager tm) {
        this.turnoManager = tm;
    }
}