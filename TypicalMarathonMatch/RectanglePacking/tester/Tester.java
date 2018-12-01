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
                int WIDTH  = BOX_SIZE + 40;
                int HEIGHT = BOX_SIZE + 40;
                BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = (Graphics2D)bi.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD3D3D3));
                g2.fillRect(0, 0, WIDTH, HEIGHT);
                g2.setColor(new Color(0xFFFFFF));
                g2.fillRect(20, 20, WIDTH - 40, HEIGHT - 40);

                for (int i = 0; i < N; i++) {
                    Color c = Color.getHSBColor((1.0f / (float)N) * (float)i, 1.0f, 0.95f);
                    g2.setColor(c);
                    g2.fillRect(20 + posX[i], 1020 - posY[i] - h[i], w[i], h[i]);
                    g2.setColor(new Color(0x3F3F3F));
                    g2.drawRect(20 + posX[i], 1020 - posY[i] - h[i], w[i], h[i]);
                }

                g2.setStroke(new BasicStroke(2.0f));
                g2.drawLine(20, 1020 - MAX_POSY, 1020, 1020 - MAX_POSY);

                g2.setFont(new Font("Arial", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                char[] ch = ("Score = " + MAX_POSY).toCharArray();
                int x = BOX_SIZE / 2;
                int y = 1020 - MAX_POSY - 5;
                g2.drawChars(ch, 0, ch.length, x, y);

                g.drawImage(bi, 0, 0, WIDTH, HEIGHT, null);
                if (save) {
                    ImageIO.write(bi, "png", new File(fileName +".png"));
                }

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
    static boolean save, vis;

    final int N = 400;
    final int RECT_MAX = 50, RECT_MIN = 5;
    final int BOX_SIZE = 1000;
    int [] h, w;
    int [] posX, posY;
    int MAX_POSY;

    /********************************************************************/

    public void generate (String seedStr) {

        try {   
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            long seed = Long.parseLong(seedStr);
            rnd.setSeed(seed);

            w = new int[N];
            h = new int[N];
            for (int i = 0; i < N; i++) {
                w[i] = rnd.nextInt(RECT_MAX - RECT_MIN + 1) + RECT_MIN;
                h[i] = rnd.nextInt(RECT_MAX - RECT_MIN + 1) + RECT_MIN;
            }
        } catch (Exception e) {
            System.err.println("An exception occurred while generating test case.");
            e.printStackTrace();
        }

    }

    public double runTest (String seed) {

        try {
            generate(seed);
            if (proc != null) try {
                getPermutation();
                boolean [][] used = new boolean[BOX_SIZE][BOX_SIZE];
                for (int i = 0; i < N; i++) {
                    for (int x = posX[i]; x < posX[i] + w[i]; x++) {
                        for (int y = posY[i]; y < posY[i] + h[i]; y++) {
                            if (used[x][y]) {
                                System.err.println("There are overlapping rectangles.");
                                return -1;
                            }
                            if (x < 0 || y < 0 || x >= BOX_SIZE || y >= BOX_SIZE) {
                                System.err.println("There is a rectangle which does not fit in the box.");
                                return -1;
                            }
                            used[x][y] = true;
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to get result.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        MAX_POSY = 0;
        for (int i = 0; i < N; i++) {
            int y = posY[i] + h[i];
            MAX_POSY = Math.max(MAX_POSY, y);
        }
        
        if (vis) {
            jf.setSize(BOX_SIZE + 40, BOX_SIZE + 40);
            jf.setVisible(true);
        }
        
        return MAX_POSY;
    }

    private void getPermutation () throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(N).append('\n');
        for (int i = 0; i < N; ++i) {
            sb.append(w[i]).append(' ');
            sb.append(h[i]).append('\n');
        }
        os.write(sb.toString().getBytes());
        os.flush();

        posX = new int[N];
        posY = new int[N]; 
        for (int i = 0; i < N; ++i) {
            posX[i] = sc.nextInt();
            posY[i] = sc.nextInt();
        }
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
            }
        }
        fileName = seed;
        Tester test = new Tester(seed);
    }

}
