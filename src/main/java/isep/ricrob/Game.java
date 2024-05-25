package isep.ricrob;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.*;

import static isep.ricrob.Game.Status.*;
import static isep.ricrob.Token.Color.*;


public class Game {


    // * Instance globale de gestion du jeu

    public static Game context;

    public static void start() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Impossible de lancer plusieurs fois la partie...");
        }
        Game.context = new Game();
        Game.context.setStatus(CHOOSE_PLAYER);
    }

    // * ---

    // Taille du plateau (SIZE x SIZE)
    public static final int SIZE = 16;

    // Constructeur privé (instance unique créée par "start()" )
    private Game() {

        board = new Tile[SIZE][SIZE];

        robots = new HashMap<>();
        robots.put(RED, new Token(RED));
        robots.put(GREEN, new Token(GREEN));
        robots.put(BLUE, new Token(BLUE));
        robots.put(YELLOW, new Token(YELLOW));

        Token.Color[] colors = Token.Color.values();
        int randomColorIndex = ( new Random() ).nextInt( colors.length );
        target = new Token( colors[randomColorIndex] );

    }
    public Token getrobotobj (Token.Color color){
        this.robots.get(color);
        return (Token) robots;
    }

    // * Gestion des événements du jeu

    public void processSelectPlayer() {
        if (this.status == CHOOSE_PLAYER) {
            // Action suivante attendue : choisir la case cible
            setStatus(CHOOSE_ROBOT);
        }
    }

    public void processSelectRobot(Token.Color color) {
        if (this.status == CHOOSE_ROBOT) {
            this.selectedRobot = this.robots.get(color);
            // Action suivante attendue : choisir la case cible
            setStatus(CHOOSE_TILE);
        }
    }

    public void gameended (){
        setStatus(CHOOSE_PLAYER);
    }

    String walls [][] = {{"c","c","c","0 -1","0 1","c","c","c","c","c","0 -1","0 1", "c","-1 0","c","c"}, // This 2d array showing walls and direction which they block , format for showing wall in each array  "top/bottom left/right"
            {"c", "c","c","c","c","-1 0","c","c","c","c","c","c","0 -1","+1 +1","c","-1 0"}, // for top direction the value will be 1 and for bottom value will be -1
            {"c", "c","c","c","c","+1 -1","0 +1","c","c","c","c","c","c","c","c","1 0"}, // for left value wil be 1 and for right value will be -1
            {"c", "c","-1 0","c","c","c","c","c","c","c","-1 -1","0 +1","c","c","c","c"},
            {"-1 0","0 -1","+1 +1","c","c","c","c","c","c","c","+1 0","c","c","0 -1","-1 +1","c"},
            {"1 0","c","c","c","c","c","c","-1-1","0 +1","c","c","c","-1 0","c","+1 0","c"},
            {"0 -1","-1 +1","c","c","c","c","c","+1 0","c","c","c","c","+1 -1","0 +1","c","c"},
            {"c", "+1 0","c","c","c","c","c","c","c","c","c","c","c","c","c","c"},
            {"c", "c","c","c","c","c","c","c","c","c","c","c","c","c","c","c"},
            {"c", "c","0 -1","-1 +1","c","c","-1 0","c","c","c","c","c","c","-1-1","0 +1","c"},
            {"-1 0","c","c","+1 0","c","0 -1","+1 +1","c","c","c","c","c","c","+1 0","c","c"},
            {"1 0","c","c","c","c","c","c","-1 0","c","0 -1","-1 +1","c","c","c","c","c"},
            {"c", "-1 0","c","c","c","c","c","+1 -1","0 +1","c","+1 0","c","c","c","c","-1 0"},
            {"c", "+1 -1","0 +1","c","c","c","c","c","c","c","c","-1 0","c","c","0 -1","+1 +1"},
            {"c", "c","c","-1 -1","0 +1","c","c","c","c","c","c","+1 -1","0 +1","c","c","c"},
            {"c", "c","c","+1 -1","0 1","c","c","c","c","c","c","c","0 -1","0 1", "c","c"}};

    int robotpositiononboard[][] = new int[4][4];


    public int findwallsinpath(int cord, int lig,int robotaxis,int robotdirection ,int robotcurrentcol,int robotcurrentlig) { // This Function checks wall in path of robot
        String[][] tempwalls = new String[16][16];
        for (int i = 0 ; i < tempwalls.length;i++){
            for (int j = 0; j < tempwalls.length; j++){
                tempwalls[i][j] = walls[i][j];
            }
        }
        System.out.println(tempwalls.length + " " + tempwalls[0].length);
        Map<Token.Color, Token> pos = getRobots();
        Set set=pos.entrySet();//Converting to Set so that we can traverse
        Iterator itr=set.iterator();
        int count = 0;
        while(itr.hasNext()){
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry entry=(Map.Entry)itr.next();
            Token robot = (Token) entry.getValue();
            if(robot != Game.context.getSelectedRobot()) {
                int rlig = robot.getLig();
                int rcol = robot.getCol();
                tempwalls[rlig][rcol] = "b";
            }


        }

        int movecounter = 0;
        if (robotaxis == 0) {
            if (robotdirection > 0) {
                for (int i = robotcurrentcol; i > 0; i--) {
                        if (tempwalls[robotcurrentlig][i].equals("b"))
                            return i +1 ;
                    if (!(walls[robotcurrentlig][i].equals("c"))) {
                        String wall[] = walls[robotcurrentlig][i].split(" ");
                        if (robotdirection == Integer.parseInt(wall[1])) {
                            return i;
                        }
                    }

                }
                return 0;
            }
            if (robotdirection < 0) {
                for (int i = robotcurrentcol; i < walls.length; i++) {
                    if (tempwalls[robotcurrentlig][i].equals("b"))
                        return i -1 ;
                    if (!(walls[robotcurrentlig][i].equals("c"))) {
                        String wall[] = walls[robotcurrentlig][i].split(" ");
                        if (robotdirection == Integer.parseInt(wall[1])) {
                            return i;
                        }
                    }

                }
                return walls.length-1;
            }
        }
        else if (robotaxis == 1) {
            if (robotdirection > 0) {
                for (int i = robotcurrentlig; i > 0; i--) {
                    if (tempwalls[i][robotcurrentcol].equals("b"))
                        return i +1 ;
                    if (!(walls[i][robotcurrentcol].equals("c"))) {
                        String wall[] = walls[i][robotcurrentcol].split(" ");
                        if (robotdirection == Integer.parseInt(wall[0])) {
                            return i;
                        }
                    }

                }
                return 0;
            } else if (robotdirection < 0) {
                for (int i = robotcurrentlig; i < walls.length; i++) {
                    if (tempwalls[i][robotcurrentcol].equals("b"))
                        return i -1 ;
                    if (!(walls[i][robotcurrentcol].equals("c"))) {
                        String wall[] = walls[i][robotcurrentcol].split(" ");
                        if (robotdirection == Integer.parseInt(wall[0])) {
                            return i;
                        }
                    }

                }
                return walls.length-1;
            }

        }
        return 0;
    }

    public String processSelectTile(int col, int lig) {
        String walls [][]   = {};

        if (this.status == CHOOSE_TILE) {
            if (
                    (this.selectedRobot.getCol() != col)
                &&
                    (this.selectedRobot.getLig() != lig)
            ) {
                return "Les déplacements en diagonale sont interdits";
            } else {
                int robotlastcol = this.selectedRobot.getCol(); // getting current col of robot
                int robotlastlig = this.selectedRobot.getLig();// getting current lig of robot
                int calculateDirectioncol = robotlastcol - col; // This calculates the  direction of the robot (up or down), -ive value for down , +ive for up
                int calculateDirectionlig = robotlastlig - lig;// This calculates the  direction of the robot (left or right)-ive value for right , +ive for leftr
                if(robotlastcol != col) {
                    if (calculateDirectioncol < 0) {
                        col = findwallsinpath(col,lig,0,-1,robotlastcol,robotlastlig);
                    }
                    else if (calculateDirectioncol > 0)
                        col =  findwallsinpath(col,lig,0,1,robotlastcol,robotlastlig);
                }
                else {
                    if (calculateDirectionlig < 0)
                        lig = findwallsinpath(col,lig,1,-1,robotlastcol,robotlastlig);
                    else if (calculateDirectionlig > 0)
                        lig = findwallsinpath(col,lig,1,1,robotlastcol,robotlastlig);
                }
                this.selectedRobot.setPosition(col,lig);
                // Action suivante attendue : choisir un robot
                setStatus(CHOOSE_ROBOT);
                return "MOVE";
            }
        }
        return null;
    }

    public void resetrobotPos(int[][] initialpos,String robotC[]) {
        Map<Token.Color, Token> pos = Game.context.getRobots();
        Set set = pos.entrySet();//Converting to Set so that we can traverse
        Iterator itr = set.iterator();
        int count = 0;
        Token robot[] = new Token[4];
        Token.Color robotcolor[] = new Token.Color[4];
        while (itr.hasNext()) {
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry entry = (Map.Entry) itr.next();
            robot[count] = (Token) entry.getValue();
            robotcolor[count++] = (Token.Color) entry.getKey();
        }
        for (int i = 0; i < initialpos.length; i++) {
            String c = String.valueOf(robot[i].getColor());
            for (int j = 0; j < robotcolor.length; j++) {
                if (c.equals(robotC[j])) {
                    robot[i].setPosition(initialpos[j][1], initialpos[j][0]);
                    break;
                }


            }
        }

    }
    // * ---

    // * Etat courant du jeu

    public enum Status {
        CHOOSE_PLAYER("Cliquez sur le bouton [Jouer]"),
        CHOOSE_ROBOT("Cliquez sur le robot à déplacer"),
        CHOOSE_TILE("Cliquez sur la case destination");
        Status(String toolTip) { this.toolTip = toolTip; }
        private String toolTip;
        public String getToolTip() { return this.toolTip; }
    }
    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        this.status = status;
        // Mise à jour du libellé d'état sur l'affichage
        StringBuilder statusMessage = new StringBuilder();
        if (playerNameProperty.get() != null) {

            statusMessage.append(playerNameProperty.get());
            statusMessage.append(" : ");

        }

        statusMessage.append( status.getToolTip() );
        this.statusToolTipProperty.set( statusMessage.toString() );
    }

    private Status status;
    // "Binding JFX" - Synchronisation avec "MainController.statusLabel"
    public StringProperty statusToolTipProperty = new SimpleStringProperty();

    // "Binding JFX" - Synchronisation avec "PlayerController.name"
    public StringProperty playerNameProperty = new SimpleStringProperty();

    private Token selectedRobot;
    public Token getSelectedRobot() { return this.selectedRobot; }

    // * ---

    // Le plateau de SIZE x SIZE cases
    private Tile[][] board;

    // Les 4 robots
    private Map<Token.Color, Token> robots;
    public Map<Token.Color, Token> getRobots() { return this.robots; }

    // La cible
    private Token target;
    public Token getTarget() { return this.target; }

}
