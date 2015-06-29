package testCodes;

import java.awt.*;

/*
 * AbsoluteLayoutDemo.java requires no other files.
 */

        import java.awt.Container;
        import java.awt.Insets;
        import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

public class AbsoluteLayoutDemo {
    public static void addComponentsToPane(Container pane , JFrame fram) {
        pane.setLayout(null);


        JButton b1 = new JButton("one");
        JButton b2 = new JButton("two");
        JButton b3 = new JButton("three");

        pane.add(b1);
        pane.add(b2);
        pane.add(b3);

        Insets insets = pane.getInsets();
        Dimension size = b1.getPreferredSize();
        b1.setBounds(fram.getWidth()/2,fram.getHeight()/2,
                size.width, size.height);
        size = b2.getPreferredSize();
        b2.setBounds(55 + insets.left, 40 + insets.top,
                size.width, size.height);
        size = b3.getPreferredSize();
        b3.setBounds(150 + insets.left, 15 + insets.top,
                size.width + 50, size.height + 20);

        pane.setPreferredSize(new Dimension(100, 100));

    }

    // pane.setPref Size must be updated
    //


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("AbsoluteLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel();
        addComponentsToPane(panel,frame);

        JScrollPane scrol = new JScrollPane(panel);
//        scrol.setHorizontalScrollBar();
        //Set up the content pane.
        //frame.getContentPane()

        //Size and display the window.
        Insets insets = frame.getInsets();
        frame.setSize(300 + insets.left + insets.right,
                125 + insets.top + insets.bottom);
        frame.getContentPane().add(scrol,BorderLayout.CENTER);


        JButton b1 = new JButton("Four");
        panel.add(b1);
        b1.setBounds(10,10,100,100);

        frame.add(panel);

        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                panel.removeAll();
                addComponentsToPane(panel,frame);
                System.out.println("componentResized");
            }
        });

    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
