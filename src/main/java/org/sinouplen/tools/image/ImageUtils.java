/**
 * 
 */
package org.sinouplen.tools.image;

import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.sun.media.jai.codec.SeekableStream;

/**
 * @author Sinouplen
 * 
 */
public class ImageUtils {

	public static BufferedImage read(File file) throws IOException {
		return read(new FileInputStream(file));
	}

	public static BufferedImage read(InputStream inputStream)
			throws IOException {
		BufferedImage bufferedImage = null;

		try {
			// We try it with ImageIO
			bufferedImage = ImageIO.read(ImageIO
					.createImageInputStream(inputStream));
		} catch (CMMException ex) {
			// inputStream.reset();
		}

		if (bufferedImage == null) {
			// inputStream.reset();

			RenderedOp renderedOp = JAI.create("stream",
					SeekableStream.wrapInputStream(inputStream, true));

			if (renderedOp != null) {
				bufferedImage = renderedOp.getAsBufferedImage();
			}
		}

		return bufferedImage;
	}
}
