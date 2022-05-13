package battleship;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    final static int SHIPS_KOL = 17;

    public static void main(String[] args) throws FileNotFoundException {
        // Write your code here
        var arr = createArr(10, 10);
        var arrPlayerTwo = createArr(10, 10);
        System.out.println("Player 1, place your ships on the game field\n");
        usersShips(arr);
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        System.out.println("Player 2, place your ships to the game field\n");
        usersShips(arrPlayerTwo);
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (Exception e) {
        }

        int playerNumber = 1;
        int kol = 0;
        int kolOne = 0;
        int kolTwo = 0;
        while (SHIPS_KOL > kol) {
            printPlayersField(arr, arrPlayerTwo, playerNumber);
            if (checkShot(arr, arrPlayerTwo, playerNumber, kolOne, kolTwo)) {
                if (playerNumber == 1) {
                    kolOne++;
                } else {
                    kolTwo++;
                }
                kol = Math.max(kolOne, kolTwo);
            }
            if (playerNumber == 1) {
                playerNumber = 2;
            } else {
                playerNumber = 1;
            }
        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    private static void usersShips(String[][] arr) {
        printField(arr, false);
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        addShip(arr, "Error! Wrong length of the Aircraft Carrier! Try again:", 4);

        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        addShip(arr, "Error! Wrong length of the Battleship! Try again:", 3);

        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        addShip(arr, "Error! Wrong length of the Submarine! Try again:", 2);

        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        addShip(arr, "Error! Wrong length of the Cruiser! Try again:", 2);

        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        addShip(arr, "Error! Wrong length of the Destroyer! Try again:", 1);
    }

    private static String[][] createArr(int seat, int row) {
        String[][] arr = new String[row][seat];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < seat; j++) {
                arr[i][j] = "~";
            }
        }
        return arr;
    }

    private static void printField(String[][] arr, boolean hideShips) {
        String printSymbols = "";
        System.out.print(" ");
        for (int a = 0; a < arr[0].length; a++) {
            System.out.print(" " + (a + 1));
        }
        System.out.println();
        for (int i = 0; i < arr.length; i++) {
            System.out.print((char) (i + 1 + 64));
            for (int j = 0; j < arr[i].length; j++) {
                printSymbols = " " + arr[i][j];
                if (hideShips) {
                    if ("O".equals(arr[i][j])) {
                        printSymbols = " ~";
                    }
                }
                System.out.print(printSymbols);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printPlayersField(String[][] arr, String[][] arrPlayerTwo, int playerNumber) {
        int number = 0;
        String[][] printArr = new String[10][10];
        while (number < 2) {
            if (number == 0) {
                if (playerNumber == 1) {
                    printArr = arrPlayerTwo;
                } else {
                    printArr = arr;
                }
            } else {
                if (playerNumber == 1) {
                    printArr = arr;
                } else {
                    printArr = arrPlayerTwo;
                }
            }
            String printSymbols = "";
            System.out.print(" ");
            for (int a = 0; a < printArr[0].length; a++) {
                System.out.print(" " + (a + 1));
            }
            System.out.println();

            for (int i = 0; i < printArr.length; i++) {
                System.out.print((char) (i + 1 + 64));
                for (int j = 0; j < printArr[i].length; j++) {
                    printSymbols = " " + printArr[i][j];
                    if (number == 0) {
                        if ("O".equals(printArr[i][j])) {
                            printSymbols = " ~";
                        }
                    }
                    System.out.print(printSymbols);
                }
                System.out.println();
            }
            if (number == 0) {
                System.out.println("---------------------");
            } else
                System.out.println();
            number++;
        }
    }

    private static void addShip(String[][] arr, String userErr, int shipLength) {
        boolean inputCoordinate = true;
        Scanner sc = new Scanner(System.in);
        while (inputCoordinate) {
            String[] s = sc.nextLine().split(" ");
            var shipCoordinates = convertCoordinates(s);
            boolean errorLocation = false;
            for (int z : shipCoordinates) {
                if (z > 9) {
                    errorLocation = true;
                }
            }
            if (errorLocation) {
                continue;
            }
            var tooClose = checkAnotherShip(arr, shipCoordinates);
            if (tooClose) {
                continue;
            }
            if (shipCoordinates[1] == shipCoordinates[3]) {
                if ((shipCoordinates[2] - shipCoordinates[0]) == shipLength) {
                    for (int i = shipCoordinates[0]; i <= shipCoordinates[2]; i++) {
                        arr[i][shipCoordinates[1]] = "O";
                    }
                    inputCoordinate = false;
                } else {
                    System.out.println(userErr);
                }
            } else if (shipCoordinates[0] == shipCoordinates[2]) {
                if ((shipCoordinates[3] - shipCoordinates[1]) == shipLength) {
                    for (int j = shipCoordinates[1]; j <= shipCoordinates[3]; j++) {
                        arr[shipCoordinates[2]][j] = "O";
                    }
                    inputCoordinate = false;
                } else {
                    System.out.println(userErr);
                }
            } else {
                System.out.println("Error! Wrong ship location! Try again:");
            }
        }
        printField(arr, false);
    }

    private static int[] convertCoordinates(String[] a) {
        int[] x = new int[4];
        var firstChar = ((int) a[0].replaceAll("[\\d]", "").charAt(0) - 65);
        var secondChar = ((int) a[1].replaceAll("[\\d]", "").charAt(0) - 65);
        var firstDigit = Integer.parseInt(a[0].replaceAll("[\\D]", "")) - 1;
        var secondDigit = Integer.parseInt(a[1].replaceAll("[\\D]", "")) - 1;
        x[0] = Math.min(firstChar, secondChar);
        x[1] = Math.min(firstDigit, secondDigit);
        x[2] = Math.max(firstChar, secondChar);
        x[3] = Math.max(firstDigit, secondDigit);
        return x;
    }

    private static boolean checkAnotherShip(String[][] arr, int[] shipCoordinates) {
        boolean tooClose = false;
        boolean inputCoordinate = true;
        while (inputCoordinate) {
            if (shipCoordinates[1] == shipCoordinates[3]) {
                int k = shipCoordinates[0] == 0 ? 0 : shipCoordinates[0] - 1;
                int m = shipCoordinates[2] == 9 ? shipCoordinates[2] : shipCoordinates[2] + 1;
                int vnStart = (shipCoordinates[1] > 0 && shipCoordinates[1] < 9) ? shipCoordinates[1] - 1 : shipCoordinates[1];
                int vnEnd = (shipCoordinates[1] > 0 && shipCoordinates[1] < 9) ? shipCoordinates[1] + 1 : shipCoordinates[1];
                for (int i = k; i <= m; i++) {
                    for (int j = vnStart; j < vnEnd; j++) {
                        if ("O".equals(arr[i][j])) {
                            tooClose = true;
                        }
                    }
                }
                inputCoordinate = false;
            } else if (shipCoordinates[0] == shipCoordinates[2]) {
                int differentStart = shipCoordinates[1] == 0 ? 0 : shipCoordinates[1] - 1;
                int differentEnd = (shipCoordinates[3] == 0 || shipCoordinates[3] == 9) ? shipCoordinates[3] : shipCoordinates[3] + 1;
                int sameStart = (shipCoordinates[0] > 0 && shipCoordinates[0] < 9) ? shipCoordinates[0] - 1 : shipCoordinates[0];
                int sameEnd = (shipCoordinates[0] >= 0 && shipCoordinates[0] < 9) ? shipCoordinates[0] + 1 : shipCoordinates[0];
                for (int i = sameStart; i <= sameEnd; i++) {
                    for (int j = differentStart; j <= differentEnd; j++) {
                        if ("O".equals(arr[i][j])) {
                            tooClose = true;
                        }
                    }
                }
                inputCoordinate = false;
            } else {
                inputCoordinate = false;
            }
        }
        if (tooClose) {
            System.out.println("Error! You placed it too close to another one. Try again:");
        }
        return tooClose;
    }

    private static boolean checkShot(String[][] arr, String[][] arrTwo, int playerNumber, int kolOne, int kolTwo) {
        boolean repeat = true;
        boolean hit = false;
        int kol = 0;
        String[][] arrForWrite = new String[10][10];
        Scanner sc = new Scanner(System.in);
        if (playerNumber == 1) {
            arrForWrite = arrTwo;
        } else {
            arrForWrite = arr;
        }
        while (repeat) {
            System.out.println("Player " + playerNumber + ", it's your turn:\n");
            String userShot = sc.nextLine();
            int[] x = new int[2];
            var firstChar = ((int) userShot.replaceAll("[\\d]", "").charAt(0) - 65);
            var firstDigit = Integer.parseInt(userShot.replaceAll("[\\D]", "")) - 1;
            if (firstChar > 9 || firstDigit > 9) {
                System.out.println("You missed!");
                repeat = true;
            } else {
                repeat = false;
                if ("O".equals(arrForWrite[firstChar][firstDigit]) || "X".equals(arrForWrite[firstChar][firstDigit])) {
                    hit = "O".equals(arrForWrite[firstChar][firstDigit]);
                    //3.05.2022+
                    if (hit) {
                        if (playerNumber == 1) {
                            kolOne++;
                        } else {
                            kolTwo++;
                        }
                        kol = Math.max(kolOne, kolTwo);
                    }
                    arrForWrite[firstChar][firstDigit] = "X";
                    boolean hitShip = false;
                    int startChar = firstChar == 0 ? firstChar : firstChar - 1;
                    int startDigit = firstDigit == 0 ? firstDigit : firstDigit - 1;
                    int endChar = firstChar == 9 ? firstChar : firstChar + 1;
                    int endDigit = firstDigit == 9 ? firstDigit : firstDigit + 1;
                    for (int i = startChar; i <= endChar; i++) {
                        for (int j = startDigit; j <= endDigit; j++) {
                            if ("O".equals(arrForWrite[i][j])) {
                                hitShip = true;
                            }
                        }
                    }
                    if (hitShip) {
                        System.out.println("You hit a ship!");
                    } else {
                        if (kol == SHIPS_KOL) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                        } else {
                            System.out.println("You sank a ship! Specify a new target:");
                        }
                    }
                    printField(arrForWrite, true);
                } else {
                    arrForWrite[firstChar][firstDigit] = "M";
                    System.out.println("You missed!");
                }
            }
            System.out.println("Press Enter and pass the move to another player");
            try {
                System.in.read();
            } catch (Exception e) {
            }
        }
        return hit;
    }
}
