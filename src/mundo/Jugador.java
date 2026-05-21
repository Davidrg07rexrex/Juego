package mundo;

public class Jugador {
    private String nombre;
    private int vida, ataque, defensa, velocidad;

    public Jugador(String nombre, int vida, int ataque, int defensa, int velocidad) {
        this.nombre = nombre;
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    // Getters (los que necesite la vista)
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getVelocidad() { return velocidad; }
}