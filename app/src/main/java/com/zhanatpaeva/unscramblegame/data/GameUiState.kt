package com.zhanatpaeva.unscramblegame.data

import android.text.BoringLayout

data class GameUiState(
    val currentScrambledWord: String = "" ,
    val currentWordCount : Int = 1 ,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false ,
    val isGameOver: Boolean= false
)