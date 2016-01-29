package data;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class Shape extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	Point point;
	public int id;
	private ArrayList<int[][]> transfo;
	private Color color;
	
	/** Constructeur */
	public Shape(int line, int column, int[][] array, Color color) {
		this.point = new Point(line, column);
		this.color = color;
		this.id = 0;
		this.transfo = createTransfo(array);
	}
	
	/** Constructeur */
	public Shape(Point point, int[][] array, Color color) {
		this((int) point.getX(), (int) point.getY(), array, color);
	}
	

	public Shape(Shape s) {
		this.point = new Point();
		this.point.x = s.point.x;
		this.point.y = s.point.y;
		this.color = s.color;
		this.id = 0;
		this.transfo = createTransfo(s.transfo.get(0));
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
		return transfo.get(id)[0].length;
	}
	
	/** Retourne la hauteur */
	public int getHeight() {
		return transfo.get(id).length;
	}
	
	/** Retourne la couleur */
	public final Color getColor() {
		return color;
	}
	
	/** Retourne vrai si la case (i, j) est occupee */
	public boolean busyCase(int i, int j) {
		return (transfo.get(id))[i][j] != 0;
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
	
	
	public ArrayList<int[][]> createTransfo(int[][] array){
		ArrayList<int[][]> list = new ArrayList<int[][]>();
		int height = array.length, width = array[0].length;
		
		// first shape
		list.add(array);
		
		// Second shape, rotation 1 (trigo pi/2)
		int[][] tab12 = new int[width][height];
		
		for(int i=0;i<width;i++){
			for(int j = 0;j<height;j++){
				tab12[i][j] = array[j][width-1-i];
			}
		}
		list.add(tab12);
		
		int[][] tab13 = new int[height][width];
		for(int i=0;i<height;i++){
			for(int j = 0;j<width;j++){
				tab13[i][j] = tab12[j][height-1-i];
			}
		}
		list.add(tab13);

		int[][] tab14 = new int[width][height];
		for(int i=0;i<width;i++){
			for(int j = 0;j<height;j++){
				tab14[i][j] = tab13[j][width-1-i];
			}
		}
		list.add(tab14);
		
		
		//horizontal symmetric
		int[][] tab21 = new int[height][width];
		for(int i=0;i<(height+1)/2;i++){
			for(int j = 0;j<width;j++){
				tab21[i][j] = array[height-1-i][j];
				tab21[height-1-i][j] = array[i][j];
			}
		}
		list.add(tab21);
		
		
		int[][] tab22 = new int[width][height];
		for(int i=0;i<width;i++){
			for(int j = 0;j<height;j++){
				tab22[i][j] = tab21[j][width-1-i];
			}
		}
		list.add(tab22);
		
		int[][] tab23 = new int[height][width];
		for(int i=0;i<height;i++){
			for(int j = 0;j<width;j++){
				tab23[i][j] = tab22[j][height-1-i];
			}
		}
		list.add(tab23);
		
		int[][] tab24 = new int[width][height];
		for(int i=0;i<width;i++){
			for(int j = 0;j<height;j++){
				tab24[i][j] = tab23[j][width-1-i];
			}
		}
		list.add(tab24);
		
		return list;
	}
	
	
	public String toString() {
		String s = "";
		for (int j = 0; j < transfo.get(id).length; j++) {
			for (int i = 0; i < transfo.get(id)[j].length; i++) {
				s += transfo.get(id)[j][i] + " ";
			}
			s += "\n";
		}
		return s;
	}
}
