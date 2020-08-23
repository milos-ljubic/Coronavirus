import java.util.Arrays;

public class Coronavirus {

	public static void main(String[] args) {

//    	 	happy path example, 3*3 matrix
		char[][] world = { { '#', '#', '#' }, { '#', '#', '#' }, { '#', '#', '#' } };
		int[] firstInfected = { 1, 1 };

//		invalid input of position for the first infected
//		char[][] world = { { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' } };
//		int[] firstInfected = { 1, 4 };

		
// 		similar like first example, 5*5 matrix	
//		char[][] world = { { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' },
//				{ '#', '#', '#', '#', '#'},{ '#', '#', '#', '#', '#' } };
//		int[] firstInfected = { 2, 3 };

//		example with uninfected people
//		char[][] world = { { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' }, { '#', '#', '#', '#', '#' },
//		{ '#', '#', '#', '.', '.'},{ '#', '#', '#', '.', '#' } };
//		int[] firstInfected = { 1, 4 };

		int[] output = codenavirus(world, firstInfected);

		printOutput(output);
	}

	static int[] codenavirus(char[][] world, int[] firstInfected) {

		int[][] matrixStart = makeMatrixDayZero(world);

		int[][] matrixFinish = makeMatrixDayOne(matrixStart, firstInfected);

		int days = countDays(matrixStart, matrixFinish);

		int[] numbers;

		if (days % 2 == 0)
			numbers = countPeople(matrixStart);
		else
			numbers = countPeople(matrixFinish);

		int infected = numbers[0];
		int recovered = numbers[1];
		int uninfected = numbers[2];

		int[] output = { days, infected, recovered, uninfected };

		return output;
	}

	static int[][] makeMatrixDayZero(char[][] input) {

		int n = input.length;
		if (n < 2)
			throw new IllegalArgumentException("Illegal value for a matrix' size.");

		int m = input[0].length;
		if (m < 2)
			throw new IllegalArgumentException("Illegal value for a matrix' size.");

		int[][] worldDayZero = new int[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (input[i][j] == '#')
					worldDayZero[i][j] = 5;
				else if (input[i][j] == '.')
					worldDayZero[i][j] = 0;
				else
					throw new IllegalArgumentException("Illegal value for the matrix' input.");
			}
		}

//		System.out.println("Day 0");
//		printMatrix(worldDayZero);

		return worldDayZero;
	}

	static int[][] makeMatrixDayOne(int[][] input, int[] position) {

		int n = input.length;
		int m = input[0].length;

		int[][] worldDayOne = new int[n][m];

		int a = position[0];
		if (a < 0 || a > n - 1)
			throw new IllegalArgumentException("The position of a first infected is out of bounds.");
		int b = position[1];
		if (b < 0 || b > m - 1)
			throw new IllegalArgumentException("The position of a first infected is out of bounds.");

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				worldDayOne[i][j] = input[i][j];
			}
		}
		if (input[a][b] != 0)
			worldDayOne[a][b] = 1;
		else
			throw new IllegalArgumentException("On this position in a matrix isn't a human being.");

//		System.out.println("Day 1");
//		printMatrix(worldDayOne);

		return worldDayOne;
	}

	static int[][] worldDailyChange(int[][] input, int[][] output) {

		int n = input.length;
		int m = input[0].length;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (input[i][j] == 1 || input[i][j] == 2) {
					output[i][j] = input[i][j] + 1;
					if (j + 1 < m && input[i][j + 1] == 5 && output[i][j + 1] != 1) {
						output[i][j + 1] = 1;
					} else if (i - 1 >= 0 && input[i - 1][j] == 5 && output[i - 1][j] != 1) {
						output[i - 1][j] = 1;
					} else if (j - 1 >= 0 && input[i][j - 1] == 5 && output[i][j - 1] != 1) {
						output[i][j - 1] = 1;
					} else if (i + 1 < n && input[i + 1][j] == 5 && output[i + 1][j] != 1) {
						output[i + 1][j] = 1;
					}
				} else if (input[i][j] == 3) {
					output[i][j] = input[i][j] + 1;
				} else if (input[i][j] == 4) {
					output[i][j] = input[i][j];
				}
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (output[i][j] == 0)
					output[i][j] = input[i][j];
			}
		}

//		printMatrix(output);
		return output;
	}

	static boolean isThereNewInfected(int[][] input1, int[][] input2) {

		int n = input1.length;
		int m = input1[0].length;
		boolean change = false;

		int i = 0;
		while (!change && i < n) {
			int j = 0;
			while (!change && j < m) {
				if (input1[i][j] != 1 && input2[i][j] == 1)
					change = true;
				else
					j++;
			}
			i++;
		}

		return change;
	}

	static int countDays(int[][] input1, int[][] input2) {
		int days = 1;
		int[][] matrixPom;

		while (isThereNewInfected(input1, input2)) {
			days++;
//			System.out.println("Day " + days);

			worldDailyChange(input2, input1);
			matrixPom = input2;
			input2 = input1;
			input1 = matrixPom;
		}

		return days;
	}

	static int[] countPeople(int[][] input) {
		int infected = 0;
		int recovered = 0;
		int uninfected = 0;

		int n = input.length;
		int m = input[0].length;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (input[i][j] == 1 || input[i][j] == 2 || input[i][j] == 3)
					infected += 1;
				else if (input[i][j] == 4)
					recovered += 1;
				else if (input[i][j] == 5)
					uninfected += 1;
			}
		}

		int[] count = { infected, recovered, uninfected };
		return count;
	}

	static void printOutput(int[] array) {
		System.out.println("Days, Infected, Recovered, Uninfected");
		System.out.println(Arrays.toString(array));
	}

	static void printMatrix(int mat[][]) {
		for (int[] row : mat)
			System.out.println(Arrays.toString(row));
		System.out.println("---------------------");
	}
}
