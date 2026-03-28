package com.zhanatpaeva.unscramblegame.ui_model

import androidx.lifecycle.ViewModel
import com.zhanatpaeva.unscramblegame.data.GameUiState
import com.zhanatpaeva.unscramblegame.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private  lateinit var currentWord: String
    private var userWords: MutableSet<String> = mutableSetOf()
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
}