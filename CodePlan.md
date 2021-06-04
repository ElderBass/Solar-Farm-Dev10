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
* **Overall** I plan to use code from other similar projects as a base and refactor for our purposes here instead of writing it all from scratch

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

### Custom DataAccessException
* Exactly what it sounds like - a custom checked exception that extends Exception which we will use to handle any operations that go awry in handling/accessing our data
### PanelRepository Interface
* Interface that will act as a dependency for **PanelFileRepository**
* This will contain all the relevant CRUD methods that the FileRepository will use:


`List<Panel> findAll() throws DataAccessException;`

`Panel add(Panel panel) throws DataAccessException;`

`Panel findById(int panelId) throws DataAccessException;`

`boolean update(Panel panel) throws DataAccessException;`

`boolean deleteById(int panelId) throws DataAccessException;`

### PanelFileRepository
* Implements PanelRepository and all its CRUD methods
* Fleshes out the CRUD methods to actually perform the correct actions
* **CRUD Methods** = findAll(), findPanelsBySection(), addPanel(), updatePanel(), and deletePanel()
    * Have a TEST file to match this file to test all the CRUD methods are working properly!
    * Work on testing simultaneously - write a method in Repository --> Go test it out
* Also contains some helper methods required for printing and saving data (taken from other projects)
    * a **writeAll()** method for writing new or updated panels to the solar-panels.csv file
    * a **getNextId()** method for generating a new unique ID for an added Panel
    * a **serialize()** method for converting a Panel entry into a .csv line
* Important Note: Have to ensure that writeAll() works correctly and that we call it in the relevant methods so as to keep our solar-panels.csv up to date and save progress made, so if the user comes back, all of the changes from last session will be reflected in the new session

### Testing
* Need to test all of our CRUD methods, both positively and negatively, to ensure they are working as expected
  * e.g. for adding a new Panel, must make sure it has all required fields, is not null (both negative) and successfully adds a validated Panel (positive)
* We will need to establish Known Good State using two files - a seed file (solar-panels-seed.csv) with some dummy data, and a test file (solar-panels-test.csv)
  * Run a **@BeforeEach setUp()** method inside PanelFileRepositoryTest that copies the seed data into the test file
    * This ensures we have reliable data that resets every time we run a new test
* Fields required in PanelFileRepositoryTest:
  * `static final String SEED_FILE_PATH = "./data/solar-panels-seed.csv";`
  * `static final String TEST_FILE_PATH = "./data/memories-test.txt";`
  * `static final PanelFileRepository respository = new PanelFileRepository(TEST_FILE_PATH);`

## Domain Layer
* This is where we will validate all the inputs from the user to ensure that the fields for a new Panel are playing by the established rules
* Consists of a **PanelResult** class and a **PanelService** class
### PanelResult Class
* Used as a dependency for the PanelService class to help validate user inputs and ensure that CRUD actions are being performed correctly
#### Fields:
    private ArrayList<String> messages = new ArrayList<>();
    private Panel payload;

* **messages** will consist of any error messages we run into while attempting to validate input and execute methods
* **payload** we will set as a Panel that has been added or updated successfully 
#### Methods:
* Method for adding error messages into the messages field
* Getter and Setter for the payload field
* isSuccess() checks the size of messages and returns a boolean based on if the size == 0 or not
  * This will be used inside PanelService to check if the methods executed without error or not

### PanelService Class
* Used to check inputs from the user and assure that the inputs are valid and within the constraints of the rules
#### Fields:
* Only field will be an instance of PanelRepository (leave it as the interface for maximum flexibility) named repository

#### Methods:
* Have methods to match the methods inside the PanelRepository PanelService uses as a dependency/field
  * findAll(), findPanelsBySection(), findById(), add, update, and deletePanel()
  * These methods will call the Repository methods using the repository field for this class
    * If called successfully, returns an empty Result object that will ultimately get passed to the Controller
    * If unsuccessful, returns a Result object with messages indicating what went wrong
    * Add and Update will also call other Validating methods to ensure the Panel data is correct
