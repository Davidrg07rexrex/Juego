package modelo;

import listas.ListaSimplementeEnlazada;

/**
 * Representa una puerta entre habitaciones
 */
public class Puerta implements Comparable<Puerta>{
    private String idDestino;
    private boolean necesitaLlave;
    private String idLlaveRequerida;
    private Celda posicion;

    public Puerta(String idDestino, Celda posicion){
        this.idDestino=idDestino;
        this.necesitaLlave=false;
        this.idLlaveRequerida=null;
        this.posicion=posicion;
    }

    public Puerta(String idDestino, String idLlaveRequerida, Celda posicion){
        this.idDestino=idDestino;
        this.idLlaveRequerida=idLlaveRequerida;
        this.necesitaLlave=true;
        this.posicion=posicion;
    }

    public String getIdDestino(){ return idDestino; }
    public boolean necesitaLlave(){ return necesitaLlave; }
    public String getIdLlaveRequerida(){ return idLlaveRequerida; }
    public Celda getPosicion(){ return posicion; }

    public boolean puedeAbrir(ListaSimplementeEnlazada<String> llavesDelJugador){
        if(!necesitaLlave) return true;
        return llavesDelJugador.contains(idLlaveRequerida);
    }

    @Override
    public int compareTo(Puerta otra){
        return this.idDestino.compareTo(otra.idDestino);
    }

    @Override
    public String toString(){
        return "Puerta -> "+idDestino+(necesitaLlave?" (necesita "+idLlaveRequerida+")":"");
    }
}
