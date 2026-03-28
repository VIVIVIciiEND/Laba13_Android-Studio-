package com.zhanatpaeva.unscramblegame.ui_model

import androidx.compose.runtime.ScopeUpdateScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zhanatpaeva.unscramblegame.data.GameUiState
import com.zhanatpaeva.unscramblegame.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.zhanatpaeva.unscramblegame.data.MAX_NO_OF_WORDS
import com.zhanatpaeva.unscramblegame.data.SCORE_INCREASE
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private  lateinit var currentWord: String
    private var userWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
    private set
    init {
        resetGame()
    }

    fun resetGame(){
        userWords.clear()
        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWordAndShuffle()
        )
    }
    private fun shuffleCurrentWord(word: String): String{
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord)==word){
            tempWord.shuffle()
        }
        return String(tempWord)
    }
    private fun pickRandomWordAndShuffle(): String{
        currentWord = allWords.random()
        while (userWords.contains(currentWord)){
            currentWord = allWords.random()
        }
        userWords.add(currentWord)
        return shuffleCurrentWord(currentWord)
    }
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }
    private fun updateGameState(updateScope: Int){
        if ( userWords.size == MAX_NO_OF_WORDS){
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false ,
                    score = updateScope ,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false ,
                    currentScrambledWord = pickRandomWordAndShuffle() ,
                    score = updateScope ,
                    currentWordCount = currentState.currentWordCount+1
                )
            }
        }
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentWord , ignoreCase = true)){
            val updateScope = _uiState.value.score + SCORE_INCREASE
            updateGameState(updateScope)
        }else{
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }
    fun skipWord(){
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }



}