package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que guarda todos los datos de una partida del juego.
 * Sigue el mismo patron que el ejemplo de clase con Usuario:
 * usa metodos genericos guardar() y cargar() con Gson.
 * Las subclases internas son POJOs simples con constructor vacio
 * (necesario para que Gson funcione correctamente).
 */
public class DatosPartida {

    /**
     * Datos del jugador: nombre y estadisticas basicas.
     */
    public static class DatosJugador {
        public String nombre;
        public int vida;
        public int vidaMaxima;
        public int ataque;
        public int defensa;
        public int movimiento;

        /** Constructor vacio que necesita Gson */
        public DatosJugador() {}
    }

    /**
     * Datos de la partida: habitacion actual, posicion del jugador
     * y turnos restantes.
     */
    public static class DatosPartidaInfo {
        public String habitacionActual;
        public int fila;
        public int columna;
        public int turnosRestantes;
        public int turnosMaximos;

        /** Constructor vacio que necesita Gson */
        public DatosPartidaInfo() {}
    }

    /**
     * Datos de un objeto del juego (arma, pocion, llave, etc.).
     */
    public static class DatosObjeto {
        public String id;
        public String nombre;
        public String tipo;
        public int bonusAtaque;
        public int bonusDefensa;
        public int bonusVida;
        public int rango;
        public boolean equipable;
        public String slot;
        public String habilidad;
        public int probabilidad;
        public int usosMaximos;
        public int usosRestantes;

        /** Constructor vacio que necesita Gson */
        public DatosObjeto() {}
    }

    /**
     * Equipamiento del jugador: todos los slots (manos, torso, cabeza, dedos).
     */
    public static class DatosEquipamiento {
        public String mano_derecha;
        public String mano_izquierda;
        public String torso;
        public String cabeza;
        public String dedo1;
        public String dedo2;
        public String dedo3;
        public String dedo4;
        public String dedo5;
        public String dedo6;
        public String dedo7;
        public String dedo8;
        public String dedo9;
        public String dedo10;

        /** Constructor vacio que necesita Gson */
        public DatosEquipamiento() {}
    }

    /**
     * Datos de una celda del mapa: tipo, posicion y contenido.
     */
    public static class DatosCelda {
        public String tipo;
        public int fila;
        public int columna;
        public boolean accesible;
        public Object contenido;

        /** Constructor vacio que necesita Gson */
        public DatosCelda() {}
    }

    /**
     * Posicion en el mapa: habitacion, fila y columna.
     */
    public static class DatosPosicion {
        public String habitacion;
        public int fila;
        public int columna;

        /** Constructor vacio que necesita Gson */
        public DatosPosicion() {}
    }

    /**
     * Datos de un enemigo ya colocado en el mapa, con su posicion.
     */
    public static class DatosEnemigoInfo {
        public String id;
        public String nombre;
        public int vida;
        public int ataque;
        public int defensa;
        public int movimiento;
        public DatosPosicion posicion;

        /** Constructor vacio que necesita Gson */
        public DatosEnemigoInfo() {}
    }

    /**
     * Datos completos de una habitacion con su matriz de celdas.
     */
    public static class DatosHabitacionCompleta {
        public String id;
        public String nombre;
        public int filas;
        public int columnas;
        public boolean esSalida;
        public DatosCelda[][] matriz;

        /** Constructor vacio que necesita Gson */
        public DatosHabitacionCompleta() {}
    }

    /**
     * Conexion entre dos habitaciones (arista del grafo).
     */
    public static class DatosConexion {
        public String origen;
        public String destino;

        /** Constructor vacio que necesita Gson */
        public DatosConexion() {}
    }

    /**
     * Plantilla de enemigo disponible en la partida.
     * Implementa Comparable para poder ordenar por id.
     */
    public static class DatosEnemigoPlantilla implements Comparable<DatosEnemigoPlantilla> {
        public String id;
        public String nombre;
        public int vida;
        public int ataque;
        public int defensa;
        public int movimiento;

        /** Constructor vacio que necesita Gson */
        public DatosEnemigoPlantilla() {}

        /** Ordena por id para poder comparar plantillas */
        @Override
        public int compareTo(DatosEnemigoPlantilla otro) {
            return this.id.compareTo(otro.id);
        }
    }

    // Campos principales de DatosPartida

    /** Datos del jugador */
    public DatosJugador jugador;

    /** Datos de la partida (habitacion, turnos, etc.) */
    public DatosPartidaInfo partida;

    /** Objetos que lleva el jugador en el inventario */
    public DatosObjeto[] inventario;

    /** Objetos equipados por el jugador */
    public DatosEquipamiento equipamiento;

    /** Todos los objetos disponibles en la partida */
    public DatosObjeto[] objetosDisponibles;

    /** Todas las plantillas de enemigos disponibles */
    public DatosEnemigoPlantilla[] enemigosDisponibles;

    /** Todas las habitaciones del mapa */
    public DatosHabitacionCompleta[] habitaciones;

    /** Todos los enemigos colocados en el mapa */
    public DatosEnemigoInfo[] enemigos;

    /** Conexiones entre habitaciones (grafo) */
    public DatosConexion[] grafo;

    /** Constructor vacio que necesita Gson */
    public DatosPartida() {}

    /**
     * Metodo generico para guardar un objeto en un archivo JSON.
     * Igual que el ejemplo de clase con Usuario:
     * usa GsonBuilder con setPrettyPrinting para que el JSON sea legible
     * y FileWriter con try-with-resources para asegurar que se cierra.
     *
     * @param <T>    tipo del objeto a guardar
     * @param ruta   ruta del archivo donde se guardara
     * @param objeto objeto que se quiere guardar
     */
    public static <T> void guardar(String ruta, T objeto) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + ruta + ": " + e.getMessage());
        }
    }

    /**
     * Metodo generico para cargar un objeto desde un archivo JSON.
     * Igual que el ejemplo de clase con Usuario:
     * usa Gson simple y FileReader con try-with-resources.
     *
     * @param <T>   tipo del objeto a cargar
     * @param ruta  ruta del archivo JSON
     * @param clase clase del objeto que se quiere cargar
     * @return      el objeto cargado, o null si hay error
     */
    public static <T> T cargar(String ruta, Class<T> clase) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, clase);
        } catch (IOException e) {
            System.err.println("Error al cargar " + ruta + ": " + e.getMessage());
            return null;
        }
    }

}
