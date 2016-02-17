//ColorPicker algorighm made by cate huston, sourced from catehuston.com/blog
//and modified to work with my program

import processing.core.PImage;

@SuppressWarnings("serial")
public class ColorPicker {
  private PImage img;
  private float hue;
  private int hueRange; 
  private float saturation;
  private float brightness;
  
  public ColorPicker() {
    hueRange = 360;
  }
  
  public ColorPicker(int hueRange) {
    this.hueRange = hueRange;
  }

  public color extractColor(PImage imgIn, int hueRangeIn) {
    img = imgIn;
    hueRange = hueRangeIn;
    colorMode(HSB, (hueRange - 1));
    try {
      img.loadPixels();
      
      int numberOfPixels = img.pixels.length;
      int[] hues = new int[hueRange];
      float[] saturations = new float[hueRange];
      float[] brightnesses = new float[hueRange];
    
      for(int i = 0; i < numberOfPixels; i++) {
        int pixel = img.pixels[i];
        int hue = Math.round(hue(pixel));
        float saturation = saturation(pixel);
        float brightness = brightness(pixel);
        hues[hue]++;
        saturations[hue] += saturation;
        brightnesses[hue] += brightness;
      }
    
      // Find the most common hue.
      int hueCount = hues[0];
      int hue = 0;
      for (int i = 1; i < hues.length; i++) {
         if (hues[i] > hueCount) {
          hueCount = hues[i];
          hue = i;
        }
      }
    
      // Set the vars for displaying the color.
      this.hue = hue;
      saturation = saturations[hue] / hueCount;
      brightness = brightnesses[hue] / hueCount;
      
      return color(hue, saturation, brightness);
    } catch(NullPointerException e) {
      return color(0, 0, 0);
    }
  }
  
  public void setHueRange(int hueRange) {
    this.hueRange = hueRange;
  }
  
  public void setImage(PImage img) {
    this.img = img;
  }
}
