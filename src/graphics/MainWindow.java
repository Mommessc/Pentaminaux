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
import application.Serialisation;
import data.Plateau;
import data.Shape;

public class MainWindow implements Observer {
	
	private JFrame frame, frameEdit;
	private BoardGame boardGame;
	private Plateau p;
	private String filename;
	
	/** Constructeur */
	public MainWindow(JFrame frame, Plateau p, String filename) {
		this.frame = frame;
		this.frame.setTitle("Puzzle Game");
		this.frame.setJMenuBar(new Menu());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setResizable(false);
		
		this.frameEdit = new JFrame();
		EditFrame editFrame = new EditFrame(frameEdit, p);
		editFrame.addObserver(this);
		
		this.boardGame = new BoardGame(p.getWidth(), p.getHeight());
		
		JPanel panneau = new JPanel();
		panneau.add(boardGame);
		this.frame.setContentPane(panneau);
		
		this.p = p;
		this.filename = filename;
		
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
				if (p.isValidLocation(shape, i, j)) {
					p.addShape(shape, i, j);
					boardGame.addShape(p, shape);
					trouve = true;
				}
			}
		}
	}
	
	public void updateShape() {
		for (Shape shape : p.getListShape()) {
			boardGame.addShape(p, shape);
		}
	}
	
	public void close(String filename) {
		if (filename != null) {
			Serialisation s = new Serialisation(filename);
			s.write(p);
		}
		dispose();
		System.exit(0);
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
					close(filename);
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
					p.removeShapes();
					boardGame.removeAll();
					boardGame.repaint();
				}
			});
		}
	}
	
}
