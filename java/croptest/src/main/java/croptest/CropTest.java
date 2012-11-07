package croptest;

import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.imgscalr.AsyncScalr;

public class CropTest 
{
	public static final String baseDir = "/Users/peter/Code/croptest/";
	
	public static void chop(BufferedImage src, int numCropX, int numCropY, String outPrefix)
	{
		int cropWidth = src.getWidth() / numCropX;
		int cropHeight = src.getHeight() / numCropY;
		int x = 0;
		int y = 0;
		for (int i = 1; i <= numCropY; i++) {
			System.out.println("Row " + i + " of 100");
			for (int j = 1; j <= numCropX; j++) {
				BufferedImage cropped = Scalr.crop(src, x, y, cropWidth, cropHeight);
				String filename = String.format("cropped/%simg%s-%s.bmp", outPrefix, i, j);
				try {
					ImageIO.write(cropped, "bmp", new File(baseDir + filename));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void asyncChop(BufferedImage src, int numCropX, int numCropY, String outPrefix)
	{
		int cropWidth = src.getWidth() / numCropX;
		int cropHeight = src.getHeight() / numCropY;
		int x = 0;
		int y = 0;
		for (int i = 1; i <= numCropY; i++) {
			System.out.println("Row " + i + " of 100");
			for (int j = 1; j <= numCropX; j++) {
				
				try {
					BufferedImage cropped = AsyncScalr.crop(src, x, y, cropWidth, cropHeight).get();
					String filename = String.format("cropped/%simg%s-%s.bmp", outPrefix, i, j);
					ImageIO.write(cropped, "bmp", new File(baseDir + filename));
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ImagingOpException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void singleThreadedChop() {
		for (int i = 1; i <= 24; i++){
			try {
				BufferedImage img = ImageIO.read(new File(baseDir + "image.bmp"));
				chop(img, 100, 100, "s-" + i + "-");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void asyncChop() {
		for (int i = 1; i <= 24; i++){
			try {
				BufferedImage img = ImageIO.read(new File(baseDir + "image.bmp"));
				chop(img, 100, 100, "s-" + i + "-");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
    public static void main(String[] args)
    {
    	System.setProperty("imgscalr.async.threadCount", "8");
    	long startTime = System.currentTimeMillis();
		singleThreadedChop();
		long endTime = System.currentTimeMillis();
		System.out.println("singleThreadedChop: " + ((endTime - startTime) / 1000) + " sec");
    }
}
