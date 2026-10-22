package rutas;

import java.util.ArrayList;

/** Pruebas sin JUnit, igual que las otras funcionalidades del TPO. */
public class PruebasRutas {
    private static int verificaciones = 0;
    private static int grupos = 0;

    public static void main(String[] args) {
        ejemploBase();
        deshabilitarConexion();
        direccion();
        desconectados();
        origenIgualDestino();
        caminoDirecto();
        caminosAlternativos();
        empates();
        tiemposGrandes();
        listaDecisiones();
        copiasProtegidas();
        datosInvalidos();
        volumen();
        System.out.println("PASS: " + verificaciones + " verificaciones en " + grupos + " grupos");
    }

    private static RedVial redEjemplo() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) red.agregarUbicacion(id);
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);
        return red;
    }

    private static void ejemploBase() {
        grupos++;
        ResultadoRuta r = CalculadorRutas.calcular(redEjemplo(), 1, 4);
        comprobar(r.hayRuta(), "Debe existir ruta");
        igual(13, r.getTiempoTotal(), "Tiempo minimo ejemplo");
        recorrido(r, 1, 2, 3, 4);
    }

    private static void deshabilitarConexion() {
        grupos++;
        RedVial red = redEjemplo();
        red.deshabilitarConexion(2, 3);
        igual(0, red.getTiempoConexion(2, 3), "Conexion deshabilitada");
        ResultadoRuta r = CalculadorRutas.calcular(red, 1, 4);
        igual(19, r.getTiempoTotal(), "Ruta recalculada");
        recorrido(r, 1, 3, 4);
        red.deshabilitarConexion(1, 3);
        r = CalculadorRutas.calcular(red, 1, 4);
        igual(20, r.getTiempoTotal(), "Segunda modificacion");
        recorrido(r, 1, 2, 4);
        red.agregarConexion(2, 3, 4);
        igual(13, CalculadorRutas.calcular(red, 1, 4).getTiempoTotal(), "Rehabilitar conexion");
    }

    private static void direccion() {
        grupos++;
        RedVial red = new RedVial();
        red.agregarUbicacion(1);
        red.agregarUbicacion(2);
        red.agregarConexion(1, 2, 8);
        igual(8, CalculadorRutas.calcular(red, 1, 2).getTiempoTotal(), "Ida");
        comprobar(!CalculadorRutas.calcular(red, 2, 1).hayRuta(), "Vuelta inexistente");
        red.agregarConexion(2, 1, 10);
        igual(10, CalculadorRutas.calcular(red, 2, 1).getTiempoTotal(), "Vuelta agregada");
        red.deshabilitarConexion(1, 2);
        igual(10, red.getTiempoConexion(2, 1), "Deshabilitar no altera vuelta");
    }

    private static void desconectados() {
        grupos++;
        RedVial red = redEjemplo();
        ResultadoRuta r = CalculadorRutas.calcular(red, 1, 5);
        comprobar(!r.hayRuta(), "Nodo aislado");
        igual(-1, r.getTiempoTotal(), "Tiempo inexistente");
        igual(0, r.getUbicaciones().size(), "Recorrido vacio");
        comprobar(r.toString().contains("No existe"), "Mensaje sin ruta");
        comprobar(!CalculadorRutas.calcular(red, 4, 1).hayRuta(), "Direccion no alcanzable");
    }

    private static void origenIgualDestino() {
        grupos++;
        ResultadoRuta r = CalculadorRutas.calcular(redEjemplo(), 5, 5);
        comprobar(r.hayRuta(), "Mismo nodo alcanzable");
        igual(0, r.getTiempoTotal(), "Mismo nodo tiempo 0");
        recorrido(r, 5);
    }

    private static void caminoDirecto() {
        grupos++;
        RedVial red = redEjemplo();
        ResultadoRuta r = CalculadorRutas.calcular(red, 1, 2);
        recorrido(r, 1, 2);
        igual(6, r.getTiempoTotal(), "Camino directo");
    }

    private static void caminosAlternativos() {
        grupos++;
        RedVial red = redEjemplo();
        red.agregarConexion(1, 4, 2);
        recorrido(CalculadorRutas.calcular(red, 1, 4), 1, 4);
        igual(2, CalculadorRutas.calcular(red, 1, 4).getTiempoTotal(), "Directa mas rapida");
        red.deshabilitarConexion(1, 4);
        igual(13, CalculadorRutas.calcular(red, 1, 4).getTiempoTotal(), "Quitar directo");
        igual(6, CalculadorRutas.calcular(red, 1, 2).getTiempoTotal(), "Otra consulta independiente");
    }

    private static void empates() {
        grupos++;
        RedVial red = new RedVial();
        for (int id = 4; id >= 1; id--) red.agregarUbicacion(id);
        red.agregarConexion(1, 2, 1);
        red.agregarConexion(1, 3, 1);
        red.agregarConexion(2, 4, 2);
        red.agregarConexion(3, 4, 2);
        ResultadoRuta r = CalculadorRutas.calcular(red, 1, 4);
        igual(3, r.getTiempoTotal(), "Empate de caminos");
        recorrido(r, 1, 2, 4); // Convencion: se confirma antes menor id.
    }

    private static void tiemposGrandes() {
        grupos++;
        RedVial red = new RedVial();
        for (int i = 1; i <= 3; i++) red.agregarUbicacion(i);
        red.agregarConexion(1, 2, Integer.MAX_VALUE);
        red.agregarConexion(2, 3, Integer.MAX_VALUE);
        igual(2L * Integer.MAX_VALUE, CalculadorRutas.calcular(red, 1, 3).getTiempoTotal(),
                "Sumar tiempos sin desbordamiento int");
        recorrido(CalculadorRutas.calcular(red, 1, 3), 1, 2, 3);
    }

    private static void listaDecisiones() {
        grupos++;
        ResultadoRuta r = CalculadorRutas.calcular(redEjemplo(), 1, 4);
        comprobar(r.getDecisiones().size() >= 9, "Traza de confirmaciones y mejoras");
        comprobar(contiene(r.getDecisiones(), "Se confirma 1"), "Confirma origen");
        comprobar(contiene(r.getDecisiones(), "Se mejora 3"), "Explica mejora");
        comprobar(contiene(r.getDecisiones(), "Se confirma 4"), "Confirma destino");
    }

    private static boolean contiene(ArrayList<String> lista, String texto) {
        for (String item : lista) if (item.contains(texto)) return true;
        return false;
    }

    private static void copiasProtegidas() {
        grupos++;
        RedVial red = redEjemplo();
        red.getUbicaciones().clear();
        igual(5, red.getUbicaciones().size(), "Ubicaciones defensivas");
        ResultadoRuta r = CalculadorRutas.calcular(red, 1, 4);
        r.getUbicaciones().clear();
        r.getDecisiones().clear();
        recorrido(r, 1, 2, 3, 4);
        comprobar(!r.getDecisiones().isEmpty(), "Decisiones defensivas");
    }

    private static void datosInvalidos() {
        grupos++;
        RedVial red = redEjemplo();
        excepcion(() -> red.agregarUbicacion(1), "Ubicacion repetida");
        excepcion(() -> red.agregarConexion(1, 2, 0), "Tiempo cero");
        excepcion(() -> red.agregarConexion(1, 2, -2), "Tiempo negativo");
        excepcion(() -> red.agregarConexion(1, 99, 4), "Destino desconocido");
        excepcion(() -> red.agregarConexion(99, 1, 4), "Origen desconocido");
        excepcion(() -> red.agregarConexion(1, 1, 4), "Auto conexion");
        excepcion(() -> red.deshabilitarConexion(1, 99), "Deshabilitar desconocido");
        excepcion(() -> red.getTiempoConexion(99, 1), "Consultar desconocido");
        excepcion(() -> CalculadorRutas.calcular(null, 1, 2), "Grafo nulo");
        excepcion(() -> CalculadorRutas.calcular(red, 99, 2), "Inicio inexistente");
        excepcion(() -> CalculadorRutas.calcular(red, 1, 99), "Destino inexistente");
        igual(6, red.getTiempoConexion(1, 2), "Invalido no cambia red");
    }

    private static void volumen() {
        grupos++;
        RedVial red = new RedVial();
        final int n = 200;
        for (int i = 0; i < n; i++) red.agregarUbicacion(i);
        for (int i = 0; i < n - 1; i++) red.agregarConexion(i, i + 1, 1);
        for (int i = 0; i < n - 2; i++) red.agregarConexion(i, i + 2, 10);
        ResultadoRuta r = CalculadorRutas.calcular(red, 0, n - 1);
        igual(n - 1, r.getTiempoTotal(), "200 nodos, tiempo optimo");
        igual(n, r.getUbicaciones().size(), "200 nodos, camino completo");
        comprobar(r.hayRuta(), "Red grande alcanzable");
    }

    private static void recorrido(ResultadoRuta resultado, int... esperado) {
        ArrayList<Integer> obtenido = resultado.getUbicaciones();
        igual(esperado.length, obtenido.size(), "Cantidad de ubicaciones en recorrido");
        for (int i = 0; i < esperado.length; i++) igual(esperado[i], obtenido.get(i), "Ubicacion " + i);
    }

    private static void comprobar(boolean condicion, String mensaje) {
        verificaciones++;
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void igual(long esperado, long obtenido, String mensaje) {
        comprobar(esperado == obtenido, mensaje + ": esperado " + esperado + ", obtenido " + obtenido);
    }

    private static void excepcion(Runnable accion, String mensaje) {
        boolean ocurrio = false;
        try { accion.run(); } catch (IllegalArgumentException ex) { ocurrio = true; }
        comprobar(ocurrio, mensaje);
    }
}
