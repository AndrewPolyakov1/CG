package test;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import java.lang.Math;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author Beta
 *
 */
public class Render implements GLEventListener{

    private static GraphicsEnvironment graphicsEnviorment;
    private static boolean isFullScreen = false;
    public static DisplayMode dm, dm_old;
    private static Dimension xgraphic;
    private static Point point = new Point(0, 0);

    private GLU glu = new GLU();

    @Override
    public void display(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);     // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();                       // Reset The View

        double delta = 60;
        float r1 = 0.5f;
        float r2 = 0.5f;
        gl.glTranslatef(0f,0.0f,-6.0f);                 // Move Left 1.5 Units And Into The Screen 6.0

        for (double t = 0; t < 360; t += delta) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);              // Top
            gl.glVertex3f((float)(Math.sin(Math.toRadians(t)) * r1), (float)(Math.cos(Math.toRadians(t)) * r1), 0.0f);              // Bottom Left
            gl.glVertex3f((float)(Math.sin(Math.toRadians(t + delta)) * r1), (float)(Math.cos(Math.toRadians(t+ delta)) * r1), 0.0f);
            gl.glEnd();
        }

        gl.glFlush();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();

        if(height <=0)
            height =1;
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // setUp open GL version 2
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Render r = new Render();
        glcanvas.addGLEventListener(r);
        glcanvas.setSize(400, 400);

        final FPSAnimator animator = new FPSAnimator(glcanvas, 300,true );

        final JFrame frame = new JFrame ("nehe: Lesson 2");

        frame.getContentPane().add(glcanvas);

        //Shutdown
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                if(animator.isStarted())
                    animator.stop();
                System.exit(0);
            }
        });

        frame.setSize(frame.getContentPane().getPreferredSize());
        /**
         * Centers the screen on start up
         *
         */
        graphicsEnviorment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice[] devices = graphicsEnviorment.getScreenDevices();

        dm_old = devices[0].getDisplayMode();
        dm = dm_old;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowX = Math.max(0, (screenSize.width - frame.getWidth()) / 2);
        int windowY = Math.max(0, (screenSize.height - frame.getHeight()) / 2);

        frame.setLocation(windowX, windowY);
        /**
         *
         */
        frame.setVisible(true);
        /*
         * Time to add Button Control
         */
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(0,0));
        frame.add(p, BorderLayout.SOUTH);

        keyBindings(p, frame, r);
        animator.start();
    }

    private static void keyBindings(JPanel p, final JFrame frame, final Render r) {

        ActionMap actionMap = p.getActionMap();
        InputMap inputMap = p.getInputMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");
        actionMap.put("F1", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                fullScreen(frame);
            }});
    }

    protected static void fullScreen(JFrame f) {
        // TODO Auto-generated method stub
        if(!isFullScreen){
            f.dispose();
            f.setUndecorated(true);
            f.setVisible(true);
            f.setResizable(false);
            xgraphic = f.getSize();
            point = f.getLocation();
            f.setLocation(0, 0);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            f.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            isFullScreen=true;
        }else{
            f.dispose();
            f.setUndecorated(false);
            f.setResizable(true);
            f.setLocation(point);
            f.setSize(xgraphic);
            f.setVisible(true);

            isFullScreen =false;
        }
    }


}