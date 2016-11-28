package data;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;

public class Plateau extends Observable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int width, height;
	private int[][] array;
	private ArrayList<Shape> listShape;
	private ArrayList<int[][]> list_sol;
	boolean lock;
	
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
		this.listShape = new ArrayList<Shape>();
		this.lock = false;
	}
	
	/** Retourne la largeur du plateau */
	public int getWidth() {
		return width;
	}
	
	/** Retourne la hauteur du plateau */
	public int getHeight() {
		return height;
	}
	
	/** Retourne vrai si la case (i, j) est a l'interieur du plateau */
	public boolean valid(int i, int j) {
		return (i >= 0 && j >= 0 && i < getHeight() && j < getWidth());
	}
	
	/** Retourne vrai si la case (i, j) n'est pas vide */
	public boolean busyCase(int i, int j) {
		return getBlock(i, j) != 0;
	}
	
	/** Retourne vrai si le plateau est bloque */
	public boolean isLocked() {
		return lock;
	}
	
	/** Retourne le contenu de la case (i, j) */
	public int getBlock(int i, int j) {
		return array[i][j];
	}
	
	/** Retourne la liste des shapes du plateau */
	public final ArrayList<Shape> getListShape() {
		return listShape;
	}
	
	/** Retourne la liste des solution trouvees */
	public final ArrayList<int[][]> getListSol() {
		return list_sol;
	}
	
	/** Set le contenu de la case (i, j) */
	public void setBlock(int i, int j, int k) {
		array[i][j] = k;
	}
	
	
	
	/** Retourne vrai si la shape peut etre placee en position (line, column) sur le plateau courant */
	public boolean isValidLocation(Shape shape, int line, int column) {
		if (line < 0 || column < 0 || line+shape.getHeight() > getHeight() || column+shape.getWidth() > getWidth()) {
			return false;
		}
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && (getBlock(line+i, column+j) > 0) ) {
					return false;
				}
			}
		}
		return true;
	}
	
	/** Retourne vrai si l'etat du plateau est admissible, toutes les shapes en dessous de id sont fixees */
	private boolean positionPossible(int id) {
		Point old_point;
		
		// Copie du plateau
		Plateau tmp = new Plateau(getWidth()-8, getHeight()-8);
		for (int i = 0; i < tmp.getHeight()-8; i++) {
			for (int j = 0; j < tmp.getWidth()-8; j++) {
				tmp.setBlock(i+4, j+4, getBlock(i+4, j+4));
			}
		}
		
		// Recuperation de toutes les postions possibles de toutes les shapes
		ArrayList<ArrayList<Point>> listPoints = new ArrayList<ArrayList<Point>>(0);
		Shape shape;
		for (int i = 0; i < listShape.size(); i++) {
			if (i > id) {
				shape = listShape.get(i);
				listPoints.add(tmp.getAllMove(shape));
			} else {
				listPoints.add(null);
			}
		}
		
		// Placement de toutes les shapes sur toutes leurs positions
		for (int i = id+1; i < listShape.size(); i++) {
			shape = listShape.get(i);
			old_point = new Point(shape.getPoint());
			for (Point point : listPoints.get(i)) {
				shape.setLocation(point);
				tmp.putShape(shape);
			}
			shape.setLocation(old_point);
		}
		
		// Verification d'etat du plateau
		boolean isPossible = true;
		for (int i = 0; i < tmp.getHeight()-8 && isPossible; i++) {
			for (int j = 0; j < tmp.getWidth()-8 && isPossible; j++) {
				if (tmp.getBlock(i+4, j+4) == 0) {
					//isPossible = false;
					return false;
				}
			}
		}
		
		return true;
		//return isPossible;
	}
	
	/** Retourne vrai si le plateau est entierement rempli */
	public boolean checkWin() {
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (i >= 4 && j >= 4 && j < getWidth()-4 && i < getHeight()-4) {
					if (getBlock(i, j) == 0) {
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
	
	/** Retourne toutes les positions possibles d'une shape sur le plateau courant */
	private final ArrayList<Point> getAllMove(Shape shape) {
		ArrayList<Point> arrayPoint = new ArrayList<Point>();
		
		for (int i = 4; i < getHeight()-3-shape.getHeight(); i++) {
			for (int j = 4; j < getWidth()-3-shape.getWidth(); j++) {
				if (isValidLocation(shape, i, j)) {
					arrayPoint.add(new Point(i, j));
				}
			}
		}
		
		return arrayPoint;
	}
	
	
	
	/** Ajoute une shape a la liste */
	public void addShape(Shape shape, int line, int column) {
		listShape.add(shape);
		shape.setLocation(line, column);
		putShape(shape);
		setChanged();
		notifyObservers(shape);
	}
	
	/** Retire toutes les shapes du plateau */
	public void removeShapes() {
		for (Shape shape : getListShape()) {
			popShape(shape);
		}
		listShape.clear();
	}
	
	/** Pose la shape sur le plateau */
	public void putShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					//setBlock(line+i, column+j, 1);
					setBlock(line+i, column+j, shape.getIndex());
				}
			}
		}
	}
	
	/** Enleve la shape du plateau */
	public void popShape(Shape shape) {
		int line = shape.getLine();
		int column = shape.getColumn();
		for (int i = 0; i < shape.getHeight(); i++) {
			for (int j = 0; j < shape.getWidth(); j++) {
				if (shape.busyCase(i, j) && valid(line+i, column+j)) {
					setBlock(line+i, column+j, 0);
				}
			}
		}
	}
	
	private void sortList(){
		Comparator<Shape> c = new Comparator<Shape>() {
			@Override
			public int compare(Shape shape1, Shape shape2) {
				int surface1 = shape1.getWidth() * shape1.getHeight();
				int surface2 = shape2.getWidth() * shape2.getHeight();
				return (surface2 - surface1);
			}
			
		};
		ArrayList<Shape> list = getListShape();
		Collections.sort(list, c);
		for(int i = 0; i<list.size(); i++){
			list.get(i).setIndex(i+1);
		}
	}
	
	/** Resout le casse tete */
	public void resolution() {
		lock = true;
		
		sortList();
		
		ArrayList<Point> old_arrayPoint = new ArrayList<Point>();
		for (Shape shape : getListShape()) {
			old_arrayPoint.add(new Point(shape.getPoint()));
			popShape(shape);
			shape.correctId = 0;
			//shape.setIndex(1);
			//System.out.println("shape :\n" + shape);
		}
		
		ArrayList<Point> arrayPoint = resolutionAux(0);
		
		if (arrayPoint == null) {
			arrayPoint = old_arrayPoint;
		}
		
		for (Shape shape : getListShape()) {
			shape.setLocation(arrayPoint.get(getListShape().indexOf(shape)));
			shape.transfoId = shape.correctId;
			putShape(shape);
		}
		
		lock = false;
	}
	
	/** Methode recursive de resolution */
	private ArrayList<Point> resolutionAux(int id) {
		if (checkWin()) {
			System.out.println(this);
			return new ArrayList<Point>();
		}
		
		if(id == listShape.size()){
			return null;
		}
		
		ArrayList<Point> arrayPoint = null;
		Shape shape = listShape.get(id);
		shape.transfoId = 0;
			
		for (Point point : getAllMove(shape)) {
			shape.setLocation(point);
			putShape(shape);
			
			//System.out.println(this);
			
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			
			//if (positionPossible(id)) {
				arrayPoint = resolutionAux(id+1);
			//}
			
			popShape(shape);
			
			if (arrayPoint != null) {
				arrayPoint.add(0, shape.getPoint());
				shape.correctId = 0;
				return arrayPoint;
			}
		}
		
		return null;
	}
	
	
	/** Trouve toutes les solutions possibles avec transformations des shapes*/
	public void resolutionTransfo() {
		lock = true;
		
		sortList();
		
		ArrayList<Point> old_arrayPoint = new ArrayList<Point>();
		for (Shape shape : getListShape()) {
			old_arrayPoint.add(new Point(shape.getPoint()));
			popShape(shape);
			shape.correctId = 0;
			//System.out.println("shape :\n" + shape);
		}
		
		ArrayList<Point> arrayPoint = resolutionAuxTransfo(0);
		
		if (arrayPoint == null) {
			arrayPoint = old_arrayPoint;
		}
		
		for (Shape shape : getListShape()) {
			shape.setLocation(arrayPoint.get(getListShape().indexOf(shape)));
			shape.transfoId = shape.correctId;
			putShape(shape);
		}
		
		lock = false;
	}
	
	/** Methode recursive de resolution avec transformations des shapes */
	private ArrayList<Point> resolutionAuxTransfo(int id) {
		if (checkWin()) {
			setChanged();
			notifyObservers(null); //pour print la solution trouvee
			return null;
		}
		
		if(id == listShape.size()){
			return null;
		}
		
		ArrayList<Point> arrayPoint = null;
		Shape shape = listShape.get(id);
		
		//pour toutes les transfos de la piece
		for(int idtransfo = 0; idtransfo < shape.transfoNb; idtransfo++){
			//set le id de la transfo
			shape.transfoId = idtransfo;
			
			for (Point point : getAllMove(shape)) {
				shape.setLocation(point);
				putShape(shape);
				
				//System.out.println(this);
				
				/*try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				
				//if (positionPossible(id)) {
					arrayPoint = resolutionAuxTransfo(id+1);
				//}
				
				popShape(shape);
				
				if (arrayPoint != null) {
					arrayPoint.add(0, shape.getPoint());
					shape.correctId = idtransfo;
					return arrayPoint;
				}
			}
			
		}
		
		return null;
	}
	
	
	public int solveIntern(){
		lock = true;
		sortList();//sort the list and initialize the index of each shape
		this.list_sol = new ArrayList<int[][]>();
		
		//ArrayList<Color> list_color = new ArrayList<Color>();
		ArrayList<Point> old_arrayPoint = new ArrayList<Point>();
		for (Shape shape : getListShape()) {
			old_arrayPoint.add(new Point(shape.getPoint()));
			popShape(shape);
			shape.correctId = 0;
		}
		
		int nb_sol = solveInternAux(0);
		
		System.out.println(nb_sol + " and size list: " + this.list_sol.size());
		
		lock = false;
		return nb_sol;
	}
	
	private int solveInternAux(int id) {
		if (checkWin()) {
			int[][] sol = new int[getHeight()][];
			for (int i = 4; i < getHeight()-4; i++) {
				sol[i] = new int[getWidth()];
				for (int j = 4; j < getWidth()-4; j++) {
					sol[i][j] = array[i][j];
				}
			}
			this.list_sol.add(sol);
			return 1;
		}
		
		if(id == listShape.size()){//si toutes les pieces sont posees, stop
			return 0;
		}
		
		Shape shape = listShape.get(id);
		int nb_sol = 0;
		
		//pour toutes les transfos de la piece
		for(int idtransfo = 0; idtransfo < shape.transfoNb; idtransfo++){
			//set le id de la transfo
			shape.transfoId = idtransfo;
			
			for (Point point : getAllMove(shape)) {
				//shape.setLocation(point);
				shape.setLocationIntern(point);
				putShape(shape);
				
				nb_sol += solveInternAux(id+1);
				
				popShape(shape);
			}
		}
		
		return nb_sol;
	}

	
	public String toString(){
		String s = "";
		
		for (int i = 4; i < height-4; i++) {
			for (int j = 4; j < width-4; j++) {
				s += array[i][j] + " ";
			}
			s+= "\n";
		}
		
		return s;
	}
}
