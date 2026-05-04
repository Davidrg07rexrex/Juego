package listas;

public class Tester{

    public static void main(String[] args) {
        System.out.println("TESTER DE ESTRUCTURAS DE DATOS\n");

        // LISTA SIMPLEMENTE ENLAZADA
        System.out.println("-- LISTA SIMPLEMENTE ENLAZADA --");
        ListaSimplementeEnlazada<Integer> lse=new ListaSimplementeEnlazada<>();
        System.out.println("Vacia al inicio: "+lse.estaVacia());
        System.out.println("Tamano al inicio: "+lse.getTamaño());

        lse.add(10);
        lse.add(20);
        lse.add(30);
        System.out.println("Lista tras add 10,20,30:");
        lse.imprimir();
        System.out.println("Tamano: "+lse.getTamaño());

        lse.addFirst(5);
        System.out.println("Tras addFirst(5):");
        lse.imprimir();

        System.out.println("Contains(20): "+lse.contains(20));
        System.out.println("Contains(99): "+lse.contains(99));
        System.out.println("get(20): "+lse.get(20));
        System.out.println("getDatoEn(1): "+lse.getDatoEn(1));
        System.out.println("getDatoEn(0): "+lse.getDatoEn(0));

        System.out.println("numApariciones(10): "+lse.numApariciones(10));
        lse.add(10);
        System.out.println("numApariciones(10) tras añadir otro 10: "+lse.numApariciones(10));

        System.out.println("del(20): "+lse.del(20));
        System.out.println("Tras del(20):");
        lse.imprimir();

        lse.clear();
        System.out.println("Tras clear, vacia: "+lse.estaVacia());
        System.out.println();



        // LISTA DOBLEMENTE ENLAZADA
        System.out.println("-- LISTA DOBLEMENTE ENLAZADA --");
        ListaDoblementeEnlazada<String> lde=new ListaDoblementeEnlazada<>();
        System.out.println("Vacia al inicio: "+lde.estaVacia());

        lde.add("hola");
        lde.add("adios");
        lde.add("test");
        System.out.println("Lista tras add hola,adios,test:");
        lde.imprimir();

        lde.addFirst("inicio");
        System.out.println("Tras addFirst(inicio):");
        lde.imprimir();

        System.out.println("Contains(test): "+lde.contains("test"));
        System.out.println("get(adios): "+lde.get("adios"));
        System.out.println("getDatoEn(2): "+lde.getDatoEn(2));

        System.out.println("del(hola): "+lde.del("hola"));
        System.out.println("Tras del(hola):");
        lde.imprimir();

        lde.clear();
        System.out.println("Tras clear, vacia: "+lde.estaVacia());
        System.out.println();

        // LISTA CIRCULAR
        System.out.println("-- LISTA CIRCULAR --");
        ListaCircular<Integer> lc=new ListaCircular<>();
        System.out.println("Vacia al inicio: "+lc.estaVacia());

        lc.add(100);
        lc.add(200);
        lc.add(300);
        System.out.println("get(200): "+lc.get(200));
        System.out.println("get(999): "+lc.get(999));

        System.out.println("del(100): "+lc.del(100));
        System.out.println("get(200) tras borrar primero: "+lc.get(200));
        System.out.println("Tamano: "+lc.getTamaño());

        lc.add(400);
        System.out.println("del(400) ultimo: "+lc.del(400));
        System.out.println("Tamano: "+lc.getTamaño());

        lc.clear();
        System.out.println("Tras clear, vacia: "+lc.estaVacia());
        System.out.println();

        // PILA
        System.out.println("-- PILA --");
        Pila<Integer> p=new Pila<>();
        System.out.println("Vacia al inicio: "+p.estaVacia());

        p.apilar(10);
        p.apilar(20);
        p.apilar(30);
        System.out.println("Cima tras apilar 10,20,30: "+p.cima());
        System.out.println("Tamano: "+p.getTamaño());

        System.out.println("desapilar(): "+p.desapilar());
        System.out.println("Cima tras desapilar: "+p.cima());
        System.out.println("Tamano: "+p.getTamaño());

        p.clear();
        System.out.println("Tras clear, vacia: "+p.estaVacia());
        System.out.println("desapilar en vacia: "+p.desapilar());
        System.out.println();

        // COLA
        System.out.println("-- COLA --");
        Cola<String> c=new Cola<>();
        System.out.println("Vacia al inicio: "+c.estaVacia());

        c.encolar("primero");
        c.encolar("segundo");
        c.encolar("tercero");
        System.out.println("Frente: "+c.frente());
        System.out.println("Tamano: "+c.getTamaño());

        System.out.println("desencolar(): "+c.desencolar());
        System.out.println("Frente tras desencolar: "+c.frente());
        System.out.println("Tamano: "+c.getTamaño());

        c.clear();
        System.out.println("Tras clear, vacia: "+c.estaVacia());
        System.out.println("desencolar en vacia: "+c.desencolar());
        System.out.println();

        // GRAFO
        System.out.println("-- GRAFO --");
        Grafo<String> grafo=new Grafo<>();

        grafo.addNodo("A");
        grafo.addNodo("B");
        grafo.addNodo("C");
        grafo.addNodo("D");
        grafo.addNodo("E");
        System.out.println("Num nodos: "+grafo.getNumNodos());

        grafo.addArista("A","B");
        grafo.addArista("A","C");
        grafo.addArista("B","D");
        grafo.addArista("C","D");
        grafo.addArista("D","E");

        System.out.println("Estructura del grafo:");
        grafo.imprimir();

        System.out.println("Vecinos de A:");
        ListaSimplementeEnlazada<String> vecinosA=grafo.getVecinos("A");
        vecinosA.imprimir();

        System.out.println("Contains(C): "+grafo.contains("C"));
        System.out.println("Contains(Z): "+grafo.contains("Z"));

        System.out.println("BFS desde A:");
        ListaSimplementeEnlazada<String> bfs=grafo.BFS("A");
        bfs.imprimir();

        System.out.println("Camino minimo de A a E:");
        ListaSimplementeEnlazada<String> camino=grafo.caminoMinimo("A","E");
        camino.imprimir();

        System.out.println("Camino minimo de A a B:");
        ListaSimplementeEnlazada<String> caminoAB=grafo.caminoMinimo("A","B");
        caminoAB.imprimir();

        System.out.println("Camino minimo de E a A (no hay):");
        ListaSimplementeEnlazada<String> caminoEA=grafo.caminoMinimo("E","A");
        System.out.println("Tamano camino: "+caminoEA.getTamaño());
        System.out.println();

        // GRAFO CON PESOS
        System.out.println("-- GRAFO CON PESOS --");
        Grafo<String> grafo2=new Grafo<>();
        grafo2.addNodo("Inicio");
        grafo2.addNodo("Sala");
        grafo2.addNodo("Pasillo");
        grafo2.addNodo("Salida");

        grafo2.addArista("Inicio","Sala",1.0);
        grafo2.addArista("Inicio","Pasillo",2.0);
        grafo2.addArista("Sala","Salida",1.5);
        grafo2.addArista("Pasillo","Salida",1.0);

        System.out.println("Estructura:");
        grafo2.imprimir();

        System.out.println("Camino minimo de Inicio a Salida:");
        ListaSimplementeEnlazada<String> cam2=grafo2.caminoMinimo("Inicio","Salida");
        cam2.imprimir();
        System.out.println();

        // ARBOL
        System.out.println("-- ARBOL --");
        Arbol<String> arbol=new Arbol<>("raiz");

        arbol.add("raiz","hijo1");
        arbol.add("raiz","hijo2");
        arbol.add("hijo1","nieto1");
        arbol.add("hijo1","nieto2");
        arbol.add("hijo2","nieto3");

        System.out.println("Estructura del arbol:");
        arbol.imprimir();

        System.out.println("Altura: "+arbol.getAltura());
        System.out.println("Num nodos: "+arbol.getNumNodos());

        System.out.println("Contains(nieto2): "+arbol.contains("nieto2"));
        System.out.println("Contains(xyz): "+arbol.contains("xyz"));
        System.out.println("get(hijo1): "+arbol.get("hijo1"));

        System.out.println("Recorrido preorden:");
        ListaSimplementeEnlazada<String> recorrido=arbol.recorrerEnOrden();
        recorrido.imprimir();

        arbol.clear();
        System.out.println("Tras clear, altura: "+arbol.getAltura());
        System.out.println("Tras clear, numNodos: "+arbol.getNumNodos());


        System.out.println("\nTODOS LOS TESTS COMPLETADOS");
    }

}
