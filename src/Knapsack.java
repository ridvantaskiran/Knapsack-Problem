import java.util.ArrayList;

// A program that implements and demonstrates three different ways for solving 
// the 0-1 knapsack problem. The first way is a recursive approach that checks
// all possible subsets of all the available items and returns the total value 
// of the items put into the knapsack. The second way differs from the first 
// way by returning an array list that contains the indexes of the items put 
// in the knapsack as well as their total value. The third way is an iterative 
// approach based on bottom-up dynamic programming with tabulation.
public class Knapsack {
   // a 2-D array to store the values and the weights of all the available items
   private static int[][] items;

   // solves the 0-1 knapsack problem recursively by checking all possible item
   // subsets and returns the total value of the items put into the knapsack
   private static int solve1(int n, int capacity) {
      // base case: the number of the available items or the capacity is zero
      if (n == 0 || capacity == 0)
         // return zero as the total value of the items put into the knapsack
         return 0;
      // test the last available item
      int index = n - 1, value = items[index][0], weight = items[index][1];
      // linear recursive case: the weight of the item exceeds the capacity
      if (weight > capacity)
         // return the output of the solve1 method excluding the item
         return solve1(n - 1, capacity);
      // binary recursive case (compare 2 possibilities: one when the item is not
      // put in the knapsack and the other when the item is put in the knapsack)
      int totalValue1 = solve1(n - 1, capacity);
      int totalValue2 = value + solve1(n - 1, capacity - weight);
      // return greater total value between the two possibilities as the total
      // value of the items put into the knapsack
      return Math.max(totalValue1, totalValue2);
   }

   // solves the 0-1 knapsack problem recursively by checking all possible item
   // subsets and returns an array list that stores the total value of the items
   // put into the knapsack followed by the indexes of these items
   private static ArrayList<Integer> solve2(int n, int capacity) {
      // base case: the number of the available items or the capacity is zero
      if (n == 0 || capacity == 0) {
         // create and return an array list that contains a single element zero
         // as the total value of the items put into the knapsack
         ArrayList<Integer> knapsack = new ArrayList<>();
         knapsack.add(0);
         return knapsack;
      }
      // test the last available item
      int index = n - 1, value = items[index][0], weight = items[index][1];
      // linear recursive case: the weight of the item exceeds the capacity
      if (weight > capacity)
         // return the output of the solve2 method excluding the item
         return solve2(n - 1, capacity);
      // binary recursive case (compare 2 possibilities: one when the item is not
      // put in the knapsack and the other when the item is put in the knapsack)
      ArrayList<Integer> knapsack1 = solve2(n - 1, capacity);
      ArrayList<Integer> knapsack2 = solve2(n - 1, capacity - weight);
      knapsack2.add(index);
      knapsack2.set(0, knapsack2.get(0) + value);
      // return the knapsack with greater total value between the 2 possibilities
      if (knapsack1.get(0) >= knapsack2.get(0))
         return knapsack1;
      else
         return knapsack2;
   }

   // a 2-D array to store the maximum possible total value for each subproblem
   // (used for bottom-up dynamic programming with tabulation)
   private static int[][] solution;

