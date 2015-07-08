package application;

import graphics.MainWindow;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import data.Plateau;

public class Application {
	
	private static String FILENAME = "test";
	public static int HAUTEUR = 7;
	public static int LARGEUR = 8;
	
	/** Lancement de l'application */
	public static void main(String[] args) {
		
		final Plateau p;
		
		final String filename = "save/" + FILENAME + "_" + HAUTEUR + LARGEUR;
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
				final JFrame fen = new JFrame();
				final MainWindow mainWindow = new MainWindow(fen, p, filename);
				fen.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent ev) {}
					
					public void keyReleased(KeyEvent ev) {}
					
					public void keyPressed(KeyEvent ev) {
						if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
							mainWindow.close(filename);
						}
					}
				});
				mainWindow.updateShape();
				fen.setVisible(true);
			}
		});
	}
}
