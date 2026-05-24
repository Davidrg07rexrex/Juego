package modelo;

public abstract class Entidad {
    protected String nombre;
    protected int vida;
    protected int ataque;
    protected int defensa;
    protected Posicion posicion;

    public Entidad(String nombre, int vida, int ataque, int defensa, Posicion posicion) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.posicion = posicion;
    }

    // Getters y setters básicos
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public Posicion getPosicion() { return posicion; }
    public void setPosicion(Posicion posicion) { this.posicion = posicion; }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void recibirDanio(int danio) {
        int danioFinal = danio - defensa;

        if (danioFinal < 1) {
            danioFinal = 1;
        }

        vida -= danioFinal;

        if (vida < 0) {
            vida = 0;
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}