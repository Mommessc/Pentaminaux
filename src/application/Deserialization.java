package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialization {
	
	private String filename;
	
	public Deserialization(String filename) {
		this.filename = filename;
	}
	
	public Object read() {
		Object o = null;
		
		try {
			final FileInputStream fichier = new FileInputStream(filename);
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(fichier);
			o = ois.readObject();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return o;
	}
}