   // solves the 0-1 knapsack problem iteratively by using a bottom-up dynamic
   // programming approach with tabulation (i.e., filling the solution matrix)
   // and returns the total value of the items put into the knapsack
   private static int solve3(int capacity) throws IllegalArgumentException {
      // the given capacity must have a positive value
      if (capacity <= 0)
         throw new IllegalArgumentException();
      // get the number of all the available items
      int numItems = items.length;
      // create the solution matrix for storing the maximum possible total value
      // for each subproblem with a row for each available item and a column for
      // each possible value of the weight capacity of the knapsack
      // (the possible values for the weight capacity are 0, 1, ... , capacity
      // as the total weight capacity and all the item weights are integers)
      solution = new int[numItems][capacity + 1];
      // compute the solutions of the subproblems that involve only the 1st item
      // and fill the first row of the solution matrix accordingly
      for (int c = 1; c <= capacity; c++) {
         int index = 0, value = items[index][0], weight = items[index][1];
         // the 1st item is placed in the knapsack if its weight does not exceed
         // the weight capacity of the knapsack
         if (weight <= c)
            solution[index][c] = value;
      }
      // compute the solutions of the remaining subproblems and fill the rest of
      // the solution matrix (all the values are zero by default in column zero)
      for (int i = 1; i < numItems; i++)
         for (int c = 1; c <= capacity; c++) {
            // test the current item that is at the index i of the items matrix
            int value = items[i][0], weight = items[i][1];
            // the item can be put in the knapsack if its weight does not exceed
            // the weight capacity of the knapsack
            int totalValue1 = 0, totalValue2;
            if (weight <= c)
               totalValue1 = value + solution[i - 1][c - weight];
            // the total value when the item is not put in the knapsack
            totalValue2 = solution[i - 1][c];
            // the solution of the current subproblem is the maximum total value
            solution[i][c] = Math.max(totalValue1, totalValue2);
         }
      // return the total value of the overall solution stored as the element
      // at the bottom-right corner in the solution matrix
      return solution[numItems - 1][capacity];
   }

   // prints the solution of the 0-1 knapsack problem (used for the 3rd approach
   // which is based on bottom-up dynamic programming with tabulation)
   private static void printSolution(boolean printSolutionMatrix) {
      // get the number of all the items and the weight capacity of the knapsack
      int numItems = solution.length, capacity = solution[0].length - 1;
      // print the solution matrix if printSolutionMatrix is given as true
      if (printSolutionMatrix) {
         System.out.println("The solution matrix");
         // print the header
         System.out.println("                     capacity - >");
         System.out.print("       value weight ");
         for (int c = 0; c <= capacity; c++)
            // assuming that each computed total value has at most 2 digits
            System.out.printf("%2d ", c);
         System.out.println();
         // print the rows
         for (int i = 0; i < numItems; i++) {
            // print item info
            int index = i + 1, value = items[i][0], weight = items[i][1];
            // assuming that the number of all the items has at most 2 digits
            System.out.printf("item%-2d %5d %6d", index, value, weight);
            // print the solution of each subproblem in the current row
            for (int c = 0; c <= capacity; c++)
               // assuming that each computed total value has at most 2 digits
               System.out.printf(" %2d", solution[i][c]);
            System.out.println(); // print a new line at the end of each row
         }
      }
      // print all the items put into the knapsack in the overall solution
      System.out.print("\nThe items in the knapsack are ");
      // get the total value of the solution (the element at the bottom-right
      // corner in the solution matrix) and initialize the total weight as zero
      int totalValue = solution[numItems - 1][capacity], totalWeight = 0;
      // using string concatenation to print items starting from lower indexes
      String knapsack = "";
      // for each item from the last item to the first item
      for (int i = numItems - 1; i >= 0; i--) {
         // the current item is in the solution if its index is zero (and there
         // is some remaining total value) or the value at the previous row in
         // the solution matrix is different from the remaining total value
         if (i == 0 || solution[i - 1][capacity] != totalValue) {
            // add the current item to the knapsack string from the left
            int index = i + 1, v = items[i][0], w = items[i][1];
            knapsack = "\nItem" + index + "(v:" + v + ", w:" + w + ")" + knapsack;
            // compute the remaining total value and the remaining capacity by
            // excluding the current item
            totalValue -= v;
            capacity -= w;
            // add the weight of the current item to the total weight to print
            totalWeight += w;
            // if the remaining total value or the remaining capacity becomes 0
            if (totalValue == 0 || capacity == 0)
               break; // no need to check the remaining items -> end the loop
         }
      }
      // print the resulting knapsack string
      System.out.println(knapsack);
      // print the total value and the total weight of the items in the knapsack
      capacity = solution[0].length - 1;
      totalValue = solution[numItems - 1][capacity];
      System.out.println("The total value of the items: " + totalValue);
      System.out.println("The total weight of the items: " + totalWeight);
   }

