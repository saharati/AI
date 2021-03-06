package util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public enum ImageType
{
	MC_BACKGROUND("background.jpg", false),
	MC_HUMAN("human.png", false),
	MC_ZOMBIE("zombie.png", false),
	MC_BOAT("boat.png", true),
	JUG_BACKGROUND("jugbackground.jpg", false),
	JUG_ARROW("arrow.png", true),
	JUG_BIGARROW("bigarrow.png", true),
	PRS_EMPTY("empty.jpg", false),
	PRS_PAPER("paper.jpg", false),
	PRS_PAPERBUTTON("paperButton.jpg", false),
	PRS_ROCK("rock.jpg", false),
	PRS_ROCKBUTTON("rockButton.jpg", false),
	PRS_SCISSORS("scissors.jpg", false),
	PRS_SCISSORSBUTTON("scissorsButton.jpg", false);
	
	private ImageIcon _icon;
	private ImageIcon _flippedIcon;
	
	private ImageType(final String url, final boolean flip)
	{
		_icon = new ImageIcon(getClass().getResource(url));
		
		if (flip)
		{
			BufferedImage flippedImage = new BufferedImage(_icon.getIconWidth(), _icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g2d = (Graphics2D) flippedImage.getGraphics();
			
			_icon.paintIcon(null, g2d, 0, 0);
			g2d.dispose();
			
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -flippedImage.getHeight());
			
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			flippedImage = op.filter(flippedImage, null);
			
			tx = AffineTransform.getScaleInstance(-1, -1);
			tx.translate(-flippedImage.getWidth(), -flippedImage.getHeight());
			
			op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			flippedImage = op.filter(flippedImage, null);
			
			_flippedIcon = new ImageIcon(flippedImage);
		}
	}
	
	public ImageIcon getIcon()
	{
		return _icon;
	}
	
	public ImageIcon getFlippedIcon()
	{
		return _flippedIcon;
	}
}