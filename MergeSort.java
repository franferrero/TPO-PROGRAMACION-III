import java.util.ArrayList;

public class MergeSort {

    // Devuelve una copia ordenada; la lista original no se modifica
    public static ArrayList<Pedido> ordenar(ArrayList<Pedido> pedidos, int criterio) {
        ArrayList<Pedido> copia = new ArrayList<>(pedidos);
        mergeSort(copia, 0, copia.size() - 1, criterio);
        return copia;
    }

    private static void mergeSort(ArrayList<Pedido> s, int inicio, int fin, int criterio) {
        if (inicio < fin) {
            int medio = (inicio + fin) / 2;
            mergeSort(s, inicio, medio, criterio);
            mergeSort(s, medio + 1, fin, criterio);
            merge(s, inicio, fin, criterio);
        }
    }

    private static void merge(ArrayList<Pedido> s, int inicio, int fin, int criterio) {
        ArrayList<Pedido> r = new ArrayList<>();
        int medio = (inicio + fin) / 2;
        int i = inicio;
        int j = medio + 1;
        for (int k = 0; k <= fin - inicio; k++) {

            if (i <= medio && (j > fin || ComparadorPedido.comparar(s.get(i), s.get(j), criterio) <= 0)) { // si cumple tomo el de la izq
                r.add(s.get(i));
                i++;
            } else { // sino tomo de la derecha
                r.add(s.get(j));
                j++;
            }
        }

        for (int k = 0; k <= fin - inicio; k++) {
            s.set(inicio + k, r.get(k));
        }
    }
}