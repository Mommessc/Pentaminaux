package data;

import java.awt.Point;
import java.util.ArrayList;

public class Plateau {
	
	private int width, height;
	private int[][] array;
	private ArrayList<Shape> arrayShape;
	
	/** Constructeur */
	public Plateau(int width, int height) {
		this.width = width+8;
		this.height = height+8;
		
		array = new int[getHeight()][];
		for (int i = 0; i < getHeight(); i++) {
			array[i] = new int[getWidth()];
			for (int j = 0; j < getWidth(); j++) {
				setBlock(i, j, 0);
			}
		}
		this.arrayShape = new ArrayList<Shape>();
	}
	
	/** Retourne la largeur du plateau */
	public int getWidth() {
		return width;
	}
	
	/** Retourne la hauteur du plateau */
	public int getHeight() {
		return height;
	}
	
	/** Retourne si la case (i, j) est a l'interieur du plateau */
	public boolean valid(int i, int j) {
		return (i >= 0 && j >= 0 && i < getHeight() && j < getWidth());
	}
	
	/** Retourne vrai si la case (i, j) n'est pas vide */
	public boolean busyCase(int i, int j) {
		return getBlock(i, j) != 0;
	}
	
	/** Retourne vrai si la shape peut etre placee sur sa position */
	public boolean isValidLocation(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		if (line < 0 || column < 0 || line+shape.getHeight() > getHeight() || column+shape.getWidth() > getWidth()) {
			return false;
		}
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && getBlock(line+i, column+j) > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	/** Retourne le contenu de la case (i, j) */
	public int getBlock(int i, int j) {
		return array[i][j];
	}
	
	/** Retourne la liste des shapes du plateau */
	public final ArrayList<Shape> getListShape() {
		return arrayShape;
	}
	
	/** Set le contenu de la case (i, j) */
	public void setBlock(int i, int j, int k) {
		array[i][j] = k;
	}
	
	
	/** Ajoute une shape au plateau */
	public void addShapeList(Shape shape) {
		arrayShape.add(shape);
		placeShape(shape);
	}
	
	/** Retire toutes les shapes du plateau */
	public void removeShapeList() {
		for (Shape shape : getListShape()) {
			removeShape(shape);
		}
		arrayShape.clear();
	}
	
	public void placeShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					array[line+i][column+j] += 1;
				}
			}
		}
	}
	
	public void removeShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					array[line+i][column+j] -= 1;
				}
			}
		}
	}
	
	/** Retourne vrai si la position des shapes remplies le plateau */
	public boolean checkWin() {
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (i >= 4 && j >= 4 && j < getWidth()-4 && i < getHeight()-4) {
					if (getBlock(i, j) != 1) {
						return false;
					}
				} else {
					if (getBlock(i, j) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/** Retourne toutes les positions possibles d'une shape sur le plateau */
	private final ArrayList<Point> getAllMove(Shape shape) {
		ArrayList<Point> arrayPoint = new ArrayList<Point>();
		Point point = (Point) shape.getPoint().clone();
		
		for (int i = 4; i < getHeight()-3-shape.getHeight(); i++) {
			for (int j = 4; j < getWidth()-3-shape.getWidth(); j++) {
				shape.setLocation(i, j);
				if (isValidLocation(shape)) {
					arrayPoint.add(new Point(i, j));
				}
			}
		}
		
		shape.setLocation(point);
		return arrayPoint;
	}
	
	/** Resout le casse tete */
	public void resolution() {
		ArrayList<Point> old_arrayPoint = new ArrayList<Point>();
		for (Shape shape : getListShape()) {
			old_arrayPoint.add((Point) shape.getPoint().clone());
			removeShape(shape);
		}
		
		ArrayList<Point> arrayPoint = resolutionAux(new ArrayList<Shape>());
		if (arrayPoint == null) {
			arrayPoint = old_arrayPoint;
		}
		
		for (Shape shape : getListShape()) {
			shape.setLocation(arrayPoint.get(getListShape().indexOf(shape)));
			placeShape(shape);
		}
	}
	
	/** Methode recursive de resolution */
	private ArrayList<Point> resolutionAux(ArrayList<Shape> arrayShapeAux) {
		if (checkWin()) {
			return new ArrayList<Point>();
		}
		
		ArrayList<Point> arrayPoint = null;
		for (Shape shape : getListShape()) {
			
			if (!arrayShapeAux.contains(shape)) {
				for (Point point : getAllMove(shape)) {
					shape.setLocation(point);
					
					placeShape(shape);
					if (positionPossible(arrayShapeAux)) {
						arrayShapeAux.add(shape);
						arrayPoint = resolutionAux(arrayShapeAux);
						arrayShapeAux.remove(shape);
					}
					removeShape(shape);
					
					if (arrayPoint != null) {
						arrayPoint.add(0, shape.getPoint());
						return arrayPoint;
					}
				}
			}
			
		}
		
		return null;
	}
	
	private boolean positionPossible(ArrayList<Shape> arrayShapeAux) {
		Point old_point;
		
		// Copie du plateau
		Plateau tmp = new Plateau(getWidth()-8, getHeight()-8);
		for (int i = 0; i < tmp.getHeight()-8; i++) {
			for (int j = 0; j < tmp.getWidth()-8; j++) {
				tmp.setBlock(i+4, j+4, getBlock(i+4, j+4));
			}
		}
		
		ArrayList<ArrayList<Point>> listPoints = new ArrayList<ArrayList<Point>>(0);
		for (Shape shape : getListShape()) {
			if (!arrayShapeAux.contains(shape)) {
				listPoints.add(tmp.getAllMove(shape));
			} else {
				listPoints.add(null);
			}
		}
		
		for (Shape shape : getListShape()) {
			if (!arrayShapeAux.contains(shape)) {
				old_point = (Point) shape.getPoint().clone();
				for (Point point : listPoints.get(getListShape().indexOf(shape))) {
					shape.setLocation(point);
					tmp.placeShape(shape);
				}
				shape.setLocation(old_point);
			}
		}
		
		boolean possible = true;
		for (int i = 0; i < tmp.getHeight()-8 && possible; i++) {
			for (int j = 0; j < tmp.getWidth()-8 && possible; j++) {
				if (tmp.getBlock(i+4, j+4) == 0) {
					possible = false;
				}
			}
		}
		
		return possible;
	}
}
