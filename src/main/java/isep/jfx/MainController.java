package isep.jfx;

import isep.ricrob.Game;
import isep.ricrob.playerdata;
import isep.ricrob.Token;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static isep.ricrob.Token.Color.*;

public class MainController {

    public final int TILE_SIZE = 40;

    @FXML
    public GridPane boardPane;

    @FXML
    public Label statusLabel;

    @FXML
    public Label pl1label;

    @FXML
    public  Label pl2label;

    public int playerNO = 0;

    // "initialize()" est appelé par JavaFX à l'affichage de la fenêtre
    @FXML
    public void initialize() {

        // Affichage un message "bloquant"
        showWarning("Ricochet Robots");

        // Construction du plateau
         String[][] tilename = {{"c","c","c","c","lrc","c","c","c","c","c","c","lrc","c","c","c","c"}, // 2d array representing the pattern on the board
                {"c","c","c","c","c","c","c","c","c","c","c","c","c","g2","c","udc"},
                {"c","c","c","c","c","b4","c","c","c","c","c","c","c","c","c","c","c"},
                {"c","c","c","c","c","c","c","c","c","c","b3","c","c","c","c","c","c"},
                {"udc","c","g1","c","c","c","c","c","c","c","c","c","c","c","r1","c"},
                {"c","c","c","c","c","c","c","r2","c","c","c","c","c","c","c","c","c"},
                {"c","y3","c","c","c","c","c","c","c","c","c","c","y4","c","c","c"},
                {"c","c","c","c","c","c","c","c","c","c","c","c","c","c","c","c","c"},
                {"c","c","c","c","c","c","c","c","c","c","c","c","c","c","c","c","c"},
                {"c","c","c","y2","c","c","c","c","c","c","c","c","c","b2","c","c",},
                {"udc","c","c","c","c","c","b1","c","c","c","c","c","c","c","c","c","c"},
                {"c","c","c","c","c","c","c","c","c","c","y1","c","c","c","c","c","udc"},
                {"c","c","c","c","c","c","c","plan","c","c","c","c","c","c","c","c","c"},
                {"c","r4","c","c","c","c","c","c","c","c","c","c","c","c","c","r3","c"},
                {"c","c","c","g3","c","c","c","c","c","c","c","g4","c","c","c","c","c"},
                {"c","c","c","c","lrc","c","c","c","c","c","c","c","c","lrc","c","c","c"}};


        // Creating Player object to save player moves
        playerdata.createplayer();
        pl1label.setTextFill(Color.BLUE);





        // ... "cell.png" doit être placé à la racine de "resources/" (sinon PB)
        for (int col = 0; col < Game.SIZE; col ++) {
            for (int lig = 0; lig < Game.SIZE; lig++) {
                Image tile  ;
                if (tilename[lig][col].equals("c"))
                   tile = new Image("cell.png", TILE_SIZE, TILE_SIZE, false, true);
                else
                    tile = new Image(tilename[lig][col] + ".jpg", TILE_SIZE, TILE_SIZE, false, true);
                ImageView tileGui = new ImageView(tile);
                final int lambdaCol = col;
                final int lambdaLig = lig;
                tileGui.setOnMouseClicked
                        (event -> {
                            String status = Game.context.processSelectTile(lambdaCol, lambdaLig);
                            if ( "MOVE".equals(status)) {
                                updateSelectedRobotPosition();
                            } else if (status != null) {
                                showWarning(status);
                            }
                        });
                boardPane.add(tileGui, col, lig);
            }
        }

        // Ajout des pièces
        addRobot(RED);
        addRobot(GREEN);
        addRobot(BLUE);
        addRobot(YELLOW);

        boardPane.add(
                new ImageView( new Image(
                        Game.context.getTarget().getColor() + "_target.png",
                        TILE_SIZE, TILE_SIZE, false, true
                ) ),
                Game.context.getTarget().getCol(),
                Game.context.getTarget().getLig()
        );

        // "Binding JFX" - Synchronisation du "Label" avec l'état du jeu
        statusLabel.textProperty().bind(Game.context.statusToolTipProperty);
        saverobotpositions(); // Saving initial positions of robot
    }

    // Affiche une boite de dialogue construite avec "SceneBuilder"
    public void showPlayerView(ActionEvent actionEvent) throws IOException {
//        if (Game.context.getStatus() == Game.Status.CHOOSE_PLAYER) {
//            System.out.println("Button is here");
//            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("player-view.fxml"));
//            Scene scene = new Scene(fxmlLoader.load());
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.showAndWait();
//        }
        Game.context.processSelectPlayer();
    }

