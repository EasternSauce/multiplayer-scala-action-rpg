package com.mygdx.game.actions

import com.mygdx.game.GameState
import com.softwaremill.quicklens.ModifyPimp

case class PositionChangeX(value: Int) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    gameState.modify(_.x).setTo(value)
  }
}
