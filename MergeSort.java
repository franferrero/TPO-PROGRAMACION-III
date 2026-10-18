public class MergeSort {

    // Devuelve una copia ordenada; el arreglo original no se modifica
    public static Pedido[] ordenar(Pedido[] pedidos, int criterio) {
        Pedido[] copia = pedidos.clone();
        mergeSort(copia, 0, copia.length - 1, criterio);
        return copia;
    }

    private static void mergeSort(Pedido[] s, int inicio, int fin, int criterio) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSort(s, inicio, medio, criterio);
            mergeSort(s, medio + 1, fin, criterio);
            merge(s, inicio, fin, criterio);
        }
    }

    private static void merge(Pedido[] s, int inicio, int fin, int criterio) {
        Pedido[] r = new Pedido[fin - inicio + 1];
        int medio = (inicio + fin) / 2;
        int i = inicio;
        int j = medio + 1;
        for (int k = 0; k <= fin - inicio; k++) {

            if (i <= medio && (j > fin || ComparadorPedido.comparar(s[i], s[j], criterio) <= 0)) { // si cumple tomo el de la izq
                r[k] = s[i];
                i++;
            } else { // sino tomo de la derecha
                r[k] = s[j];
                j++;
            }
        }

        for (int k = 0; k <= fin - inicio; k++) {
            s[inicio + k] = r[k];
        }
    }
}