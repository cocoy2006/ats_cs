package molab.main.java.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.android.ddmlib.RawImage;
import com.android.monkeyrunner.MonkeyImage;
import com.android.monkeyrunner.adb.AdbMonkeyImage;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtil {
	
	private static final Logger log = Logger.getLogger(ImageUtil.class.getName());

	private static BufferedImage convertSnapshot(BufferedImage image) {
		// Convert the image to ARGB so ImageIO writes it out nicely
		BufferedImage argb = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = argb.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return argb;
	}
	

	/**
	 * convert from {@link RawImage} to {@link String}(data uri) */
	public static String convert(RawImage image) {
		if(image != null) {
			MonkeyImage mImage = new AdbMonkeyImage(image);
			BufferedImage bImage = mImage.createBufferedImage();
			return convert(bImage);
		}
		return null;
	}
	
	/**
	 * convert from {@link BufferedImage} to {@link String}(data uri) */
	public static String convert(BufferedImage bImage) {
		ByteArrayOutputStream baso = new ByteArrayOutputStream();
		bImage = convertSnapshot(bImage);
		try {
			ImageIO.write(bImage, "png", baso);
			return new BASE64Encoder().encode(baso.toByteArray());
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return null;
	}
	
	/**
	 * convert from {@link String}(data uri) to {@link BufferedImage} */
	public static BufferedImage convert(String dataUri) {
		try {
			byte[] bytes = new BASE64Decoder().decodeBuffer(dataUri);
			InputStream in = new ByteArrayInputStream(bytes);
			return ImageIO.read(in);
		} catch (IOException e) {
			log.severe(e.getMessage());
		}
		return null;
	}

	public static void writeToJPGFile(BufferedImage bImage, String file,
			float quality) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(file));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
			param.setQuality(quality, false);
			encoder.setJPEGEncodeParam(param);
			encoder.encode(bImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ImageFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double matches(BufferedImage pre, BufferedImage cur) {
		int width = cur.getWidth();
		int height = cur.getHeight();
		int numDiffPixels = 0;
		// Now, go through pixel-by-pixel and check that the images are the same
		for (int y = 0; y < height; y++) {
			if(height > pre.getHeight()) {
				break;
			}
			for (int x = 0; x < width; x++) {
				if(width > pre.getWidth()) {
					break;
				}
				if (cur.getRGB(x, y) == pre.getRGB(x, y)) {
					numDiffPixels++;
				}
			}
		}
		double numberPixels = (height * width);
		return numDiffPixels / numberPixels;
	}
	
	public static BufferedImage zoom(RawImage rImage) {
		if(rImage != null) {
			MonkeyImage mImage = new AdbMonkeyImage(rImage);
			BufferedImage bImage = mImage.createBufferedImage();
			return zoom(bImage);
		}
		return null;
	}
	
	public static BufferedImage zoom(BufferedImage bImage) {
		if(bImage != null) {
			float screenQ = 0.9f;
			if(bImage.getWidth() <= 320) {
				
			} else if(bImage.getWidth() <= 480) {
				screenQ = 0.7f;
			} else if(bImage.getWidth() <= 700) {
				screenQ = 0.5f;
			} else if(bImage.getWidth() <= 1300){
				screenQ = 0.3f;
			} else {
				screenQ = 0.1f;
			}
			return zoom(bImage, screenQ);
		}
		return null;
	}
	
	public static BufferedImage zoom(BufferedImage bImage, float screenQ) {
		if(bImage != null) {
			int width = (int) (bImage.getWidth() * screenQ);
			int height = (int) (bImage.getHeight() * screenQ);
			return zoom(bImage, width, height);
		}
		return null;
	}
	
	public static BufferedImage zoom(BufferedImage bImage, int width, int height) {
		if(bImage != null) {
			if(bImage.getWidth() == width && bImage.getHeight() == height) {
				return bImage;
			}
			BufferedImage nImage = new BufferedImage(width, height, bImage.getType());
			Graphics g = nImage.getGraphics();
			g.drawImage(bImage, 0, 0, width, height, null);
			g.dispose();
			return nImage;
		}
		return null;
	}

}
