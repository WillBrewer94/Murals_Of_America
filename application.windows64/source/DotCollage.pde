import g4p_controls.*;

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

void setup() {
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

void draw() {
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

void drawMap() {
  for(int i = 0; i < stateList.length; i++) {
    stateList[i].displayDots();
  }
  
  for(int i = 0; i < stateList.length; i++) {
    stateList[i].displayState();
    stateList[i].overCircle();
  }
}

void createStates() {
  for(int i = 0; i < stateList.length; i++) {
    stateList[i] = new State(stateLocX[i], stateLocY[i], 10, stateNames[i]);
  }
}

//VERY big calculation
void createDots() {
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

void mousePressed() {
  boolean isOver = false;
  
  for(int i = 0; i < stateList.length; i++) {
    isOver = stateList[i].getIsOver();
    
    if(isOver) {
      currState = stateList[i];
      i = stateList.length;
    }
  }
}

void displaySidebar() {
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

void handleButtonEvents(GButton button, GEvent event) {
  if(button == mapButton && event == GEvent.CLICKED) {
    isMap = !isMap;
  }
}



