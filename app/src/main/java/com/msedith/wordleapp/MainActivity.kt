package com.msedith.wordleapp

import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var randomWord: String
    private var attempts = 0
    private val maxAttempts = 3
    private val wordLength = 4
    private lateinit var submitButton: Button
    private lateinit var inputText: EditText
    private lateinit var gridLayout: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById<EditText>(R.id.inputText)
        submitButton = findViewById<Button>(R.id.submitGuess)
        gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        randomWord = FourLetterWordList.getRandomFourLetterWord().uppercase()

        populateGrid()

        startNewGame()

        submitButton.setOnClickListener{
            val guess = inputText.text.toString().uppercase()
            if (submitButton.text == getString(R.string.reset_string)) {
                startNewGame()
            } else if (guess.isNotEmpty()){
                inputText.text.clear()
                checkCorrectness(guess)
            }
        }
    }
    object FourLetterWordList {
        private val fourLetterWords = listOf(
            "Area", "Army", "Baby", "Back", "Ball", "Band", "Bank", "Base", "Bill", "Body",
            "Book", "Call", "Card", "Care", "Case", "Cash", "City", "Club", "Cost", "Date",
            "Deal", "Door", "Duty", "East", "Edge", "Face", "Fact", "Farm", "Fear", "File",
            "Film", "Fire", "Firm", "Fish", "Food", "Foot", "Form", "Fund", "Game", "Girl",
            "Goal", "Gold", "Hair", "Half", "Hall", "Hand", "Head", "Help", "Hill", "Home",
            "Hope", "Hour", "Idea", "Jack", "John", "Kind", "King", "Lack", "Lady", "Land",
            "Life", "Line", "List", "Look", "Lord", "Loss", "Love", "Mark", "Mary", "Mind",
            "Miss", "Move", "Name", "Need", "News", "Note", "Page", "Pain", "Pair", "Park",
            "Part", "Past", "Path", "Paul", "Plan", "Play", "Post", "Race", "Rain", "Rate",
            "Rest", "Rise", "Risk", "Road", "Rock", "Role", "Room", "Rule", "Sale", "Seat",
            "Shop", "Show", "Side", "Sign", "Site", "Size", "Skin", "Sort", "Star", "Step",
            "Task", "Team", "Term", "Test", "Text", "Time", "Tour", "Town", "Tree", "Turn",
            "Type", "Unit", "User", "View", "Wall", "Week", "West", "Wife", "Will", "Wind",
            "Wine", "Wood", "Word", "Work", "Year", "Bear", "Beat", "Blow", "Burn", "Call",
            "Care", "Cast", "Come", "Cook", "Cope", "Cost", "Dare", "Deal", "Deny", "Draw",
            "Drop", "Earn", "Face", "Fail", "Fall", "Fear", "Feel", "Fill", "Find", "Form",
            "Gain", "Give", "Grow", "Hang", "Hate", "Have", "Head", "Hear", "Help", "Hide",
            "Hold", "Hope", "Hurt", "Join", "Jump", "Keep", "Kill", "Know", "Land", "Last",
            "Lead", "Lend", "Lift", "Like", "Link", "Live", "Look", "Lose", "Love", "Make",
            "Mark", "Meet", "Mind", "Miss", "Move", "Must", "Name", "Need", "Note", "Open",
            "Pass", "Pick", "Plan", "Play", "Pray", "Pull", "Push", "Read", "Rely", "Rest",
            "Ride", "Ring", "Rise", "Risk", "Roll", "Rule", "Save", "Seek", "Seem", "Sell",
            "Send", "Shed", "Show", "Shut", "Sign", "Sing", "Slip", "Sort", "Stay", "Step",
            "Stop", "Suit", "Take", "Talk", "Tell", "Tend", "Test", "Turn", "Vary", "View",
            "Vote", "Wait", "Wake", "Walk", "Want", "Warn", "Wash", "Wear", "Will", "Wish",
            "Work", "Able", "Back", "Bare", "Bass", "Blue", "Bold", "Busy", "Calm", "Cold",
            "Cool", "Damp", "Dark", "Dead", "Deaf", "Dear", "Deep", "Dual", "Dull", "Dumb",
            "Easy", "Evil", "Fair", "Fast", "Fine", "Firm", "Flat", "Fond", "Foul", "Free",
            "Full", "Glad", "Good", "Grey", "Grim", "Half", "Hard", "Head", "High", "Holy",
            "Huge", "Just", "Keen", "Kind")
            fun getRandomFourLetterWord(): String {
            return fourLetterWords.random()
        }
    }
    private fun populateGrid() {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val columnCount = wordLength
        val rowCount = maxAttempts

        val totalCells = wordLength * maxAttempts
        for (i in 0 until totalCells) {
            val gridItemView = LayoutInflater.from(this).inflate(R.layout.grid_item, gridLayout, false) as TextView
            gridLayout.addView(gridItemView)
        }
    }
    private fun checkCorrectness(guess:String){
        if (guess.length != randomWord.length) {
            Toast.makeText(this, "Enter a $wordLength-letter word", Toast.LENGTH_SHORT).show()
            return
        }

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until randomWord.length) {
            val cellIndex = attempts * wordLength + i
            val letterView = gridLayout.getChildAt(cellIndex) as TextView
            letterView.text = guess[i].toString()

            if (guess[i] == randomWord[i]) {
                letterView.setBackgroundResource(R.drawable.tile_correct)
            } else if (randomWord.contains(guess[i])) {
                letterView.setBackgroundResource(R.drawable.tile_present)
            } else {
                letterView.setBackgroundResource(R.drawable.tile_absent)
            }
        }
        attempts++
        if (guess == randomWord || attempts == maxAttempts) {
            gameEnd(guess == randomWord)
        }
    }
    private fun startNewGame() {
        clearGrid()
        randomWord = FourLetterWordList.getRandomFourLetterWord().uppercase()
        attempts = 0
        submitButton.text = getString(R.string.guess_string)
        populateGrid()
    }
    private fun clearGrid() {
        gridLayout.removeAllViews()
    }
    private fun gameEnd(won: Boolean) {
        val message = if (won) "Congratulations, you've guessed the word!" else "Try again. The word was $randomWord."
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        // Reset game or provide an option to restart here...
        submitButton.text = getString(R.string.reset_string)
    }
}