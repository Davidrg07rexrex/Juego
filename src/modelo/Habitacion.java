package modelo;

import listas.ListaSimplementeEnlazada;

/**
 * Representa una habitacion del juego
 * Cada habitacion es una matriz de celdas con enemigos, objetos y puertas
 */
public class Habitacion implements Comparable<Habitacion>{
    private String id; //Identificador unico
    private String nombre;
    private Celda[][] matriz; //Matriz de celdas
    private ListaSimplementeEnlazada<Enemigo> enemigos; //Enemigos en esta habitacion
    private ListaSimplementeEnlazada<Puerta> puertas; //Puertas de esta habitacion
    private boolean esSalida; //Si esta habitacion tiene salida al exterior
    private int turnosMaximos; //Turnos maximos para esta habitacion (opcional)

    public Habitacion(String id, String nombre, int filas, int columnas){
        //Objetivo:Crear una habitacion con matriz vacia
        this.id=id;
        this.nombre=nombre;
        this.matriz=new Celda[filas][columnas];
        this.enemigos=new ListaSimplementeEnlazada<>();
        this.puertas=new ListaSimplementeEnlazada<>();
        this.esSalida=false;
        this.turnosMaximos=-1;
        //Inicializar celdas vacias
        for(int i=0; i<filas; i++){
            for(int j=0; j<columnas; j++){
                matriz[i][j]=new Celda(i, j);
            }
        }
    }

    //Getters
    public String getId(){ return id; }
    public String getNombre(){ return nombre; }
    public Celda[][] getMatriz(){ return matriz; }
    public int getFilas(){ return matriz.length; }
    public int getColumnas(){ return matriz[0].length; }
    public ListaSimplementeEnlazada<Enemigo> getEnemigos(){ return enemigos; }
    public ListaSimplementeEnlazada<Puerta> getPuertas(){ return puertas; }
    public boolean esSalida(){ return esSalida; }
    public void setEsSalida(boolean esSalida){ this.esSalida=esSalida; }
    public int getTurnosMaximos(){ return turnosMaximos; }
    public void setTurnosMaximos(int turnosMaximos){ this.turnosMaximos=turnosMaximos; }

    public Celda getCelda(int fila, int columna){
        //Objetivo:Obtener una celda por sus coordenadas
        if(fila<0 || fila>=matriz.length || columna<0 || columna>=matriz[0].length)
            return null;
        return matriz[fila][columna];
    }

    public void colocarEnemigo(Enemigo enemigo, int fila, int columna){
        //Objetivo:Colocar un enemigo en una celda
        Celda celda=getCelda(fila, columna);
        if(celda!=null){
            celda.setTipo("enemigo");
            celda.setContenido(enemigo);
            enemigo.setPosicion(celda);
            enemigos.add(enemigo);
        }
    }

    public void colocarObjeto(Objeto objeto, int fila, int columna){
        //Objetivo:Colocar un objeto en una celda
        Celda celda=getCelda(fila, columna);
        if(celda!=null){
            celda.setTipo("objeto");
            celda.setContenido(objeto);
        }
    }

    public void colocarPuerta(Puerta puerta){
        //Objetivo:Colocar una puerta en su celda
        Celda celda=puerta.getPosicion();
        if(celda!=null){
            celda.setTipo("puerta");
            celda.setContenido(puerta);
            puertas.add(puerta);
        }
    }

    public void colocarTrampa(int fila, int columna){
        //Objetivo:Colocar una trampa en una celda
        Celda celda=getCelda(fila, columna);
        if(celda!=null){
            celda.setTipo("trampa");
            celda.setAccesible(false);
        }
    }

    public boolean celdaValida(int fila, int columna){
        //Objetivo:Comprobar si una posicion esta dentro de la matriz
        return fila>=0 && fila<matriz.length && columna>=0 && columna<matriz[0].length;
    }

    @Override
    public int compareTo(Habitacion otra){
        return this.id.compareTo(otra.id);
    }

    @Override
    public String toString(){
        return nombre+" ["+matriz.length+"x"+matriz[0].length+"] "+(esSalida?"(SALIDA)":"");
    }
}
