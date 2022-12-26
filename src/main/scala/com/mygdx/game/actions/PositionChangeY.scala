package com.mygdx.game.actions

import com.mygdx.game.model.GameState
import com.mygdx.game.model.ids.CreatureId
import com.softwaremill.quicklens.{ModifyPimp, QuicklensMapAt}

case class PositionChangeY(playerId: CreatureId, value: Float) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.creatures.at(playerId).params.pos.y).setTo(value)
  }
}
