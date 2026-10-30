import java.awt.*;
import javax.swing.*;

public class Main extends JFrame{

	
	public Main () {
		super("Runebound");
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		Game play = new Game();
		((Component) play).setFocusable(true);
		
		Color RoyalBlue = new Color(7,0,51);
		

		setBackground(RoyalBlue);
		
		
		getContentPane().add(play);
		
		setVisible(true);
		play.requestFocusInWindow();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	

	public static void main(String[] args) {
		Main run = new Main();
		

	}


}
