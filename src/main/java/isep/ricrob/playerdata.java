package isep.ricrob;

public  class playerdata { // Player class that stores the moves of each player , along with it inital robot positon are stored and color.
    // it also contains a boolean variable for each player to check if he has reached the goal for not

    public static playerdata pd;
    public int[] move;
    static int[][] rinitialpositions;
    static String[] robotcolor;
    static Boolean[]  meetgoal;

    public playerdata(){
       move  = new int[2];
       rinitialpositions = new int[4][2];
       robotcolor = new String[4];
        meetgoal= new Boolean[2];
        meetgoal[0] = false;
        meetgoal[1] = false;
    }

    public static void createplayer(){// Creates a player data once , it is a static member and method
        pd = new playerdata();
    }
    public void incrementmove(int playerno){
        move[playerno]++;
    } // increment move of the selected player
    public static void goalmet(int playerno){
        meetgoal[playerno] = true;
    } // set goal to true if player has reached goal , this is a setter

    public static Boolean getgoalmet(int playerno){ // to get the boolean value player whether he has reached goal or not
        return meetgoal[playerno];
    }
    public void resetMoveToZero(int playerno){
        move[playerno] = 0;
    } // Resets the move to selected player to zero
    public int getplayermove(int playerno){
        return move[playerno];
    } // retrive no of moves of the player
    public static void saverobotpos(int pos[][],String color[]){ // saved starting position of robots when game is started
        for (int i = 0 ; i < rinitialpositions.length ; i++){
            for (int j = 0 ; j < rinitialpositions[i].length; j++){
                rinitialpositions[i][j]= pos[i][j];
                robotcolor[i] = color[i];

            }
        }
    }
    public static int[][] getsavedrobotpos(){
        return rinitialpositions;
    } // retreive 2d array of saved robot position
    public  static  String [] getrobotcolors(){return robotcolor;} // retrieve 1d array of saved robot colours , used to map wwith 2d arrray of rinitialpositions[][]

}
