package mundo;

/**
 * Representa un enemigo del juego
 * Se mueve hacia el jugador y le ataca
 */
public class Enemigo extends Entidad implements Comparable<Enemigo>{
    private String id;
    private int dañoBase;

    public Enemigo(String id, String nombre, int vida, int ataque, int defensa, int movimiento){
        super(nombre, vida, ataque, defensa, movimiento);
        this.id=id;
        this.dañoBase=ataque;
    }

    public String getId(){ return id; }
    public int getDañoBase(){ return dañoBase; }

    public int compareTo(Enemigo otro){
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString(){
        return "Enemigo "+nombre+" [V:"+vida+" A:"+ataque+" D:"+defensa+"]";
    }
}
