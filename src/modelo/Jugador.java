package modelo;

import listas.ListaSimplementeEnlazada;
import mundo.Objeto;

public class Jugador extends Entidad {

    private ListaSimplementeEnlazada<Objeto> inventario;

    public Jugador(String nombre, int vida, int ataque, int defensa, Posicion posicion) {
        super(nombre, vida, ataque, defensa, posicion);
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