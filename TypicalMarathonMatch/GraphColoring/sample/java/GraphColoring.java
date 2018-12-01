import java.io.*;
import java.security.*;
import java.util.*;

public class GraphColoring {

    public static void main (String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int N = sc.nextInt();
            int M = sc.nextInt();
            int a[] = new int[M];
            int b[] = new int[M];
            for (int i = 0; i < M; i++) {
                a[i] = sc.nextInt();
                b[i] = sc.nextInt();
            }
            for (int i = 0; i < N; ++i) {
                System.out.println(i);
            }
        }
        catch (Exception e) {

        }
    }

}
