package com.mygdx.game.actions

import com.mygdx.game.model.GameState
import com.softwaremill.quicklens.ModifyPimp

case class RemovePlayer(playerId: String) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.players).using(list => list.removed(playerId))
  }
}
