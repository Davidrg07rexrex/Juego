package mundo;

import modelo.Entidad;
import modelo.Posicion;

public class Enemigo extends Entidad {
    private String id;
    private int dañoBase;

    public Enemigo(String id, String nombre, int vida, int ataque, int defensa, int movimiento) {
        super(nombre, vida, ataque, defensa, new Posicion(0, 0)); // posición inicial (luego se asigna)
        this.id = id;
        this.dañoBase = ataque;
    }

    public String getId() { return id; }
    public int getDañoBase() { return dañoBase; }

    @Override
    public String toString() {
        return "Enemigo " + getNombre() + " [V:" + getVida() + " A:" + getAtaque() + " D:" + getDefensa() + "]";
    }
}