   // generates random integers in the range [1, 9] for the value and the weight
   // of each item, stores them in a 2-D array, uses each approach to solve the
   // same 0-1 knapsack problem defined by the weight capacity 20 and 10 random
   // items, and prints the solution and the execution time for each approach
   public static void main(String[] args) {
      // there are 10 items and the weight capacity of the knapsack is 20 kgs
      int numItems = 10, weightCapacity = 20;
      // create the 2-D items array to store a value and a weight for each item
      items = new int[numItems][2];
      // create random integers in the range [1, 9] for the value and the weight
      // of each item and store them in the items array while printing the items
      System.out.println("There are " + numItems + " available items.");
      for (int i = 0; i < numItems; i++) {
         int v = 1 + (int) (Math.random() * 9); // the value of the item
         int w = 1 + (int) (Math.random() * 9); // the weight of the item
         int[] item = { v, w };
         items[i] = item;
         // print each item on the console
         System.out.println("Item" + (i + 1) + "(v:" + v + ", w:" + w + ")");
      }
      // print the weight capacity of the knapsack on the console
      System.out.println("The weight capacity of the knapsack: " + weightCapacity);

      // solve the 0-1 knapsack problem by using the first approach that checks
      // all possible subsets of the available items and returns the solution as
      // the total value of the items put into the knapsack
      // -----------------------------------------------------------------------
      System.out.println("\nChecking all possible subsets of all the items (v1)");
      System.out.println("-----------------------------------------------------");
      // System.nanoTime() is used instead of System.currentTimeMillis() as it
      // has a better precision
      long startTime = System.nanoTime();
      int totalValue = solve1(numItems, weightCapacity);
      long endTime = System.nanoTime();
      // print the solution and the execution time on the console
      System.out.println("The total value of the selected items: " + totalValue);
      double executionTime = (endTime - startTime) / 1000000.0;
      System.out.println("Execution time: " + executionTime + " milliseconds");

      // solve the 0-1 knapsack problem by using the second approach that checks
      // all possible subsets of the available items and returns the solution as
      // an array list that contains the total value of the items in the knapsack
      // followed by the indexes of these items
      // -----------------------------------------------------------------------
      System.out.println("\nChecking all possible subsets of all the items (v2)");
      System.out.println("-----------------------------------------------------");
      startTime = System.nanoTime();
      ArrayList<Integer> knapsack = solve2(numItems, weightCapacity);
      endTime = System.nanoTime();
      // print the solution and the execution time on the console
      System.out.println("The items in the knapsack are ");
      totalValue = knapsack.get(0);
      int totalWeight = 0;
      for (int k = 1; k < knapsack.size(); k++) {
         int i = knapsack.get(k), v = items[i][0], w = items[i][1];
         System.out.println("Item" + (i + 1) + "(v:" + v + ", w:" + w + ")");
         totalWeight += w;
      }
      System.out.println("The total value of the items: " + totalValue);
      System.out.println("The total weight of the items: " + totalWeight);
      executionTime = (endTime - startTime) / 1000000.0;
      System.out.println("Execution time: " + executionTime + " milliseconds");

      // solve the 0-1 knapsack problem by using the third approach which is an
      // iterative method based on bottom-up dynamic programming with tabulation
      // -----------------------------------------------------------------------
      System.out.println("\nUsing bottom-up dynamic programming with tabulation");
      System.out.println("-----------------------------------------------------");
      // the solve3 method returns the total value of the solution
      startTime = System.nanoTime();
      totalValue = solve3(weightCapacity);
      endTime = System.nanoTime();
      // print the solution and the execution time on the console
      printSolution(true);
      executionTime = (endTime - startTime) / 1000000.0;
      System.out.println("Execution time: " + executionTime + " milliseconds");
   }
}
