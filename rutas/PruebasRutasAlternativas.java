package rutas;

import java.util.ArrayList;
import java.util.Random;

/**
 * Casos de prueba de B3 (rutas alternativas con restricciones).
 * Se ejecuta con main: cada verificacion imprime OK o FALLO.
 * Las rutas esperadas se escriben como secuencias de ids; las redes de ejemplo usan
 * la misma red de la funcionalidad 2, donde las ubicaciones 1 a 5 se conectan asi:
 * 1 -> 2 (6), 1 -> 3 (16), 1 -> 4 (30), 2 -> 3 (4), 2 -> 4 (14), 3 -> 4 (3), y la 5 queda aislada.
 */
public class PruebasRutasAlternativas {

    private static int total = 0;
    private static int fallos = 0;

    public static void main(String[] args) {
        ejemploPrueba();
        tiempoMaximoDescartaRutas();
        tiempoMaximoEsInclusivo();
        tiempoMaximoMenorQueTodasLasRutas();
        unaUbicacionProhibida();
        variasUbicacionesProhibidas();
        prohibidaYTiempoMaximoJuntos();
        prohibidaQueNoEstaEnNingunCamino();
        origenODestinoProhibido();
        origenIgualDestino();
        redConCiclo();
        destinoSinConexion();
        sentidoDeLasConexiones();
        conexionDeshabilitada();
        redCompleta();
        idsDistintosDeLasPosiciones();
        empateEntreRutas();
        resultadoYListasNoSeModifican();
        textoDeSalida();
        datosInvalidos();
        contraFuerzaBruta();

        System.out.println();
        System.out.println("Total: " + total + " | Fallos: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    // ---------- Casos de prueba ----------

    // De 1 a 4 sin restricciones hay 4 rutas, que se encuentran en este orden
    private static void ejemploPrueba() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 1000, lista());

        verificar("Ejemplo: hay 4 rutas", r.cantidad() == 4);
        verificar("Ejemplo: 1 -> 2 -> 3 -> 4 en 13 minutos", rutaEs(r, 0, 13, 1, 2, 3, 4));
        verificar("Ejemplo: 1 -> 2 -> 4 en 20 minutos", rutaEs(r, 1, 20, 1, 2, 4));
        verificar("Ejemplo: 1 -> 3 -> 4 en 19 minutos", rutaEs(r, 2, 19, 1, 3, 4));
        verificar("Ejemplo: 1 -> 4 en 30 minutos", rutaEs(r, 3, 30, 1, 4));
        verificar("Ejemplo: la mejor es 1 -> 2 -> 3 -> 4",
                idsIguales(r.getMejorRuta(), new int[]{1, 2, 3, 4}) && r.getMejorTiempo() == 13);
    }

