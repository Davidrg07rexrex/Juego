package logica;

import controlador.GameControllerModel;
import io.DatosPartida;
import io.DatosPartida.*;
import modelo.*;
import listas.*;
import mundo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    // Seguimiento de objetos recogidos del suelo (para guardado)
    private java.util.ArrayList<String> objetosRecogidos = new java.util.ArrayList<>();

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
                || objetivo.getColumna() < 0 || objetivo.getColumna() >= getCurrentRoomCols()) {
            log.add("No puedes atacar fuera del mapa.");
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
        objetosRecogidos.add(habitacionActual.getId() + ":" + pos.getFila() + ":" + pos.getColumna());
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

    // ---------- SAVE / LOAD ----------
    // DTO para guardar el estado completo
    public static class DatosGuardado {
        public DatosJugador jugador;
        public DatosPartidaInfo partida;
        public DatosObjeto[] inventario;
        public DatosEquipamiento equipamiento;
        public DatosEnemigoInfo[] enemigos;
        public String[] celdasVacias;
    }

    @Override
    public void saveGame(String file) {
        try {
            DatosGuardado dg = new DatosGuardado();

            dg.jugador = new DatosJugador();
            dg.jugador.nombre = jugador.getNombre();
            dg.jugador.vida = jugador.getVida();
            dg.jugador.vidaMaxima = jugador.getVidaMaxima();
            dg.jugador.ataque = jugador.getAtaque();
            dg.jugador.defensa = jugador.getDefensa();
            dg.jugador.velocidad = jugador.getVelocidad();
            dg.jugador.movimiento = jugador.getVelocidad();

            dg.partida = new DatosPartidaInfo();
            dg.partida.habitacionActual = habitacionActual.getId();
            dg.partida.fila = jugador.getPosicion().getFila();
            dg.partida.columna = jugador.getPosicion().getColumna();
            dg.partida.turnosRestantes = turnosRestantes;

            ListaSimplementeEnlazada<Objeto> inv = jugador.getInventario();
            dg.inventario = new DatosObjeto[inv.getTamaño()];
            for (int i = 0; i < inv.getTamaño(); i++) {
                dg.inventario[i] = objetoToDatos(inv.getDatoEn(i));
            }

            dg.equipamiento = new DatosEquipamiento();

            dg.celdasVacias = objetosRecogidos.toArray(new String[0]);

            int totalEnemigos = 0;
            for (int h = 0; h < listaHabitaciones.getTamaño(); h++) {
                HabitacionModelo habModelo = listaHabitaciones.getDatoEn(h);
                if (habModelo instanceof Habitacion) {
                    Habitacion hab = (Habitacion) habModelo;
                    for (int f = 0; f < hab.getFilas(); f++) {
                        for (int c = 0; c < hab.getColumnas(); c++) {
                            if (hab.getCelda(f, c) != null && hab.getCelda(f, c).getTipo().equals("enemigo")
                                    && hab.getCelda(f, c).getContenido() instanceof Enemigo) {
                                totalEnemigos++;
                            }
                        }
                    }
                }
            }
            dg.enemigos = new DatosEnemigoInfo[totalEnemigos];
            int idx = 0;
            for (int h = 0; h < listaHabitaciones.getTamaño(); h++) {
                HabitacionModelo habModelo = listaHabitaciones.getDatoEn(h);
                if (habModelo instanceof Habitacion) {
                    Habitacion hab = (Habitacion) habModelo;
                    for (int f = 0; f < hab.getFilas(); f++) {
                        for (int c = 0; c < hab.getColumnas(); c++) {
                            Celda celda = hab.getCelda(f, c);
                            if (celda == null) continue;
                            if (celda.getTipo().equals("enemigo") && celda.getContenido() instanceof Enemigo) {
                                Enemigo e = (Enemigo) celda.getContenido();
                                dg.enemigos[idx] = new DatosEnemigoInfo();
                                dg.enemigos[idx].id = e.getId();
                                dg.enemigos[idx].nombre = e.getNombre();
                                dg.enemigos[idx].vida = e.getVida();
                                dg.enemigos[idx].ataque = e.getAtaque();
                                dg.enemigos[idx].defensa = e.getDefensa();
                                dg.enemigos[idx].movimiento = e.getMovimiento();
                                dg.enemigos[idx].posicion = new DatosPosicion();
                                dg.enemigos[idx].posicion.habitacion = hab.getId();
                                dg.enemigos[idx].posicion.fila = f;
                                dg.enemigos[idx].posicion.columna = c;
                                idx++;
                            }
                        }
                    }
                }
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(dg, writer);
            }
            log.add("Partida guardada en " + file);
        } catch (Exception e) {
            log.add("Error al guardar partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadGame(String file) {
        try {
            java.io.File archivoGuardado = new java.io.File(file);
            if (!archivoGuardado.exists()) {
                log.add("Error: no existe el archivo de guardado: " + file);
                return;
            }

            Gson gson = new Gson();
            DatosGuardado dg;
            try (FileReader reader = new FileReader(file)) {
                dg = gson.fromJson(reader, DatosGuardado.class);
            }
            if (dg == null) {
                log.add("Error: el archivo " + file + " esta vacio o es invalido");
                return;
            }

            jugador.setVida(dg.jugador.vida);
            jugador.setVidaMaxima(dg.jugador.vidaMaxima);
            jugador.setAtaque(dg.jugador.ataque);
            jugador.setDefensa(dg.jugador.defensa);
            jugador.setVelocidad(dg.jugador.velocidad);

            Posicion pos = new Posicion(dg.partida.fila, dg.partida.columna);
            jugador.setPosicion(pos);

            for (int i = 0; i < listaHabitaciones.getTamaño(); i++) {
                HabitacionModelo hab = listaHabitaciones.getDatoEn(i);
                if (hab.getId().equals(dg.partida.habitacionActual)) {
                    this.habitacionActual = hab;
                    break;
                }
            }

            this.turnosRestantes = dg.partida.turnosRestantes;

            if (dg.celdasVacias != null) {
                for (String s : dg.celdasVacias) {
                    String[] parts = s.split(":");
                    if (parts.length == 3) {
                        for (int h = 0; h < listaHabitaciones.getTamaño(); h++) {
                            HabitacionModelo hm = listaHabitaciones.getDatoEn(h);
                            if (hm instanceof Habitacion && hm.getId().equals(parts[0])) {
                                int f = Integer.parseInt(parts[1]);
                                int c = Integer.parseInt(parts[2]);
                                ((Habitacion) hm).eliminarObjeto(f, c);
                                break;
                            }
                        }
                    }
                }
                objetosRecogidos.clear();
                for (String s : dg.celdasVacias) {
                    objetosRecogidos.add(s);
                }
            }

            ListaSimplementeEnlazada<Objeto> inv = jugador.getInventario();
            int tamOriginal = inv.getTamaño();
            for (int i = 0; i < tamOriginal; i++) {
                inv.remove(inv.getDatoEn(0));
            }
            if (dg.inventario != null) {
                for (DatosObjeto dobj : dg.inventario) {
                    Objeto obj = crearObjetoDesdeDatos(dobj);
                    if (obj != null) {
                        jugador.agregarAlInventario(obj);
                        if (dobj.tipo != null && dobj.tipo.equals("llave")) {
                            jugador.agregarLlave(obj.getId());
                        }
                    }
                }
            }

            if (dg.enemigos != null) {
                for (DatosEnemigoInfo ei : dg.enemigos) {
                    for (int h = 0; h < listaHabitaciones.getTamaño(); h++) {
                        HabitacionModelo habModelo = listaHabitaciones.getDatoEn(h);
                        if (habModelo instanceof Habitacion && habModelo.getId().equals(ei.posicion.habitacion)) {
                            Habitacion hab = (Habitacion) habModelo;
                            Celda celda = hab.getCelda(ei.posicion.fila, ei.posicion.columna);
                            if (celda != null && celda.getContenido() instanceof Enemigo) {
                                Enemigo e = (Enemigo) celda.getContenido();
                                e.setVida(ei.vida);
                                if (ei.vida <= 0) {
                                    hab.eliminarEnemigo(ei.posicion.fila, ei.posicion.columna);
                                }
                            }
                            break;
                        }
                    }
                }
            }

            reconfigurarTurnos();

            Posicion pJug = jugador.getPosicion();
            celdasAlcanzables = Movimiento.celdasAlcanzables(
                    habitacionActual,
                    pJug.getFila(),
                    pJug.getColumna(),
                    jugador.getVelocidad()
            );

            gameOver = false;
            victoria = false;

            log.add("Partida cargada desde " + file);
        } catch (Exception e) {
            log.add("Error al cargar partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DatosObjeto objetoToDatos(Objeto obj) {
        DatosObjeto d = new DatosObjeto();
        d.id = obj.getId();
        d.nombre = obj.getNombre();
        d.tipo = obj.getTipo();
        if (obj instanceof Arma) {
            Arma a = (Arma) obj;
            d.bonusAtaque = a.getBonusAtaque();
            d.bonusDefensa = a.getBonusDefensa();
            d.rango = a.getRango();
            d.slot = a.getSlot();
            d.habilidad = a.getHabilidad();
            d.probabilidad = a.getProbabilidad();
            d.equipable = true;
        } else if (obj instanceof Pocion) {
            Pocion p = (Pocion) obj;
            d.bonusVida = p.getBonusVida();
            d.equipable = false;
            d.usosMaximos = 1;
            d.usosRestantes = 1;
        }
        return d;
    }

    private Objeto crearObjetoDesdeDatos(DatosObjeto dobj) {
        if (dobj == null || dobj.tipo == null) return null;
        switch (dobj.tipo) {
            case "arma":
                Arma arma = new Arma(dobj.id, dobj.nombre, dobj.bonusAtaque, dobj.rango, dobj.slot);
                if (dobj.habilidad != null) arma.setHabilidad(dobj.habilidad);
                if (dobj.probabilidad > 0) arma.setProbabilidad(dobj.probabilidad);
                arma.setBonusDefensa(dobj.bonusDefensa);
                return arma;
            case "pocion":
                return new Pocion(dobj.id, dobj.nombre, dobj.bonusVida);
            case "llave":
                return new Llave(dobj.id, dobj.nombre);
            default:
                return null;
        }
    }

    // Getter para todas las habitaciones (lo necesita GameUIController tras load)
    public ListaSimplementeEnlazada<HabitacionModelo> getListaHabitaciones() {
        return listaHabitaciones;
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
            iniciarNuevoTurno();   // arranca el siguiente turno (enemigo o jugador)
        }
    }

    private void ejecutarTurnoIA() {
        if (turnoManager == null || gameOver) return;

        Entidad actual = turnoManager.getEntidadActual();

        if (actual == null) {
            gameOver = true;
            log.add("No quedan entidades. Fin del juego.");
            return;
        }

        if (actual instanceof Jugador) {
            return;
        }

        IAEnemigo.ejecutar(actual, jugador, habitacionActual, turnoManager, log);

        if (!jugador.estaVivo()) {
            gameOver = true;
            log.add("Fin del juego. Has sido derrotado.");
            return;
        }

        turnoManager.finalizarTurno();

        // Después del turno del enemigo, avanzar al siguiente turno
        iniciarNuevoTurno();
    }

    private void intentarCambiarHabitacion() {
        Posicion p = jugador.getPosicion();
        String destino = habitacionActual.getDestinoPuerta(p.getFila(), p.getColumna());
        if (destino != null) {
            // Verificar llave
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
                reconfigurarTurnos();                    // ← AÑADIR ESTA LÍNEA
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

    public void setTurnoManager(TurnoManager tm) { this.turnoManager = tm; }

    public int getTurnosRestantes() { return turnosRestantes; }
    private void reconfigurarTurnos() {
        if (habitacionActual instanceof Habitacion) {
            ListaSimplementeEnlazada<Enemigo> enemigosHab = ((Habitacion) habitacionActual).getEnemigos();
            Enemigo[] array = new Enemigo[enemigosHab.getTamaño()];
            for (int i = 0; i < enemigosHab.getTamaño(); i++) {
                array[i] = enemigosHab.getDatoEn(i);
            }
            TurnoManager nuevoTM = new TurnoManager(jugador, array);
            setTurnoManager(nuevoTM);
            nuevoTM.iniciarTurno();  // empieza el turno del jugador en la nueva sala
        }
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
}