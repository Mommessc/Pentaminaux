package application;

import graphics.BoardEdit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import data.Plateau;

public class EditFrame extends Observable {
	
	private JFrame frame;
	private BoardEdit boardsetting;
	
	public EditFrame(JFrame frame, Plateau p) {
		this.frame = frame;
		this.frame.setJMenuBar(new Menu());
		
		boardsetting = new BoardEdit(p.getWidth(), p.getHeight());
		
		frame.setContentPane(new JPanel());
		frame.getContentPane().add(boardsetting);
	}
	
	private class Menu extends JMenuBar {
		
		private static final long serialVersionUID = 1L;
		JMenu edit, color;
		JMenuItem valid, clear, close, rouge, vert, bleu, orange, cyan, rose, violet, blanc;
		
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
			
			
			rouge = new JMenuItem("Rouge");
			vert = new JMenuItem("Vert");
			bleu = new JMenuItem("Bleu");
			orange = new JMenuItem("Orange");
			cyan = new JMenuItem("Cyan");
			rose = new JMenuItem("rose");
			violet = new JMenuItem("Violet");
			blanc = new JMenuItem("Blanc");
			color = new JMenu("Couleur");
			color.add(rouge);
			color.add(vert);
			color.add(bleu);
			color.add(orange);
			color.add(cyan);
			color.add(rose);
			color.add(violet);
			color.add(blanc);
			
			// Ajout des menus au menu "this"
			this.add(edit);
			this.add(color);
			
			valid.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (!boardsetting.isEmpty()) {
						setChanged();
						notifyObservers(boardsetting.getShape());
					}
				}
			});
			
			clear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.clear();
				}
			});
			
			close.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.dispose();
				}
			});
			
			rouge.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.red);
				}
			});
			
			vert.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.green);
				}
			});
			
			bleu.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.blue);
				}
			});
			
			orange.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.orange);
				}
			});
			
			cyan.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.cyan);
				}
			});
			
			rose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.pink);
				}
			});
			
			violet.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.magenta);
				}
			});
			
			blanc.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					boardsetting.setColor(Color.white);
				}
			});
			
		}
	}
	
}
