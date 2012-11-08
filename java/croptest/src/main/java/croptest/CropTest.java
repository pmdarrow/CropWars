package croptest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class CropTest {
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();

		System.out.println(new File(".").getCanonicalPath());

		File image = new File("src/main/java/croptest/image.bmp");

		for (int i = 0; i < 24; i++) {
			BufferedImage img = ImageIO.read(image);
			Random r = new Random();
			int w = img.getWidth();
			int h = img.getHeight();

			for (int j = 0; j < 1000; j++) {
				int l = r.nextInt(w - 50);
				int t = r.nextInt(h - 50);

				BufferedImage out = img.getSubimage(l, t, 50, 50);

				String filename = String.format("cropped/img-%s.jpg", (i + 1)
						* (j + 1));
				ImageIO.write(out, "jpg", new File("src/main/java/croptest/"
						+ filename));

			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Time: " + ((endTime - startTime) / 1000) + " sec");
	}
}
