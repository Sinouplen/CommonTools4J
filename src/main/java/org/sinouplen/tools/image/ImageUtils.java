/**
 * 
 */
package org.sinouplen.tools.image;

import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.sun.media.jai.codec.SeekableStream;

/**
 * @author Sinouplen
 * 
 */
public final class ImageUtils {

	private ImageUtils() {

	}

	public static BufferedImage read(File file) throws IOException {
		BufferedImage bufferedImage = null;

		try {
			// We try it with ImageIO
			bufferedImage = ImageIO.read(ImageIO
					.createImageInputStream(new FileInputStream(file)));
		} catch (CMMException ex) {
			ex.getCause();
		}

		if (bufferedImage == null) {
			RenderedOp renderedOp = JAI.create("stream", SeekableStream
					.wrapInputStream(new FileInputStream(file), true));

			if (renderedOp != null) {
				bufferedImage = renderedOp.getAsBufferedImage();
			}
		}

		return bufferedImage;
	}
}
