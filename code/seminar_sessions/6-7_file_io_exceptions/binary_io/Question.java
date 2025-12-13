public class Question {
    
    private String question;
    private int level;  // 1 = Easy, 2 = Medium, 3 = Hard
    private String category;
    
    public Question(String question, int level, String category) {
        this.question = question;
        this.level = level;
        this.category = category;
    }
    
    public String getQuestion() { return question; }
    public int getLevel() { return level; }
    public String getCategory() { return category; }
    
    public String getLevelName() {
        switch (level) {
            case 1: return "Easy";
            case 2: return "Medium";
            case 3: return "Hard";
            default: return "Unknown";
        }
    }
}
