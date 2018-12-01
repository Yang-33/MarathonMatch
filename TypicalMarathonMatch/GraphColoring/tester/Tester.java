import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.security.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.HashMap;

public class Tester {

    /********************************************************************/

    JFrame jf;
    Visualizer v;
    InputStream is;
    OutputStream os;
    BufferedReader br;

    static Process proc;
    static String fileName, exec;
    static boolean vis;
    static int sel_ver;
    static boolean press;

    final int MAXN = 200, MINN = 20;
    int VIS_SIZE = 1000;
    int N,M;
    int Score;
    int a[],b[],col[];
    int posX[],posY[];
    int numX[],numY[];
    Map<Integer, Integer> map;

    /********************************************************************/

    public class Visualizer extends JPanel implements MouseListener, WindowListener {
        
        public void paint(Graphics g) {
            try {
                BufferedImage bi = new BufferedImage(VIS_SIZE + 100, VIS_SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = (Graphics2D)bi.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xD3D3D3));
                g2.fillRect(0, 0, VIS_SIZE + 100, VIS_SIZE);
                g2.setColor(new Color(0xFFFFFF));
                g2.fillRect(10, 10, VIS_SIZE - 20, VIS_SIZE - 20);
                for (int i = 0; i < M; i++) {
                    if (a[i] == sel_ver) continue;
                    if (b[i] == sel_ver) continue;
                    if (sel_ver < 0) continue;
                    if (col[a[i]] == col[b[i]]) continue;
                    g2.setColor(new Color(0xD3D3D3));
                    g2.drawLine(posX[a[i]], posY[a[i]], posX[b[i]], posY[b[i]]);
                }
                for (int i = 0; i < M; i++) {
                    if (col[a[i]] == col[b[i]] && sel_ver < 0) {
                        g2.setColor(new Color(0xFF0000));
                        g2.drawLine(posX[a[i]], posY[a[i]], posX[b[i]], posY[b[i]]);
                    } else if (col[a[i]] == col[b[i]] && (sel_ver == a[i] || sel_ver == b[i])) {
                        g2.setColor(new Color(0xFF0000));
                        g2.drawLine(posX[a[i]], posY[a[i]], posX[b[i]], posY[b[i]]);
                    } else if (a[i] == sel_ver || b[i] == sel_ver || sel_ver < 0) {
                        g2.setColor(new Color(0x000000));
                        g2.drawLine(posX[a[i]], posY[a[i]], posX[b[i]], posY[b[i]]);
                    }
                }
                for (int i = 0; i < N; i++) {
                    Color c = Color.getHSBColor((float)map.get(col[i]) / (float)map.size(), 1.0f, 0.95f);
                    g2.setColor(c);
                    g2.fillOval(posX[i] - 6, posY[i] - 6, 12, 12);
                    g2.setColor(new Color(0x000000));
                    g2.drawOval(posX[i] - 6, posY[i] - 6, 12, 12);
                }
                if (sel_ver >= 0) {
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    char[] ch = ("" + sel_ver).toCharArray();
                    g2.drawChars(ch, 0, ch.length, numX[sel_ver], numY[sel_ver]);
                    for (int i = 0; i < M; i++) {
                        if (a[i] == sel_ver) {
                            char[] cht = ("" + b[i]).toCharArray();
                            g2.drawChars(cht, 0, cht.length, numX[b[i]], numY[b[i]]);
                        } else if (b[i] == sel_ver) {
                            char[] cht = ("" + a[i]).toCharArray();
                            g2.drawChars(cht, 0, cht.length, numX[a[i]], numY[a[i]]);
                        }
                    }
                }
                g2.setFont(new Font("Arial", Font.PLAIN, 16));
                FontMetrics fm = g2.getFontMetrics();
                char[] chs1 = ("     Score").toCharArray();
                char[] chs2 = ("" + Score).toCharArray();
                char[] chn = ("   N = " + N).toCharArray();
                char[] chm = ("   M = " + M).toCharArray();
                g2.drawChars(chs1, 0, chs1.length, VIS_SIZE, 100);
                g2.drawChars(chs2, 0, chs2.length, VIS_SIZE + 50 - chs2.length * 8, 120);
                g2.drawChars(chn, 0, chn.length, VIS_SIZE, 160);
                g2.drawChars(chm, 0, chm.length, VIS_SIZE, 185);
                if (press) {
                    g2.setColor(new Color(0xA6A6A6));
                    g2.fillRect(VIS_SIZE + 5, 230, 80, 25);
                    g2.setColor(new Color(0x000000));
                    g2.drawRect(VIS_SIZE + 5, 230, 80, 25);
                    char[]chsave = ("save").toCharArray();
                    g2.drawChars(chsave, 0, chsave.length, VIS_SIZE + 28, 248);
                    ImageIO.write(bi, "png", new File(fileName +".png"));
                } else {
                    g2.setColor(new Color(0xD4D4D4));
                    g2.fillRect(VIS_SIZE + 5, 230, 80, 25);
                    g2.setColor(new Color(0x000000));
                    g2.drawRect(VIS_SIZE + 5, 230, 80, 25);
                    char[]chsave = ("save").toCharArray();
                    g2.drawChars(chsave, 0, chsave.length, VIS_SIZE + 28, 248);
                }
                g.drawImage(bi, 0, 0, VIS_SIZE + 100, VIS_SIZE, null);
            } catch (Exception e) { 
                e.printStackTrace();
            }
        }

