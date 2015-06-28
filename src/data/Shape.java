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
	
	/** Retourne la position */
	public final Point getPoint() {
		return point;
	}
	
	/** Retourne la position X */
	public int getLine() {
		return (int) point.getX();
	}
	
	/** Retourne la position Y */
	public int getColumn() {
		return (int) point.getY();
	}
	
	/** Retourne la largeur */
	public int getWidth() {
		return (int) dimension.getWidth();
	}
	
	/** Retourne la hauteur */
	public int getHeight() {
		return (int) dimension.getHeight();
	}
	
	/** Retourne la couleur */
	public final Color getColor() {
		return color;
	}
	
	/** Retourne vrai si la case (i, j) est occupee */
	public boolean busyCase(int i, int j) {
		return array[i][j] != 0;
	}
	
	/** Place la shape sur la case (i, j) */
	public void setLocation(int i, int j) {
		point.setLocation(i, j);
		setChanged();
		notifyObservers();
	}
	
	/** Place la shape sur la position donnee par le point */
	public void setLocation(Point point) {
		setLocation((int) point.getX(), (int) point.getY());
	}
	
	/** Translate la position */
	public void tranlsate(int ti, int tj) {
		setLocation((int) point.getX() + ti, (int) point.getY() + tj);
	}
	
	public String toString() {
		String s = "";
		for (int j = 0; j < array.length; j++) {
			for (int i = 0; i < array[j].length; i++) {
				s += array[j][i] + " ";
			}
			s += "\n";
		}
		return s;
	}
}
