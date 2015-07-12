package testCodes;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;



/*
* ComboBoxDemo.java uses these additional files:
*   images/Bird.gif
*   images/Cat.gif
*   images/Dog.gif
*   images/Rabbit.gif
*   images/Pig.gif
*/
public class helloSwing_form extends JPanel
        implements ActionListener,test {
    JLabel picture;

    int imageIndex;
    String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
    ArrayList<test> listeners = new ArrayList<test>();
    public void setImageIndex(int in){
        imageIndex = in;
    }

    public void addListener(test t){
        listeners.add(t);
    }

    public helloSwing_form() {
        super(new BorderLayout());

        imageIndex = 0;

        //Create the combo box, select the item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        JComboBox petList = new JComboBox(petStrings);
        petList.setSelectedIndex(4);
        petList.addActionListener(this);
        addListener(this);
        //Set up the picture.
        picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.LEFT);
        updateLabel(petStrings[petList.getSelectedIndex()]);
        picture.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //The preferred size is hard-coded to be the width of the
        //widest image and the height of the tallest image + the border.
        //A real program would compute this.
        picture.setPreferredSize(new Dimension(100, 100));

        //Lay out the demo.
        add(petList, BorderLayout.PAGE_START);
        add(picture, BorderLayout.PAGE_END);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

//        System.out.println("Fuck");
    }

    /** Listens to the combo box. */
//    public void actionPerformed(ActionEvent e) {
//        JComboBox cb = (JComboBox)e.getSource();
//        String petName = (String)cb.getSelectedItem();
//        updateLabel(petName);
//    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        imageIndex = cb.getSelectedIndex();
        //String petName = (String)cb.getSelectedItem();
        //updateLabel(petName);
        for(test t : listeners)
        {
            t.change();
        }
    }

    protected void updateLabel(String name) {
        ImageIcon icon = createImageIcon("images/" + name + ".gif");//"D:\\Scala\\My_Projects\\SwingHello\\images\\" +
        picture.setIcon(icon);
        picture.setToolTipText("A drawing of a " + name.toLowerCase());
        if (icon != null) {
            picture.setText(null);
        } else {
            picture.setText("Image not found");
        }
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = helloSwing_form.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ComboBoxDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new helloSwing_form();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });

    }

//    public void setUpGUI(){
//        //Schedule a job for the event-dispatching thread:
//        //creating and showing this application's GUI.
////        javax.swing.SwingUtilities.invokeLater(new Runnable() {
////            public void run() {
////                createAndShowGUI();
////            }
////        });
//
//        createAndShowGUI();
//        System.out.println("FUCK!");
//    }

    @Override
    public void change() {
        String petName = petStrings[imageIndex];
        updateLabel(petName);
    }
}




//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
///**
// * Created by Mirzaa on 6/21/2015.
// */
//public class testCodes.helloSwing_form extends JFrame {
//    private JButton clickMeButton;
//    private JPanel rootPanel;
//    private JLabel testLabel;
//
//    public testCodes.helloSwing_form() {
//        super("Hello Swing!");
//
//
//    }
//
//    private void createUIComponents() {
//        // TODO: place custom component creation code here
//        setContentPane(rootPanel);
//
//        pack();
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        clickMeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showConfirmDialog(testCodes.helloSwing_form.this,"You clicked Me!");
//            }
//        });
//
//        ImageIcon a = new ImageIcon("D:\\Scala\\My_Projects\\SwingHello\\leaseydoux.png");
//        testLabel.setIcon(a);
//
//        setVisible(true);
//
//    }
//}

