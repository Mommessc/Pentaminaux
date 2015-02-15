package application;

import graphics.BoardFrame;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import data.Plateau;
import data.Shape;

public class Application implements Observer {
	
	private JFrame frame, frameEdit;
	private BoardFrame boardframe;
	private Plateau p;
	
	public Application(final JFrame frame, final JFrame frameEdit) {
		this.frame = frame;
		this.frame.setJMenuBar(new Menu());
		
		frame.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			
			public void keyReleased(KeyEvent arg0) {}
			
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
					frame.dispose();
					frameEdit.dispose();
					System.exit(0);
				}
			}
		});
		
		this.p = new Plateau(3, 4);
		this.boardframe = new BoardFrame(p.getWidth(), p.getHeight());
		this.p.addObserver(boardframe);
		
		this.frameEdit = frameEdit;
		EditFrame editFrame = new EditFrame(frameEdit, p);
		editFrame.addObserver(this);
		
		frame.setContentPane(new JPanel());
		frame.getContentPane().add(boardframe);
	}
	
	@Override
	public void update(Observable o, Object object) {
		Shape shape = (Shape) object;
		boolean trouve = false;
		
		for (int i=0; i<p.getHeight() && !trouve; i++) {
			for (int j=0; j<p.getWidth() && !trouve; j++) {
				shape.setLocation(i, j);
				trouve = p.addShapeList(shape);
			}
		}
	}
	
	private class Menu extends JMenuBar {
		
		private static final long serialVersionUID = 1L;
		JMenu fichier, edit;
		JMenuItem soluce, end, newShape, clear;
		
		/** Constructeur */
		public Menu() {
			super();
			
			soluce = new JMenuItem("Resolution");
			end = new JMenuItem("Quitter");
			fichier = new JMenu("Fichier");
			fichier.add(soluce);
			fichier.add(end);
			
			newShape = new JMenuItem("Nouvelle forme");
			clear = new JMenuItem("Nettoyer");
			edit = new JMenu("Edit");
			edit.add(newShape);
			edit.add(clear);
			
			// Ajout des menus au menu "this"
			this.add(fichier);
			this.add(edit);
			
			soluce.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					p.resolution();
					boardframe.repaint();
				}
			});
			
			end.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.dispose();
					frameEdit.dispose();
					System.exit(0);
				}
			});
			
			newShape.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (!frameEdit.isVisible()) {
						frameEdit.setVisible(true);
					}
				}
			});
			
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					p.removeList();
					boardframe.removeAll();
					boardframe.repaint();
				}
			});
			
		}
	}
	
	public static void main(String[] args) {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		int width = (int) tk.getScreenSize().getWidth();
		int height = (int) tk.getScreenSize().getHeight();
		
		JFrame frame, frameEdit;
		
		frame = new JFrame("Application");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocation(width / 6, height / 6);
		frame.setResizable(false);
		
		frameEdit = new JFrame("Edit");
		frameEdit.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameEdit.setLocation(0, 0);
		frameEdit.setResizable(false);
		
		new Application(frame, frameEdit);
		
		frame.pack();
		frame.setVisible(true);
		frameEdit.pack();
		frameEdit.setVisible(false);
	}

}

