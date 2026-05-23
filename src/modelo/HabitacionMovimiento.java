package modelo;

public interface HabitacionMovimiento {
    int getFilas();
    int getColumnas();
    boolean esTransitable(int fila, int columna); // true si no hay pared ni entidad bloqueante
}