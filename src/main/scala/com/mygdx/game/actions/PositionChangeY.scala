package com.mygdx.game.actions

import com.mygdx.game.model.GameState
import com.softwaremill.quicklens.{ModifyPimp, QuicklensMapAt}

case class PositionChangeY(playerId: String, value: Float) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.players.at(playerId).y).setTo(value)
  }
}
