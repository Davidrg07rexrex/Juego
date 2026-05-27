package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Datos de una partida del juego
public class DatosPartida {

    // Datos del jugador
    public static class DatosJugador {
        public String nombre;
        public int vida;
        public int vidaMaxima;
        public int ataque;
        public int defensa;
        public int velocidad;
        public int movimiento;

        // Necesario para Gson
        public DatosJugador() {}
    }

    // Datos de la partida
    public static class DatosPartidaInfo {
        public String habitacionActual;
        public int fila;
        public int columna;
        public int turnosRestantes;
        public int turnosMaximos;

        // Necesario para Gson
        public DatosPartidaInfo() {}
    }

    // Datos de un objeto (arma, pocion, llave...)
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

        // Necesario para Gson
        public DatosObjeto() {}
    }

    // Slots de equipamiento del jugador
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

        // Necesario para Gson
        public DatosEquipamiento() {}
    }

    // Datos de una celda del mapa
    public static class DatosCelda {
        public String tipo;
        public int fila;
        public int columna;
        public boolean accesible;
        public Object contenido;

        // Necesario para Gson
        public DatosCelda() {}
    }

    // Posicion en el mapa
    public static class DatosPosicion {
        public String habitacion;
        public int fila;
        public int columna;

        // Necesario para Gson
        public DatosPosicion() {}
    }

    // Datos de un enemigo colocado en el mapa
    public static class DatosEnemigoInfo {
        public String id;
        public String nombre;
        public int vida;
        public int ataque;
        public int defensa;
        public int movimiento;
        public DatosPosicion posicion;

        // Necesario para Gson
        public DatosEnemigoInfo() {}
    }

    // Datos completos de una habitacion
    public static class DatosHabitacionCompleta {
        public String id;
        public String nombre;
        public int filas;
        public int columnas;
        public boolean esSalida;
        public DatosCelda[][] matriz;

        // Necesario para Gson
        public DatosHabitacionCompleta() {}
    }

    // Conexion entre habitaciones
    public static class DatosConexion {
        public String origen;
        public String destino;

        // Necesario para Gson
        public DatosConexion() {}
    }

    // Plantilla de enemigo
    public static class DatosEnemigoPlantilla implements Comparable<DatosEnemigoPlantilla> {
        public String id;
        public String nombre;
        public int vida;
        public int ataque;
        public int defensa;
        public int velocidad;
        public int movimiento;

        // Necesario para Gson
        public DatosEnemigoPlantilla() {}

        // Ordena por id
        @Override
        public int compareTo(DatosEnemigoPlantilla otro) {
            return this.id.compareTo(otro.id);
        }
    }

    // Campos principales
    public DatosJugador jugador;
    public DatosPartidaInfo partida;
    public DatosObjeto[] inventario;
    public DatosEquipamiento equipamiento;
    public DatosObjeto[] objetosDisponibles;
    public DatosEnemigoPlantilla[] enemigosDisponibles;
    public DatosHabitacionCompleta[] habitaciones;
    public DatosEnemigoInfo[] enemigos;
    public DatosConexion[] grafo;

    // Necesario para Gson
    public DatosPartida() {}

    // Guarda un objeto en JSON
    public static <T> void guardar(String ruta, T objeto) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(objeto, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + ruta + ": " + e.getMessage());
        }
    }

    // Carga un objeto desde JSON
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
