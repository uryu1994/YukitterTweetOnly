package application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;

public class ThumbnailBuilder {
	private static int width;
	private static int height;
	private static final int standardSize = 90;
	
	public static Image getThumbnailImage(URL url) {
		BufferedImage resizedImage = null;
		try {
			BufferedImage loadImage = ImageIO.read(url);
			System.out.println(url.toExternalForm());
			Image image = new Image(url.toExternalForm());
			
			width = (int) image.getWidth();
			height = (int) image.getHeight();
			
			System.out.println("        width = [" + width + "] /         height = [" + height + "]");
			//縦横の縮小倍率を求める
		    double magnification = ((double)standardSize / width);
		    width  = (int)(image.getWidth() * magnification);
		    height = (int)(image.getHeight() * magnification);
			System.out.println("resized width = [" + width + "] / resized height = [" + height + "]");
			
		    //-- サムネイル用のイメージを作成 --//
			resizedImage = new BufferedImage(width, height, loadImage.getType());
	    	resizedImage.getGraphics().drawImage(loadImage, 0, 0, width, height, null);
	    	
/*
			Graphics2D g2d = image.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
			g2d.drawImage(image, 0, 0, dw, dh, null); 
*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SwingFXUtils.toFXImage(resizedImage, null);
	}
}
