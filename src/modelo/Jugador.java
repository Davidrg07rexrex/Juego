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

    // Aquí puedes añadir métodos adicionales como:
    // public void agregarAlInventario(Objeto o) { ... }
    // public void usarItem(Objeto o) { ... }
}