package modelo;

import listas.ListaSimplementeEnlazada;

/**
 * Representa al jugador del juego
 * Tiene inventario, equipamiento y stats
 */
public class Jugador extends Entidad implements Comparable<Jugador>{
    private ListaSimplementeEnlazada<Objeto> inventario; //Objetos que lleva
    private int turnosRestantes;
    private boolean haMovido; //Si ya se ha movido este turno
    private boolean haActuado; //Si ya ha realizado una accion este turno

    public Jugador(String nombre, int vida, int ataque, int defensa, int movimiento){
        //Objetivo:Crear el jugador con stats base
        super(nombre, vida, ataque, defensa, movimiento);
        this.inventario=new ListaSimplementeEnlazada<>();
        this.turnosRestantes=50;
        this.haMovido=false;
        this.haActuado=false;
    }

    //Getters
    public ListaSimplementeEnlazada<Objeto> getInventario(){ return inventario; }
    public int getTurnosRestantes(){ return turnosRestantes; }
    public boolean hasMovido(){ return haMovido; }
    public boolean hasActuado(){ return haActuado; }

    public void decrementarTurnos(){
        turnosRestantes--;
    }

    public void nuevoTurno(){
        //Objetivo:Resetear acciones del turno
        haMovido=false;
        haActuado=false;
    }

    public boolean recogerObjeto(Objeto obj){
        //Objetivo:Añadir un objeto al inventario
        if(inventario.contains(obj)) return false;
        inventario.add(obj);
        return true;
    }

    public Objeto sacarObjeto(String id){
        //Objetivo:Sacar un objeto del inventario por su id
        for(int i=0; i<inventario.getTamaño(); i++){
            Objeto obj=inventario.getDatoEn(i);
            if(obj.getId().equals(id)){
                inventario.del(obj);
                return obj;
            }
        }
        return null;
    }

    public int getAtaqueTotal(){
        int total=ataque;
        for(int i=0; i<inventario.getTamaño(); i++){
            Objeto obj=inventario.getDatoEn(i);
            if(obj.isEquipable() && obj.getTipo().equals("arma")){
                total+=obj.getBonusAtaque();
            }
        }
        return total;
    }

    public int getDefensaTotal(){
        int total=defensa;
        for(int i=0; i<inventario.getTamaño(); i++){
            Objeto obj=inventario.getDatoEn(i);
            if(obj.isEquipable() && (obj.getTipo().equals("escudo") || obj.getTipo().equals("armadura"))){
                total+=obj.getBonusDefensa();
            }
        }
        return total;
    }

    public int compareTo(Jugador otro){
        return this.nombre.compareTo(otro.nombre);
    }

    @Override
    public String toString(){
        return "Jugador "+nombre+" [V:"+vida+"/"+vidaMaxima+" A:"+getAtaqueTotal()+" D:"+getDefensaTotal()+" M:"+movimiento+" T:"+turnosRestantes+"]";
    }
}