    // Con maximo 19 quedan afuera las rutas de 20 y 30 minutos
    private static void tiempoMaximoDescartaRutas() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 19, lista());

        verificar("Maximo 19: quedan 2 rutas", r.cantidad() == 2);
        verificar("Maximo 19: queda la de 13 minutos", rutaEs(r, 0, 13, 1, 2, 3, 4));
        verificar("Maximo 19: queda la de 19 minutos", rutaEs(r, 1, 19, 1, 3, 4));
    }

    // Una ruta de exactamente el tiempo maximo es valida
    private static void tiempoMaximoEsInclusivo() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 13, lista());

        verificar("Maximo 13: la ruta de exactamente 13 minutos entra", r.cantidad() == 1);
        verificar("Maximo 13: es 1 -> 2 -> 3 -> 4", rutaEs(r, 0, 13, 1, 2, 3, 4));
    }

    private static void tiempoMaximoMenorQueTodasLasRutas() {
        RedVial red = redEjemplo();

        verificar("Maximo 12 (la mejor tarda 13): no hay rutas", !BuscadorRutas.buscar(red, 1, 4, 12, lista()).hayRutas());
        verificar("Maximo 0: no hay rutas entre ubicaciones distintas", !BuscadorRutas.buscar(red, 1, 4, 0, lista()).hayRutas());
    }

    private static void unaUbicacionProhibida() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 1000, lista(3));

        verificar("Prohibida 3: quedan 2 rutas", r.cantidad() == 2);
        verificar("Prohibida 3: queda 1 -> 2 -> 4", rutaEs(r, 0, 20, 1, 2, 4));
        verificar("Prohibida 3: queda 1 -> 4", rutaEs(r, 1, 30, 1, 4));
        verificar("Prohibida 3: la mejor es 1 -> 2 -> 4",
                idsIguales(r.getMejorRuta(), new int[]{1, 2, 4}) && r.getMejorTiempo() == 20);
    }

    private static void variasUbicacionesProhibidas() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 1000, lista(2, 3));

        verificar("Prohibidas 2 y 3: solo queda la ruta directa", r.cantidad() == 1 && rutaEs(r, 0, 30, 1, 4));
    }

    private static void prohibidaYTiempoMaximoJuntos() {
        // Sin pasar por 3 quedan las de 20 y 30 minutos; con maximo 25 solo la de 20
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 25, lista(3));

        verificar("Prohibida y maximo juntos: queda una ruta", r.cantidad() == 1);
        verificar("Prohibida y maximo juntos: es 1 -> 2 -> 4", rutaEs(r, 0, 20, 1, 2, 4));
    }

    private static void prohibidaQueNoEstaEnNingunCamino() {
        RedVial red = redEjemplo();

        verificar("Prohibida fuera de los caminos (5): siguen las 4 rutas",
                BuscadorRutas.buscar(red, 1, 4, 1000, lista(5)).cantidad() == 4);
        verificar("Lista de prohibidas nula: equivale a no prohibir nada",
                BuscadorRutas.buscar(red, 1, 4, 1000, null).cantidad() == 4);
    }

    private static void origenODestinoProhibido() {
        RedVial red = redEjemplo();

        verificar("Origen prohibido: no hay rutas", !BuscadorRutas.buscar(red, 1, 4, 1000, lista(1)).hayRutas());
        verificar("Destino prohibido: no hay rutas", !BuscadorRutas.buscar(red, 1, 4, 1000, lista(4)).hayRutas());
        verificar("Origen igual a destino y prohibido: no hay rutas",
                !BuscadorRutas.buscar(red, 5, 5, 1000, lista(5)).hayRutas());
    }

    // Igual que en la funcionalidad 2: la ruta de una sola ubicacion, con tiempo 0
    private static void origenIgualDestino() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 2, 2, 1000, lista());

        verificar("Origen igual a destino: una sola ruta", r.cantidad() == 1);
        verificar("Origen igual a destino: la ruta es solo esa ubicacion con tiempo 0", rutaEs(r, 0, 0, 2));
        verificar("Origen igual a destino con maximo 0: sigue siendo valida",
                BuscadorRutas.buscar(redEjemplo(), 2, 2, 0, lista()).hayRutas());
    }

    // Con la conexion 3 -> 2 existe el ciclo 2 -> 3 -> 2: ninguna ruta debe repetir ubicaciones
    private static void redConCiclo() {
        RedVial red = redEjemplo();
        red.agregarConexion(3, 2, 2);
        RutasAlternativas r = BuscadorRutas.buscar(red, 1, 4, 1000, lista());

        verificar("Ciclo: hay 5 rutas (aparece 1 -> 3 -> 2 -> 4)", r.cantidad() == 5);
        verificar("Ciclo: 1 -> 3 -> 2 -> 4 en 32 minutos", rutaEs(r, 2, 32, 1, 3, 2, 4));
        boolean sinRepetidas = true;
        for (int i = 0; i < r.cantidad(); i++) {
            if (!sinRepetidos(r.getRuta(i))) {
                sinRepetidas = false;
            }
        }
        verificar("Ciclo: ninguna ruta repite ubicaciones", sinRepetidas);
        verificar("Ciclo: la mejor sigue siendo 1 -> 2 -> 3 -> 4",
                idsIguales(r.getMejorRuta(), new int[]{1, 2, 3, 4}) && r.getMejorTiempo() == 13);
    }

    private static void destinoSinConexion() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 5, 1000, lista());

        verificar("Destino aislado: no hay rutas", !r.hayRutas() && r.cantidad() == 0);
        verificar("Sin rutas: la mejor ruta esta vacia", r.getMejorRuta().isEmpty());
        verificar("Sin rutas: el mejor tiempo es -1", r.getMejorTiempo() == -1);
        verificar("Sin rutas: el mensaje lo informa", r.toString().contains("No existe"));
    }

    private static void sentidoDeLasConexiones() {
        RedVial red = redEjemplo();

        verificar("Sentido contrario (4 a 1): no hay rutas", !BuscadorRutas.buscar(red, 4, 1, 1000, lista()).hayRutas());

        red.agregarConexion(4, 1, 5);
        RutasAlternativas r = BuscadorRutas.buscar(red, 4, 1, 1000, lista());
        verificar("Con la conexion 4 -> 1 agregada: hay una ruta", r.cantidad() == 1 && rutaEs(r, 0, 5, 4, 1));
    }

    private static void conexionDeshabilitada() {
        RedVial red = redEjemplo();
        red.deshabilitarConexion(2, 3);
        RutasAlternativas r = BuscadorRutas.buscar(red, 1, 4, 1000, lista());

        verificar("Conexion 2 -> 3 deshabilitada: quedan 3 rutas", r.cantidad() == 3);
        verificar("Conexion deshabilitada: ya no existe 1 -> 2 -> 3 -> 4", !contieneRuta(r, 1, 2, 3, 4));
        verificar("Conexion deshabilitada: la mejor es 1 -> 3 -> 4",
                idsIguales(r.getMejorRuta(), new int[]{1, 3, 4}) && r.getMejorTiempo() == 19);
    }

    // En una red completa de 5 ubicaciones hay 16 caminos simples de 1 a 5: 1 + 3 + 6 + 6
    private static void redCompleta() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) {
            red.agregarUbicacion(id);
        }
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (i != j) {
                    red.agregarConexion(i, j, 1);
                }
            }
        }
        RutasAlternativas r = BuscadorRutas.buscar(red, 1, 5, 100, lista());

        verificar("Red completa: hay 16 rutas", r.cantidad() == 16);
        verificar("Red completa: la mejor es la directa", idsIguales(r.getMejorRuta(), new int[]{1, 5}) && r.getMejorTiempo() == 1);
        verificar("Red completa con maximo 2: 1 ruta de 1 tramo y 3 de 2 tramos",
                BuscadorRutas.buscar(red, 1, 5, 2, lista()).cantidad() == 4);
        verificar("Red completa sin 2 ni 3: quedan 1 -> 5 y 1 -> 4 -> 5",
                BuscadorRutas.buscar(red, 1, 5, 100, lista(2, 3)).cantidad() == 2);
    }

    // Las ubicaciones se agregan en el orden 30, 20, 10: el id no coincide con el lugar que ocupa
    private static void idsDistintosDeLasPosiciones() {
        RedVial red = new RedVial();
        red.agregarUbicacion(30);
        red.agregarUbicacion(20);
        red.agregarUbicacion(10);
        red.agregarConexion(10, 20, 5);
        red.agregarConexion(20, 30, 7);
        red.agregarConexion(10, 30, 20);
        RutasAlternativas r = BuscadorRutas.buscar(red, 10, 30, 100, lista());

        verificar("Ids distintos de posiciones: hay 2 rutas", r.cantidad() == 2);
        verificar("Ids distintos de posiciones: existe 10 -> 30 en 20 minutos", contieneRuta(r, 10, 30));
        verificar("Ids distintos de posiciones: existe 10 -> 20 -> 30 en 12 minutos", contieneRuta(r, 10, 20, 30));
        verificar("Ids distintos de posiciones: la mejor es 10 -> 20 -> 30",
                idsIguales(r.getMejorRuta(), new int[]{10, 20, 30}) && r.getMejorTiempo() == 12);
        verificar("Ids distintos de posiciones: prohibida la 20 queda 10 -> 30",
                BuscadorRutas.buscar(red, 10, 30, 100, lista(20)).cantidad() == 1);
    }

    // Dos rutas con el mismo tiempo: la mejor es la que se encontro primero
    private static void empateEntreRutas() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 4; id++) {
            red.agregarUbicacion(id);
        }
        red.agregarConexion(1, 2, 1);
        red.agregarConexion(1, 3, 1);
        red.agregarConexion(2, 4, 2);
        red.agregarConexion(3, 4, 2);
        RutasAlternativas r = BuscadorRutas.buscar(red, 1, 4, 100, lista());

        verificar("Empate: hay 2 rutas de 3 minutos", r.cantidad() == 2 && r.getTiempo(0) == 3 && r.getTiempo(1) == 3);
        verificar("Empate: la mejor es la primera encontrada (1 -> 2 -> 4)",
                idsIguales(r.getMejorRuta(), new int[]{1, 2, 4}) && r.getMejorTiempo() == 3);
    }

    private static void resultadoYListasNoSeModifican() {
        RutasAlternativas r = BuscadorRutas.buscar(redEjemplo(), 1, 4, 1000, lista());
        r.getRuta(0).clear();
        r.getMejorRuta().clear();

        verificar("Modificar la ruta devuelta no altera el resultado", rutaEs(r, 0, 13, 1, 2, 3, 4));
        verificar("Modificar la mejor ruta devuelta no altera el resultado", r.getMejorRuta().size() == 4);

        ArrayList<Integer> prohibidas = lista(3);
        BuscadorRutas.buscar(redEjemplo(), 1, 4, 1000, prohibidas);
        verificar("La lista de prohibidas no se modifica", prohibidas.size() == 1 && prohibidas.get(0) == 3);
    }

    private static void textoDeSalida() {
        String texto = BuscadorRutas.buscar(redEjemplo(), 1, 4, 19, lista()).toString();

        verificar("Texto: indica la cantidad de rutas", texto.contains("Rutas encontradas: 2"));
        verificar("Texto: muestra una ruta con su tiempo", texto.contains("1 -> 2 -> 3 -> 4 | 13 minutos"));
        verificar("Texto: muestra otra ruta con su tiempo", texto.contains("1 -> 3 -> 4 | 19 minutos"));
        verificar("Texto: indica la de menor tiempo", texto.contains("Menor tiempo: 1 -> 2 -> 3 -> 4 | 13 minutos"));
    }

    private static void datosInvalidos() {
        final RedVial red = redEjemplo();

        verificar("Red nula lanza excepcion", lanzaExcepcion(() -> BuscadorRutas.buscar(null, 1, 4, 10, lista())));
        verificar("Origen inexistente lanza excepcion", lanzaExcepcion(() -> BuscadorRutas.buscar(red, 99, 4, 10, lista())));
        verificar("Destino inexistente lanza excepcion", lanzaExcepcion(() -> BuscadorRutas.buscar(red, 1, 99, 10, lista())));
        verificar("Tiempo maximo negativo lanza excepcion", lanzaExcepcion(() -> BuscadorRutas.buscar(red, 1, 4, -1, lista())));
        verificar("Ubicacion prohibida inexistente lanza excepcion",
                lanzaExcepcion(() -> BuscadorRutas.buscar(red, 1, 4, 10, lista(99))));
    }

    // Compara contra una busqueda exhaustiva sin podas en redes aleatorias pequenias:
    // el backtracking debe encontrar exactamente las mismas rutas, con los mismos tiempos
    private static void contraFuerzaBruta() {
        Random azar = new Random(42);
        int distintos = 0;
        int casos = 500;
        for (int caso = 0; caso < casos; caso++) {
            int cantidad = 2 + azar.nextInt(5);
            int[] ids = new int[cantidad];
            for (int i = 0; i < cantidad; i++) {
                ids[i] = 10 * (i + 1);
            }
            for (int i = cantidad - 1; i > 0; i--) { // se agregan en orden mezclado: id y posicion no coinciden
                int k = azar.nextInt(i + 1);
                int aux = ids[i];
                ids[i] = ids[k];
                ids[k] = aux;
            }
            RedVial red = new RedVial();
            for (int id : ids) {
                red.agregarUbicacion(id);
            }
            for (int origen : ids) {
                for (int destino : ids) {
                    if (origen != destino && azar.nextInt(100) < 45) {
                        red.agregarConexion(origen, destino, 1 + azar.nextInt(9));
                    }
                }
            }
            int idOrigen = ids[azar.nextInt(cantidad)];
            int idDestino = ids[azar.nextInt(cantidad)];
            long tiempoMaximo = azar.nextInt(30);
            ArrayList<Integer> prohibidas = new ArrayList<>();
            for (int id : ids) {
                if (azar.nextInt(100) < 15) {
                    prohibidas.add(id);
                }
            }

            RutasAlternativas r = BuscadorRutas.buscar(red, idOrigen, idDestino, tiempoMaximo, prohibidas);
            ArrayList<ArrayList<Integer>> esperadas = fuerzaBruta(red, idOrigen, idDestino, tiempoMaximo, prohibidas);

            boolean coincide = r.cantidad() == esperadas.size();
            for (int i = 0; coincide && i < r.cantidad(); i++) {
                coincide = esperadas.contains(r.getRuta(i)) && r.getTiempo(i) == tiempoDe(red, r.getRuta(i));
            }
            if (!coincide) {
                distintos++;
            }
        }
        verificar("Fuerza bruta: " + casos + " redes aleatorias coinciden (diferencias: " + distintos + ")", distintos == 0);
    }

    // Arma todas las secuencias de ubicaciones sin repetir, y despues se queda con las que cumplen las condiciones.
    // No usa ninguna poda; solo para comparar
    private static ArrayList<ArrayList<Integer>> fuerzaBruta(RedVial red, int idOrigen, int idDestino,
                                                             long tiempoMaximo, ArrayList<Integer> prohibidas) {
        ArrayList<ArrayList<Integer>> todas = new ArrayList<>();
        ArrayList<Integer> camino = new ArrayList<>();
        camino.add(idOrigen);
        todasLasSecuencias(red, camino, idDestino, todas);

        ArrayList<ArrayList<Integer>> validas = new ArrayList<>();
        for (ArrayList<Integer> secuencia : todas) {
            if (esRutaValida(red, secuencia, tiempoMaximo, prohibidas)) {
                validas.add(secuencia);
            }
        }
        return validas;
    }

    private static void todasLasSecuencias(RedVial red, ArrayList<Integer> camino, int idDestino,
                                           ArrayList<ArrayList<Integer>> resultado) {
        if (camino.get(camino.size() - 1) == idDestino) {
            resultado.add(new ArrayList<>(camino));
            return;
        }
        for (int id : red.getUbicaciones()) {
            if (!camino.contains(id)) {
                camino.add(id);
                todasLasSecuencias(red, camino, idDestino, resultado);
                camino.remove(camino.size() - 1);
            }
        }
    }

    private static boolean esRutaValida(RedVial red, ArrayList<Integer> ruta, long tiempoMaximo, ArrayList<Integer> prohibidas) {
        long tiempo = 0;
        for (int i = 0; i < ruta.size(); i++) {
            if (prohibidas.contains(ruta.get(i))) {
                return false;
            }
            if (i > 0) {
                int tramo = red.getTiempoConexion(ruta.get(i - 1), ruta.get(i));
                if (tramo <= 0) {
                    return false;
                }
                tiempo += tramo;
            }
        }
        return tiempo <= tiempoMaximo;
    }

    // ---------- Auxiliares ----------

    // Misma red de la funcionalidad 2
    private static RedVial redEjemplo() {
        RedVial red = new RedVial();
        for (int id = 1; id <= 5; id++) {
            red.agregarUbicacion(id);
        }
        red.agregarConexion(1, 2, 6);
        red.agregarConexion(1, 3, 16);
        red.agregarConexion(1, 4, 30);
        red.agregarConexion(2, 3, 4);
        red.agregarConexion(2, 4, 14);
        red.agregarConexion(3, 4, 3);
        return red;
    }

    private static ArrayList<Integer> lista(int... ids) {
        ArrayList<Integer> lista = new ArrayList<>();
        for (int id : ids) {
            lista.add(id);
        }
        return lista;
    }

    // La ruta numero 'indice' tiene exactamente esos ids y ese tiempo total
    private static boolean rutaEs(RutasAlternativas r, int indice, long tiempo, int... esperado) {
        if (indice >= r.cantidad()) {
            return false; // no existe esa ruta
        }
        return idsIguales(r.getRuta(indice), esperado) && r.getTiempo(indice) == tiempo;
    }

    private static boolean contieneRuta(RutasAlternativas r, int... esperado) {
        for (int i = 0; i < r.cantidad(); i++) {
            if (idsIguales(r.getRuta(i), esperado)) {
                return true;
            }
        }
        return false;
    }

    private static boolean idsIguales(ArrayList<Integer> obtenida, int[] esperado) {
        if (obtenida.size() != esperado.length) {
            return false;
        }
        for (int i = 0; i < esperado.length; i++) {
            if (obtenida.get(i) != esperado[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean sinRepetidos(ArrayList<Integer> ruta) {
        for (int i = 0; i < ruta.size(); i++) {
            for (int j = i + 1; j < ruta.size(); j++) {
                if (ruta.get(i).equals(ruta.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Suma de los tiempos de los tramos de una ruta
    private static long tiempoDe(RedVial red, ArrayList<Integer> ruta) {
        long suma = 0;
        for (int i = 1; i < ruta.size(); i++) {
            suma += red.getTiempoConexion(ruta.get(i - 1), ruta.get(i));
        }
        return suma;
    }

    private interface Accion {
        void ejecutar();
    }

    private static boolean lanzaExcepcion(Accion accion) {
        try {
            accion.ejecutar();
        } catch (IllegalArgumentException e) {
            return true;
        }
        return false;
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) {
            fallos++;
        }
        System.out.println((condicion ? "OK    " : "FALLO ") + descripcion);
    }
}
