import java.util.ArrayList;

// A template to implement and demonstrate a recursive method based on top-down 
// dynamic programming with memoization for solving the 0-1 knapsack problem and
// compare this method with the basic recursive method that checks all possible 
// item subsets in terms of the number of the invocations for a single call.
public class KnapsackProblem {
   // a 2-D array to store the values and the weights of all the available items
   private static int[][] items;

   // 2 counters to demonstrate the efficiency of memoization on the same problem
   // count1 -> how many times the method without memoization is invoked
   // count2 -> how many times the method with memoization is invoked
   private static int count1 = 0, count2 = 0;

   // solves the 0-1 knapsack problem recursively by checking all possible item
   // subsets and returns an array list that stores the total value of the items
   // put into the knapsack followed by the indexes of these items
   private static ArrayList<Integer> knapsack(int n, int capacity) {
      // increase the counter that stores how many times this method is invoked
      count1++;
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
         // return the output of the knapsack method excluding the item
         return knapsack(n - 1, capacity);
      // binary recursive case (compare 2 possibilities: one when the item is not
      // put in the knapsack and the other when the item is put in the knapsack)
      ArrayList<Integer> knapsack1 = knapsack(n - 1, capacity);
      ArrayList<Integer> knapsack2 = knapsack(n - 1, capacity - weight);
      knapsack2.add(index);
      knapsack2.set(0, knapsack2.get(0) + value);
      // return the knapsack with greater total value between the 2 possibilities
      if (knapsack1.get(0) >= knapsack2.get(0))
         return knapsack1;
      else
         return knapsack2;
   }

   // a 2-D array to store the maximum possible total value for each computed
   // subproblem (used for memoization)
   private static Integer[][] solution;

   // solves the 0-1 knapsack problem recursively by using an approach based on
   // top-down dynamic programming with memoization (fills and uses the solution
   // matrix as needed to prevent repeated computations for any subproblem) and
   // returns the total value of the items put into the knapsack
   private static int knapsackMemoization(int n, int capacity) {
	   count2++;
		
		if (n == 0 || capacity == 0) 
			return 0;
		else if (solution[n-1][capacity] != null)
			return solution[n-1][capacity];
		else if (items[n-1][1] > capacity) {
			solution[n-1][capacity] = knapsackMemoization(n-1, capacity);
			return solution[n-1][capacity];
		}
		else {
			solution[n-1][capacity] = Math.max((items[n-1][0] + knapsackMemoization(n-1, capacity - items[n-1][1])), (knapsackMemoization(n-1, capacity)));
			return solution[n-1][capacity];
		}
   }

   // prints the solution of the 0-1 knapsack problem (used for the 2nd approach
   // which is based on top-down dynamic programming with memoization)
   private static void printSolution(boolean printSolutionMatrix) {
	   int numItems = solution.length, capacity = solution[0].length - 1;
		
		if (printSolutionMatrix) {
			System.out.println("The solution matrix");
			
			System.out.println("                     capacity - >");
			System.out.print("       value weight ");
			for (int c = 0; c <= capacity; c++)
				System.out.printf("%2d ", c);
			System.out.println();
			
			for (int i = 0; i < numItems; i++) {
	
				int index = i + 1, value = items[i][0], weight = items[i][1];
				
				System.out.printf("item%-2d %5d %6d", index, value, weight);
				
				for (int c = 0; c <= capacity; c++)
					if (solution[i][c] != null)
						System.out.printf(" %2d", solution[i][c]);
					else
						System.out.printf(" %2c", '-');
				System.out.println(); 
			}
		}
		
		System.out.print("\nThe items in the knapsack are ");
		
		int totalValue = solution[numItems - 1][capacity], totalWeight = 0;
		
		String knapsack = "";
		
		for (int i = numItems - 1; i >= 0; i--) {

			if (i == 0 || solution[i - 1][capacity] != totalValue) {
				
				int index = i + 1, v = items[i][0], w = items[i][1];
				knapsack = "\nItem" + index + "(v:" + v + ", w:" + w + ")" + knapsack;
				
				totalValue -= v;
				capacity -= w;
				
				totalWeight += w;
				
				if (totalValue == 0 || capacity == 0)
					break; 
			}
		}
		
		System.out.println(knapsack);
		
		capacity = solution[0].length - 1;
		totalValue = solution[numItems - 1][capacity];
		System.out.println("The total value of the items: " + totalValue);
		System.out.println("The total weight of the items: " + totalWeight);
	}


   // generates random integers in the range [1, 9] for the value and the weight
   // of each item, stores them in a 2-D array, uses both the recursive knapsack
   // method that checks all possible item subsets and the knapsackMemoization
   // method that is based on top-down dynamic programming with memoization for
   // solving the same 0-1 knapsack problem defined by the weight capacity 20 and
   // 10 random items, counts the number of the times these methods are invoked
   // recursively, and prints the results on the console
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
      // an array list that contains the total value of the items in the knapsack
      // followed by the indexes of these items
      // -----------------------------------------------------------------------
      System.out.println("\nChecking all possible subsets of all the items");
      System.out.println("-----------------------------------------------------");
      ArrayList<Integer> knapsack = knapsack(numItems, weightCapacity);
      // print the solution on the console
      System.out.println("The items in the knapsack are ");
      int totalValue = knapsack.get(0);
      int totalWeight = 0;
      for (int k = 1; k < knapsack.size(); k++) {
         int i = knapsack.get(k), v = items[i][0], w = items[i][1];
         System.out.println("Item" + (i + 1) + "(v:" + v + ", w:" + w + ")");
         totalWeight += w;
      }
      System.out.println("The total value of the items: " + totalValue);
      System.out.println("The total weight of the items: " + totalWeight);
      // print how many times the knapsack method is invoked
      System.out.println("# times the recursive method is invoked: " + count1);

      // solve the 0-1 knapsack problem by using the second approach which is
      // based on top-down dynamic programming with memoization
      // -----------------------------------------------------------------------
      System.out.println("\nUsing top-down dynamic programming with memoization");
      System.out.println("-----------------------------------------------------");
      // create the solution matrix with numItems rows and weightCapacity + 1
      // columns (the default initial value is null for each element)
      solution = new Integer[numItems][weightCapacity + 1];
      // the knapsackMemoization method returns the total value of the solution
      totalValue = knapsackMemoization(numItems, weightCapacity);
      // print the solution on the console
      printSolution(true);
      // print how many times the knapsackMemoization method is invoked
      System.out.println("# times the recursive method is invoked: " + count2);
   }
}
