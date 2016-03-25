package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import application.Application;
import application.Serialisation;
import data.Plateau;
import data.Shape;

public class MainWindow implements Observer {
	
	private JFrame frame;
	private BoardGame boardGame;
	private EditFrame frameEdit;
	private Plateau p;
	
	/** Constructeur */
	public MainWindow(Plateau p) {
		
		this.frame = new JFrame("Puzzle Game");
		this.frame.setJMenuBar(new Menu());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setResizable(false);
		
		frameEdit = new EditFrame(p);
		frameEdit.addObserver(this);
		
		this.boardGame = new BoardGame(p);
		
		JPanel panneau = new JPanel();
		panneau.add(boardGame);
		this.frame.setContentPane(panneau);
		
		this.p = p;
		
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	@Override
	public void update(Observable o, Object object) {
		Shape shape = (Shape) object;
		boolean trouve = false;
		int h = p.getHeight() - shape.getHeight() + 1;
		int w = p.getWidth() - shape.getWidth() + 1;
		for (int i = 0; i < h && !trouve; i++) {
			for (int j = 0; j < w && !trouve; j++) {
				if (p.isValidLocation(shape, i, j)) {
					p.addShape(shape, i, j);
					//boardGame.addShape(p, shape);
					trouve = true;
				}
			}
		}
	}
	
	/** Ferme l'application */
	public void close() {
		frameEdit.close();
		frame.dispose();
		System.exit(0);
	}
	
	/** Classe privee */
	private class Menu extends JMenuBar {
		
		private static final long serialVersionUID = 1L;
		JMenu fichier, edit;
		JMenuItem clear, save, end, newShape, soluce, soltransfo;
		
		/** Constructeur */
		public Menu() {
			super();
			
			clear = new JMenuItem("Nouveau plateau");
			save = new JMenuItem("Sauvegarder");
			end = new JMenuItem("Quitter");
			fichier = new JMenu("Fichier");
			fichier.add(clear);
			fichier.add(save);
			fichier.add(end);
			
			newShape = new JMenuItem("Nouvelle forme");
			soluce = new JMenuItem("Resolution");
			soltransfo = new JMenuItem("Solve transfo");
			edit = new JMenu("Edit");
			edit.add(newShape);
			edit.add(soluce);
			edit.add(soltransfo);
			
			// Ajout des menus
			this.add(fichier);
			this.add(edit);
			
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					p.removeShapes();
					boardGame.removeAll();
					boardGame.repaint();
				}
			});
			
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					String filename = "save/" + Application.FILENAME + "_" + Application.HAUTEUR + "x" + Application.LARGEUR;
					Serialisation s = new Serialisation(filename);
					s.write(p);
				}
			});
			
			end.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					close();
				}
			});
			
			newShape.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					if (!frameEdit.getFrame().isVisible()) {
						frameEdit.getFrame().setVisible(true);
					}
				}
			});
			
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
		
			
			soltransfo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							long time = System.currentTimeMillis();
							p.solveTransfo();
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
		
			
			
		}
	}
}
