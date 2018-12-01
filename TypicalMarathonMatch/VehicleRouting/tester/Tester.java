import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.security.*;
import javax.swing.*;
import javax.imageio.*;

public class Tester {

    public class Visualizer extends JPanel implements WindowListener {
    	
        public void paint(Graphics g) {

            try {
                BufferedImage bi = new BufferedImage(SIZE_VIS_X + VEHICLE_VIEW_WIDTH, SIZE_VIS_Y, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = (Graphics2D)bi.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0xD3D3D3));
                g2.fillRect(0, 0, SIZE_VIS_X + VEHICLE_VIEW_WIDTH, SIZE_VIS_Y);
                g2.setColor(new Color(0xFFFFFF));
                g2.fillRect(10, 10, SIZE_VIS_X - 20, SIZE_VIS_Y - 20);

                for (int i = 0; i < perm.length; i++) {
                    int x = depotX, y = depotY;
                    Color c = Color.getHSBColor((1.0f / (float)M) * (float)i, 1.0f, 0.95f);
                    g2.setColor(c);
                    for (int j = 0; j < perm[i].length; j++) {
                        int idx = perm[i][j];
                        if (j % cap[i] == 0) {
                            g2.drawLine(x * 10 + 20, y * 10 + 20, 
                                        depotX * 10 + 20, depotY * 10 + 20);
                            x = depotX;
                            y = depotY;
                        }
                        g2.drawLine(x * 10 + 20, y * 10 + 20, 
                                    posX[idx] * 10 + 20, posY[idx] * 10 + 20);
                        x = posX[idx];
                        y = posY[idx];
                    }
                    g2.drawLine(x * 10 + 20, y * 10 + 20, 
                                depotX * 10 + 20, depotY * 10 + 20);
                }

                for (int i = 0; i < perm.length; i++) {
                    for (int j = 0; j < perm[i].length; j++) {
                        int idx = perm[i][j];
                        Color c = Color.getHSBColor((1.0f / (float)M) * (float)i, 1.0f, 0.95f);
                        g2.setColor(c);
                        g2.fillOval(posX[idx] * 10 + 17, posY[idx] * 10 + 16, 8, 8);
                        g2.setColor(new Color(0x000000));
                        g2.drawOval(posX[idx] * 10 + 17, posY[idx] * 10 + 16, 8, 8);
                    }
                }

                if (numb) {
                    g2.setFont(new Font("Arial", Font.PLAIN, 12));
                    FontMetrics fm = g2.getFontMetrics();
                    for (int i = 0; i < N; ++i) {
                        char[] ch = ("" + i).toCharArray();
                        int x = posX[i] * 10 + 15;
                        int y = posY[i] * 10 + 15;
                        g2.drawChars(ch, 0, ch.length, x, y);
                    }
                }

                g2.setColor(new Color(0x000000));
                g2.fillOval(depotX * 10 + 10, depotY * 10 + 10, 20, 20);

                /* vehicle infomation */
                int worst_idx = -1;
                double worst_time = -1.0;
                for (int i = 0; i < M; i++) {
                    if (worst_time < dist[i] / (double)speed[i]) {
                        worst_time = dist[i] / (double)speed[i];
                        worst_idx = i;
                    }
                }

                BasicStroke wideStroke = new BasicStroke(2.0f);
                g2.setStroke(wideStroke);
                for (int i = 0; i < M; i++) {
                    
                    g2.setColor(new Color(0xFFFFFF));
                    g2.fillRect(SIZE_VIS_X, i * 100 + 10, VEHICLE_VIEW_WIDTH - 10, 90);
                    Color c = Color.getHSBColor((1.0f / (float)M) * (float)i, 1.0f, 0.95f);
                    g2.setColor(c);
                    g2.drawRect(SIZE_VIS_X, i * 100 + 10, VEHICLE_VIEW_WIDTH - 10, 90);
                    
                    g2.setColor(new Color(0x000000));
                    FontMetrics fm = g2.getFontMetrics();
                    char[] ch0 = ("Vehicle" + i).toCharArray();
                    char[] ch1 = ("capacity : " + cap[i]).toCharArray();
                    char[] ch2 = ("speed : " + speed[i]).toCharArray();
                    char[] ch3 = ("distance : " + dist[i]).toCharArray();
                    char[] ch4 = ("time : " + dist[i] / (double)speed[i] + "\n").toCharArray();
                    g2.setFont(new Font("Arial", Font.BOLD, 13));
                    g2.drawChars(ch0, 0, ch0.length, SIZE_VIS_X + 10, i * 100 + 28);
                    g2.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2.drawChars(ch1, 0, ch1.length, SIZE_VIS_X + 10, i * 100 + 42);
                    g2.drawChars(ch2, 0, ch2.length, SIZE_VIS_X + 10, i * 100 + 57);
                    g2.drawChars(ch3, 0, ch3.length, SIZE_VIS_X + 10, i * 100 + 72);

                    if (i == worst_idx) {
                        g2.setFont(new Font("Arial", Font.BOLD, 12));
                        g2.setColor(new Color(0xFF0000));
                    }
                    g2.drawChars(ch4, 0, ch4.length, SIZE_VIS_X + 10, i * 100 + 87);
                }   
                
                g.drawImage(bi, 0, 0, SIZE_VIS_X + VEHICLE_VIEW_WIDTH, SIZE_VIS_Y, null);
                if (save) ImageIO.write(bi, "png", new File(fileName +".png"));

            } catch (Exception e) { 
                e.printStackTrace();
            }
        }

