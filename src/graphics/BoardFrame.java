package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import data.Plateau;
import data.Shape;

public class BoardFrame extends JComponent implements Observer {
	
	private static final long serialVersionUID = 1L;
	private int boardWidth, boardHeight;
	
	public BoardFrame(int pWidth, int pHeight) {
		super();
		this.boardWidth = pWidth - 8;
		this.boardHeight = pHeight - 8;
		
		setLayout(null);
		setPreferredSize(new Dimension(pWidth*30, pHeight*30));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.darkGray);
		g.fillRect(120, 120, boardWidth*30, boardHeight*30);
	}
	
	@Override
	public void update(Observable observable, Object object) {
		Plateau p = (Plateau) observable;
		Shape shape = (Shape) object;
		add(new SpriteShape(p, shape));
		repaint();
	}
	
}
