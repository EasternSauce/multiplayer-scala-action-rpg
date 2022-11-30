package com.mygdx.game.actions

import com.mygdx.game.GameState
import com.softwaremill.quicklens.ModifyPimp

case class PositionChangeY(value: Int) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.y).setTo(value)
  }
}