        public Visualizer () {
            addMouseListener(this);
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

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            for (int i = 0; i < N; i++) {
                int dX = posX[i] - x;
                int dY = posY[i] - y;
                if (dX * dX + dY * dY <= 6 * 6) {
                    sel_ver = i;
                    repaint();
                    return;
                }
            }
            if (x <= VIS_SIZE + 85 && x >= VIS_SIZE + 5) {
                if (y <= 250 && y >= 230) { 
                    if (!press) {
                        press = true;
                        repaint();
                        return;
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            press = false;
            repaint();
        }

        public void mouseClicked(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }

    }

    /********************************************************************/

    public void generate (String seedStr) {
        try {
            SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
            long seed = Long.parseLong(seedStr);
            rnd.setSeed(seed);
            N = rnd.nextInt(MAXN - MINN + 1) + MINN;
            M = rnd.nextInt(N * (N - 1) / 5 - 2 * N) + 2 * N;
            a = new int[M];
            b = new int[M];
            boolean used[][] = new boolean[N][N];
            int esum = 0;
            while (esum < M) {
                int at = rnd.nextInt(N);
                int bt = rnd.nextInt(N);
                if (at == bt || used[at][bt]) continue;
                used[at][bt] = true;
                used[bt][at] = true;
                a[esum] = at;
                b[esum] = bt;
                esum++;
            }
            posX = new int[N];
            posY = new int[N];
            numX = new int[N];
            numY = new int[N];
            for (int i = 0; i < N; i++) {
                double theta = 2.0 * Math.PI * (double)i / (double)N;
                double xt = Math.cos(theta) * (double)VIS_SIZE * 0.45 + (double)VIS_SIZE / 2;
                double yt = Math.sin(theta) * (double)VIS_SIZE * 0.45 + (double)VIS_SIZE / 2;
                posX[i] = (int)xt;
                posY[i] = (int)yt;
                xt = Math.cos(theta) * (double)VIS_SIZE * 0.47 + (double)VIS_SIZE / 2 - 6;
                yt = Math.sin(theta) * (double)VIS_SIZE * 0.47 + (double)VIS_SIZE / 2 + 6;
                numX[i] = (int)xt;
                numY[i] = (int)yt;
            }
            sel_ver = -1;
        } catch (Exception e) {
            System.err.println("An exception occurred while generating test case.");
            e.printStackTrace();
        }
    }

    public double runTest (String seed) {

        try {
            generate(seed);
            if (proc != null) try {
                col = getColor();
                map = new HashMap<Integer, Integer>();
                for (int i = 0; i < N; i++) {
                    if (!map.containsKey(col[i])) {
                        map.put(col[i], (int)map.size());
                    }
                }
                Score = (int)map.size();
                for (int i = 0; i < M; i++) {
                    if (col[a[i]] == col[b[i]]) {
                        Score = -1;
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to get result from output.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        if (vis) {
            jf.setSize(VIS_SIZE + 100, VIS_SIZE);
            jf.setVisible(true);
        }
        return (double)Score;
    }

    private int [] getColor () throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(N).append(' ');
        sb.append(M).append('\n');
        for (int i = 0; i < M; ++i) {
            sb.append(a[i]).append(' ');
            sb.append(b[i]).append('\n');
        }
        os.write(sb.toString().getBytes());
        os.flush();

        int [] ret = new int[N];
        for (int i = 0; i < N; ++i) {
            ret[i] = Integer.parseInt(br.readLine());
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
                br = new BufferedReader(new InputStreamReader(is));
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
            }
        }
        fileName = seed;
        Tester test = new Tester(seed);
    }

}
