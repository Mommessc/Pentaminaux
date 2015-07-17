package application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import graphics.MainWindow;

import javax.swing.SwingUtilities;

import data.Plateau;

public class Application {
	
	public static String FILENAME = "test";
	public static int HAUTEUR = 15;
	public static int LARGEUR = 15;
	
	/** Lancement de l'application */
	public static void main(String[] args) {
		
		final Plateau p;
		
		final String filename = "save/" + FILENAME + "_" + HAUTEUR + "x" + LARGEUR;
		File fichier = new File(filename);
		if (fichier.exists()) {
			Deserialization d = new Deserialization(filename);
			p = (Plateau) d.read();
		} else {
			p = new Plateau(LARGEUR, HAUTEUR);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final MainWindow fen = new MainWindow(p);
				fen.getFrame().addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent ev) {}
					
					public void keyReleased(KeyEvent ev) {}
					
					public void keyPressed(KeyEvent ev) {
						if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
							fen.close();
						}
					}
				});
				fen.getFrame().setVisible(true);
			}
		});
	}
}
