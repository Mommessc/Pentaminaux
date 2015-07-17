package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import data.Plateau;
import data.Shape;

public class BoardGame extends JComponent implements Observer {
	
	private static final long serialVersionUID = 1L;
	private int boardWidth, boardHeight;
	
	/** Constructeur */
	public BoardGame(Plateau p) {
		super();
		this.boardWidth = p.getWidth() - 8;
		this.boardHeight = p.getHeight() - 8;
		
		p.addObserver(this);
		
		for (Shape shape : p.getListShape()) {
			addShape(p, shape);
		}
		
		setLayout(null);
		setPreferredSize(new Dimension(p.getWidth()*30, p.getHeight()*30));
	}
	
	/** Ajoute une shape sur le board */
	public void addShape(Plateau p, Shape shape) {
		SpriteShape sp = new SpriteShape(p, shape);
		shape.addObserver(sp);
		add(sp);
		repaint();
	}
	
	@Override
	public void update(Observable obs, Object object) {
		Plateau p = (Plateau) obs; 
		Shape shape = (Shape) object;
		addShape(p, shape);
	}
	
	/** Dessine le plateau */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.darkGray);
		g.fillRect(120, 120, boardWidth*30, boardHeight*30);
	}
}
