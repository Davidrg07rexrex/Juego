package io;

import io.DatosPartida;
import io.DatosPartida.DatosObjeto;
import io.DatosPartida.DatosEnemigoPlantilla;
import io.DatosPartida.DatosHabitacionCompleta;
import io.DatosPartida.DatosCelda;
import io.DatosPartida.DatosConexion;
import io.Log;
import io.EnemigoPlantilla;
import mundo.*;
import listas.*;
import com.google.gson.internal.LinkedTreeMap;

// Carga el mundo del juego desde un archivo JSON
public class CargadorMundos {

    // Metodo principal: carga el mundo desde la ruta indicada
    public static Object[] cargarMundo(String ruta) {
        Log log = new Log();
        log.registrar("Cargando mundo desde: " + ruta);

        DatosPartida datos = DatosPartida.cargar(ruta, DatosPartida.class);
        if (datos == null) {
            log.registrar("Error: No se pudo cargar el archivo " + ruta);
            return null;
        }

        ListaSimplementeEnlazada<Objeto> objetosDisponibles = crearObjetos(datos.objetosDisponibles);
        ListaSimplementeEnlazada<EnemigoPlantilla> plantillas = crearPlantillas(datos.enemigosDisponibles);
        ListaSimplementeEnlazada<Habitacion> habitaciones = crearHabitaciones(datos.habitaciones, objetosDisponibles, plantillas);
        Grafo<String> grafo = crearGrafo(datos.grafo);

        return new Object[] { log, habitaciones, grafo, objetosDisponibles, datos.jugador };
    }

    // Construye la lista de objetos desde los datos JSON
    private static ListaSimplementeEnlazada<Objeto> crearObjetos(DatosObjeto[] datosObjetos) {
        ListaSimplementeEnlazada<Objeto> lista = new ListaSimplementeEnlazada<>();
        if (datosObjetos == null) return lista;

        for (DatosObjeto d : datosObjetos) {
            if (d.tipo.equals("arma")) {
                Arma arma = new Arma(d.id, d.nombre, d.bonusAtaque, d.rango, d.slot);
                arma.setHabilidad(d.habilidad);
                arma.setProbabilidad(d.probabilidad);
                arma.setUsosMaximos(d.usosMaximos);
                lista.add(arma);
            } else if (d.tipo.equals("pocion")) {
                Pocion pocion = new Pocion(d.id, d.nombre, d.bonusVida);
                lista.add(pocion);
            } else if (d.tipo.equals("llave")) {
                Llave llave = new Llave(d.id, d.nombre);
                lista.add(llave);
            }
        }

        return lista;
    }

    // Construye las plantillas de enemigos desde los datos JSON
    private static ListaSimplementeEnlazada<EnemigoPlantilla> crearPlantillas(DatosEnemigoPlantilla[] datosPlantillas) {
        ListaSimplementeEnlazada<EnemigoPlantilla> lista = new ListaSimplementeEnlazada<>();
        if (datosPlantillas == null) return lista;

        for (DatosEnemigoPlantilla d : datosPlantillas) {
            EnemigoPlantilla p = new EnemigoPlantilla(d.id, d.nombre, d.vida, d.ataque, d.defensa, d.movimiento);
            lista.add(p);
        }

        return lista;
    }

