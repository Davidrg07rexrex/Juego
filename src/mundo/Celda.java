package mundo;

// Representa una celda dentro de la matriz de una habitacion
public class Celda implements Comparable<Celda>{
    private String tipo; //Tipo de contenido: "vacia", "enemigo", "objeto", "puerta", "salida", "trampa"
    private int fila;
    private int columna;
    private boolean accesible;
    private Object contenido; //Referencia al contenido (Enemigo, Objeto, Puerta, etc)

    public Celda(int fila, int columna){
        //Objetivo:Crear una celda vacia en la posicion dada
        this.fila=fila;
        this.columna=columna;
        this.tipo="vacia";
        this.accesible=true;
        this.contenido=null;
    }

    public Celda(int fila, int columna, String tipo){
        //Objetivo:Crear una celda con un tipo especifico
        this.fila=fila;
        this.columna=columna;
        this.tipo=tipo;
        this.accesible=!tipo.equals("trampa");
        this.contenido=null;
    }

    //Getters y setters
    public String getTipo(){ return tipo; }
    public void setTipo(String tipo){ this.tipo=tipo; }
    public int getFila(){ return fila; }
    public int getColumna(){ return columna; }
    public boolean isAccesible(){ return accesible; }
    public void setAccesible(boolean accesible){ this.accesible=accesible; }
    public Object getContenido(){ return contenido; }
    public void setContenido(Object contenido){ this.contenido=contenido; }

    @Override
    public int compareTo(Celda otra){
        //Objetivo:Comparar celdas por fila y luego columna
        if(this.fila!=otra.fila)
            return Integer.compare(this.fila, otra.fila);
        return Integer.compare(this.columna, otra.columna);
    }

    @Override
    public String toString(){
        return "("+fila+","+columna+") ["+tipo+"]";
    }
}
