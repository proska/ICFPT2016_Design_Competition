package testCodes;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;

import javax.swing.*;

/**
 * Created by proska on 7/30/15.
 */
public class doubleBufTest {

    static JFrame window = new JFrame();

    public static void main(String[] a) {

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(30, 30, 300, 300);

        window.setVisible(true);

        window.getContentPane().add(new MyTile(0, 0));

        window.setIgnoreRepaint(true);
        window.createBufferStrategy(2);


        for(int i = 0; i < 1 ;i++){
            draw();
        }

    }


    static int x = 10;
    static int y = 10;

    public static  void draw (){
        BufferStrategy strategy = window.getBufferStrategy();

        window.removeAll();

        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

//        window.getContentPane().add(new MyTile(x, y));

        Image img1 = Toolkit.getDefaultToolkit().getImage("/home/proska/ICFPT2016_Design_Competition/UTTrax/src/main/scala/testCodes/Rabbit.gif");
//        Image img2 = img1.getScaledInstance(100,100,Image.SCALE_DEFAULT);
//        g.drawImage(img1, x, y,);

        x += 10;
        y += 10;
        System.out.println("("+x + "," +y+ ")");

    }

}

class MyTile extends JComponent {

    int x,y;

    public MyTile(int x ,int y) {
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        ///home/proska/ICFPT2016_Design_Competition/UTTrax/src/main/scala/testCodes/
        Image img1 = Toolkit.getDefaultToolkit().getImage("/home/proska/ICFPT2016_Design_Competition/UTTrax/src/main/scala/testCodes/Rabbit.gif");
//        Image img2 = img1.getScaledInstance(100,100,Image.SCALE_DEFAULT);
        g2.drawImage(img1, x, y, this);
        g2.finalize();
    }
}