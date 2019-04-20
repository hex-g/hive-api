package hive.album.storage;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
  private static int imageSizeInPixels =256;
  public static BufferedImage resizeImageToSquare(BufferedImage inputtedImage) {
    // multi-pass bilinear div 2
    var bufferedImageWithNewSize = new BufferedImage(imageSizeInPixels, imageSizeInPixels, BufferedImage.TYPE_INT_RGB);
    var reSizer = bufferedImageWithNewSize.createGraphics();
    reSizer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    reSizer.drawImage(inputtedImage, 0, 0, imageSizeInPixels, imageSizeInPixels, null);
    reSizer.dispose();
    return bufferedImageWithNewSize;
  }
}
