package rutas;

import java.util.ArrayList;
import java.util.Random;


public class PruebasFloydWarshall {
    private static int pruebas = 0;
    private static int grupos = 0;

    public static void main(String[] args) {
        ejemploInicial();
        multiplesConsultas();
        deshabilitarYReprocesar();
        conexionesDirigidas();
        inalcanzables();
        mismaUbicacion();
        conexionIndirectaYDirecta();
        cicloYEmpates();
        identificadoresNoConsecutivos();
        tiemposGrandes();
        copiasProtegidas();
        datosInvalidos();
        grafoVacioYUnico();
        comparacionConDijkstra();
        volumen();
        System.out.println("PASS: " + pruebas + " verificaciones en " + grupos + " grupos");
    }

    private static RedVial redBase() {
        RedVial red = new RedVial();
        for (int i = 1; i <= 5; i++) red.agregarUbicacion(i);
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);
        return red;
    }

    private static void ejemploInicial() {
        grupos++;
        FloydWarshall floyd = new FloydWarshall(redBase());
        igual(13, floyd.consultarTiempo(1, 4), "Ruta principal");
        camino(floyd.consultarRecorrido(1, 4), 1, 2, 3, 4);
        ResultadoRuta resultado = floyd.consultar(1, 4);
        comprobar(resultado.hayRuta(), "Existe el recorrido");
        igual(13, resultado.getTiempoTotal(), "Tiempo en resultado");
        camino(resultado.getUbicaciones(), 1, 2, 3, 4);
    }

    private static void multiplesConsultas() {
        grupos++;
        FloydWarshall floyd = new FloydWarshall(redBase());
        igual(6, floyd.consultarTiempo(1, 2), "1 a 2");
        igual(10, floyd.consultarTiempo(1, 3), "1 a 3");
        igual(13, floyd.consultarTiempo(1, 4), "1 a 4");
        igual(4, floyd.consultarTiempo(2, 3), "2 a 3");
        igual(7, floyd.consultarTiempo(2, 4), "2 a 4");
        igual(3, floyd.consultarTiempo(3, 4), "3 a 4");
        camino(floyd.consultarRecorrido(2, 4), 2, 3, 4);
        camino(floyd.consultarRecorrido(1, 3), 1, 2, 3);
        igual(13, floyd.consultarTiempo(1, 4), "Consulta repetida sin recalcular");
    }

    private static void deshabilitarYReprocesar() {
        grupos++;
        RedVial red = redBase();
        FloydWarshall anterior = new FloydWarshall(red);
        red.deshabilitarConexion(2, 3);
        // Una preparacion conserva el estado de la red al momento de construirla.
        igual(13, anterior.consultarTiempo(1, 4), "El resultado anterior es una foto de la red");
        FloydWarshall nuevo = new FloydWarshall(red);
        igual(19, nuevo.consultarTiempo(1, 4), "Reprocesar luego de deshabilitar");
        camino(nuevo.consultarRecorrido(1, 4), 1, 3, 4);
        red.deshabilitarConexion(1, 3);
        igual(20, new FloydWarshall(red).consultarTiempo(1, 4), "Segundo cambio");
        red.agregarConexion(2, 3, 4);
        igual(13, new FloydWarshall(red).consultarTiempo(1, 4), "Rehabilitar");
        red.agregarUbicacion(6);
        igual(-1, new FloydWarshall(red).consultarTiempo(1, 6), "Ubicacion agregada aislada");
    }

    private static void conexionesDirigidas() {
        grupos++;
        RedVial red = new RedVial();
        red.agregarUbicacion(7);
        red.agregarUbicacion(8);
        red.agregarConexion(7, 8, 9);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(9, floyd.consultarTiempo(7, 8), "Ida");
        igual(-1, floyd.consultarTiempo(8, 7), "Vuelta inexistente");
        red.agregarConexion(8, 7, 15);
        igual(15, new FloydWarshall(red).consultarTiempo(8, 7), "Vuelta habilitada");
        red.deshabilitarConexion(7, 8);
        floyd = new FloydWarshall(red);
        igual(-1, floyd.consultarTiempo(7, 8), "Ida deshabilitada");
        igual(15, floyd.consultarTiempo(8, 7), "Vuelta no se afecta");
    }

    private static void inalcanzables() {
        grupos++;
        FloydWarshall floyd = new FloydWarshall(redBase());
        igual(-1, floyd.consultarTiempo(1, 5), "Nodo aislado");
        igual(-1, floyd.consultarTiempo(4, 1), "Sentido no alcanzable");
        igual(0, floyd.consultarRecorrido(1, 5).size(), "Sin recorrido");
        comprobar(!floyd.consultar(1, 5).hayRuta(), "Resultado vacio");
        igual(-1, floyd.consultar(1, 5).getTiempoTotal(), "Resultado sin tiempo");
        comprobar(floyd.consultar(1, 5).toString().contains("No existe"), "Mensaje sin conexion");
    }

    private static void mismaUbicacion() {
        grupos++;
        FloydWarshall floyd = new FloydWarshall(redBase());
        for (int i = 1; i <= 5; i++) {
            igual(0, floyd.consultarTiempo(i, i), "Diagonal de cero");
            camino(floyd.consultarRecorrido(i, i), i);
        }
    }

    private static void conexionIndirectaYDirecta() {
        grupos++;
        RedVial red = redBase();
        red.agregarConexion(1, 4, 2);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(2, floyd.consultarTiempo(1, 4), "Conexion directa mejor");
        camino(floyd.consultarRecorrido(1, 4), 1, 4);
        red.deshabilitarConexion(1, 4);
        igual(13, new FloydWarshall(red).consultarTiempo(1, 4), "Camino indirecto mejor");
        igual(6, new FloydWarshall(red).consultarTiempo(1, 2), "Otra consulta");
    }

    private static void cicloYEmpates() {
        grupos++;
        RedVial red = new RedVial();
        for (int id = 4; id >= 1; id--) red.agregarUbicacion(id);
        red.agregarConexion(1, 2, 1);
        red.agregarConexion(1, 3, 1);
        red.agregarConexion(2, 4, 2);
        red.agregarConexion(3, 4, 2);
        red.agregarConexion(3, 2, 2);
        red.agregarConexion(2, 3, 2);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(3, floyd.consultarTiempo(1, 4), "Empate de caminos");
        comprobar(floyd.consultarRecorrido(1, 4).size() == 3, "Tres ubicaciones");
        igual(2, floyd.consultarTiempo(3, 2), "Ciclo 3-2");
        igual(2, floyd.consultarTiempo(2, 3), "Ciclo 2-3");
        caminoValido(red, floyd.consultar(1, 4));
    }

    private static void identificadoresNoConsecutivos() {
        grupos++;
        RedVial red = new RedVial();
        red.agregarUbicacion(999);
        red.agregarUbicacion(-2);
        red.agregarUbicacion(50);
        red.agregarConexion(999, -2, 5);
        red.agregarConexion(-2, 50, 7);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(12, floyd.consultarTiempo(999, 50), "IDs arbitrarios");
        camino(floyd.consultarRecorrido(999, 50), 999, -2, 50);
        igual(-1, floyd.consultarTiempo(50, 999), "Sin vuelta");
    }

    private static void tiemposGrandes() {
        grupos++;
        RedVial red = new RedVial();
        red.agregarUbicacion(1);
        red.agregarUbicacion(2);
        red.agregarUbicacion(3);
        red.agregarUbicacion(4);
        for (int i = 1; i <= 3; i++) red.agregarConexion(i, i + 1, Integer.MAX_VALUE);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(3L * Integer.MAX_VALUE, floyd.consultarTiempo(1, 4), "Sumas con long");
        camino(floyd.consultarRecorrido(1, 4), 1, 2, 3, 4);
    }

    private static void copiasProtegidas() {
        grupos++;
        RedVial red = redBase();
        FloydWarshall floyd = new FloydWarshall(red);
        red.getUbicaciones().clear();
        ArrayList<Integer> camino = floyd.consultarRecorrido(1, 4);
        camino.clear();
        igual(4, floyd.consultarRecorrido(1, 4).size(), "Recorrido defensivo");
        ResultadoRuta resultado = floyd.consultar(1, 4);
        resultado.getUbicaciones().clear();
        igual(4, floyd.consultar(1, 4).getUbicaciones().size(), "Resultado defensivo");
        igual(5, red.getUbicaciones().size(), "Red original intacta");
    }

    private static void datosInvalidos() {
        grupos++;
        excepcion(() -> new FloydWarshall(null), "Red nula");
        FloydWarshall floyd = new FloydWarshall(redBase());
        excepcion(() -> floyd.consultarTiempo(55, 1), "Origen inexistente");
        excepcion(() -> floyd.consultarTiempo(1, 55), "Destino inexistente");
        excepcion(() -> floyd.consultarRecorrido(55, 1), "Origen inexistente recorrido");
        excepcion(() -> floyd.consultar(1, 55), "Destino inexistente resultado");
        RedVial red = redBase();
        excepcion(() -> red.agregarConexion(1, 2, 0), "No permite cero");
        excepcion(() -> red.agregarConexion(1, 2, -2), "No permite negativo");
    }

    private static void grafoVacioYUnico() {
        grupos++;
        FloydWarshall vacio = new FloydWarshall(new RedVial());
        excepcion(() -> vacio.consultarTiempo(1, 1), "Grafo vacio");
        RedVial uno = new RedVial();
        uno.agregarUbicacion(15);
        FloydWarshall floyd = new FloydWarshall(uno);
        igual(0, floyd.consultarTiempo(15, 15), "Unico nodo");
        camino(floyd.consultarRecorrido(15, 15), 15);
    }

    private static void comparacionConDijkstra() {
        grupos++;
        RedVial red = new RedVial();
        final int n = 30;
        for (int i = 0; i < n; i++) red.agregarUbicacion(i * 3 + 10);
        Random random = new Random(2026);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && random.nextInt(5) == 0) {
                    red.agregarConexion(i * 3 + 10, j * 3 + 10, 1 + random.nextInt(50));
                }
            }
        }
        FloydWarshall floyd = new FloydWarshall(red);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int origen = i * 3 + 10;
                int destino = j * 3 + 10;
                ResultadoRuta esperado = CalculadorRutas.calcular(red, origen, destino);
                ResultadoRuta obtenido = floyd.consultar(origen, destino);
                igual(esperado.getTiempoTotal(), obtenido.getTiempoTotal(), "Floyd vs Dijkstra " + origen + "-" + destino);
                comprobar(obtenido.hayRuta() == esperado.hayRuta(), "Alcanzabilidad " + origen + "-" + destino);
                if (obtenido.hayRuta()) caminoValido(red, obtenido);
            }
        }
    }

    private static void volumen() {
        grupos++;
        final int n = 180;
        RedVial red = new RedVial();
        for (int i = 0; i < n; i++) red.agregarUbicacion(i);
        for (int i = 0; i < n - 1; i++) red.agregarConexion(i, i + 1, 1);
        for (int i = 0; i < n - 2; i++) red.agregarConexion(i, i + 2, 9);
        FloydWarshall floyd = new FloydWarshall(red);
        igual(n - 1, floyd.consultarTiempo(0, n - 1), "Cadena 180 ubicaciones");
        igual(n, floyd.consultarRecorrido(0, n - 1).size(), "Reconstruccion completa");
        igual(-1, floyd.consultarTiempo(n - 1, 0), "Direccion inversa inalcanzable");
    }

    private static void caminoValido(RedVial red, ResultadoRuta resultado) {
        ArrayList<Integer> ids = resultado.getUbicaciones();
        long suma = 0;
        for (int i = 1; i < ids.size(); i++) {
            int tiempo = red.getTiempoConexion(ids.get(i - 1), ids.get(i));
            comprobar(tiempo > 0, "Cada paso existe");
            suma += tiempo;
        }
        igual(resultado.getTiempoTotal(), suma, "El recorrido suma el tiempo minimo");
    }

    private static void camino(ArrayList<Integer> obtenido, int... esperado) {
        igual(esperado.length, obtenido.size(), "Longitud del recorrido");
        for (int i = 0; i < esperado.length; i++) igual(esperado[i], obtenido.get(i), "Ubicacion en recorrido");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        pruebas++;
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void igual(long esperado, long obtenido, String mensaje) {
        comprobar(esperado == obtenido, mensaje + ": esperado " + esperado + ", obtenido " + obtenido);
    }

    private static void excepcion(Runnable accion, String mensaje) {
        boolean lanzo = false;
        try { accion.run(); } catch (IllegalArgumentException ex) { lanzo = true; }
        comprobar(lanzo, mensaje);
    }
}
