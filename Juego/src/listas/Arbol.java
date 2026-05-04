package listas;

public class Arbol<T extends Comparable<T>> {
    private NodoArbol<T> raiz; //La raíz del árbol

    public Arbol(){
        this.raiz=null;
    }

    public Arbol(T datoRaiz){
        this.raiz=new NodoArbol<>(datoRaiz);
    }

    public void add(T padre, T dato){
        //Objetivo:Añadir un hijo a un nodo padre existente
        if(raiz==null) return;
        NodoArbol<T> nodoPadre=buscarNodo(raiz, padre);
        if(nodoPadre!=null){
            NodoArbol<T> nuevo=new NodoArbol<>(dato);
            nodoPadre.hijos.add(nuevo);
        }
    }

    public boolean contains(T dato){
        //Objetivo:Comprobar si un dato existe en el árbol
        return buscarNodo(raiz, dato)!=null;
    }

    public T get(T dato){
        //Objetivo:Obtener un dato del árbol
        NodoArbol<T> nodo=buscarNodo(raiz, dato);
        if(nodo!=null) return nodo.dato;
        return null;
    }

    public ListaSimplementeEnlazada<T> recorrerEnOrden(){
        //Objetivo:Recorrer el árbol en orden (preorden) y devolver los datos
        ListaSimplementeEnlazada<T> resultado=new ListaSimplementeEnlazada<>();
        recorrerPreorden(raiz, resultado);
        return resultado;
    }

    public int getAltura(){
        //Objetivo:Calcular la altura del árbol
        if(raiz==null) return 0;
        return calcularAltura(raiz);
    }

    public int getNumNodos(){
        //Objetivo:Contar el número total de nodos
        if(raiz==null) return 0;
        return contarNodos(raiz);
    }

    public void clear(){
        //Objetivo:Vaciar el árbol
        raiz=null;
    }

    public void imprimir(){
        //Objetivo:Mostrar el árbol de forma visual
        if(raiz!=null)
            imprimirNivel(raiz, 0);
    }

    private NodoArbol<T> buscarNodo(NodoArbol<T> actual, T dato){
        //Objetivo:Buscar un nodo en el subárbol desde actual
        if(actual==null) return null;
        if(actual.dato.compareTo(dato)==0) return actual;

        //Buscar en los hijos
        for(int i=0; i<actual.hijos.getTamaño(); i++){
            NodoArbol<T> hijo=actual.hijos.getDatoEn(i);
            NodoArbol<T> encontrado=buscarNodo(hijo, dato);
            if(encontrado!=null) return encontrado;
        }
        return null;
    }

    private void recorrerPreorden(NodoArbol<T> actual, ListaSimplementeEnlazada<T> resultado){
        //Objetivo:Recorrer en preorden (raíz primero, luego hijos)
        if(actual==null) return;
        resultado.add(actual.dato);
        for(int i=0; i<actual.hijos.getTamaño(); i++){
            recorrerPreorden(actual.hijos.getDatoEn(i), resultado);
        }
    }

    private int calcularAltura(NodoArbol<T> actual){
        //Objetivo:Calcular altura del subárbol desde actual
        if(actual.hijos.getTamaño()==0) return 1; //Es hoja

        int maxAltura=0;
        for(int i=0; i<actual.hijos.getTamaño(); i++){
            int h=calcularAltura(actual.hijos.getDatoEn(i));
            if(h>maxAltura) maxAltura=h;
        }
        return 1+maxAltura;
    }

    private int contarNodos(NodoArbol<T> actual){
        //Objetivo:Contar nodos del subárbol desde actual
        int contador=1; //Contamos este nodo
        for(int i=0; i<actual.hijos.getTamaño(); i++){
            contador+=contarNodos(actual.hijos.getDatoEn(i));
        }
        return contador;
    }

    private void imprimirNivel(NodoArbol<T> actual, int nivel){
        //Objetivo:Imprimir el árbol con sangría por nivel
        for(int i=0; i<nivel; i++) System.out.print("  ");
        System.out.println(actual.dato);
        for(int i=0; i<actual.hijos.getTamaño(); i++){
            imprimirNivel(actual.hijos.getDatoEn(i), nivel+1);
        }
    }
}
