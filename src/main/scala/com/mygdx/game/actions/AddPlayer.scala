package com.mygdx.game.actions

import com.mygdx.game.model.{GameState, Player}
import com.softwaremill.quicklens.ModifyPimp

case class AddPlayer(playerId: String, posX: Float, posY: Float) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    val player = Player(playerId, posX, posY)
    gameState.modify(_.players).using(list => list.updated(playerId, player))
  }
}
