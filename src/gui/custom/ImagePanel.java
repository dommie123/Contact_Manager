package gui.custom;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7282811107982205836L;

    private Image image;
    
    public ImagePanel() {
    	this.image = null;
    }
    
    public ImagePanel(Image image) {
        this.image = image;
    }
    
    public Image getImage() {
    	return image;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    	this.revalidate();
    	this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (image != null)
        	g.drawImage(image, 0, 0, this);
    }
}
