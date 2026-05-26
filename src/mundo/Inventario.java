package mundo;

import listas.ListaSimplementeEnlazada;

// Gestiona los objetos que el jugador lleva durante la partida
public class Inventario {
    private ListaSimplementeEnlazada<Objeto> objetos;  // Lista enlazada simple
    private int capacidadMaxima;                       // Maximo de objetos que puede llevar
    private int pesoActual;                            // Peso actual (para futura ampliacion)
    
    // Sistema de equipamiento: cada slot guarda un objeto (o null si vacio)
    // Los slots disponibles son fijos
    private String[] slots = {"mano_derecha", "mano_izquierda", "torso", "cabeza",
                              "dedo1", "dedo2", "dedo3", "dedo4", "dedo5",
                              "dedo6", "dedo7", "dedo8", "dedo9", "dedo10"};
    private Objeto[] equipados;  // Array paralelo a slots, cada posicion guarda el objeto equipado

    // Constructor: crea un inventario vacio con una capacidad maxima
    public Inventario(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.pesoActual = 0;
        this.objetos = new ListaSimplementeEnlazada<>();  // Uso la lista enlazada del companero
        this.equipados = new Objeto[slots.length];         // Todos los slots empiezan vacios
    }

    // Agrega un objeto al inventario
    public boolean agregar(Objeto obj) {
        if (objetos.getTamaño() >= capacidadMaxima) {
            return false;  // Inventario lleno
        }
        if (objetos.contains(obj)) {
            return false;  // Ya tenemos ese objeto (no permito duplicados exactos)
        }
        objetos.add(obj);  // Uso el metodo add de la lista enlazada
        return true;
    }

    // Elimina un objeto del inventario por su id
    public Objeto eliminar(String id) {
        // Para eliminar necesito el objeto, creo uno temporal solo con el id
        // para buscarlo con el contains/get de la lista
        for (int i = 0; i < objetos.getTamaño(); i++) {
            Objeto obj = objetos.getDatoEn(i);
            if (obj.getId().equals(id)) {
                objetos.del(obj);  // Uso el metodo del de la lista enlazada
                
                // Si estaba equipado, lo desequipo
                for (int j = 0; j < slots.length; j++) {
                    if (equipados[j] == obj) {
                        equipados[j] = null;
                        break;
                    }
                }
                return obj;
            }
        }
        return null;
    }

    // Busca un objeto por su id
    public Objeto buscar(String id) {
        for (int i = 0; i < objetos.getTamaño(); i++) {
            Objeto obj = objetos.getDatoEn(i);
            if (obj.getId().equals(id)) {
                return obj;
            }
        }
        return null;
    }

    // Comprueba si hay un objeto con ese id
    public boolean contiene(String id) {
        return buscar(id) != null;
    }

    // Numero de objetos en el inventario
    public int getTamanio() {
        return objetos.getTamaño();
    }

    // El inventario esta vacio?
    public boolean estaVacio() {
        return objetos.estaVacia();
    }

    // Obtiene un objeto por su posicion en la lista
    public Objeto getObjetoEn(int posicion) {
        return objetos.getDatoEn(posicion);
    }

    // EQUIPAR: pone un objeto en el slot correspondiente
    public boolean equipar(String idObjeto) {
        Objeto obj = buscar(idObjeto);
        if (obj == null) return false;      // No tengo ese objeto
        if (!obj.isEquipable()) return false; // No se puede equipar
        
        String slotObjeto = obj.getSlot();
        
        // Busco el indice del slot
        int indiceSlot = -1;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].equals(slotObjeto)) {
                indiceSlot = i;
                break;
            }
        }
        if (indiceSlot == -1) return false;  // Slot no valido
        
        // Si ya hay algo en ese slot, lo desequipo primero
        if (equipados[indiceSlot] != null) {
            return false;  // Slot ocupado, hay que desequipar primero
        }
        
        // Equipo el objeto
        equipados[indiceSlot] = obj;
        return true;
    }

    // DESEQUIPAR: quita un objeto de un slot
    public Objeto desequipar(String slot) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].equals(slot)) {
                Objeto obj = equipados[i];
                equipados[i] = null;
                return obj;
            }
        }
        return null;  // Slot no encontrado
    }

    // Obtiene el objeto equipado en un slot
    public Objeto getEquipado(String slot) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].equals(slot)) {
                return equipados[i];
            }
        }
        return null;
    }

    // Vacia todo el inventario
    public void vaciar() {
        objetos.clear();
        for (int i = 0; i < equipados.length; i++) {
            equipados[i] = null;
        }
    }

    // Muestra todos los objetos del inventario por consola
    public void listar() {
        if (objetos.estaVacia()) {
            System.out.println("Inventario vacio");
            return;
        }
        System.out.println("=== INVENTARIO ===");
        for (int i = 0; i < objetos.getTamaño(); i++) {
            Objeto obj = objetos.getDatoEn(i);
            String equipado = "";
            // Compruebo si esta equipado en algun slot
            for (int j = 0; j < slots.length; j++) {
                if (equipados[j] == obj) {
                    equipado = " [EQUIPADO en " + slots[j] + "]";
                    break;
                }
            }
            System.out.println("  " + (i+1) + ". " + obj.getDescripcion() + equipado);
        }
        System.out.println("==================");
    }

    // Obtener todos los objetos como String (para guardar partida)
    public String[] getIdsObjetos() {
        String[] ids = new String[objetos.getTamaño()];
        for (int i = 0; i < objetos.getTamaño(); i++) {
            ids[i] = objetos.getDatoEn(i).getId();
        }
        return ids;
    }
}
