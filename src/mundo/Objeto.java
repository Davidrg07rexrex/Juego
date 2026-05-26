package mundo;

// Clase abstracta base para todos los objetos del juego
public abstract class Objeto implements Comparable<Objeto> {
    protected String id;           // Identificador unico del objeto
    protected String nombre;       // Nombre para mostrar
    protected String tipo;         // "arma", "pocion", "llave", etc.
    protected boolean equipable;   // Si se puede equipar en un slot
    protected String slot;         // Donde se equipa: "mano_derecha", "torso", etc.
    protected int usosMaximos;     // -1 si es infinito, otro numero si tiene usos limitados
    protected int usosRestantes;   // Usos que quedan

    // Constructor: crea un objeto con sus datos basicos
    public Objeto(String id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.equipable = false;
        this.slot = null;
        this.usosMaximos = -1;    // Por defecto usos infinitos
        this.usosRestantes = -1;
    }

    // ----- GETTERS -----
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public boolean isEquipable() { return equipable; }
    public String getSlot() { return slot; }
    public int getUsosRestantes() { return usosRestantes; }

    // Marca el objeto como equipable y en que slot
    public void setEquipable(boolean equipable, String slot) {
        this.equipable = equipable;
        this.slot = slot;
    }

    // Establece el numero maximo de usos (y los restantes igual)
    public void setUsosMaximos(int usosMaximos) {
        this.usosMaximos = usosMaximos;
        this.usosRestantes = usosMaximos;
    }

    // Usar el objeto: consume un uso si tiene limites
    public boolean usar() {
        if (usosRestantes == 0) return false;  // Ya no quedan usos
        if (usosRestantes > 0) {
            usosRestantes--;  // Consumimos un uso
            if (usosRestantes == 0) return false;  // Se acabo
        }
        return true;  // Usos infinitos o aun quedan
    }

    // Metodo abstracto: cada subclase da su propia descripcion
    public abstract String getDescripcion();

    @Override
    public int compareTo(Objeto otro) {
        return this.id.compareTo(otro.id);
    }

    @Override
    public abstract String toString();
}
