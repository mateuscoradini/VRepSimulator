package br.com.coradini.robot.execute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import br.com.coradini.robot.comport.FuzzyWallFollower;
import br.com.coradini.robot.comport.PositionLocator;
import br.com.coradini.robot.handlers.RobotHandler;
import br.com.coradini.robot.handlers.VREPHandler;
import br.com.coradini.robot.monitor.RobotComportMonitor;
import br.com.coradini.robot.panel.GraphPanel;
import br.com.coradini.robot.panel.LinesComponent;
import br.com.coradini.robot.pojo.Position;

public class Execute {

	private static VREPHandler vrepHandlerSimulator;

	public static void main(String[] args) throws IOException {

		vrepHandlerSimulator = new VREPHandler();
		vrepHandlerSimulator.connect();

		RobotHandler robot = new RobotHandler(vrepHandlerSimulator);

		// Avoid-obstacle

		// Wall-Follower
		FuzzyWallFollower fuzzyWallFollower = new FuzzyWallFollower();


		PositionLocator locator = new PositionLocator(robot);
		locator.initializePositionGroundTruth();
		
		RobotComportMonitor monitor = new RobotComportMonitor(fuzzyWallFollower, robot, locator);
		monitor.run();
	}

	public void createGUI() {
		GraphPanel mainPanel = new GraphPanel(null);
		mainPanel.setPreferredSize(new Dimension(800, 600));
		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void drawLocation(Position position) {

		Runnable myRunnable = new Runnable() {

			public void run() {
				System.out.println("Runnable running");
			}
		};

		Thread thread = new Thread(myRunnable);
		thread.start();

	}

}
