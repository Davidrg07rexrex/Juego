package listas;

public class Grafo<T extends Comparable<T>> {
    private ListaSimplementeEnlazada<NodoGrafo<T>> nodos; //Lista de todos los nodos del grafo

    public Grafo(){
        this.nodos=new ListaSimplementeEnlazada<>();
    }

    public void addNodo(T dato){
        //Objetivo:Añadir un nuevo nodo al grafo con el dato proporcionado
        NodoGrafo<T> nuevo=new NodoGrafo<>(dato);
        nodos.add(nuevo);
    }

    public void addArista(T origen, T destino){
        //Objetivo:Crear una conexión entre dos nodos del grafo
        NodoGrafo<T> nOrigen=buscarNodo(origen);
        NodoGrafo<T> nDestino=buscarNodo(destino);
        if(nOrigen!=null && nDestino!=null){
            AristaGrafo<T> nueva=new AristaGrafo<>(nDestino);
            nOrigen.aristas.add(nueva);
        }
    }

    public void addArista(T origen, T destino, double peso){
        //Objetivo:Crear una conexión con peso entre dos nodos
        NodoGrafo<T> nOrigen=buscarNodo(origen);
        NodoGrafo<T> nDestino=buscarNodo(destino);
        if(nOrigen!=null && nDestino!=null){
            AristaGrafo<T> nueva=new AristaGrafo<>(nDestino, peso);
            nOrigen.aristas.add(nueva);
        }
    }

    public ListaSimplementeEnlazada<T> getVecinos(T dato){
        //Objetivo:Obtener la lista de vecinos de un nodo
        ListaSimplementeEnlazada<T> vecinos=new ListaSimplementeEnlazada<>();
        NodoGrafo<T> nodo=buscarNodo(dato);
        if(nodo!=null){
            for(int i=0; i<nodo.aristas.getTamaño(); i++){
                AristaGrafo<T> arista=nodo.aristas.getDatoEn(i);
                vecinos.add(arista.destino.dato);
            }
        }
        return vecinos;
    }

    public boolean contains(T dato){
        //Objetivo:Comprobar si un nodo existe en el grafo
        return buscarNodo(dato)!=null;
    }

    public ListaSimplementeEnlazada<T> BFS(T inicio){
        //Objetivo:Recorrer el grafo en anchura desde un nodo inicio
        //Devuelve la lista de nodos visitados en orden BFS
        ListaSimplementeEnlazada<T> visitados=new ListaSimplementeEnlazada<>();
        Cola<T> cola=new Cola<>();
        ListaSimplementeEnlazada<T> visitadosSet=new ListaSimplementeEnlazada<>(); //Para saber que ya visitamos

        cola.encolar(inicio);
        visitadosSet.add(inicio);

        while(!cola.estaVacia()){
            T actual=cola.desencolar();
            visitados.add(actual);

            ListaSimplementeEnlazada<T> vecinos=getVecinos(actual);
            for(int i=0; i<vecinos.getTamaño(); i++){
                T vecino=vecinos.getDatoEn(i);
                if(!visitadosSet.contains(vecino)){
                    visitadosSet.add(vecino);
                    cola.encolar(vecino);
                }
            }
        }
        return visitados;
    }

