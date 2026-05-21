package mundo;

/**
 * Clase base abstracta para cualquier entidad con stats
 * Jugador y Enemigo heredan de esta clase
 */
public abstract class Entidad{
    protected String nombre;
    protected int vida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defensa;
    protected int movimiento;
    protected Celda posicion;

    public Entidad(String nombre, int vida, int ataque, int defensa, int movimiento){
        //Objetivo:Crear una entidad con stats basicos
        this.nombre=nombre;
        this.vidaMaxima=vida;
        this.vida=vida;
        this.ataque=ataque;
        this.defensa=defensa;
        this.movimiento=movimiento;
    }

    public String getNombre(){ return nombre; }
    public int getVida(){ return vida; }
    public int getVidaMaxima(){ return vidaMaxima; }
    public int getAtaque(){ return ataque; }
    public int getDefensa(){ return defensa; }
    public int getMovimiento(){ return movimiento; }
    public Celda getPosicion(){ return posicion; }

    public void setVida(int vida){
        if(vida<0) this.vida=0;
        else if(vida>vidaMaxima) this.vida=vidaMaxima;
        else this.vida=vida;
    }

    public void setPosicion(Celda posicion){
        this.posicion=posicion;
    }

    public boolean estaVivo(){
        return vida>0;
    }

    public void recibirDanio(int ataqueEnemigo){
        int danio=Math.max(0, ataqueEnemigo-defensa);
        this.vida-=danio;
        if(this.vida<0) this.vida=0;
    }

    @Override
    public String toString(){
        return nombre+" (V:"+vida+"/"+vidaMaxima+" A:"+ataque+" D:"+defensa+" M:"+movimiento+")";
    }
}
