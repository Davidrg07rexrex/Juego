package modelo;

import listas.ListaSimplementeEnlazada;
import mundo.Objeto;

public class Jugador extends Entidad {

    private ListaSimplementeEnlazada<Objeto> inventario;
    private ListaSimplementeEnlazada<String> llaves = new ListaSimplementeEnlazada<>();

    public void agregarLlave(String idLlave) {
        if (!llaves.contains(idLlave)) {
            llaves.add(idLlave);
        }
    }

    public boolean tieneLlave(String idLlave) {
        return llaves.contains(idLlave);
    }
    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
        if (this.vida > vidaMaxima) this.vida = vidaMaxima;
    }

    public Jugador(String nombre, int vida, int ataque, int defensa, int velocidad, Posicion posicion) {
        super(nombre, vida, ataque, defensa, velocidad, posicion);
        this.inventario = new ListaSimplementeEnlazada<>();
    }

    public ListaSimplementeEnlazada<Objeto> getInventario() {
        return inventario;
    }

    public void atacar(Entidad objetivo) {
        if (objetivo != null && objetivo.estaVivo()) {
            objetivo.recibirDanio(this.ataque);
        }
    }
    public void agregarAlInventario(Objeto obj) {
        inventario.add(obj);
    }
}