    // Construye las habitaciones con sus celdas
    private static ListaSimplementeEnlazada<Habitacion> crearHabitaciones(
            DatosHabitacionCompleta[] datosHabitaciones,
            ListaSimplementeEnlazada<Objeto> objetosDisponibles,
            ListaSimplementeEnlazada<EnemigoPlantilla> plantillas) {

        ListaSimplementeEnlazada<Habitacion> lista = new ListaSimplementeEnlazada<>();
        if (datosHabitaciones == null) return lista;

        for (DatosHabitacionCompleta dh : datosHabitaciones) {
            Habitacion hab = new Habitacion(dh.id, dh.nombre, dh.filas, dh.columnas);

            if (dh.esSalida) {
                hab.setEsSalida(true);
            }

            for (int f = 0; f < dh.matriz.length; f++) {
                for (int c = 0; c < dh.matriz[f].length; c++) {
                    DatosCelda celdaDatos = dh.matriz[f][c];
                    String tipo = celdaDatos.tipo;

                    if (tipo.equals("objeto")) {
                        if (celdaDatos.contenido instanceof LinkedTreeMap) {
                            LinkedTreeMap mapa = (LinkedTreeMap) celdaDatos.contenido;
                            String idObj = (String) mapa.get("id");

                            for (int i = 0; i < objetosDisponibles.getTamaño(); i++) {
                                Objeto plantilla = objetosDisponibles.getDatoEn(i);
                                if (plantilla.getId().equals(idObj)) {
                                    Objeto nuevo = null;
                                    if (plantilla instanceof Arma) {
                                        Arma a = (Arma) plantilla;
                                        Arma copia = new Arma(a.getId(), a.getNombre(), a.getBonusAtaque(), a.getRango(), a.getSlot());
                                        copia.setHabilidad(a.getHabilidad());
                                        copia.setProbabilidad(a.getProbabilidad());
                                        nuevo = copia;
                                    } else if (plantilla instanceof Pocion) {
                                        Pocion p = (Pocion) plantilla;
                                        nuevo = new Pocion(p.getId(), p.getNombre(), p.getBonusVida());
                                    } else if (plantilla instanceof Llave) {
                                        Llave l = (Llave) plantilla;
                                        nuevo = new Llave(l.getId(), l.getNombre());
                                    }
                                    if (nuevo != null) {
                                        hab.colocarObjeto(celdaDatos.fila, celdaDatos.columna, nuevo);
                                    }
                                    break;
                                }
                            }
                        }

                    } else if (tipo.equals("enemigo")) {
                        if (celdaDatos.contenido instanceof LinkedTreeMap) {
                            LinkedTreeMap mapa = (LinkedTreeMap) celdaDatos.contenido;
                            String idEnem = (String) mapa.get("id");

                            for (int i = 0; i < plantillas.getTamaño(); i++) {
                                EnemigoPlantilla p = plantillas.getDatoEn(i);
                                if (p.getId().equals(idEnem)) {
                                    Enemigo enemigo = p.crearEnemigo();
                                    hab.colocarEnemigo(celdaDatos.fila, celdaDatos.columna, enemigo);
                                    break;
                                }
                            }
                        }

                    } else if (tipo.equals("puerta")) {
                        if (celdaDatos.contenido instanceof LinkedTreeMap) {
                            LinkedTreeMap mapa = (LinkedTreeMap) celdaDatos.contenido;
                            String destino = (String) mapa.get("destino");
                            Boolean necesitaLlave = (Boolean) mapa.get("necesitaLlave");

                            Celda celdaPuerta = hab.getCelda(celdaDatos.fila, celdaDatos.columna);
                            if (destino != null && celdaPuerta != null) {
                                if (necesitaLlave != null && necesitaLlave) {
                                    String idLlave = (String) mapa.get("idLlave");
                                    Puerta puerta = new Puerta(destino, idLlave, celdaPuerta);
                                    hab.colocarPuerta(celdaDatos.fila, celdaDatos.columna, puerta);
                                } else {
                                    Puerta puerta = new Puerta(destino, celdaPuerta);
                                    hab.colocarPuerta(celdaDatos.fila, celdaDatos.columna, puerta);
                                }
                            }
                        }

                    } else if (tipo.equals("trampa")) {
                        int danio = 0;
                        if (celdaDatos.contenido instanceof LinkedTreeMap) {
                            LinkedTreeMap mapa = (LinkedTreeMap) celdaDatos.contenido;
                            Object d = mapa.get("danio");
                            if (d instanceof Double) {
                                danio = ((Double) d).intValue();
                            } else if (d instanceof Long) {
                                danio = ((Long) d).intValue();
                            }
                        }
                        Trampa trampa = new Trampa(danio);
                        hab.colocarTrampa(celdaDatos.fila, celdaDatos.columna, trampa);
                    }
                }
            }

            lista.add(hab);
        }

        return lista;
    }

    // Construye el grafo de conexiones entre habitaciones
    private static Grafo<String> crearGrafo(DatosConexion[] datosConexiones) {
        Grafo<String> grafo = new Grafo<>();
        if (datosConexiones == null) return grafo;

        for (DatosConexion dc : datosConexiones) {
            if (!grafo.contains(dc.origen)) {
                grafo.addNodo(dc.origen);
            }
            if (!grafo.contains(dc.destino)) {
                grafo.addNodo(dc.destino);
            }
            grafo.addArista(dc.origen, dc.destino);
        }

        return grafo;
    }
}