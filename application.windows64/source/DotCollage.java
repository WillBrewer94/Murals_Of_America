import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import g4p_controls.*; 
import processing.core.PImage; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DotCollage extends PApplet {



PImage map;
State currState;

int[] stateLocX;
int[] stateLocY;
String[] stateNames;
State[] stateList;
boolean isMap = true;
PImage currImage;
PFont font1, font2;

GButton mapButton;

public void setup() {
  size(865, 524);
  
  //default currentState
  stateList = new State[48];
  currState = new State(0, 0, 10, "Pick a State");
  
  //state names
  stateNames = new String[] {"Alabama", "Arizona", "Arkansas", "California", "Colorado"
                            , "Connecticut", "Delaware", "Florida", "Georgia", "Idaho"
                            , "Illinois", "Nebraska", "Nevada", "New Hampshire"
                            , "New Jersey", "New Mexico", "New York", "North Carolina"
                            , "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania"
                            , "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine"
                            , "Maryland", "Massachusetts", "Michigan", "Minnesota"
                            , "Mississippi", "Missouri", "Montana", "Rhode Island"
                            , "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont"
                            , "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
                            
  //state x-coordinate locations              
  stateLocX = new int[] {404, 137, 341, 61, 209, 522, 510, 467, 440, 131, 372, 269, 98, 531
                        , 516, 199, 502, 479, 264, 435, 291, 77, 482, 402, 328, 280, 414
                        , 345, 545, 493, 532, 413, 317, 374, 340, 184, 538, 467, 266, 408, 276
                        , 149, 520, 482, 94, 459, 360, 197};
              
  //state y-coordinate locations            
  stateLocY = new int []{361, 332, 339, 280, 282, 225, 264, 412, 361, 201, 271, 253, 263
                        , 197, 249, 341, 212, 316, 175, 263, 333, 194, 247, 270, 247
                        , 292, 302, 393, 172, 264, 213, 223, 197, 366, 296, 174, 222, 342, 214
                        , 323, 385, 271, 195, 290, 150, 279, 213, 228};  
  
  
  //map image
  map = loadImage("usaMAP.jpg"); 
  
  //GUI elements
  G4P.registerSketch(this);
  mapButton = new GButton(this, 10, 490, 70, 20, "Toggle Map"); 
  
  //loads font and title
  font1 = loadFont("BrushScriptMT-48.vlw");
  font2 = loadFont("BrushScriptMT-33.vlw");
  fill(255);
  textFont(font1, 48);
  text("Murals of America", 160, 100);
             
  //loads data and display           
  createStates();
  createDots();
  colorMode(RGB, 255);
  background(0);
}

public void draw() {
  background(0);
  stroke(255);
  strokeWeight(2);
  line(600, 0, 600, 524);
  line(0, 60, 865, 60);
  
  textFont(font1);
  text("Murals of America", 160, 40);
  textFont(font2);
  text(currState.getStateName(), 650, 40);
  
  fill(255);
  rect(0, 61, 599, 464);
  
  if(isMap) {
    image(map, 0, 60);
  }
  
  if(currState.getImages().length == 0) {
     text("No Murals Here", 650, 100);
  }
  drawMap();
  displaySidebar();
}

public void drawMap() {
  for(int i = 0; i < stateList.length; i++) {
    stateList[i].displayDots();
  }
  
  for(int i = 0; i < stateList.length; i++) {
    stateList[i].displayState();
    stateList[i].overCircle();
  }
}

public void createStates() {
  for(int i = 0; i < stateList.length; i++) {
    stateList[i] = new State(stateLocX[i], stateLocY[i], 10, stateNames[i]);
  }
}

//VERY big calculation
public void createDots() {
  println("\nPulling Color Data...");
  println("Please wait a couple minutes for computation to complete.");
  for(int i = 0; i < stateList.length; i++) {
    stateList[i].dotSetup();
    if(i % 8 == 0) {
      float percent = (float)i / 48;
      int percentDone = (int)((double)percent * 100);
      println(percentDone + "% Loaded...");
    }
  }
  
  println("100% loaded...");
  println("Done!");
}

public void mousePressed() {
  boolean isOver = false;
  
  for(int i = 0; i < stateList.length; i++) {
    isOver = stateList[i].getIsOver();
    
    if(isOver) {
      currState = stateList[i];
      i = stateList.length;
    }
  }
}

public void displaySidebar() {
  PImage[] stateImages = currState.getImages();
  
  try {
    for(int i = 0; i < stateImages.length; i++) {
      PImage tempImage = stateImages[i];
      tempImage.resize(150, 80);
      image(tempImage, 660, 70 + 90 * i);
    }
    
  } catch(NullPointerException e) {
    //do nothing
  }
}

public void handleButtonEvents(GButton button, GEvent event) {
  if(button == mapButton && event == GEvent.CLICKED) {
    isMap = !isMap;
  }
}



//ColorPicker algorighm made by cate huston, sourced from catehuston.com/blog
//and modified to work with my program



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

  public int extractColor(PImage imgIn, int hueRangeIn) {
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
public class Dot {
  private int c;
  private float radius = 12;
  private float x, y;
  
  public Dot(int c, float x, float y) {
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
// Each result is used to create a DplaItem
class DplaItem {
  float x, y;
  float diameter;
  String displayTxt;
  boolean over = false;
  private String apiKey = "1b3522e362dd16d481a28f65590aa035";
  JSONObject jsonParent;
  JSONObject sr;
  PImage imgError;

  // Constructor
  public DplaItem(JSONObject j) {
    x = random(0, width);
    y = random(0, height);
    diameter = 10;

    // Original JSONObject
    jsonParent = j;
    
    //loads error image
    imgError = loadImage("imgError.jpg");
    
    // Nested JSONObject that contains useful information
    sr = SourceResource();
    
    // Modify this to determine what is revealed in mouseOver
    displayTxt = getCollectionTITLE();
  }

  // Return original URL of item
  public String getItemURL() {
    String shownAt = jsonParent.getString("isShownAt");
    println(shownAt);
    return shownAt;
  }

  // Get inside SourceResource (Where a lot of useful info resides)
  public JSONObject SourceResource() {
    JSONObject source = jsonParent.getJSONObject("sourceResource");
    return source;
  }

  // Return Title
  public String getTitle() {
    try {
      return sr.getString("title");
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // Return Description
  public String getDescription() {
    try {
      return sr.getString("description");
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // Return Format
  public String getFormat() {
    try {
      return sr.getString("format");
    }     
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // Return Collection Name
  public String getCollectionNAME() {
    try {
      return sr.getJSONObject("collection").getString("name");
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // Return Provider Name
  public String getProviderName() {
    try {
      return sr.getJSONArray("provider").toString();
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }
  
  public String getType() {
    try {
      return "";
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // Return CollectionID
  public String getCollectionID() {
    try {
      return sr.getJSONObject("collection").getString("id");
    }
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }
  
  public PImage getImage() {
    try {
      return loadImage(jsonParent.getString("object"), "jpg");
    } catch(Exception e) {
      return imgError;
    }
  }

  // Return Title
  public String getCollectionTITLE() {
    try {
      return sr.getJSONObject("collection").getString("title");
    } 
    catch (Exception e) {
      String s = "NA";
      return s;
    }
  }

  // CHecking if mouse is over the Entry
  public void rollover(float px, float py) {
    float d = dist(px, py, x, y);
    if (d < diameter/2) {
      over = true;
    } else {
      over = false;
    }
  }
}

public class SearchQuery {

  //Please put in your own api_key here. This page explains how you get one: http://dp.la/info/developers/codex/policies/#get-a-key
  private String apikey = "1b3522e362dd16d481a28f65590aa035";

  private String searchQuery;
  private String searchFilter;
  private int numPages;

  // Constructor
  public SearchQuery(String qu, int n, String stateName) {
    searchQuery = qu;
    numPages = n;
    
    println(stateName + " loading...");
    stateName = stateName.replaceAll(" ", "+");
    
    //Use this filter to narrow your search. This API page is going to be your biggest help: http://dp.la/info/developers/codex/requests/
    searchFilter = "sourceResource.type=image"
                 + "&sourceResource.subject.name=Mural+painting+and+decoration+AND+Art"  
                 + "&sourceResource.spatial.state=" 
                 + stateName + "&q=";
  }

  // Search function
  public JSONArray search() {
    String queryURL = "";

    //Modify search query here. You will need to string query parameters together to get the JSON file you want.
    queryURL = "http://api.dp.la/v2/items?" + searchFilter + searchQuery + "&api_key=" + apikey + "&page_size=" + numPages;

    //println("Search: " + queryURL);

    JSONObject dplaData = loadJSONObject(queryURL);
    
    JSONArray results = dplaData.getJSONArray("docs");  

    return results;
  }
}
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
  
  public void createDot(int c) {
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
  
  public void loadData() {
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
  
  public void loadImages() {
    picList = new PImage[entries.length];
   
    for(int i = 0; i < picList.length; i++) {
      picList[i] = entries[i].getImage();
    }
  }
  
  public void overCircle() {
    float disX = locX - mouseX;
    float disY = locY - mouseY;
    
    if(sqrt(sq(disX) + sq(disY)) < diameter / 2) {
      isOver = true;
    } else {
      isOver = false;
    }
  }
  
  public boolean getIsOver() {
    return isOver;
  }
  
  public PImage[] getImages() {
    return picList;
  }
  
  public String getStateName() {
    return stateName;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DotCollage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
