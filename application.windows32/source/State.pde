public class State {
  private int locX;
  private int locY;
  private int diameter;
  private String stateName;
  private String apikey = "1b3522e362dd16d481a28f65590aa035";
  private boolean isOver = false;
  
  private DplaItem[] entries;
  private PImage[] picList;
  private JSONArray json;
  private ArrayList<Dot> dots;
  
  public State(int locX, int locY, int diameter, String stateName) {
    this.locX = locX;
    this.locY = locY;
    this.stateName = stateName;
    this.diameter = diameter;
    
    loadData();
    loadImages();
    
    dots = new ArrayList<Dot>();
    entries = new DplaItem[5];
  }
  
  //analyzes pixels of each image list, and creates colored dots around the image center
  public void dotSetup() {
    ColorPicker cPick = new ColorPicker();
    
    for(int i = 0; i < picList.length; i++) {
      createDot(color(cPick.extractColor(picList[i], 360)));
      createDot(color(cPick.extractColor(picList[i], 150)));
      createDot(color(cPick.extractColor(picList[i], 80)));
      createDot(color(cPick.extractColor(picList[i], 10)));
      
    }
  }
  
  void createDot(color c) {
    float x = random(-30, 30) + locX;
    float y = random(-30, 30) + locY;
    
    dots.add(new Dot(c, x, y));
  }
  
  public void displayState() {
    stroke(0);
    
    if(isOver) {
      fill(0);
      strokeWeight(6);
    } else {
      fill(255);
      strokeWeight(3);
    }
    
    ellipse(locX, locY, diameter, diameter);
  }
  
  public void displayDots() {
    if(isOver) {
      stroke(0);
      strokeWeight(3);
    } else {
      stroke(255);
      strokeWeight(1);
    }
    
    for(int i = 0; i < dots.size(); i++) {
      dots.get(i).display();
    }
  }
  
  void loadData() {
    //Enter a search term here as well as number of pages  
    SearchQuery mySearch = new SearchQuery("art", 5, stateName);
    
    // Handle search results here
    JSONArray JSONresults = mySearch.search();
    entries = new DplaItem[JSONresults.size()];
  
    // Use search results to fill array of JSONObjects
    for (int i = 0; i < JSONresults.size (); i++) {
      JSONObject rec = JSONresults.getJSONObject(i);
      DplaItem di = new DplaItem(rec);
      entries[i] = di;
    }
  }
  
  void loadImages() {
    picList = new PImage[entries.length];
   
    for(int i = 0; i < picList.length; i++) {
      picList[i] = entries[i].getImage();
    }
  }
  
  void overCircle() {
    float disX = locX - mouseX;
    float disY = locY - mouseY;
    
    if(sqrt(sq(disX) + sq(disY)) < diameter / 2) {
      isOver = true;
    } else {
      isOver = false;
    }
  }
  
  boolean getIsOver() {
    return isOver;
  }
  
  PImage[] getImages() {
    return picList;
  }
  
  String getStateName() {
    return stateName;
  }
}
