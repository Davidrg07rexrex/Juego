package mundo;

public class Objeto {
    protected String id;
    protected String nombre;
    protected String tipo;
    protected boolean equipable;
    protected String slot;

    public Objeto(String id, String nombre, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.equipable = false;
        this.slot = null;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }

    public void setEquipable(boolean equipable, String slot) {
        this.equipable = equipable;
        this.slot = slot;
    }

    public boolean isEquipable() { return equipable; }
    public String getSlot() { return slot; }

    public String getDescripcion() {
        return nombre;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}