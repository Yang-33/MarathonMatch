import java.io.*;
import java.security.*;
import java.util.*;

public class RectanglePacking {

    public static void main (String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int N = sc.nextInt();
            int w[] = new int[N];
            int h[] = new int[N];
            for (int i = 0; i < N; i++) {
                w[i] = sc.nextInt();
                h[i] = sc.nextInt();
            }
            for (int i = 0; i < N; ++i) {
                System.out.println((50 * (i % 20)) + " " + (50 * (i / 20)));
            }
        }
        catch (Exception e) {

        }
    }

}
