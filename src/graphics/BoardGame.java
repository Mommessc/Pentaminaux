package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import data.Plateau;
import data.Shape;

public class BoardGame extends JComponent implements Observer {
	
	private static final long serialVersionUID = 1L;
	private int boardWidth, boardHeight;
	private int nb_sol;
	private Plateau p;
	public Boolean free;
	
	/** Constructeur */
	public BoardGame(Plateau p) {
		super();
		this.boardWidth = p.getWidth() - 8;
		this.boardHeight = p.getHeight() - 8;
		this.nb_sol = 1;
		this.p = p;
		this.free = true;
		
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
	
	
	public void update(Observable obs, Object object) {
		Plateau p = (Plateau) obs; 
		Shape shape = (Shape) object;
		
		if(shape == null){
			java.awt.image.BufferedImage im = new java.awt.image.BufferedImage(this.getWidth(), this.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
			this.paint(im.getGraphics());
			try {
				ImageIO.write(im, "PNG", new java.io.File("Images/shot_" + (nb_sol++) + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			addShape(p, shape);
		}
		
	}
	
	public void printsol(){
		/*ArrayList<Shape> listShapes = new ArrayList<Shape>();
		for(Shape sh : p.getListShape()){
			listShapes.add(new Shape(sh));
		}
		p.removeShapes();*/
		System.out.println("i'm here");
		this.removeAll();
		//this.repaint();
		
		this.free=false;
		System.out.println("also here");
		this.repaint();
		//this.free=true;
		System.out.println("said here !!");
		for(Shape sh : p.getListShape()){
			addShape(p,sh);
			//p.addShape(sh,sh.getLine(), sh.getColumn());
		}
		//this.repaint();
	}
	
	/** Dessine le plateau */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());
		System.out.println("blabla");
		if(free){
			System.out.println("coucou" + free);
			g.setColor(Color.darkGray);
			g.fillRect(120, 120, boardWidth*30, boardHeight*30);
		}
		else{
			System.out.println("and here");
			ArrayList<Shape> list = p.getListShape();
			for(int[][] array : p.getListSol()){
				for (int i = 0; i < array.length; i++) {
					for (int j = 0; j < array[i].length; j++) {
						g.setColor( list.get(array[i][j]-1).getColor());
						g.fillRect(j*30+121, i*30+121, 28, 28);
					}
				}
			}
			System.out.println("repaint done");
		}
	}
}