    public ListaSimplementeEnlazada<T> caminoMinimo(T inicio, T fin){
        //Objetivo:Encontrar el camino más corto entre dos nodos usando BFS
        //Devuelve la lista de nodos del camino, o lista vacía si no hay camino
        ListaSimplementeEnlazada<T> camino=new ListaSimplementeEnlazada<>();

        //Comprobamos que inicio y fin existen en el grafo
        if(!contains(inicio) || !contains(fin)){
            return camino; //Lista vacía
        }

        Cola<T> cola=new Cola<>();

        //Usamos listas paralelas para reconstruir el camino
        //nodosVisitados[i] tiene el nodo visitado en posición i
        //padres[i] tiene el padre de nodosVisitados[i], o null si es el inicio
        ListaSimplementeEnlazada<T> nodosVisitados=new ListaSimplementeEnlazada<>();
        ListaSimplementeEnlazada<Boolean> esInicio=new ListaSimplementeEnlazada<>(); //true si es el nodo inicio(sin padre)

        cola.encolar(inicio);
        nodosVisitados.add(inicio);
        esInicio.add(true); //El inicio no tiene padre

        boolean encontrado=false;

        while(!cola.estaVacia()){
            T actual=cola.desencolar();

            if(actual.compareTo(fin)==0){
                encontrado=true;
                break;
            }

            ListaSimplementeEnlazada<T> vecinos=getVecinos(actual);
            for(int i=0; i<vecinos.getTamaño(); i++){
                T vecino=vecinos.getDatoEn(i);
                //Comprobamos si ya fue visitado
                if(!nodosVisitados.contains(vecino)){
                    nodosVisitados.add(vecino);
                    esInicio.add(false); //Este SÍ tiene padre
                    //Guardamos el padre en una posición paralela
                    //Para reconstruir: buscar vecino en nodosVisitados, su padre está en esa posición

                    cola.encolar(vecino);
                }
            }
        }

        if(encontrado){
            //Reconstruir camino desde fin hasta inicio
            Pila<T> pila=new Pila<>();
            T actual=fin;

            while(true){
                pila.apilar(actual);
                //Buscar actual en nodosVisitados para saber si es inicio
                for(int i=0; i<nodosVisitados.getTamaño(); i++){
                    if(nodosVisitados.getDatoEn(i).compareTo(actual)==0){
                        if(esInicio.getDatoEn(i)){
                            //Es el inicio, ya no tiene padre, paramos
                            actual=null;
                        } else {
                            //Buscamos su padre: es el vecino que lo añadió
                            //Para simplificar, buscamos en el grafo qué vecino ya fue visitado antes
                            actual=buscarPadre(actual, nodosVisitados);
                        }
                        break;
                    }
                }
                if(actual==null) break;
            }

            //Volcamos la pila al camino (ya queda en orden correcto)
            while(!pila.estaVacia()){
                camino.add(pila.desapilar());
            }
        }

        return camino;
    }

    private T buscarPadre(T hijo, ListaSimplementeEnlazada<T> nodosVisitados){
        //Objetivo:Buscar qué nodo de los visitados tiene a 'hijo' como vecino
        //El padre será el primero que cumpla esto(BFS garantiza que es el más cercano)
        for(int i=0; i<nodosVisitados.getTamaño(); i++){
            T candidato=nodosVisitados.getDatoEn(i);
            ListaSimplementeEnlazada<T> vecinos=getVecinos(candidato);
            if(vecinos.contains(hijo)){
                return candidato;
            }
        }
        return null;
    }

    private NodoGrafo<T> buscarNodo(T dato){
        //Objetivo:Buscar y devolver un nodo del grafo por su dato
        for(int i=0; i<nodos.getTamaño(); i++){
            NodoGrafo<T> nodo=nodos.getDatoEn(i);
            if(nodo.dato.compareTo(dato)==0){
                return nodo;
            }
        }
        return null; //No encontrado
    }

    public int getNumNodos(){
        //Objetivo:Devolver el número de nodos del grafo
        return nodos.getTamaño();
    }

    public void imprimir(){
        //Objetivo:Mostrar todos los nodos y sus conexiones
        for(int i=0; i<nodos.getTamaño(); i++){
            NodoGrafo<T> nodo=nodos.getDatoEn(i);
            System.out.print(nodo.dato + " -> ");
            for(int j=0; j<nodo.aristas.getTamaño(); j++){
                AristaGrafo<T> arista=nodo.aristas.getDatoEn(j);
                System.out.print(arista.destino.dato + " ");
            }
            System.out.println();
        }
    }
}
