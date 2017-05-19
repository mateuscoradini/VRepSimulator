package br.com.coradini.robot.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LinesComponent extends JComponent {

	public static class Line {
		final float x1;
		final float y1;
		final float x2;
		final float y2;
		final Color color;

		public Line(float x1, float y1, float x2, float y2, Color color) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.color = color;
		}
	}

	private final LinkedList<Line> lines = new LinkedList<Line>();

	public void addLine(float x1, float x2, float x3, float x4) {
		addLine(x1, x2, x3, x4, Color.black);
	}

	public void addLine(float x1, float x2, float x3, float x4, Color color) {
		lines.add(new Line(x1, x2, x3, x4, color));
		repaint();
	}

	public void clearLines() {
		lines.clear();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.draw(new Line2D.Double(1.000,1.22,78.98098,3.098));
		
		for (Line line : lines) {
			
			//g.drawLine();
			g2.setColor(Color.RED);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.draw(new Line2D.Double(line.x1, line.y1, line.x2, line.y2));
		}
		super.paintComponent(g2);
	}

	public static void init() {
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final LinesComponent comp = new LinesComponent();
		comp.setPreferredSize(new Dimension(320, 200));
		testFrame.getContentPane().add(comp, BorderLayout.CENTER);
		JPanel buttonsPanel = new JPanel();
		JButton newLineButton = new JButton("New Line");
		JButton clearButton = new JButton("Clear");
		buttonsPanel.add(newLineButton);
		buttonsPanel.add(clearButton);
		testFrame.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comp.clearLines();
			}
		});
		testFrame.pack();
		testFrame.setVisible(true);
	}
	

}