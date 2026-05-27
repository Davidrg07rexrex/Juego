package mundo;

import java.util.Map;
import io.DatosPartida.DatosCelda;
import listas.ListaSimplementeEnlazada;
import modelo.Entidad;
import modelo.HabitacionModelo;
import modelo.Posicion;

// Representa una habitacion del juego compuesta por una matriz de celdas
public class Habitacion implements HabitacionModelo {
    private String id;                   // Identificador unico de la habitacion
    private String nombre;               // Nombre para mostrar
    private Celda[][] matriz;            // MATRIZ: el array bidimensional de celdas
    private int filas;                   // Numero de filas de la matriz
    private int columnas;                // Numero de columnas de la matriz
    private boolean esSalida;            // Si esta habitacion es la salida del juego

    // Constructor: crea la habitacion con su matriz vacia
    public Habitacion(String id, String nombre, int filas, int columnas) {
        this.id = id;
        this.nombre = nombre;
        this.filas = filas;
        this.columnas = columnas;
        this.esSalida = false;
        
        // Creo la matriz con el array bidimensional
        this.matriz = new Celda[filas][columnas];
        
        // Recorro toda la matriz y creo celdas vacias
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Celda(i, j);  // Cada celda vacia por defecto
            }
        }
    }

    // ----- GETTERS BASICOS -----
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Celda[][] getMatriz() { return matriz; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public boolean esSalida() { return esSalida; }
    public void setEsSalida(boolean esSalida) { this.esSalida = esSalida; }

    // Obtener una celda especifica por sus coordenadas
    public Celda getCelda(int fila, int columna) {
        if (!celdaValida(fila, columna)) {
            return null;  // Posicion fuera de la matriz
        }
        return matriz[fila][columna];
    }

    // Comprueba si una posicion esta dentro de los limites
    public boolean celdaValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    // Coloca un enemigo en una celda especifica
    public void colocarEnemigo(int fila, int columna, Enemigo enemigo) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("enemigo");
            celda.setContenido(enemigo);
            enemigo.setPosicion(new Posicion(fila, columna));
        }
    }

    // Coloca un objeto en una celda especifica
    public void colocarObjeto(int fila, int columna, Objeto objeto) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("objeto");
            celda.setContenido(objeto);
        }
    }

    // Coloca una puerta en una celda especifica
    public void colocarPuerta(int fila, int columna, String idDestino) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("puerta");
            // Guardo el id de la habitacion destino como contenido
            celda.setContenido(idDestino);
        }
    }

    // Coloca una puerta que necesita llave
    public void colocarPuertaConLlave(int fila, int columna, String idDestino, String idLlave) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("puerta");
            // Guardo destino:llave como contenido para saber que llave necesita
            celda.setContenido(idDestino + ":" + idLlave);
        }
    }

    // Coloca una trampa en una celda
    public void colocarTrampa(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("trampa");
            celda.setAccesible(false);
        }
    }

    // Limpia una celda (la deja vacia)
    public void limpiarCelda(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("vacia");
            celda.setContenido(null);
            celda.setAccesible(true);
        }
    }

    // Busca un enemigo en la matriz por sus coordenadas
    public Entidad getEnemigoEn(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("enemigo")) {
            return (Enemigo) celda.getContenido();
        }
        return null;
    }

    // Busca un objeto en la matriz por sus coordenadas
    public Objeto getObjetoEn(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("objeto")) {
            return (Objeto) celda.getContenido();
        }
        return null;
    }

    // Compara dos habitaciones por su id
    @Override
    public int compareTo(HabitacionModelo o) {
        return this.id.compareTo(o.getId());
    }

    @Override
    public String toString() {
        return nombre + " (" + filas + "x" + columnas + ")" + (esSalida ? " [SALIDA]" : "");
    }

    //Metodo que coloca una puerta guardando el objeto Puerta
    public void colocarPuerta(int fila, int columna, Puerta puerta) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("puerta");
            celda.setContenido(puerta);
        }
    }

    //Metodo que coloca una puerta con llave guardando el objeto Puerta
    public void colocarPuertaConLlave(int fila, int columna, Puerta puerta) {
        colocarPuerta(fila, columna, puerta);
    }

    //Metodo que coloca una trampa con un danio especifico
    public void colocarTrampa(int fila, int columna, Trampa trampa) {
        Celda celda = getCelda(fila, columna);
        if (celda != null) {
            celda.setTipo("trampa");
            celda.setAccesible(false);
            celda.setContenido(trampa);
        }
    }

    //Metodo que devuelve si se puede pasar por una celda
    public boolean esTransitable(int fila, int columna) {
        if (!celdaValida(fila, columna)) return false;
        Celda celda = getCelda(fila, columna);
        if (celda == null) return false;
        if (!celda.isAccesible()) return false;
        String tipo = celda.getTipo();
        return !tipo.equals("enemigo") && !tipo.equals("trampa");
    }

    //Metodo que devuelve el simbolo de una celda
    public String getSimbolo(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda == null) return "?";
        String tipo = celda.getTipo();
        if (tipo.equals("vacia")) return "·";
        if (tipo.equals("enemigo")) return "E";
        if (tipo.equals("objeto")) return "O";
        if (tipo.equals("puerta")) return "P";
        if (tipo.equals("trampa")) return "T";
        return "?";
    }

    //Metodo que elimina un enemigo de una celda
    public void eliminarEnemigo(int fila, int columna) {
        limpiarCelda(fila, columna);
    }

    //Metodo que mueve un enemigo a otra celda
    public void moverEnemigo(Entidad enemigo, int nuevaFila, int nuevaColumna) {
        Posicion pos = enemigo.getPosicion();
        if (pos != null) {
            limpiarCelda(pos.getFila(), pos.getColumna());
        }
        if (enemigo instanceof Enemigo) {
            colocarEnemigo(nuevaFila, nuevaColumna, (Enemigo) enemigo);
        }
    }

    //Metodo que elimina un objeto de una celda
    public void eliminarObjeto(int fila, int columna) {
        limpiarCelda(fila, columna);
    }

    //Metodo que devuelve el destino de una puerta
    public String getDestinoPuerta(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("puerta") && celda.getContenido() instanceof Puerta) {
            return ((Puerta) celda.getContenido()).getIdDestino();
        }
        return null;
    }

    //Metodo que devuelve si una puerta necesita llave
    public boolean puertaNecesitaLlave(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("puerta") && celda.getContenido() instanceof Puerta) {
            return ((Puerta) celda.getContenido()).necesitaLlave();
        }
        return false;
    }

    //Metodo que devuelve el id de la llave que necesita una puerta
    public String getIdLlavePuerta(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("puerta") && celda.getContenido() instanceof Puerta) {
            return ((Puerta) celda.getContenido()).getIdLlaveRequerida();
        }
        return null;
    }

    //Metodo que devuelve si una celda es una trampa
    public boolean esTrampa(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda == null) return false;
        return celda.getTipo().equals("trampa");
    }

    //Metodo que devuelve el danio de una trampa
    public int getDanioTrampa(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        if (celda != null && celda.getTipo().equals("trampa") && celda.getContenido() instanceof Trampa) {
            return ((Trampa) celda.getContenido()).getDanio();
        }
        return 0;
    }

    //Metodo que inicializa la habitacion desde una matriz de datos
    public void inicializarDesdeMatriz(DatosCelda[][] datosMatriz,
                                       ListaSimplementeEnlazada<Objeto> listaObjetos,
                                       ListaSimplementeEnlazada<Enemigo> listaEnemigos) {
        for (int i = 0; i < datosMatriz.length; i++) {
            for (int j = 0; j < datosMatriz[i].length; j++) {
                DatosCelda dato = datosMatriz[i][j];
                if (dato == null) continue;

                String tipo = dato.tipo;
                if (tipo == null || tipo.equals("vacia")) continue;

                int fila = dato.fila;
                int columna = dato.columna;

                if (tipo.equals("objeto")) {
                    if (dato.contenido instanceof Map) {
                        Map<String, Object> mapObj = (Map<String, Object>) dato.contenido;
                        String idObj = (String) mapObj.get("id");
                        if (idObj != null) {
                            Objeto obj = buscarObjetoPorId(listaObjetos, idObj);
                            if (obj != null) colocarObjeto(fila, columna, obj);
                        }
                    }
                } else if (tipo.equals("enemigo")) {
                    if (dato.contenido instanceof Map) {
                        Map<String, Object> mapEnem = (Map<String, Object>) dato.contenido;
                        String idEnem = (String) mapEnem.get("id");
                        if (idEnem != null) {
                            Enemigo enemigo = buscarEnemigoPorId(listaEnemigos, idEnem); // <-- aquí se usa listaEnemigos
                            if (enemigo != null) colocarEnemigo(fila, columna, enemigo);
                        }
                    }
                } else if (tipo.equals("puerta")) {
                    // ... (código de puerta)
                } else if (tipo.equals("trampa")) {
                    // ... (código de trampa)
                }
            }
        }
    }
    

    // Métodos de búsqueda auxiliares (privados)
    private Objeto buscarObjetoPorId(ListaSimplementeEnlazada<Objeto> lista, String id) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            Objeto obj = lista.getDatoEn(i);
            if (obj.getId().equals(id)) return obj;
        }
        return null;
    }

    private Enemigo buscarEnemigoPorId(ListaSimplementeEnlazada<Enemigo> lista, String id) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            Enemigo e = lista.getDatoEn(i);
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
}
