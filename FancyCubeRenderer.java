import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FancyCubeRenderer extends JPanel implements ActionListener {
    private static final int SIZE = 400;
    private static final int HALF_SIZE = SIZE / 2;
    private static final int DEPTH = 150;
    private static final int[][] CUBE_VERTICES = {
            {-1, -1, -1}, {1, -1, -1}, {1, 1, -1}, {-1, 1, -1}, // Back face
            {-1, -1, 1}, {1, -1, 1}, {1, 1, 1}, {-1, 1, 1}  // Front face
    };
    private static final int[][] CUBE_EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, // Back edges
            {4, 5}, {5, 6}, {6, 7}, {7, 4}, // Front edges
            {0, 4}, {1, 5}, {2, 6}, {3, 7}  // Connecting edges
    };
    private double angleX = 0, angleY = 0;
    private final Timer timer;
    private int bgOffset = 0; // Background animation

    public FancyCubeRenderer() {
        timer = new Timer(16, this);
        timer.start();
    }

    private Point3D rotate(Point3D p, double angleX, double angleY) {
        double cosX = Math.cos(angleX), sinX = Math.sin(angleX);
        double cosY = Math.cos(angleY), sinY = Math.sin(angleY);

        double x = p.x * cosY - p.z * sinY;
        double z = p.x * sinY + p.z * cosY;
        double y = p.y * cosX - z * sinX;
        z = p.y * sinX + z * cosX;
        return new Point3D(x, y, z);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Smooth graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fancy animated background
        drawBackground(g2d);
        
        // Draw cube
        g2d.translate(getWidth() / 2, getHeight() / 2);

        Point3D[] projected = new Point3D[CUBE_VERTICES.length];
        for (int i = 0; i < CUBE_VERTICES.length; i++) {
            projected[i] = rotate(new Point3D(CUBE_VERTICES[i][0] * HALF_SIZE, 
                                              CUBE_VERTICES[i][1] * HALF_SIZE, 
                                              CUBE_VERTICES[i][2] * DEPTH), angleX, angleY);
        }

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.WHITE);

        // Draw cube edges with transparency effect
        for (int[] edge : CUBE_EDGES) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2d.drawLine((int) projected[edge[0]].x, (int) projected[edge[0]].y, 
                         (int) projected[edge[1]].x, (int) projected[edge[1]].y);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        int width = getWidth(), height = getHeight();
        Color color1 = new Color((bgOffset % 255), 50, 150);
        Color color2 = new Color(50, (bgOffset % 255), 150);
        GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2, true);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angleX += 0.02;
        angleY += 0.015;
        bgOffset += 2;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fancy 3D Cube Renderer");
        FancyCubeRenderer renderer = new FancyCubeRenderer();

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(renderer);
        frame.setVisible(true);
    }

    static class Point3D {
        double x, y, z;
        Point3D(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }
    }
}
