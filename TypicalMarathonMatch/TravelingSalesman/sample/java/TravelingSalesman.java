import java.io.*;
import java.security.*;
import java.util.*;

public class TravelingSalesman {

    public static void main (String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            int N = sc.nextInt();
            int posX[] = new int[N];
            int posY[] = new int[N];
            for (int i = 0; i < N; i++) {
                posX[i] = sc.nextInt();
                posY[i] = sc.nextInt();
            }
            for (int i = 0; i < N; ++i) {
                System.out.println(i);
            }
        }
        catch (Exception e) {

        }
    }

}

