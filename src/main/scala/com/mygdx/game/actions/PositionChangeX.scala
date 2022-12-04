package com.mygdx.game.actions

import com.mygdx.game.model.GameState
import com.softwaremill.quicklens.{ModifyPimp, QuicklensMapAt}

case class PositionChangeX(playerId: String, value: Float) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.players.at(playerId).x).setTo(value)
  }
}
