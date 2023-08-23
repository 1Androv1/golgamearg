package gol;
import java.util.Random;

public class GolExampleD {
    static String createRandomGrid(int rows, int cols, String population) {
        var random = new Random();
        StringBuilder grid = new StringBuilder(); // Using StringBuilder for efficient string concatenation
        //System.out.println(population);

        if("rnd".equals(population) || rows != 3 || cols != 3){
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid.append(random.nextBoolean() ? "X" : ".");
                }
                grid.append("\n");
            }
            return grid.toString();
        }else{
            StringBuilder modificarPoblacion = new StringBuilder();
            //estos validadores es para modificar la cadena de que si tiene un 1 la pase a una "X" y si tiene un 0 la pase a un "."

            for (char c : population.toCharArray()) {
                if (c == '1') {
                    modificarPoblacion.append("X");
                } else if (c == '0') {
                    modificarPoblacion.append(".");
                } else if (c == '#') {
                    modificarPoblacion.append("\n");
                } else {
                    modificarPoblacion.append(c);
                }
            }

            String cadenaValor = modificarPoblacion.toString();
            String nuevacadena = concatenateSubPopulations(cadenaValor);

            return nuevacadena;
        }

    }


    public static void main(String[] args) throws Exception {
        // Convertir la cadena en una matriz de caracteres
        //CASO 1: 3x3 w=3 h=3 g=100 s=1000 p=101#010#111
        //CASO 2: w=10 h=10 g=100 s=1000 p=rnd = se puede agrandar hasta 50x50

        parseIntArgs(args);

        int newrows = 0;
        int newcols = 0;
        String population = "";
        int generation = 0;
        int speed = 0;

        for (String arg : args) {
            String[] keyValue = arg.split("=");

            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "0";

            switch (key){
                case "w":
                        newrows = Integer.parseInt(value);
                    break;
                case "h":
                    newcols = Integer.parseInt(value);
                    break;
                case "p":
                    population = value;
                    break;
                case "g":
                    generation = Integer.parseInt(value);
                    break;
                case"s":
                    speed = Integer.parseInt(value);
                    break;
            }

        }

        String initialRandomGrid = createRandomGrid(newrows, newcols, population);

        char[][] gridArray = new char[newrows][newcols];
        String[] lines = initialRandomGrid.split("\n");
        for (int i = 0; i < lines.length; i++) {
            gridArray[i] = lines[i].toCharArray();
        }

        // Generar la primera generación y aplicar las reglas del juego de la vida
        final char[][] nextGeneration = generateNextGeneration(gridArray);

        final GolGenerator generator = new GolGenerator() {
            private char[][] currentGeneration = nextGeneration; // Guardar la generación actual

            @Override
            public String getNextGenerationAsString(long generation) {
                if (generation == 0) {
                    return initialRandomGrid;
                } else {
                    // Actualizar la matriz para la siguiente generación
                    currentGeneration = generateNextGeneration(currentGeneration);

                    // CONVERTIMOS LA MATRIZ EN CARACTERES EN UNA NUEVA GENERACION
                    StringBuilder newGenGrid = new StringBuilder();
                    for (char[] row : currentGeneration) {
                        newGenGrid.append(row).append("\n");
                    }
                    return newGenGrid.toString();
                }
            }
        };

        // Will use a Terminal Console - SU USO ES PARA VISUALIZAR EL JUEGO
        SwingRenderer.render(generator, new GolSettings(newrows, newcols, speed, generation));
    }

    //Reglas del juego de la vida
    private static char[][] generateNextGeneration(char[][] currentGeneration) {
        char[][] nextGeneration = new char[currentGeneration.length][currentGeneration[0].length];

        //for en la matriz en la generacion actual.
        for (int i = 0; i < currentGeneration.length; i++) {
            for (int j = 0; j < currentGeneration[i].length; j++) {
                int liveNeighbors = countLiveNeighbors(currentGeneration, i, j);

                if (currentGeneration[i][j] == 'X') {
                    // Reglas 1 y 2: Si una célula viva tiene 2 o 3 vecinos vivos, sobrevive; en caso contrario, muere.
                    nextGeneration[i][j] = (liveNeighbors == 2 ) ? 'X' : '.';
                } else {
                    // Regla 4: Si una muerta tiene 3 vecinos vivos, revive.
                    nextGeneration[i][j] = (liveNeighbors == 3) ? 'X' : '.';
                }
            }
        }
        return nextGeneration;
    }

    public static void printArgs(String[] args) {
        if (args.length == 5) {
            for (String arg : args) {
                System.out.println(arg);
            }
        } else {
            System.out.println("ERROR: 5 argumentos requeridos.");
        }
    }

    public static void parseIntArgs(String[] args) {
        if (args.length != 5) {
            System.out.println("ERROR: 5 argumentos requeridos.");
            return;
        }

        int vWidth = 0;
        int vHeight = 0;
        int vGeneration = 0;
        int vSpeed = 0;
        String vPopulation = "";
        String ValorRnd = "";

        for (String arg : args) {
            String[] keyValue = arg.split("=");

            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "0";

            switch (key) {
                case "w":
                    if (isValidWidth(Integer.parseInt(value))) {
                        vWidth = Integer.parseInt(value);
                        System.out.println("width=" + vWidth);
                    }else if(keyValue.length == 2){
                        System.out.println("width= [Invalido]");
                    }else{
                        System.out.println("width= [Invalido]");
                    }
                    break;
                case "h":
                    if (isValidHeight(Integer.parseInt(value))) {
                        vHeight = Integer.parseInt(value);
                        System.out.println("height=" + vHeight);
                    }else {
                        System.out.println("height= [Invalido]");
                    }
                    break;
                case "g":
                    if (isValidGeneration(Integer.parseInt(value))) {
                        vGeneration = Integer.parseInt(value);
                        System.out.println("generations=" + vGeneration);
                    } else {
                        System.out.println("generations= [No Presente]");
                    }
                    break;
                case "s":
                    if (isValidSpeed(Integer.parseInt(value))) {
                        vSpeed = Integer.parseInt(value);
                        System.out.println("speed=" + vSpeed);
                    } else {
                        System.out.println("speed= [No Presente]");
                    }
                    break;
                case "p":
                    ValorRnd = value;
                    if(isValidSubPopulation(value) || "rnd".equals(ValorRnd)){
                        vPopulation = value;
                        System.out.println("population=" + vPopulation);
                    }else{
                        System.out.println("population=[No  Presente]");
                    }
                    break;
                default:
                    System.out.println("ERROR: Sin Argumentos: " + key);
                    break;
            }

        }
    }

    private static boolean isValidWidth(int width) {
        return width == 10 || width == 20 || width == 40 || width == 80;
    }

    private static boolean isValidHeight(int height) {
        return height == 10 || height == 20 || height == 40 || height == 80;
    }

    private static boolean isValidGeneration(int generation) {
        return generation >= 100;
    }

    private static boolean isValidSpeed(int speed) {
        return speed >= 250 && speed <= 1000;
    }
    private static String concatenateSubPopulations(String subPopulation) {
        String[] population = subPopulation.split("#");
        StringBuilder contruirCadena = new StringBuilder();

        /* SE REALIZA UN FOREACH DEL ARRAY POPULATION QUE FUE DIVIDIDA POR EL #
         PARA PODER AGREGAR UNA CADENA CON #*/
        for (String cadena : population) {
            contruirCadena.append(cadena).append("#");
        }
        if (contruirCadena.length() > 0) {
            //Si la cadena  tiene una longitud mayor a 0, quitamos el último #
            // que se agregó en el último ciclo del bucle, ya que no se necesita al final.
            contruirCadena.deleteCharAt(contruirCadena.length() - 1);
        }

        return contruirCadena.toString(); //retornamos la cadena ejemplo si es ##110 la va retornar como 000#000#110
    }

    private static boolean isValidSubPopulation(String subPopulation) {
        String[] population = subPopulation.split("#");

        for (String valida : population) {
            if (valida.length() != 3) {
                return false;
            }
        }

        return true;
    }

    // Función para contar las células vivas vecinas
    private static int countLiveNeighbors(char[][] grid, int x, int y) {
        int liveNeighbors = 0;

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        // for para los desplazamientos para de celulas vecinas vivas
        for (int i = 0; i < 8; i++) {

            int newX = x + dx[i];
            int newY = y + dy[i];

            // Verificar si las coordenadas están dentro de los límites de la cuadrícula
            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {
                if (grid[newX][newY] == 'X') {
                    liveNeighbors++;
                }
            }
        }
        return liveNeighbors;
    }
}
