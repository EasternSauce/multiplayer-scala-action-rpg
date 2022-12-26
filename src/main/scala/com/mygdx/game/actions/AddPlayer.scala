package com.mygdx.game.actions

import com.mygdx.game.model.ids.CreatureId
import com.mygdx.game.model._
import com.softwaremill.quicklens.ModifyPimp

case class AddPlayer(playerId: CreatureId, posX: Float, posY: Float) extends GameStateAction {
  override def applyToGameState(gameState: GameState): GameState = {
    val player = Human(
      CreatureParams(
        creatureId = playerId,
        pos = Vector2(posX, posY),
        textureName = "placeholder",
        isPlayer = true,
        neutralStanceFrame = 0,
        textureWidth = 64,
        textureHeight = 64,
        frameCount = 8,
        frameDuration = 1,
        dirMap = Map(),
        animationTimer = SimpleTimer(),
        width = 2,
        height = 2,
        movingDir = Vector2(0, 0),
        currentLife = 100f,
        maxLife = 100f,
        areaId = gameState.defaultAreaId,
        currentStamina = 100f,
        maxStamina = 100f
      )
    )
    gameState.modify(_.creatures).using(list => list.updated(playerId, player))
  }
}
