import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Puzzle {

    private final String goalState = "b12 345 678";
    private String currentState;
    private int maxNodes = Integer.MAX_VALUE;

    public Puzzle(){
        currentState = goalState;
    }

    public void setState(String state){
        currentState = state;
    }

    public void printState(){
        System.out.println(currentState.charAt(0) + " | " + currentState.charAt(1) + " | " + currentState.charAt(2));
        System.out.println("_________");
        System.out.println(currentState.charAt(4) + " | " + currentState.charAt(5) + " | " + currentState.charAt(6));
        System.out.println("_________");
        System.out.println(currentState.charAt(8) + " | " + currentState.charAt(9) + " | " + currentState.charAt(10));
    }

    public void setMaxNodes(int max){
        maxNodes = max;
    }

    public void move(String direction){
        if(direction.equals("up")){
            currentState = doMoveUp();
        }
        else if(direction.equals("down")){
            currentState = doMoveDown();
        }
        else if(direction.equals("left")){
            currentState = doMoveLeft();
        }
        else if(direction.equals("right")){
            currentState = doMoveRight();
        }
        else System.out.print("Invalid Direction");
    }

    private String doMoveUp() {
        int blank = currentState.indexOf('b');
        if(blank < 3){
            return currentState;
        }
        int target = blank-4;
        char[] c = currentState.toCharArray();

        char temp = c[target];
        c[target] = 'b';
        c[blank] = temp;

        return new String(c);
    }

    private String doMoveDown() {
        int blank = currentState.indexOf('b');
        if(blank > 7){
            return currentState;
        }
        int target = blank+4;
        char[] c = currentState.toCharArray();

        char temp = c[target];
        c[target] = 'b';
        c[blank] = temp;

        return new String(c);
    }

    private String doMoveLeft() {
        int blank = currentState.indexOf('b');
        if(blank == 0 || blank == 4 || blank == 8){
            return currentState;
        }
        int target = blank-1;
        char[] c = currentState.toCharArray();

        char temp = c[target];
        c[target] = 'b';
        c[blank] = temp;

        return new String(c);
    }

    private String doMoveRight() {
        int blank = currentState.indexOf('b');
        if(blank == 2 || blank == 6 || blank == 10){
            return currentState;
        }
        int target = blank+1;
        char[] c = currentState.toCharArray();

        char temp = c[target];
        c[target] = 'b';
        c[blank] = temp;

        return new String(c);
    }

    public void randomizeState(int n){
        Random rand = new Random();
        String[] moves = {"up", "down", "left", "right"};
        for(int i=0; i<n; i++){
            move(moves[rand.nextInt(4)]);
        }
    }

    public void solveAStar(boolean heuristic){

        PriorityQueue<Node> queue = new PriorityQueue<>();

        int n = 0;
        int hn = (heuristic) ? calculateHeuristic1(currentState) : calculateHeuristic2(currentState);
        int gn = 0;
        Node current = new Node(currentState, hn, gn);

        String nextState = doMoveUp();
        if(!nextState.equals(currentState)){
            queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), 1, "up", current));
        }
        nextState = doMoveLeft();
        if(!nextState.equals(currentState)){
            queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), 1, "left", current));
        }
        nextState = doMoveRight();
        if(!nextState.equals(currentState)){
            queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), 1, "right", current));
        }
        nextState = doMoveDown();
        if(!nextState.equals(currentState)){
            queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), 1, "down", current));
        }
        while(!currentState.equals(goalState)  && n < maxNodes){
            current = queue.poll();
            this.setState(current.getState());
            gn = current.getGn();
            nextState = doMoveUp();
            if(!nextState.equals(currentState) || !nextState.equals(current.getParent().getState())){
                queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), gn+1, "up", current));
            }
            nextState = doMoveLeft();
            if(!nextState.equals(currentState) || !nextState.equals(current.getParent().getState())){
                queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), gn+1, "left", current));
            }
            nextState = doMoveRight();
            if(!nextState.equals(currentState) || !nextState.equals(current.getParent().getState())){
                queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), gn+1, "right", current));
            }
            nextState = doMoveDown();
            if(!nextState.equals(currentState) || !nextState.equals(current.getParent().getState())){
                queue.add(new Node(nextState, (heuristic) ? calculateHeuristic1(nextState) : calculateHeuristic2(nextState), gn+1, "down", current));
            }
            n++;
        }
        if(n == maxNodes){
            System.out.println("Max Nodes " + n + " reached. Terminating Search.");
            return;
        }
        System.out.println(getMoves(current));
    }

    private String getMoves(Node node) {
        String result = node.getMove();
        Node n = node.getParent();
        int num = 0;
        while(n != null){
            result = n.getMove() + ", " + result;
            num++;
            n = n.getParent();
        }
        return "Total moves to goal: " + num + "\nMove sequence: " + result;
    }

    public int calculateHeuristic1(String state){
        int result = 0;
        for(int i = 1; i < state.length(); i++){
            if(state.charAt(i) != goalState.charAt(i)){
                result++;
            }
        }
        return result;
    }

    public int calculateHeuristic2(String s){
        int result = 0;
        char[][] state = toMatrix(s);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(state[i][j] == '1'){
                    result += Math.abs(i-0) + Math.abs(j-1);
                }
                else if(state[i][j] == '2'){
                    result += Math.abs(i-0) + Math.abs(j-2);
                }
                else if(state[i][j] == '3'){
                    result += Math.abs(i-1) + Math.abs(j-0);
                }
                else if(state[i][j] == '4'){
                    result += Math.abs(i-1) + Math.abs(j-1);
                }
                else if(state[i][j] == '5'){
                    result += Math.abs(i-1) + Math.abs(j-2);
                }
                else if(state[i][j] == '6'){
                    result += Math.abs(i-2) + Math.abs(j-0);
                }
                else if(state[i][j] == '7'){
                    result += Math.abs(i-2) + Math.abs(j-1);
                }
                else if(state[i][j] == '8'){
                    result += Math.abs(i-2) + Math.abs(j-2);
                }
            }
        }
        return result;
    }

    private char[][] toMatrix(String state){
        String[] first = state.split(" ");
        char[][] result = {first[0].toCharArray(), first[1].toCharArray(), first[2].toCharArray()};
        return result;
    }

    public static void readFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        Puzzle eight = new Puzzle();
        while(sc.hasNextLine()){
            String command = sc.next();
            if(command.equals("setState")){
                eight.setState(sc.next() + " " + sc.next() + " " + sc.next());
            }
            else if(command.equals("printState")){
                eight.printState();
            }
            else if(command.equals("move")){
                command = sc.next();
                eight.move(command);
            }
            else if(command.equals("randomizeState")){
                int n = sc.nextInt();
                eight.randomizeState(n);
            }
            else if(command.equals("solve")){
                command = sc.next();
                if(command.equals("A-star")){
                    command = sc.next();
                    if(command.equals("h1")){
                        eight.solveAStar(true);
                        System.out.println("poopy");
                    }
                    else{
                        eight.solveAStar(false);
                    }
                }
                else if(command.equals("beam")){

                }
                else{
                    System.out.println("Invalid input: " + command);
                }
            }
            else if(command.equals("maxNodes")){
                int n = sc.nextInt();
                eight.setMaxNodes(n);
            }
            else{
                System.out.println("Invalid input: " + command);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Puzzle eight = new Puzzle();
        readFile("src/test.txt");
        //eight.randomizeState(5000);
        //eight.solveAStar(false);
        //eight.printState();
    }
}
