package com.mygdx.game.actions

import com.mygdx.game.GameState

trait GameStateAction {
  def applyToGameState(gameState: GameState): GameState
}
