package com.mygdx.game.actions

import com.mygdx.game.model.GameState

trait GameStateAction {
  def applyToGameState(gameState: GameState): GameState
}
