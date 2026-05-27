package mundo;

import listas.Cola;
import listas.ListaSimplementeEnlazada;
import modelo.Entidad;
import modelo.Jugador;
import modelo.Posicion;

// Representa un enemigo del juego que puede moverse y atacar al jugador
public class Enemigo extends Entidad {
    private String id;
    private int danioBase;
    private int movimiento;

    // Constructor: crea un enemigo con sus estadisticas
    public Enemigo(String id, String nombre, int vida, int ataque, int defensa, int movimiento) {
        super(nombre, vida, ataque, defensa, new Posicion(0, 0));
        this.id = id;
        this.danioBase = ataque;
        this.movimiento = movimiento;
    }

    // ----- GETTERS -----
    public String getId() { return id; }
    public int getDanioBase() { return danioBase; }
    public int getMovimiento(){return movimiento;}

    // Calcula el siguiente paso hacia el jugador usando BFS
    public Celda calcularMovimientoHacia(Celda jugadorPos, Celda[][] matriz) {
        if (jugadorPos == null || matriz == null) return null;
        
        int filas = matriz.length;
        int columnas = matriz[0].length;
        
        // La cola se usa para el recorrido BFS
        Cola<Celda> cola = new Cola<>();
        
        // Lista para marcar las celdas visitadas
        // Uso un array de booleanos para saber si ya pase por ahi
        boolean[][] visitado = new boolean[filas][columnas];
        
        // Matriz para reconstruir el camino:
        // guardo de que celda vine a cada celda
        // padre[fila][columna] me dice desde donde llegue
        Celda[][] padre = new Celda[filas][columnas];
        
        // Empiezo desde mi posicion actual
        Posicion miPos = this.getPosicion();
        if (miPos == null) return null;
        Celda inicio = matriz[miPos.getFila()][miPos.getColumna()];
        
        cola.encolar(inicio);
        visitado[inicio.getFila()][inicio.getColumna()] = true;
        
        // Direcciones: arriba, abajo, izquierda, derecha
        int[] df = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        
        boolean encontrado = false;
        
        // BFS: mientras la cola no este vacia
        while (!cola.estaVacia()) {
            Celda actual = cola.desencolar();
            
            // Si llegue a la posicion del jugador, termine
            if (actual.getFila() == jugadorPos.getFila() && 
                actual.getColumna() == jugadorPos.getColumna()) {
                encontrado = true;
                break;
            }
            
            // Pruebo las 4 direcciones
            for (int i = 0; i < 4; i++) {
                int nf = actual.getFila() + df[i];
                int nc = actual.getColumna() + dc[i];
                
                // Compruebo que la celda vecina sea valida y no visitada
                if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas && !visitado[nf][nc]) {
                    Celda vecina = matriz[nf][nc];
                    
                    // Solo puedo moverme a celdas vacias o a la del jugador
                    // No puedo atravesar paredes, trampas, enemigos, etc.
                    if (vecina.getTipo().equals("vacia") || 
                        (nf == jugadorPos.getFila() && nc == jugadorPos.getColumna())) {
                        
                        cola.encolar(vecina);
                        visitado[nf][nc] = true;
                        padre[nf][nc] = actual;  // Guardo el padre para reconstruir
                    }
                }
            }
        }
        
        // Si encontre un camino, reconstruyo el PRIMER PASO
        if (encontrado) {
            // Voy desde la posicion del jugador hacia atras hasta encontrar
            // la celda que esta justo al lado de mi posicion actual
            Celda paso = jugadorPos;
            Celda anterior = null;
            
            while (paso != inicio) {
                anterior = paso;
                paso = padre[paso.getFila()][paso.getColumna()];
                if (paso == null) break;
            }
            
            return anterior;  // Devuelvo el siguiente paso hacia el jugador
        }
        
        return null;  // No hay camino
    }

    // Causa danio al jugador usando la formula del enunciado
    public int atacar(Jugador jugador) {
        if (jugador == null) return 0;
        
        double aleatorio = Math.random();  // Numero entre 0 y 1
        
        // Calculo el danio con la formula
        int danio = (int)(this.ataque * (aleatorio * 2));
        int defensaJugador = jugador.getDefensa();
        
        int danioFinal = Math.max(0, danio - defensaJugador);
        
        // Aplico el danio al jugador
        jugador.recibirDanio(danioFinal);
        
        return danioFinal;
    }

    @Override
    public String toString() {
        return "Enemigo " + nombre + " [V:" + vida + " A:" + ataque + " D:" + defensa + "]";
    }
}
