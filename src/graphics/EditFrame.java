package graphics;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import data.Plateau;

public class EditFrame extends Observable {
	
	private JFrame frame;
	private BoardEdit boardsetting;
	
	/** Constucteur */
	public EditFrame(Plateau p) {
		this.frame = new JFrame("Editor");
		this.frame.setJMenuBar(new Menu());
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setResizable(false);
		
		boardsetting = new BoardEdit(p.getWidth(), p.getHeight());
		
		JPanel panneau = new JPanel();
		panneau.add(boardsetting);
		this.frame.setContentPane(panneau);
		
		this.frame.pack();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void close() {
		frame.dispose();
	}
	
	/** Classe privee */
	private class Menu extends JMenuBar {
		
		private static final long serialVersionUID = 1L;
		JMenu edit, color;
		JMenuItem valid, clear, close;
		JMenuItem personal, rouge, orange, jaune, vert, cyan, bleu, violet;
		
		/** Constructeur */
		public Menu() {
			super();
			
			valid = new JMenuItem("Valider");
			clear = new JMenuItem("Nettoyer");
			close = new JMenuItem("Fermer");
			
			edit = new JMenu("Edit");
			edit.add(valid);
			edit.add(clear);
			edit.add(close);
			
			personal = new JMenuItem("Personnalisee");
			rouge = new JMenuItem("Rouge");
			orange = new JMenuItem("Orange");
			jaune = new JMenuItem("Jaune");
			vert = new JMenuItem("Vert");
			cyan = new JMenuItem("Cyan");
			bleu = new JMenuItem("Bleu");
			violet = new JMenuItem("Violet");
			
			color = new JMenu("Couleur");
			color.add(personal);
			color.add(rouge);
			color.add(orange);
			color.add(jaune);
			color.add(vert);
			color.add(cyan);
			color.add(bleu);
			color.add(violet);
			
			// Ajout des menus
			this.add(edit);
			this.add(color);
			
			valid.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					if (!boardsetting.isEmpty()) {
						setChanged();
						notifyObservers(boardsetting.getShape());
						boardsetting.clear();
					}
				}
			});
			
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.clear();
				}
			});
			
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					frame.dispose();
				}
			});
			
			personal.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					Color color = JColorChooser.showDialog(frame, "Choisir une couleur", Color.red);
					if (color != null) {
						boardsetting.setColor(color);	
					}
				}
			});
			
			rouge.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.red);
				}
			});
			
			orange.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.orange);
				}
			});
			
			jaune.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.yellow);
				}
			});
			
			vert.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.green);
				}
			});
			
			cyan.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.cyan);
				}
			});
			
			bleu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.blue);
				}
			});
			
			violet.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent ev) {
					boardsetting.setColor(Color.magenta);
				}
			});
		}
	}
}
