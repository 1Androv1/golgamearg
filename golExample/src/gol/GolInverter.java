package gol;
public class GolInverter implements GolGenerator{

    public static int[][] grid;

    public GolInverter(int[][] grid) {
        this.grid = grid;
    }

    // Invert 1->0 and 0->1 in a Matrix
    private void invertValues(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = matrix[i][j] == 1 ? 0 : 1;
            }
        }
    }

    // Converts a matrix to a flat String
    // [0,0,0][1,1,1][0,1,0] => "...\nXXX\n.X."
    private String toGridString(int[][] matrix) {
        //Agregamos las reglas de las celulas
        /*
        Las reglas del juego son las siguientes:
        * Si una célula está viva y tiene dos o tres vecinas vivas, sobrevive.
        * Si una célula está muerta y tiene tres vecinas vivas, nace.
        * Si una célula está viva y tiene más de tres vecinas vivas, muere.

        */
        String grid = new String("");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                grid += (matrix[i][j] == 1 ? "X" : ".");
            }
            grid += "\n";
        }
        return grid;
    }

    @Override
    public String getNextGenerationAsString(long generation) {
        if (generation != 0) {
            // STEP 7. We calculate the next generation
            invertValues(grid);
        }
        // STEP 8. We return the next generation (as a String)
        return toGridString(grid);
    }

}
