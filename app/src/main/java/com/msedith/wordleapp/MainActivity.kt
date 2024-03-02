package com.msedith.wordleapp

import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import androidx.core.content.ContextCompat
import nl.dionsegijn.konfetti.KonfettiView

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
            "Area", "Army", "Baby", "Back", "Bend")
            fun getRandomFourLetterWord(): String {
            return fourLetterWords.random()
        }
    }
    private fun populateGrid() {
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        val totalCells = wordLength * maxAttempts
        for (i in 0 until totalCells) {
            val gridItemView = LayoutInflater.from(this).inflate(R.layout.grid_item, gridLayout, false) as TextView
            gridLayout.addView(gridItemView)
        }
    }
    private fun isValidGuess(guess: String): Boolean {
        return guess.length == wordLength && guess.all { it.isLetter() }
    }
    private fun checkCorrectness(guess:String){
        if (!isValidGuess(guess)) {
            Toast.makeText(this, "Invalid guess. Enter a 4-letter word with no numbers or special characters.", Toast.LENGTH_SHORT).show()
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
        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.grayblack)) // Ensure this color is defined in your colors.xml
        findViewById<KonfettiView>(R.id.viewKonfetti).reset()
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

        if (won) {
            val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.light_teal))
            val viewKonfetti = findViewById<KonfettiView>(R.id.viewKonfetti)
            viewKonfetti.build()
                .addColors(Color.parseColor("#9B59B6"), Color.parseColor("#8E44AD")) // Purple shades
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(Size(12))
                .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }
        submitButton.text = getString(R.string.reset_string)
    }
}