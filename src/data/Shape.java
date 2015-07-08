package data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.Observable;

public class Shape extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	Point point;
	Dimension dimension;
	private int[][] array;
	private Color color;
	
	/** Constructeur */
	public Shape(int line, int column, int[][] array, Color color) {
		this.point = new Point(line, column);
		this.dimension = new Dimension(array[0].length, array.length);
		this.array = array;
		this.color = color;
	}
	
	/** Constructeur */
	public Shape(Point point, int[][] array, Color color) {
		this((int) point.getX(), (int) point.getY(), array, color);
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
	
	/** Place la shape en position (line, column) */
	public void setLocation(int line, int column) {
		point.setLocation(line, column);
		setChanged();
		notifyObservers();
	}
	
	/** Place la shape sur la position donnee par le point */
	public void setLocation(Point point) {
		setLocation((int) point.getX(), (int) point.getY());
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
