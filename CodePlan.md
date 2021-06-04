# Solar Farm Plan

## Overall Flow
* Start with model - fields and methods
* Move into Data Layer - creating repository and interface dependency for repo
    * Create custom DataAccessException class for this
    * Start by writing methods in repository and TESTING them right after
        * Will likely want/need a seed data file for panels
* Once done with Data Layer --> move on to Domain Layer
    * Create Result class first, flesh out the basic methods
    * Move to Service class after, using methods from the Result
        * TEST these service methods as I make them!
* After Data Layer --> move to UI Layer with View and Controller
    * Work on View and Controller concurrently to flesh out CRUD methods
    * Implement menu options for all the different actions
    * Recycle helper methods from other projects in View

* *NOTE: I likely won't do this as linearly as I outlined and may work on one CRUD route as a time from backend to frontend (e.g. start with adding a Panel, then move to Reading Panels, etc). Then move onto the next
* *ANOTHER NOTE In fact I DO plan on fleshing out each layer bit by, starting from the Data Layer but moving up into View as I go

## Start With Panel and Material Enum

### Fields:
    private String section;
    private int row;
    private int col;
    private int year;
    private Material material;
    private boolean isTracking;

### Methods:
* Empty Constructor
* Constructor with arguments for all fields
* Getters and Setters for all fields

### Material Enum:
* Has Enums for all potential solar panel materials
* String field "abbreviation" for each of the types
* Constructor for Material
* Getter for retrieving the abbreviation

## Data Layer

### PanelRepository Interface
* Interface that will act as a dependency for **PanelFileRepository**