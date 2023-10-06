package in.ac.iiitb.sample;

public class AnalysisDriver {
    private static long fact_iter(int n) {
        long res = 1;
        while (n > 1) {
            res *= n;
            n--;
        }
        return res;
    }

    private static long fact_rec(int n) {
        return fact_aux(n, 1);
    }

    // Tail recursive implementation of factorial
    private static long fact_aux(int n, long aux) {
        if (n <= 1) return aux;
        return fact_aux(n - 1, aux * n);
    }

    public static void main(String[] args) {
        System.out.println(fact_rec(5));
        System.out.println(fact_iter(5));
        int[] arr = {4, 3, 2, 1};
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                int temp = 0;
                if (arr[j] < arr[i]) {
                    temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            System.out.print(arr[i] + " ");
        }
    }
}
