package hive.album.storage;

import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageUtils {
  private static final String IMAGE_PATTERN = "(.+\\.(gif|png|bmp|jpeg|jpg)$)";
  private ImageUtils(){
  }
  public static boolean validateIfHasAnImageAsExtension(final String image){
    var pattern = Pattern.compile(IMAGE_PATTERN);
    var matcher = pattern.matcher(image);
    return matcher.matches();
  }

  public static BufferedImage resizeImageToSquare(BufferedImage inputtedImage,int imageSizeInPixels) {
    // multi-pass bilinear div 2
    var bufferedImageWithNewSize = new BufferedImage(imageSizeInPixels, imageSizeInPixels, BufferedImage.TYPE_INT_RGB);
    var reSizer = bufferedImageWithNewSize.createGraphics();
    reSizer.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    reSizer.drawImage(inputtedImage, 0, 0, imageSizeInPixels, imageSizeInPixels, null);
    reSizer.dispose();
    return bufferedImageWithNewSize;
  }
  public static BufferedImage generateRandomImage(){
    var img=new BufferedImage(4,4,BufferedImage.TYPE_INT_RGB);
    var maxH=img.getHeight();
    var maxV=img.getWidth();
    for(int vertical=0;vertical<maxV;vertical++){
      for(int horizontal=0;horizontal<maxH;horizontal++){
        int red = (int)(Math.random()*256);
        int green = (int)(Math.random()*256);
        int blue = (int)(Math.random()*256);
        var pixel=(red<<16)|(green<<8)|blue;
        img.setRGB(horizontal,vertical,pixel);
      }
    }
    return img;
  }
  public static BufferedImage generateRandomBWImage(){
    var img=new BufferedImage(9,9,BufferedImage.TYPE_INT_RGB);
    var maxH=img.getHeight();
    var maxV=img.getWidth();
    for(int vertical=0;vertical<maxV;vertical++){
      for(int horizontal=0;horizontal<maxH;horizontal++){
        var pixel=0;
        if(Math.random()<0.7){
          pixel=Integer.MAX_VALUE;
        }else{
          pixel=Integer.MIN_VALUE;
        }
        img.setRGB(horizontal,vertical,pixel);
      }
    }
    return img;
  }
}
