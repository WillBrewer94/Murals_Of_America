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