* Additional Methods for Validating inputs
  * Main one needed for validating the addition/updating of a panel
    * Panel cannot be null, cannot be missing required fields, row/col must be <= 250
    * NO DUPLICATE PANELS: pretty simple, just check a list of all the panels for a match, i.e.
      * if (array.get(i).getSection.equals(newPanel.getSection) &&  array.get(i).getRow == newPanel.getRow && array.get(i).getCol == newPanel.getCol) { duplicate --> reject }
* These methods will add messages to an instance of PanelResult if errors happen, and those messages will get passed back to the controller
  * Controller checks for messages, and if there are none, that indicates the action was performed successfully

### Testing
* For testing the service, we must use a **RepositoryDouble** which is basically just a copy of our PanelFileRepository and implements the PanelRepository interface
  * This will help establish known good state while keeping our actual data repository separate from the service
  * The double itself will have in its constructor some dummy data for establishing state
* This test file will be much more rigorous than the PanelFileRepository test as we have to make sure the methods are checking for bad data correctly, necessitating far more test cases
  * e.g. we have to make sure int/col are <= 250, checking to make sure no duplicates are added, ensuring required fields are filled out, etc

## UI Layer
* Consists of View and Controller classes - this is where all of our menus will be printed to the console, user input gathered on choices/actions to perform and then passed to the Service
### View
* Main portal for the user to interact with the application
* Will need methods for displaying menus, displaying data, and capturing user choices and inputs and navigating menus
* **Fields**
  * Only field needed for view is a Scanner instance for capturing user inputs

#### Methods:
* Will need methods for initiating certain CRUD operations, such as making a new Solar Panel and updating existing ones
* Need methods for displaying all panels, displaying panels by section, displaying the menu of options and grabbing the user choice
* Helper methods taken from other projects (Explore Venus and Unexplained Encounters mostly) for capturing user inputs in the right format
  * e.g. readInt() --> prompts user for an integer (may require it within a certain min-max range) and stays in a loop until the user correctly enters an integer
  * others include readString(), readRequiredString(), readSection() (for viewing Panels by section)
  * Also have a displayHeader() method for each of the different menu options (prints a string to console declaring what menu/submenu user is in)
  * printResult() method --> prints out any error messages return from the PanelResult during validation, or a "Success" message if no error messages exist
### Controller
* This is the go-between for the View and our PanelService classes
* As such, it will have two fields: a **private final View view** field and a **private final PanelService service** field
* The Controller will use methods from both of these Classes to execute the CRUD operations as selected by the user in View, sending data along to Service and getting back success/failure messages

#### Methods:
* **run()** and **runMenuLoop()** will be first and foremost here
  * run() will call runMenuLoop() in a try/catch block
  * runMenuLoop() will continue printing the menu asking the user for an action until the user selects "Exit" to leave the program
    * It will call a method from view that prompts a user for a menu choice and returns that choice back as an int
    * runMenuLoop() then takes that int and evaluates it with a switch statement to call the relevant method for that operation
* Methods for performing all the operations in aforementioned switch statement
  * i.e. need methods for creating a solar panel, updating a panel, deleting a panel, displaying all panels, and displaying panels by section
  * general formula for these methods is calling a method from view to initiate the action, then passing that along into a Service method, which returns a result, then passing that result back to the View
## Tasks
* Flesh out skeletons of all classes, starting with Panel model (e.g. writing out fields, cosntructors/getters/setters, declaring methods without writing them, etc)
  * Time estimate: 60 minutes
* Flesh out Seed file with some dummy data - might try and make it fairly exhaustive
  * Time estimate: 15-20 minutes
* Go through CRUD methods one by one and work out the whole path from Data through domain to UI
  * May even take a "squeeze" approach where I start with Repository, then move to View/Controller, before getting to the Service/Result
  * Most likely will start in Data Layer, moving up to Result/Service
  * Each of these cycles will likely take at least 2 hours
    * Start with findAll() and displaying them, then do add, then update, then delete
    * work in findById() as well
* Test Repository Methods positively and negatively
  * Time estimate: 70 minutes
* Test Service methods positively and negatively
  * Time estimate: 70 minutes
* Test out UI functionality once all other methods are for sure working
  * Time estimate: 40 minutes
* Document Bugs along the way and debug them (either immediately or later)
  * Time estimate for total debugging: 2 - 6 hours (depending on nature and breadth of bugs)