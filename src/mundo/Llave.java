package mundo;

// Representa una llave que permite abrir puertas especificas
public class Llave extends Objeto implements Comparable<Objeto> {

    // Constructor: crea una llave
    public Llave(String id, String nombre) {
        super(id, nombre, "llave");
        // Las llaves por defecto no son equipables (se usan desde el inventario)
    }

    @Override
    public String getDescripcion() {
        return nombre + " (Llave)";
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
