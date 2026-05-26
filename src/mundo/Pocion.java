package mundo;

// Representa una pocion que el jugador puede usar para recuperar vida
public class Pocion extends Objeto implements Comparable<Objeto> {
    private int bonusVida;

    // Constructor: crea una pocion
    public Pocion(String id, String nombre, int bonusVida) {
        super(id, nombre, "pocion");
        this.bonusVida = bonusVida;
        setUsosMaximos(1);  // Las pociones normalmente son de un solo uso
    }

    public int getBonusVida() { return bonusVida; }

    @Override
    public String getDescripcion() {
        return nombre + " (cura +" + bonusVida + " vida, " + usosRestantes + " uso(s))";
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
