package com.mygdx.game.model

import com.mygdx.game.model.WorldDirection.WorldDirection
import com.mygdx.game.model.ids.{AreaId, CreatureId}

case class CreatureParams(
  creatureId: CreatureId,
  pos: Vector2,
  textureName: String,
  isPlayer: Boolean,
  neutralStanceFrame: Int,
  textureWidth: Int,
  textureHeight: Int,
  frameCount: Int,
  frameDuration: Int,
  dirMap: Map[WorldDirection, Int],
  animationTimer: SimpleTimer,
  width: Float,
  height: Float,
  movingDir: Vector2,
  currentLife: Float,
  maxLife: Float,
  areaId: AreaId,
  currentStamina: Float,
  maxStamina: Float
) {}
