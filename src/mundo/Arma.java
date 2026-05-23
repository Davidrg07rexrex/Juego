package mundo;

public class Arma extends Objeto {
    private int bonusAtaque;
    private int rango;
    private String habilidad;
    private int probabilidad;

    public Arma(String id, String nombre, int bonusAtaque, int rango, String slot) {
        super(id, nombre, "arma");
        this.bonusAtaque = bonusAtaque;
        this.rango = rango;
        this.habilidad = "ninguna";
        this.probabilidad = 0;
        setEquipable(true, slot);
    }

    public int getBonusAtaque() { return bonusAtaque; }
    public int getRango() { return rango; }
    public String getHabilidad() { return habilidad; }
    public int getProbabilidad() { return probabilidad; }

    public void setHabilidad(String habilidad) { this.habilidad = habilidad; }
    public void setProbabilidad(int probabilidad) { this.probabilidad = probabilidad; }

    public boolean intentarHabilidad() {
        if (habilidad.equals("ninguna")) return false;
        int numeroAleatorio = (int)(Math.random() * 100);
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
}