import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * HOLIDAY ROULETTE - Student Assignment
 * 
 * Your task: Complete this program to read questions from a binary file
 * and create an interactive quiz game!
 * 
 * HINT: The binary file "holiday_roulette.bin" contains an ArrayList<Question>
 * 
 * Scoring: Easy = 0.5pts | Medium = 1pt | Hard = 1.5pts
 * 
 */
public class HolidayRouletteStarter {
    
    private ArrayList<Question> questions;
    private Random random;
    private double score;
    
    public HolidayRouletteStarter() {
        this.questions = new ArrayList<>();
        this.random = new Random();
        this.score = 0;
    }
    
    /**
     * TODO #1: Load questions from the binary file (20 points)
     * 
     * Steps:
     * 1. Create an ObjectInputStream wrapped around a FileInputStream
     * 2. Use readObject() to read the ArrayList
     * 3. Cast the result to ArrayList<Question>
     * 4. Use try-with-resources for automatic closing
     * 
     * @param filename the path to the binary file
     */
    @SuppressWarnings("unchecked")
    public void loadQuestions(String filename) 
            throws FileNotFoundException, IOException, ClassNotFoundException {
        
        // YOUR CODE HERE
        
    }
    
    /**
     * TODO #2: Print all questions (15 points)
     * 
     * Loop through the questions ArrayList and print each one.
     * Use the Question's toString() method for nice formatting.
     */
    public void printAllQuestions() {
        
        // YOUR CODE HERE
        
    }
    
    /**
     * TODO #3: Return a random question - Roulette spin! (20 points)
     * 
     * Use the Random class to pick a random index from the questions list.
     * Return null if the list is empty.
     * 
     * @return a randomly selected Question, or null if no questions
     */
    public Question getRandomQuestion() {
        
        // YOUR CODE HERE
        
        return null;
    }
    
    /**
     * TODO #4: Filter questions by difficulty level (25 points)
     * 
     * Create a new ArrayList and add only questions that match the given level.
     * Level: 1 = Easy, 2 = Medium, 3 = Hard
     * 
     * @param level the difficulty level to filter by
     * @return ArrayList containing only questions of that level
     */
    public ArrayList<Question> getQuestionsByLevel(int level) {
        
        // YOUR CODE HERE
        
        return new ArrayList<>();
    }
    
    /**
     * Returns the total number of questions loaded.
     */
    public int getQuestionCount() {
        return questions.size();
    }
    
    /**
     * Calculates points based on question difficulty.
     * Easy (1) = 0.5 points | Medium (2) = 1 point | Hard (3) = 1.5 points
     */
    public double calculatePoints(Question question) {
        return question.getLevel() * 0.5;
    }
    
    /**
     * Adds points to the current score.
     */
    public void addScore(double points) {
        this.score += points;
    }
    
    /**
     * Gets the current score.
     */
    public double getScore() {
        return score;
    }
    
    /**
     * TODO #5: Complete the main method 
     * 
     * 1. Create a HolidayRouletteStarter object
     * 2. Load questions from "holiday_roulette.bin"
     * 3. Handle all possible exceptions appropriately:
     *    - FileNotFoundException
     *    - IOException  
     *    - ClassNotFoundException
     * 4. Test your methods by:
     *    - Printing total questions loaded
     *    - Printing all questions
     *    - Getting and printing a random question
     *    - Getting and printing all "Easy" questions (level 1)
     */
    public static void main(String[] args) {
        
        // YOUR CODE HERE
        
    }
}