    public void onNextTurn(ActionEvent actionEvent) { // The method resets the postion of the robots and also changes selected player
//        System.out.println("Old Player NO " + playerNO + " Moves are = " + playerdata.pd.move[playerNO] );
        if(playerNO == 0) { // if Player 1 selected then select player 2
            playerNO = 1;
            pl1label.setTextFill(Color.BLACK);
            pl2label.setTextFill(Color.BLUE);

            Game.context.resetrobotPos(playerdata.getsavedrobotpos(),playerdata.getrobotcolors());
            updateAllRobotPosition();
            if(playerdata.pd.getgoalmet(0)){

            }
            else
                playerdata.pd.resetMoveToZero(playerNO); // Resets the move of her to zero if he has not reached the goal and turn is given to other player



        }
        else if(playerNO == 1) {// else select player 1
            if(playerdata.pd.getgoalmet(0)){

            }
            else
                playerdata.pd.resetMoveToZero(playerNO);// Resets the move of her to zero if he has not reached the goal and turn is given to other player
            pl2label.setTextFill(Color.BLACK);
            pl1label.setTextFill(Color.BLUE);
            Game.context.resetrobotPos(playerdata.getsavedrobotpos(),playerdata.getrobotcolors());
            updateAllRobotPosition();
            ifplayerswon();
            Game.context.gameended();
            return;


        }



        System.out.println("New Player No " + playerNO);
    }

    public void ifplayerswon(){ // This method wil be executed when both players have played the game
        pl2label.setTextFill(Color.BLACK);
        pl1label.setTextFill(Color.BLACK);
        if(playerdata.getgoalmet(0) && playerdata.getgoalmet(1)){
            if(playerdata.pd.getplayermove(0) > playerdata.pd.getplayermove(1))
                System.out.println("Player one won");
            else
                System.out.println("Player Two won");
        }
        if(playerdata.getgoalmet(0) || playerdata.getgoalmet(1) ){
            if(playerdata.getgoalmet(0))
                System.out.println("Player one won");
            else
                System.out.println("Player Two won");
        }
    }


    private void addRobot(Token.Color color) {
        Token robot = Game.context.getRobots().get(color);
        ImageView robotGui = new ImageView( new Image(
                color.name() + "_robot.png",
                TILE_SIZE, TILE_SIZE, false, true
        ) );
        robotGui.setOnMouseClicked
                (event -> Game.context.processSelectRobot(color));
        boardPane.add(robotGui, robot.getCol(), robot.getLig());
        // Association de l' "ImageView" avec le robot stocké dans le jeu
        robot.setGui(robotGui);
    }

    private void updateSelectedRobotPosition() {
        Token robot = Game.context.getSelectedRobot();
        System.out.println(robot.getCol() + " " + robot.getLig());
        GridPane.setConstraints(robot.getGui(), robot.getCol(), robot.getLig());
        if(robot.getCol() == Game.context.getTarget().getCol() && robot.getLig() == Game.context.getTarget().getLig()){
            playerdata.pd.goalmet(playerNO);
            System.out.println(playerNO + " Meet the Goal " + playerdata.pd.getplayermove(playerNO));
        }
        playerdata.pd.incrementmove(playerNO);
    }
    // For updating positions of all robot at once
    private void updateAllRobotPosition(){
        Map<Token.Color, Token> pos = Game.context.getRobots();
        Set set = pos.entrySet();//Converting to Set so that we can traverse
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry entry = (Map.Entry) itr.next();
            Token robot = (Token) entry.getValue();
            GridPane.setConstraints(robot.getGui(), robot.getCol(), robot.getLig());

        }

    }

    private void showWarning(String message) {
        Alert startMessage
                = new Alert(Alert.AlertType.INFORMATION, message);
        startMessage.setHeaderText(null);
        startMessage.setGraphic(null);
        startMessage.showAndWait();
    }

    public static void saverobotpositions() { // This method saves robot positions by getting current position of robots and save them is playerdata.saverobot(int [][],int[])
        Map<Token.Color, Token> pos = Game.context.getRobots();
        Set set = pos.entrySet();//Converting to Set so that we can traverse
        Iterator itr = set.iterator();
        int counter = 0;
        int[][] robotpos = new int[4][2];
        String[] robotcolor = new String[4];
        while (itr.hasNext()) {
            //Converting to Map.Entry so that we can get key and value separately
            Map.Entry entry = (Map.Entry) itr.next();
            Token robot = (Token) entry.getValue();
            String key  = String.valueOf(entry.getKey()) ;

            int rlig = robot.getLig();
            int rcol = robot.getCol();
            robotpos[counter][0] = rlig; // saving lig
            robotpos[counter][1] = rcol; // saving col
            robotcolor[counter] = key;
            counter ++;
            }
        playerdata.saverobotpos(robotpos,robotcolor); // passing 2d array to save all robot initial positions
        for (int i = 0 ; i < robotpos.length ; i++){
            System.out.println( "robot lig  = "+ robotpos[i][0] + "//robot col = " + robotpos[i][1] + "//Color : " + robotcolor[i] + "\n");

        }

    }// End of method SaverobotPositions
}
