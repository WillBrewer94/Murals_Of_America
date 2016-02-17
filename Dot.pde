public class Dot {
  private color c;
  private float radius = 12;
  private float x, y;
  
  public Dot(color c, float x, float y) {
    this.c = c;
    this.radius = radius;
    this.x = x;
    this.y = y;
  }
  
  public void display() {
    stroke(c);
    fill(c);
    ellipse(x, y, radius, radius);
  }
}
