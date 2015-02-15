package data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Observable;

public class Shape extends Observable {
	
	Point point;
	Dimension dimension;
	private int[][] array;
	private Color color;
	
	/** Constructeur */
	public Shape(Point point, int[][] array, Color color) {
		this.point = point;
		this.dimension = new Dimension(array[0].length, array.length);
		this.array = array;
		this.color = color;
	}
	
	/** Constructeur */
	public Shape(int line, int column, int[][] array, Color color) {
		this(new Point(line, column), array, color);
	}
	
	public final Point getPoint() {
		return point;
	}
	
	public int getLine() {
		return (int) point.getX();
	}
	
	public int getColumn() {
		return (int) point.getY();
	}
	
	public int getWidth() {
		return (int) dimension.getWidth();
	}
	
	public int getHeight() {
		return (int) dimension.getHeight();
	}
	
	public final Color getColor() {
		return color;
	}
	
	public boolean busyCase(int i, int j) {
		return array[i][j] != 0;
	}
	
	public void setLocation(int i, int j) {
		point.setLocation(i, j);
		setChanged();
		notifyObservers();
	}
	
	public void setLocation(Point point) {
		setLocation((int) point.getX(), (int) point.getY());
	}
	
	public void tranlsate(int ti, int tj) {
		point.translate(ti, tj);
		setChanged();
		notifyObservers();
	}
	
	public String toString() {
		String s = "";
		for (int j=0; j<array.length; j++) {
			for (int i=0; i<array[j].length; i++) {
				s += array[j][i] + " ";
			}
			s += "\n";
		}
		return s;
	}
}