        public Visualizer () {
            jf.addWindowListener(this);
        }

        public void windowClosing(WindowEvent e) {
            if (proc != null) {
            	try { 
            		proc.destroy();
            	} catch (Exception ex) {
            		ex.printStackTrace();
            	}
            }
            System.exit(0);
        }

        public void windowActivated(WindowEvent e) { }
        public void windowDeactivated(WindowEvent e) { }
        public void windowOpened(WindowEvent e) { }
        public void windowClosed(WindowEvent e) { }
        public void windowIconified(WindowEvent e) { }
        public void windowDeiconified(WindowEvent e) { }

    }

    /********************************************************************/

    JFrame jf;
    Visualizer v;
    InputStream is;
    OutputStream os;
    Scanner sc;

    static Process proc;
    static String fileName, exec;
    static boolean save, vis, numb, debug;

    final int SIZE = 100 + 1;
    final int SIZE_VIS_X = (SIZE + 3) * 10;
    final int SIZE_VIS_Y = (SIZE + 3) * 10;
    final int VEHICLE_VIEW_WIDTH = 250;
    final int MAXN = 500, MINN = 50;
    final int MAXM = 10,  MINM = 3;
    final int MAX_CAP = 20, MIN_CAP = 5;
    final int MAX_SPEED = 20, MIN_SPEED = 1;

    int N,M;
    int depotX, depotY;
    int [] posX, posY;
    int [] cap;
    int [] speed;
    int [][] perm;
    double [] dist;

    /********************************************************************/

    public void print_debug (double score) {
        System.err.println("\n" + "Number of vertices : " + N);
        System.err.println("Number of vehicles : " + M);
        System.err.println("Worst time : " + score + "\n");
        for (int i = 0; i < M; i++) {
            System.err.println("#Vehicle" + i);
            System.err.println(" capacity : " + cap[i]);
            System.err.println(" speed    : " + speed[i]);
            System.err.println(" distance : " + dist[i]);
            System.err.println(" time     : " + dist[i] / (double)speed[i] + "\n");
        }
    }

    public void generate (String seedStr) {

        try {
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            long seed = Long.parseLong(seedStr);
            rnd.setSeed(seed);

            N = rnd.nextInt(MAXN - MINN + 1) + MINN;
            M = rnd.nextInt(MAXM - MINM + 1) + MINM;

            depotX = rnd.nextInt(SIZE);
            depotY = rnd.nextInt(SIZE);
            boolean [][] usedPos = new boolean[SIZE][SIZE];
            usedPos[depotX][depotY] = true;
            posX = new int[N];
            posY = new int[N];
            for (int i = 0; i < N; i++) {
                int x,y;
                do {
                    x = rnd.nextInt(SIZE);
                    y = rnd.nextInt(SIZE);
                } while (usedPos[x][y]);
                usedPos[x][y] = true;
                posX[i] = x;
                posY[i] = y;
            }

            cap   = new int[M];
            speed = new int[M];
            for (int i = 0; i < M; i++) {
                cap[i]   = rnd.nextInt(MAX_CAP - MIN_CAP + 1) + MIN_CAP;
                speed[i] = rnd.nextInt(MAX_SPEED - MIN_SPEED + 1) + MIN_SPEED;
            }
            
        } catch (Exception e) {
            System.err.println("An exception occurred while generating test case.");
            e.printStackTrace();
        }

    }

    double get_dist (int x1, int y1, int x2, int y2) {
        double lx = (double)(x1 - x2);
        double ly = (double)(y1 - y2);
        return Math.sqrt(lx * lx + ly * ly);
    }

    public double runTest (String seed) {

        try {
            generate(seed);
            if (proc == null) return -1;
            try {
                perm = getPermutation();
                boolean [] used = new boolean[N];
                for (int i = 0; i < perm.length; i++) {
                    for (int j = 0; j < perm[i].length; j++) {
                        if (used[perm[i][j]]) {
                            System.err.println("There are vertices that are delivered multiple times.");
                            return -1;
                        }
                        used[perm[i][j]] = true;
                    }
                }
                for (int i = 0; i < N; i++) {
                    if (!used[i]) {
                        System.err.println("There are vertices that are not delivered.");
                        return -1;
                    }
                }
            } catch (Exception e) {
                return -1;
            }
        } catch (Exception e) {
            System.err.println("Failed to get result.");
            e.printStackTrace();
            return -1;
        }

        if (vis) {
            jf.setSize(SIZE_VIS_X + VEHICLE_VIEW_WIDTH, SIZE_VIS_Y);
            jf.setVisible(true);
        }
        
        double score = -1.0;
        dist = new double[M];
        for (int i = 0; i < perm.length; i++) {
            dist[i] = 0.0;
            int x = depotX, y = depotY;
            for (int j = 0; j < perm[i].length; j++) {
                if (j % cap[i] == 0) {
                    dist[i] += get_dist(x, y, depotX, depotY);
                    x = depotX;
                    y = depotY;
                }
                dist[i] += get_dist(x, y, posX[perm[i][j]], posY[perm[i][j]]);
                x = posX[perm[i][j]];
                y = posY[perm[i][j]];
            }
            dist[i] += get_dist(x, y, depotX, depotY);
            double time = dist[i] / (double) speed[i];
            score = Math.max(score, time);
        }

        if (debug) print_debug(score);

        return score;
    }

    private int [][] getPermutation () throws IOException {
        
        StringBuffer sb = new StringBuffer();
        sb.append(N).append(' ');
        sb.append(M).append('\n');
        sb.append(depotX).append(' ');
        sb.append(depotY).append('\n');
        for (int i = 0; i < N; i++) {
            sb.append(posX[i]).append(' ');
            sb.append(posY[i]).append('\n'); 
        }
        for (int i = 0; i < M; i++) {
            sb.append(cap[i]).append(' ');
            sb.append(speed[i]).append('\n'); 
        }
        os.write(sb.toString().getBytes());
        os.flush();

        int [][] ret = new int[M][];
        for (int i = 0; i < M; ++i) {
            int L = sc.nextInt();
            ret[i] = new int[L];
            for (int j = 0; j < L; j++) {
                ret[i][j] = sc.nextInt();
            }
        }
        return ret;
    }

    public Tester (String seed) {
    	if (vis) {
    		jf = new JFrame();
            v = new Visualizer();
            jf.getContentPane().add(v);
        }
        if (exec != null) {
            try {
                Runtime rt = Runtime.getRuntime();
                proc = rt.exec(exec);
                os = proc.getOutputStream();
                is = proc.getInputStream();
                sc = new Scanner(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Score = " + runTest(seed));
        if (proc != null) {
            try { 
                proc.destroy(); 
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        }
    }

    public static void main (String[] args) {
        String seed = "1";
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-seed")) {
            	seed = args[++i];
            } else if (args[i].equals("-exec")) {
                exec = args[++i];
            } else if (args[i].equals("-vis")) {
                vis = true;
            } else if (args[i].equals("-save")) {
                save = true;
                vis = true;
            } else if (args[i].equals("-num")) {
                numb = true;
            } else if (args[i].equals("-debug")) {
                debug = true;
            }
        }
        fileName = seed;
        Tester test = new Tester(seed);
    }

}