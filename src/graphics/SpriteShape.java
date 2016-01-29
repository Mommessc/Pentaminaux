package graphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import data.Plateau;
import data.Shape;

public class SpriteShape extends JComponent implements MouseListener, MouseMotionListener, Observer {
	
	private static final long serialVersionUID = 1L;
	
	private Plateau p;
	private Shape shape;
	private int lineMouse, columnMouse;
	private int lineClickedMouse, columnClickedMouse;
	
	
	/** Constructeur */
	public SpriteShape(Plateau p, Shape shape) {
		super();
		this.p = p;
		this.shape = shape;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setLocation(shape.getColumn()*30, shape.getLine()*30);
		setSize(shape.getWidth()*30, shape.getHeight()*30);
	}
	
	@Override
	public void update(Observable o, Object object) {
		setLocation(shape.getColumn()*30, shape.getLine()*30);
	}
	
	
	/** Dessine la shape */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(shape.getColor());
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j)) {
					g.fillRect(j*30+1, i*30+1, 28, 28);
				}
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent ev) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent ev) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent ev) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent ev) {
		lineClickedMouse = (getY() + ev.getY()) / 30;
		columnClickedMouse = (getX() + ev.getX()) / 30;
	}
	
	@Override
	public void mouseReleased(MouseEvent ev) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent ev) {
		if (p.isLocked()) {
			return;
		}
		
		lineMouse = (getY() + ev.getY()) / 30;
		columnMouse = (getX() + ev.getX()) / 30;
		
		if (lineMouse != lineClickedMouse || columnMouse != columnClickedMouse) {
			p.popShape(shape);
			int i = shape.getLine() + (lineMouse - lineClickedMouse);
			int j = shape.getColumn() + (columnMouse - columnClickedMouse);
			if (p.isValidLocation(shape, i, j)) {
				shape.setLocation(i, j);
				lineClickedMouse = lineMouse;
				columnClickedMouse = columnMouse;
			}
			p.putShape(shape);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent ev) {
		
	}
	
	
	
}
