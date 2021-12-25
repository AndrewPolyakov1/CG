package OGL;

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
import java.nio.FloatBuffer;
import java.text.ParseException;

public class Render implements GLEventListener {
    public static Input inputForm;
    private static GraphicsEnvironment graphicsEnviorment;
    private static boolean isFullScreen = false;
    public static DisplayMode dm, dm_old;
    private static Dimension xgraphic;
    private static Point point = new Point(0, 0);

    private final GLU glu = new GLU();
    //Change values
    public float delta = 3;
    public float r1 = 0.8f;
    public float r2 = 0.2f;
    public float half_h = 0.5f;
    public float illumination;

    private float xrot;
    private final int[] texture = new int[3];

    //Debug

    public int printed = 0;

    //End Debug




    //Lighting
    private final float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
    private final float[] lightDiffuse = {0.8f, 0.8f, 0.8f, 1.0f};
    private final float[] lightSpecular = {1.0f, 1.0f, 1.0f, 1.0f};
    private int filter;
    private boolean light;
    private final double pi = Math.PI;

    float move[] = {0f, 0f, 0f};
    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        /*
        * Change like on form
        * */
            try {
                delta = Float.parseFloat(inputForm.textField.getText());
                if (delta <= 0 || delta >= 180) {
                    delta = 120;
                }
                illumination = Float.parseFloat(inputForm.textField_2.getText());
                if (illumination <= 0 || illumination >= 128) {
                    illumination = 20;
                }
            }
            catch (NullPointerException e){

            }
            catch (NumberFormatException e2){

            }





