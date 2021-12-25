package OGL;

import java.awt.EventQueue;

import javax.swing.*;

public class Input {

    public static JFrame frame;
    public static JTextField textField, textField_2;
    public static JLabel lblName, lbl2;
    public static JButton ok;


    /**
     * Launch the application.
     */
    public static void run() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Input window = new Input();

                    textField = new JTextField();
                    textField.setBounds(130, 30, 90, 20);
                    frame.getContentPane().add(textField);
                    textField.setColumns(10);

                    lblName = new JLabel("Angle delta");
                    lblName.setBounds(20, 30, 100, 14);
                    frame.getContentPane().add(lblName);

                    lbl2 = new JLabel("Lighting");
                    lbl2.setBounds(20, 60, 100, 14);
                    frame.getContentPane().add(lbl2);

//                    ok = new JButton();
//                    ok.setText("OK");
//                    ok.setBounds(20, 90, 60, 30);
//                    frame.getContentPane().add(ok);

                    textField_2 = new JTextField();
                    textField_2.setBounds(130, 60, 90, 20);
                    frame.getContentPane().add(textField_2);
                    textField_2.setColumns(10);

                    textField.setText("1");
                    textField_2.setText("20");


                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Input() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

    }

}