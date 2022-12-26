package com.mygdx.game.actions

import com.mygdx.game.model.GameState
import com.mygdx.game.model.ids.CreatureId
import com.softwaremill.quicklens.ModifyPimp

case class RemovePlayer(playerId: CreatureId) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.creatures).using(list => list.removed(playerId))
  }
}