        /*
         * Change like on form
         * */



        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);     // Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();                       // Reset The View
        gl.glTranslatef(0f, 0f, -4.0f);

        float[] lightPosition = {5f, 5f, 0f, 1f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition, 0);

        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[filter]);
        /*
        * Light
        */
        float black[] = {0.0f, 0.0f, 0.0f, 1.0f};
        float white[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float color[] = {0.6f, 0.6f, 0.6f, 1.0f};

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, FloatBuffer.wrap(color));
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, FloatBuffer.wrap(color));
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, FloatBuffer.wrap(white));

        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, illumination); // 0 - 128

        if (light)
            gl.glEnable(GL2.GL_LIGHTING);
        else
            gl.glDisable(GL2.GL_LIGHTING);

        gl.glTranslatef( move[0], move[1], move[2] );

        //Cone bottom
        for (double t = 0; t < 360; t += delta) {
            gl.glBegin(GL2.GL_TRIANGLES);

            gl.glNormal3d(0, 0, -1);
            gl.glTexCoord2f(0.5f, 0.5f);
            gl.glVertex3f(0.0f, 0.0f, -half_h);
            gl.glTexCoord2f((float) Math.sin(Math.toRadians(t)) / 2 + 0.5f, (float) (Math.cos(Math.toRadians(t)) / 2 + 0.5f));
            gl.glVertex3f((float) (Math.sin(Math.toRadians(t)) * r1), (float) (Math.cos(Math.toRadians(t)) * r1), -half_h);
            gl.glTexCoord2f((float) Math.sin(Math.toRadians(t + delta)) / 2 + 0.5f, (float) (Math.cos(Math.toRadians(t + delta)) / 2 + 0.5f));
            gl.glVertex3f((float) (Math.sin(Math.toRadians(t + delta)) * r1), (float) (Math.cos(Math.toRadians(t + delta)) * r1), -half_h);
            gl.glEnd();
        }

        //Cone top

        for (double t = 0; t < 360; t += delta) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glNormal3d(0, 0, -1);
            gl.glTexCoord2f(0.5f, 0.5f);
            gl.glVertex3f(0.0f, 0.0f, half_h);
            gl.glTexCoord2f((float) Math.sin(Math.toRadians(t)) / 2 + 0.5f, (float) (Math.cos(Math.toRadians(t)) / 2 + 0.5f));
            gl.glVertex3f((float) (Math.sin(Math.toRadians(t)) * r2), (float) (Math.cos(Math.toRadians(t)) * r2), half_h);
            gl.glTexCoord2f((float) Math.sin(Math.toRadians(t + delta)) / 2 + 0.5f, (float) (Math.cos(Math.toRadians(t + delta)) / 2 + 0.5f));
            gl.glVertex3f((float) (Math.sin(Math.toRadians(t + delta)) * r2), (float) (Math.cos(Math.toRadians(t + delta)) * r2), half_h);
            gl.glEnd();
        }


        //Sides
        gl.glBegin(GL2.GL_QUAD_STRIP);
        for (double t = 0; t < 360; t += delta) {

            float x1, x2, x3, x4;
            float y1, y2, y3, y4;
            x1 = (float) (Math.sin(Math.toRadians(t)) * r1);
            x2 = (float) (Math.sin(Math.toRadians(t)) * r2);
            x3 = (float) (Math.sin(Math.toRadians(t + delta)) * r1);
            x4 = (float) (Math.sin(Math.toRadians(t + delta)) * r2);

            y1 = (float) (Math.cos(Math.toRadians(t)) * r1);
            y2 = (float) (Math.cos(Math.toRadians(t)) * r2);
            y3 = (float) (Math.cos(Math.toRadians(t + delta)) * r1);
            y4 = (float) (Math.cos(Math.toRadians(t + delta)) * r2);


            //Calculating normals
            Vector tmp1 = new Vector(x3 - x1, y3 - y1, half_h * 2);
            Vector tmp2 = new Vector(x4 - x2, y4 - y2, half_h * 2);
            Vector res = tmp1.vectorMult(tmp2);
            float x = (float) res.x;
            float y = (float) res.y;
            float z = (float) res.z;
            gl.glNormal3d(-x, -y, -z);
            //Top first
            gl.glTexCoord2f(x1 / 2 + .5f, y1/ 2 + .5f);

            gl.glVertex3f(x1, y1, -half_h);
            //Bottom first
            gl.glTexCoord2f(x2/ 2 + .5f, y2/ 2 + .5f);

            gl.glVertex3f(x2, y2, half_h);
            //Bottom second
            gl.glTexCoord2f(x3/ 2 + .5f, y3/ 2 + .5f);

            gl.glVertex3f(x3, y3, -half_h);
            //Top second
            gl.glTexCoord2f(x4/ 2 + .5f, y4/ 2 + .5f);

            gl.glVertex3f(x4, y4, half_h);

            if (printed < 4){
                System.out.println(printed);
                System.out.println((t / 360));
                System.out.println(((t + delta) / 360));
                printed++;
            }

        }
        gl.glEnd();




        xrot += .1f;


    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void init(GLAutoDrawable drawable) {

        inputForm = new Input();

        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.2f, 0.2f, 0.2f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_NORMALIZE);

        gl.glEnable(GL2.GL_TEXTURE_2D);
        try {
            File im = new File("data/texture1.jpg");
            Texture t = TextureIO.newTexture(im, true);
            texture[0] = t.getTextureObject(gl);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0]);

            t = TextureIO.newTexture(im, true);
            texture[1] = t.getTextureObject(gl);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[1]);

            t = TextureIO.newTexture(im, true);
            texture[2] = t.getTextureObject(gl);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[2]);


        } catch (IOException e) {
            e.printStackTrace();
        }



        /**
         * Lighting
         */
        gl.glOrthof(-1.0f, 1.0f, -1.0f, 3.0f, -3.0f, 3.0f);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, this.lightAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, this.lightDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, this.lightSpecular, 0);
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 15.0f);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, 1);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHTING);

        this.light = true;

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        final GL2 gl = drawable.getGL().getGL2();

        if (height <= 0)
            height = 1;
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public static void main(String[] args) {

        inputForm.run();

        // setUp open GL version 2
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Render r = new Render();
        glcanvas.addGLEventListener(r);
        glcanvas.setSize(600, 600);

        final FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);

        final JFrame frame = new JFrame("Lab3-6");




        frame.getContentPane().add(glcanvas);

        //Shutdown
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (animator.isStarted())
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
        frame.setVisible(true);
        /*
         * Time to add Button Control
         */
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(0, 0));
        frame.add(p, BorderLayout.SOUTH);

        keyBindings(p, frame, r);
        animator.start();
    }

    private static void keyBindings(JPanel p, final JFrame frame, final Render r) {

        ActionMap actionMap = p.getActionMap();
        InputMap inputMap = p.getInputMap();
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "F1");
        actionMap.put("F1", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                fullScreen(frame);
            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        actionMap.put("UP", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.move[1] += 0.1f;
            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        actionMap.put("DOWN", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.move[1] -= 0.1f;
            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
        actionMap.put("LEFT", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.move[0] += 0.1f;
            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        actionMap.put("RIGHT", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.move[0] -= 0.1f;
            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "F");
        actionMap.put("F", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.filter++;
                if (r.filter > r.texture.length - 1)
                    r.filter = 0;

            }
        });
        /**
         *
         */
        /*
         *
         */
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0), "L");
        actionMap.put("L", new AbstractAction() {

            /**
             *
             */
            private static final long serialVersionUID = -6576101918414437189L;

            @Override
            public void actionPerformed(ActionEvent drawable) {
                // TODO Auto-generated method stub
                r.light = !r.light;
            }
        });
        /**
         *
         */

    }

    protected static void fullScreen(JFrame f) {
        if (!isFullScreen) {
            f.dispose();
            f.setUndecorated(true);
            f.setVisible(true);
            f.setResizable(false);
            xgraphic = f.getSize();
            point = f.getLocation();
            f.setLocation(0, 0);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            f.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            isFullScreen = true;
        } else {
            f.dispose();
            f.setUndecorated(false);
            f.setResizable(true);
            f.setLocation(point);
            f.setSize(xgraphic);
            f.setVisible(true);

            isFullScreen = false;
        }
    }


}