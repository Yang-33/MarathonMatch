import java.io.*;
import java.security.*;
import java.util.*;

public class VehicleRouting {

    public static void main (String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int N = sc.nextInt();
            int M = sc.nextInt();
            int depotX = sc.nextInt();
            int depotY = sc.nextInt();
            int posX[] = new int[N];
            int posY[] = new int[N];
            for (int i = 0; i < N; i++) {
                posX[i] = sc.nextInt();
                posY[i] = sc.nextInt();
            }
            int cap[] = new int[M];
            int speed[] = new int[M];
            for (int i = 0; i < M; i++) {
                cap[i] = sc.nextInt();
                speed[i] = sc.nextInt();
            }
            int ans_size[] = new int[M];
            int ans[][] = new int[M][N];
            for (int i = 0; i < N; i++) {
                ans[i % M][ans_size[i % M]++] = i;
            }
            for (int i = 0; i < M; ++i) {
                System.out.print(ans_size[i]);
                for (int j = 0; j < ans_size[i]; j++) {
                    System.out.print(" " + ans[i][j]);
                }
                System.out.println();
            }
        }
        catch (Exception e) {

        }
    }

}
