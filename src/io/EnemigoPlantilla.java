package io;

import mundo.Enemigo;

/**
 * Plantilla para crear enemigos a partir de datos JSON.
 * 
 * Almacena las caracteristicas de un tipo de enemigo (vida, ataque,
 * defensa, movimiento) y permite crear nuevas instancias con esos
 * valores a traves del metodo crearEnemigo().
 * 
 * Esto evita tener que escribir los mismos valores cada vez que
 * aparece un enemigo del mismo tipo en el juego.
 */
public class EnemigoPlantilla implements Comparable<EnemigoPlantilla> {
    private String id;
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private int movimiento;

    public EnemigoPlantilla(String id, String nombre, int vida, int ataque, int defensa, int movimiento) {
        this.id = id;
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.movimiento = movimiento;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getMovimiento() { return movimiento; }

    /**
     * Crea una nueva instancia de Enemigo con los valores de esta plantilla
     */
    public Enemigo crearEnemigo() {
        return new Enemigo(id, nombre, vida, ataque, defensa, movimiento);
    }

    @Override
    public int compareTo(EnemigoPlantilla otro) {
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString() {
        return "Plantilla: " + nombre + " (V:" + vida + " A:" + ataque + " D:" + defensa + ")";
    }
}
