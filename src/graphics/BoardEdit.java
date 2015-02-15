package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import data.Shape;

public class BoardEdit extends JComponent implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	private int width, height;
	private int[][] array;
	private Color color;
	
	public BoardEdit(int pWidth, int pHeight) {
		super();
		this.width = pWidth - 8;
		this.height = pHeight - 8;
		
		array = new int[height][];
		for (int i=0; i<height; i++) {
			array[i] = new int[width];
			for (int j=0; j<width; j++) {
				array[i][j] = 0;
			}
		}
		this.color = Color.red;
		
		addMouseListener(this);
		
		setLayout(null);
		setPreferredSize(new Dimension((width+2)*30, (height+2)*30));
	}
	
	public void setColor(Color color) {
		this.color = color;
		repaint();
	}
	
	public Shape getShape() {
		int res;
		int line_top_to_delete = 0;
		for (int i=0; i<height; i++) {
			res = 0;
			for (int j : array[i]) {
				res += j;
			}
			if (res == 0) {
				line_top_to_delete += 1;
			} else {
				i = height;
			}
		}
		
		int line_bottom_to_delete = height;
		for (int i=height-1; i>=0; i--) {
			res = 0;
			for (int j : array[i]) {
				res += j;
			}
			if (res == 0) {
				line_bottom_to_delete -= 1;
			} else {
				i = -1;
			}
		}
		int column_left_to_delete = 0;
		for (int j=0; j<width; j++) {
			res = 0;
			for (int i=0; i<height; i++) {
				res += array[i][j];
			}
			if (res == 0) {
				column_left_to_delete += 1;
			} else {
				j = width;
			}
		}
		int column_right_to_delete = width;
		for (int j=width-1; j>=0; j--) {
			res = 0;
			for (int i=0; i<height; i++) {
				res += array[i][j];
			}
			if (res == 0) {
				column_right_to_delete -= 1;
			} else {
				j = -1;
			}
		}
		
		int array_width = column_right_to_delete - column_left_to_delete;
		int array_height = line_bottom_to_delete - line_top_to_delete;
		int[][] new_array = new int[array_height][];
		for (int i=line_top_to_delete; i<line_bottom_to_delete; i++) {
			new_array[i-line_top_to_delete] = new int[array_width];
			for (int j=column_left_to_delete; j<column_right_to_delete; j++) {
				new_array[i-line_top_to_delete][j-column_left_to_delete] = array[i][j];
			}
		}
		
		return new Shape(0, 0, new_array, color);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.darkGray);
		g.fillRect(30, 30, width*30, height*30);
		
		g.setColor(color);
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (array[i][j] != 0) {
					g.fillRect((j+1)*30+1, (i+1)*30+1, 28, 28);
				}
			}
		}
	}
		
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent ev) {
		int line = ev.getY() / 30;
		int column = ev.getX() / 30;
		if (line >= 1 && column >= 1 && line <= this.height && column <= this.width) {
			if (array[line-1][column-1] == 1) {
				array[line-1][column-1] = 0;
			} else {
				array[line-1][column-1] = 1;
			}
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isEmpty() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (array[i][j] != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void clear() {
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				array[i][j] = 0;
			}
		}
		repaint();
	}
	
}
