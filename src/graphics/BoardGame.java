package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

import data.Plateau;
import data.Shape;

public class BoardGame extends JComponent {
	
	private static final long serialVersionUID = 1L;
	private int boardWidth, boardHeight;
	
	/** Constructeur */
	public BoardGame(int width, int height) {
		super();
		this.boardWidth = width - 8;
		this.boardHeight = height - 8;
		
		setLayout(null);
		setPreferredSize(new Dimension(width*30, height*30));
	}
	
	/** Ajoute une shape au plateau graphique */
	public void addShape(Plateau p, Shape shape) {
		SpriteShape sp = new SpriteShape(p, shape);
		shape.addObserver(sp);
		add(sp);
		repaint();
	}
	
	/** Dessine le plateau */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.darkGray);
		g.fillRect(120, 120, boardWidth*30, boardHeight*30);
	}
}
