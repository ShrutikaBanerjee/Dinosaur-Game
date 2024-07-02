import java.awt.Color;

import javax.swing.*;
public class App {
	

	public static void main(String[] args) {
		int boardHeight=200;
		int boardWidth = 1200;
		
	JFrame frame = new JFrame();
	frame.setSize(boardWidth, boardHeight);
	frame.setLocation(50,50);
	frame.setBackground(Color.white);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	Dinosaur chromeDinosaur = new Dinosaur();
	frame.add(chromeDinosaur);
	frame.pack();
	chromeDinosaur.requestFocus();
	frame.setVisible(true);
	
	
	
	}

}
