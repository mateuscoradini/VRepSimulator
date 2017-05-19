package br.com.coradini.robot.panel;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Canvas {

	JLabel view;
	BufferedImage surface;
	Random random = new Random();

	public Canvas(final int xActual, final int yActual, final int xPrevious,final int yPrevious) {
		surface = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		view = new JLabel(new ImageIcon(surface));
		Graphics g = surface.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 600, 400);
		g.setColor(Color.BLACK);		
		g.drawLine(10, 20, 10, 30);
		
		g.dispose();

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				addNewElement(xActual, yActual, xPrevious, yPrevious);
			}
		};
		Timer timer = new Timer(200, listener);
		timer.start();
	}

	public void addNewElement(int xActual, int yActual, int xPrevious,int yPrevious) {
		//boolean drawArc = random.nextBoolean();	
		Graphics g = surface.getGraphics();
		g.drawLine(xActual, yActual, xPrevious, yPrevious);
		g.setColor(Color.BLUE);
		g.dispose();
		view.repaint();
	}

	public static void main(String[] args) {
		Canvas canvas = new Canvas(1,10, 1, 35);
		JFrame frame = new JFrame();
		int vertexes = 0;
		// Change this next part later to be dynamic.
		vertexes = 10;
		int canvasSize = vertexes * vertexes;
		frame.setSize(canvasSize, canvasSize);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(canvas.view);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	

}
