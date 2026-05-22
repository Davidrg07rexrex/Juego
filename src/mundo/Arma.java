package mundo;

/**
 * CLASE ARMA
 * 
 * Representa un arma que el jugador puede equipar.
 * Hereda de Objeto.
 * 
 * Tiene:
 * - bonusAtaque: puntos extra que suma al ataque del jugador
 * - rango: 1 = cuerpo a cuerpo, 2+ = distancia
 * - habilidad: efecto especial ("ninguna", "area", "doble_ataque", etc.)
 * - probabilidad: % de que se active la habilidad al atacar
 */
public class Arma extends Objeto implements Comparable<Objeto> {
    private int bonusAtaque;
    private int rango;
    private String habilidad;
    private int probabilidad;

    /**
     * Constructor: crea un arma con sus atributos basicos
     * Por defecto es equipable en mano_derecha y cuerpo a cuerpo
     */
    public Arma(String id, String nombre, int bonusAtaque, int rango, String slot) {
        super(id, nombre, "arma");
        this.bonusAtaque = bonusAtaque;
        this.rango = rango;
        this.habilidad = "ninguna";
        this.probabilidad = 0;
        setEquipable(true, slot);  // Las armas siempre son equipables
    }

    // ----- GETTERS Y SETTERS -----
    public int getBonusAtaque() { return bonusAtaque; }
    public int getRango() { return rango; }
    public String getHabilidad() { return habilidad; }
    public int getProbabilidad() { return probabilidad; }

    public void setHabilidad(String habilidad) { this.habilidad = habilidad; }
    public void setProbabilidad(int probabilidad) { this.probabilidad = probabilidad; }

    /**
     * Comprueba si la habilidad especial se activa
     * Genera un numero aleatorio y si es menor que la probabilidad, se activa
     */
    public boolean intentarHabilidad() {
        if (habilidad.equals("ninguna")) return false;  // No tiene habilidad
        int numeroAleatorio = (int)(Math.random() * 100);  // 0 a 99
        return numeroAleatorio < probabilidad;
    }

    @Override
    public String getDescripcion() {
        String desc = nombre + " (ATK+" + bonusAtaque + ", rango " + rango + ")";
        if (!habilidad.equals("ninguna")) {
            desc += " [" + habilidad + " " + probabilidad + "%]";
        }
        return desc;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
