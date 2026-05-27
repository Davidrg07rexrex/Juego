package logica;

import io.DatosPartida;
import io.DatosPartida.*;
import modelo.*;
import mundo.*;
import listas.*;

public class InicializadorJuego {

    public static JuegoReal cargarDesdeJSON(String rutaArchivo) {
        DatosPartida datos = DatosPartida.cargar(rutaArchivo, DatosPartida.class);
        if (datos == null) {
            System.out.println("Error al cargar el archivo JSON.");
            return null;
        }

        // 1. Lista de objetos disponibles (usamos nuestra lista enlazada)
        ListaSimplementeEnlazada<Objeto> listaObjetos = new ListaSimplementeEnlazada<>();
        for (DatosObjeto dobj : datos.objetosDisponibles) {
            Objeto obj = crearObjetoDesdeDatos(dobj);
            listaObjetos.add(obj);
        }

        // 2. Lista de plantillas de enemigos
        ListaSimplementeEnlazada<Enemigo> plantillasEnemigos = new ListaSimplementeEnlazada<>();
        for (DatosEnemigoPlantilla dep : datos.enemigosDisponibles) {
            Enemigo enemigo = new Enemigo(dep.id, dep.nombre, dep.vida, dep.ataque, dep.defensa, 1, dep.movimiento);
            plantillasEnemigos.add(enemigo);
        }

        // 3. Crear habitaciones (sin mapas)
        ListaSimplementeEnlazada<HabitacionModelo> listaHabitaciones = new ListaSimplementeEnlazada<>();
        for (DatosHabitacionCompleta dh : datos.habitaciones) {
            Habitacion hab = new Habitacion(dh.id, dh.nombre, dh.filas, dh.columnas);
            hab.setEsSalida(dh.esSalida);

            // Llamar a inicializarDesdeMatriz con listas en lugar de mapas
            hab.inicializarDesdeMatriz(dh.matriz, listaObjetos, plantillasEnemigos);
            listaHabitaciones.add(hab);
        }

        // 4. Posicionar enemigos del array "enemigos" en sus habitaciones
        ListaSimplementeEnlazada<Enemigo> todosLosEnemigos = new ListaSimplementeEnlazada<>();
        for (DatosEnemigoInfo ei : datos.enemigos) {
            // Buscar plantilla por id
            Enemigo plantilla = buscarEnemigoPorId(plantillasEnemigos, ei.id);
            if (plantilla != null) {
                Enemigo instancia = new Enemigo(plantilla.getId(), plantilla.getNombre(),
                        plantilla.getVida(), plantilla.getAtaque(), plantilla.getDefensa(),1,
                        plantilla.getMovimiento());
                instancia.setPosicion(new Posicion(ei.posicion.fila, ei.posicion.columna));
                // Buscar habitación por id
                Habitacion hab = (Habitacion) buscarHabitacionPorId(listaHabitaciones, ei.posicion.habitacion);
                if (hab != null) {
                    hab.colocarEnemigo(ei.posicion.fila, ei.posicion.columna, instancia);
                    todosLosEnemigos.add(instancia);
                }
            }
        }

        // 5. Grafo
        Grafo<HabitacionModelo> grafo = new Grafo<>();
        for (int i = 0; i < listaHabitaciones.getTamaño(); i++) {
            grafo.addNodo(listaHabitaciones.getDatoEn(i));
        }
        for (DatosConexion con : datos.grafo) {
            HabitacionModelo origen = buscarHabitacionPorId(listaHabitaciones, con.origen);
            HabitacionModelo destino = buscarHabitacionPorId(listaHabitaciones, con.destino);
            if (origen != null && destino != null) {
                grafo.addArista(origen, destino);
            }
        }

        // 6. Jugador
        Jugador jugador = new Jugador(datos.jugador.nombre, datos.jugador.vida, datos.jugador.ataque,
                datos.jugador.defensa,datos.jugador.velocidad ,new Posicion(datos.partida.fila, datos.partida.columna));
        jugador.setVidaMaxima(datos.jugador.vidaMaxima);

        // 7. Inventario inicial y llaves
        for (DatosObjeto dobj : datos.inventario) {
            Objeto obj = buscarObjetoPorId(listaObjetos, dobj.id);
            if (obj != null) {
                jugador.agregarAlInventario(obj);
                if (obj.getTipo().equals("llave")) {
                    jugador.agregarLlave(obj.getId());
                }
            }
        }

        // 8. Habitación inicial
        HabitacionModelo habInicial = buscarHabitacionPorId(listaHabitaciones, datos.partida.habitacionActual);

        // 9. JuegoReal
        JuegoReal juego = new JuegoReal(jugador, habInicial, grafo, listaHabitaciones, 30);

        // 10. TurnoManager solo con enemigos de la habitación inicial
        if (habInicial instanceof Habitacion) {
            ListaSimplementeEnlazada<Enemigo> enemigosIniciales = ((Habitacion) habInicial).getEnemigos();
            if (enemigosIniciales.getTamaño() > 0) {
                Enemigo[] arrayEnemigos = new Enemigo[enemigosIniciales.getTamaño()];
                for (int i = 0; i < enemigosIniciales.getTamaño(); i++) {
                    arrayEnemigos[i] = enemigosIniciales.getDatoEn(i);
                }
                TurnoManager tm = new TurnoManager(jugador, arrayEnemigos);
                juego.setTurnoManager(tm);
                juego.iniciarNuevoTurno();
            }
        }

        return juego;
    }

    // Métodos auxiliares de búsqueda (sin java.util)
    private static Objeto buscarObjetoPorId(ListaSimplementeEnlazada<Objeto> lista, String id) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            Objeto obj = lista.getDatoEn(i);
            if (obj.getId().equals(id)) return obj;
        }
        return null;
    }

    private static Enemigo buscarEnemigoPorId(ListaSimplementeEnlazada<Enemigo> lista, String id) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            Enemigo e = lista.getDatoEn(i);
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    private static HabitacionModelo buscarHabitacionPorId(ListaSimplementeEnlazada<HabitacionModelo> lista, String id) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            HabitacionModelo hab = lista.getDatoEn(i);
            if (hab.getId().equals(id)) return hab;
        }
        return null;
    }

    // Creación de objetos concretos desde los datos
    private static Objeto crearObjetoDesdeDatos(DatosObjeto dobj) {
        switch (dobj.tipo) {
            case "arma":
                Arma arma = new Arma(dobj.id, dobj.nombre, dobj.bonusAtaque, dobj.rango, dobj.slot);
                if (dobj.habilidad != null) arma.setHabilidad(dobj.habilidad);
                if (dobj.probabilidad > 0) arma.setProbabilidad(dobj.probabilidad);
                return arma;
            case "pocion":
                return new Pocion(dobj.id, dobj.nombre, dobj.bonusVida);
            case "llave":
                return new Llave(dobj.id, dobj.nombre);
            default:
                // Objeto genérico (como los que usas en el Dummy)
                return new ObjetoGenerico(dobj.id, dobj.nombre, dobj.tipo);
        }
    }

    // Clase auxiliar para objetos genéricos (necesaria porque Objeto es abstracta)
    private static class ObjetoGenerico extends Objeto {
        public ObjetoGenerico(String id, String nombre, String tipo) {
            super(id, nombre, tipo);
        }
        @Override public String getDescripcion() { return nombre; }
        @Override public String toString() { return nombre; }
    }
}
