package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import application.Deserialization;
import application.Serialisation;
import data.Plateau;
import data.Shape;

public class MainWindow implements Observer {
	
	private static String FILENAME = "test";
	public static int HAUTEUR = 4;
	public static int LARGEUR = 3;
	
	private JFrame frame, frameEdit;
	private BoardFrame boardframe;
	private Plateau p;
	
	/** Constructeur */
	public MainWindow(JFrame frame, Plateau p) {
		this.frame = frame;
		this.frame.setTitle("Puzzle Game");
		this.frame.setJMenuBar(new Menu());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setResizable(false);
		
		this.frameEdit = new JFrame();
		EditFrame editFrame = new EditFrame(frameEdit, p);
		editFrame.addObserver(this);
		
		this.boardframe = new BoardFrame(p.getWidth(), p.getHeight());
		this.p = p;
		
		JPanel panneau = new JPanel();
		panneau.add(boardframe);
		this.frame.setContentPane(panneau);
				
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}
	
	/** Efface les fenetres */
	public void dispose() {
		frame.dispose();
		frameEdit.dispose();
	}
	
	@Override
	public void update(Observable o, Object object) {
		Shape shape = (Shape) object;
		boolean trouve = false;
		int h = p.getHeight() - shape.getHeight() + 1;
		int w = p.getWidth() - shape.getWidth() + 1;
		for (int i = 0; i < h && !trouve; i++) {
			for (int j = 0; j < w && !trouve; j++) {
				shape.setLocation(i, j);
				if (p.isValidLocation(shape)) {
					p.addShapeList(shape);
					boardframe.addShape(p, shape);
					trouve = true;
				}
			}
		}
	}
	
	public void updateShape() {
		for (Shape shape : p.getListShape()) {
			boardframe.addShape(p, shape);
		}
	}
	
	/** Classe privee */
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
			
			// Ajout des menus
			this.add(fichier);
			this.add(edit);
			
			soluce.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							long time = System.currentTimeMillis();
							p.resolution();
							long millis = System.currentTimeMillis() - time;
							if (!p.checkWin()) {
								JOptionPane.showMessageDialog(frame,
									    "Aucune solution trouvee",
									    "Resultat",
									    JOptionPane.WARNING_MESSAGE);
							} else {
								String str = String.format("%02dh %02dm %02ds", 
										TimeUnit.MILLISECONDS.toHours(millis),
										TimeUnit.MILLISECONDS.toMinutes(millis) -  
										TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
										TimeUnit.MILLISECONDS.toSeconds(millis) - 
										TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));   
								JOptionPane.showMessageDialog(frame,
									    "Solution trouvee en " + str,
									    "Resultat",
									    JOptionPane.DEFAULT_OPTION);
							}
						}
					});
					t.start();
				}
			});
			
			end.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					Serialisation s = new Serialisation(FILENAME);
					s.write(p);
					dispose();
					System.exit(0);
				}
			});
			
			newShape.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					if (!frameEdit.isVisible()) {
						frameEdit.setVisible(true);
					}
				}
			});
			
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					p.removeShapeList();
					boardframe.removeAll();
					boardframe.repaint();
				}
			});
		}
	}
	
	/** Lancement de l'application */
	public static void main(String[] args) {
		
		final Plateau p;
		
		File fichier = new File(FILENAME);		
		if (fichier.exists()) {
			Deserialization d = new Deserialization(FILENAME);
			p = (Plateau) d.read();
		} else {
			p = new Plateau(LARGEUR, HAUTEUR);
		}
		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame fen = new JFrame();
				final MainWindow mainWindow = new MainWindow(fen, p);
				fen.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent ev) {}
					
					public void keyReleased(KeyEvent ev) {}
					
					public void keyPressed(KeyEvent ev) {
						if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
							mainWindow.dispose();
							System.exit(0);
						}
					}
				});
				mainWindow.updateShape();
				fen.setVisible(true);
			}
		});
	}
}
