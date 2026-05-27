package modelo;

public abstract class Entidad {
    protected String nombre;
    protected int vida;
    protected int vidaMaxima;          // <--- REINCORPORADO
    protected int ataque;
    protected int defensa;
    protected Posicion posicion;
    protected int velocidad;

    public Entidad(String nombre, int vida, int ataque, int defensa, int velocidad, Posicion posicion) {
        this.nombre = nombre;
        this.vidaMaxima = vida;        // al nacer, vida máxima = vida inicial
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.posicion = posicion;
        this.velocidad = velocidad;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }   // <--- GETTER
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public Posicion getPosicion() { return posicion; }

    public void setVida(int vida) {
        if (vida < 0) this.vida = 0;
        else if (vida > vidaMaxima) this.vida = vidaMaxima;
        else this.vida = vida;
    }

    public void setVidaMaxima(int vidaMaxima) {         // <--- SETTER
        this.vidaMaxima = vidaMaxima;
        if (this.vida > vidaMaxima) this.vida = vidaMaxima;
    }

    public void setPosicion(Posicion posicion) { this.posicion = posicion; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public boolean estaVivo() { return vida > 0; }

    public void recibirDanio(int danio) {
        // El parámetro 'danio' ya viene calculado por la fórmula de ataque (Combate.resolver)
        // No se resta la defensa aquí, porque Combate.resolver ya lo hace.
        this.vida -= danio;
        if (this.vida < 0) this.vida = 0;
    }

    @Override
    public String toString() {
        return nombre + " (" + vida + "/" + vidaMaxima + ")";
    }
}