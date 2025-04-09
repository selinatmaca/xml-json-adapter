import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("App");
        frame.setContentPane(new App().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1280,720);
        frame.setVisible(true);
    }
}
