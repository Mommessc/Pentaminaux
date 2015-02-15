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
	
	public SpriteShape(Plateau p, Shape shape) {
		super();
		this.p = p;
		this.shape = shape;
		this.shape.addObserver(this);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setLocation(shape.getColumn()*30, shape.getLine()*30);
		setSize(shape.getWidth()*30, shape.getHeight()*30);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(shape.getColor());
		for (int j=0; j<shape.getWidth(); j++) {
			for (int i=0; i<shape.getHeight(); i++) {
				if (shape.busyCase(i, j)) {
					g.fillRect(j*30+1, i*30+1, 28, 28);
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		lineClickedMouse = (getY() + ev.getY()) / 30;
		columnClickedMouse = (getX() + ev.getX()) / 30;
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent ev) {
		lineMouse = (getY() + ev.getY()) / 30;
		columnMouse = (getX() + ev.getX()) / 30;
		
		if (lineMouse != lineClickedMouse || columnMouse != columnClickedMouse) {
			p.removeShape(shape);
			shape.tranlsate(lineMouse-lineClickedMouse, columnMouse-columnClickedMouse);
			p.placeShape(shape);
			if (!p.checkShape(shape)) {
				p.removeShape(shape);
				shape.tranlsate(lineClickedMouse-lineMouse, columnClickedMouse-columnMouse);
				p.placeShape(shape);
			} else {
				lineClickedMouse = lineMouse;
				columnClickedMouse = columnMouse;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update(Observable o, Object object) {
		setLocation(shape.getColumn()*30, shape.getLine()*30);
	}
	
}
