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
	
	private Motion motion;
	
	/** Constructeur */
	public SpriteShape(Plateau p, Shape shape) {
		super();
		this.p = p;
		this.shape = shape;
		
		this.motion = new Motion();
		motion.start();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		setLocation(shape.getColumn()*30, shape.getLine()*30);
		setSize(shape.getWidth()*30, shape.getHeight()*30);
	}
	
	@Override
	public void update(Observable o, Object object) {
		if (!p.isLocked()) {
			motion.moveTo(shape.getLine(), shape.getColumn());
			if (motion.isWaiting()) {
				synchronized(motion) {
					motion.notify();
				}
			}
		} else {
			setLocation(shape.getColumn()*30, shape.getLine()*30);
		}
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
	
	/** Thread prive */
	private class Motion extends Thread {
		
		private int line, column, realLine, realColumn;
		private boolean wait;
		
		/** Constructeur */
		public Motion() {
			this.line = shape.getLine();
			this.column = shape.getColumn();
			this.realLine = shape.getLine() * 30;
			this.realColumn = shape.getColumn() * 30;
			this.wait = true;
		}
		
		public boolean isWaiting() {
			return wait;
		}
		
		public void moveTo(int line, int column) {
			this.line = line;
			this.column = column;
		}
		
		private boolean isMoving() {
			return ((line * 30 != realLine) || (column * 30 != realColumn));
		}
		
		@Override
		public void run() {
			
			synchronized(this) {
				while (true) {
					
					try {
						wait = true;
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					wait = false;
					
					while (isMoving()) {
						
						if (line * 30 < realLine) {
							realLine--;
						} else if (line * 30 > realLine) {
							realLine++;
						}
						
						if (column * 30 < realColumn) {
							realColumn--;
						} else if (column * 30 > realColumn) {
							realColumn++;
						}
						
						setLocation(realColumn, realLine);
						
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
}
