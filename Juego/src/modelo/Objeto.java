package modelo;

/**
 * Representa un objeto del juego que el jugador puede recoger y usar
 */
public class Objeto implements Comparable<Objeto>{
    private String id;
    private String nombre;
    private String tipo; // "arma", "escudo", "pocion", "llave", "especial"
    private int bonusAtaque;
    private int bonusDefensa;
    private int bonusVida;
    private int usosMaximos; // -1 si es infinito
    private int usosRestantes;
    private boolean equipable;
    private String slot; // "mano_derecha", "mano_izquierda", "torso", "cabeza", "dedos"

    public Objeto(String id, String nombre, String tipo){
        //Objetivo:Crear un objeto basico
        this.id=id;
        this.nombre=nombre;
        this.tipo=tipo;
        this.bonusAtaque=0;
        this.bonusDefensa=0;
        this.bonusVida=0;
        this.usosMaximos=-1;
        this.usosRestantes=-1;
        this.equipable=false;
        this.slot=null;
    }

    public String getId(){ return id; }
    public String getNombre(){ return nombre; }
    public String getTipo(){ return tipo; }
    public int getBonusAtaque(){ return bonusAtaque; }
    public int getBonusDefensa(){ return bonusDefensa; }
    public int getBonusVida(){ return bonusVida; }
    public int getUsosRestantes(){ return usosRestantes; }
    public boolean isEquipable(){ return equipable; }
    public String getSlot(){ return slot; }

    public void setBonusAtaque(int bonusAtaque){ this.bonusAtaque=bonusAtaque; }
    public void setBonusDefensa(int bonusDefensa){ this.bonusDefensa=bonusDefensa; }
    public void setBonusVida(int bonusVida){ this.bonusVida=bonusVida; }
    public void setEquipable(boolean equipable, String slot){
        this.equipable=equipable;
        this.slot=slot;
    }
    public void setUsosMaximos(int usosMaximos){
        this.usosMaximos=usosMaximos;
        this.usosRestantes=usosMaximos;
    }

    public boolean usar(){
        //Objetivo:Usar el objeto, consumiendo un uso si tiene limites
        if(usosRestantes==0) return false;
        if(usosRestantes>0){
            usosRestantes--;
            if(usosRestantes==0) return false;
        }
        return true;
    }

    @Override
    public int compareTo(Objeto otro){
        return this.id.compareTo(otro.id);
    }

    @Override
    public String toString(){
        String s=nombre+" ["+tipo+"]";
        if(bonusAtaque>0) s+=" ATK+"+bonusAtaque;
        if(bonusDefensa>0) s+=" DEF+"+bonusDefensa;
        if(bonusVida>0) s+=" VIDA+"+bonusVida;
        if(usosRestantes>0) s+=" usos:"+usosRestantes;
        return s;
    }
}
