import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, m, t;

        n = scanner.nextInt();
        m = scanner.nextInt();
        t = scanner.nextInt();

        Maze[] maze = new Maze[t];

        for(int i = 0; i < t; i++) {
            maze[i] = new Maze(n, m);

            for (int j = 0; j < i; j++)
                if (maze[i].isEqual(maze[j])) {
                    maze[i] = new Maze(n, m);
                    j = -1;
                }


            maze[i].finSolution();
            maze[i].print();
            System.out.println();
        }

    }
}

class Maze {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    int n, m;
    char[][] maze;
    boolean[][] visited;
    boolean[][] isVisited;
    int[][] solution;
    int numSolution = 0;
    Maze(int n, int m) {
        this.m = m * 2 + 1;
        this.n = n * 2 + 1;

        maze = new char[this.n][this.m];
        visited = new boolean[this.n][this.m];
        isVisited = new boolean[this.n][this.m];
        solution = new int[2][10000];

        for(int i = 0; i < this.n; i++) {
            for(int j = 0; j < this.m; j++){
                maze[i][j] = '1';

                if(i % 2 == 1 && j % 2 == 1)
                    maze[i][j] = ' ';
            }
        }

        maze[0][1] = 'e';
        maze[this.n - 1][this.m - 2] = 'e';

        Random random = new Random();
        make(1, 1, random);
    }
    void make(int I, int J, Random random){
        visited[I][J] = true;

        // 0 right
        // 1 left
        // 2 up
        // 3 down

        while(true){
            int x = random.nextInt();
            x %= 4;

            if(x == 0 && J + 2 < this.m && !visited[I][J + 2]){
                maze[I][J + 1] = '0';
                make(I, J + 2, random);
            }

            if(x == 1 && J - 2 > -1 && !visited[I][J - 2]){
                maze[I][J - 1] = '0';
                make(I, J - 2, random);
            }

            if(x == 2 && I - 2 > -1 && !visited[I - 2][J]){
                maze[I - 1][J] = '0';
                make(I - 2, J, random);
            }

            if(x == 3 && I + 2 < this.n && !visited[I + 2][J]){
                maze[I + 1][J] = '0';
                make(I + 2, J, random);
            }

            if(!((J + 2 < this.m && !visited[I][J + 2]) || (J - 2 > -1 && !visited[I][J - 2]) || (I + 2 < this.n && !visited[I + 2][J]) || (I - 2 > -1 && !visited[I - 2][J])))
                return;
        }
    }
    void print(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++) {
                if((i == 0 || i == n - 1) && (j == 0 || j == m - 1))
                    System.out.print("+ ");

                else if(maze[i][j] == '1' && i % 2 == 0 && j % 2 == 0 && i != 0 && j != 0 && j != m - 1 && i != n - 1)
                    System.out.print("+ ");

                else if(maze[i][j] == '1' && i % 2 == 0 && j != 0 && j != m - 1)
                    System.out.print("- ");

                else if(maze[i][j] == '1')
                    System.out.print("| ");

                else if(maze[i][j] == '0')
                    System.out.print("  ");

                else if(maze[i][j] == '*')
                    System.out.print(ANSI_RED + "* " + ANSI_RESET);

                else
                    System.out.print("  ");
            }
            System.out.println();
        }
    }
    boolean isEqual(Maze maze){
        for(int i = 0; i < n; i += 2)
            for(int j = 1; j < m; j += 2)
                if(this.maze[i][j] != maze.maze[i][j])
                    return false;

        return true;
    }

    void finSolution(){
        this.solution(1, 1);
        this.addSolution();
    }
    boolean solution(int I, int J){
        isVisited[I][J] = true;

        if(I == n - 2 && J == m - 2) {
            solution[0][numSolution] = I;
            solution[1][numSolution] = J;
            numSolution++;

            return true;
        }

        while(true){
            if(J + 2 < m && maze[I][J + 1] == '0' && !isVisited[I][J + 2]){
                if(solution(I, J + 2)){
                    solution[0][numSolution] = I;
                    solution[1][numSolution] = J;
                    numSolution++;

                    return true;
                }
            }
            if(J - 2 > -1 && maze[I][J - 1] == '0' && !isVisited[I][J - 2]){
                if(solution(I, J - 2)){
                    solution[0][numSolution] = I;
                    solution[1][numSolution] = J;
                    numSolution++;

                    return true;
                }
            }
            if(I + 2 < n && maze[I + 1][J] == '0' && !isVisited[I + 2][J]){
                if(solution(I + 2, J)){
                    solution[0][numSolution] = I;
                    solution[1][numSolution] = J;
                    numSolution++;

                    return true;
                }
            }
            if(I - 2 > - 1 && maze[I - 1][J] == '0' && !isVisited[I - 2][J]){
                if(solution(I - 2, J)){
                    solution[0][numSolution] = I;
                    solution[1][numSolution] = J;
                    numSolution++;

                    return true;
                }
            }

            if(!((I + 2 < n && !isVisited[I + 2][J] && maze[I + 1][J] == '0') || (I - 2 > -1 && !isVisited[I - 2][J] && maze[I - 1][J] == '0') || (J + 2 < m && !isVisited[I][J + 2] && maze[I][J + 1] == '0') && (J - 2 > -1 &&!isVisited[I][J - 2] && maze[I][J - 1] == '0')))
                return false;
        }
    }
    void addSolution(){
        for(int i = 0; i < numSolution; i++)
            maze[solution[0][i]][solution[1][i]] = '*';
    }
}