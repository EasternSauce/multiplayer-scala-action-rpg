package com.mygdx.game.model

import com.mygdx.game.model.WorldDirection.WorldDirection

abstract class Creature {
  val params: CreatureParams

  def isAlive: Boolean = true // TODO

  def isMoving: Boolean = true

  def facingDirection: WorldDirection =
    params.movingDir.angleDeg() match {
      case angle if angle >= 45 && angle < 135  => WorldDirection.Up
      case angle if angle >= 135 && angle < 225 => WorldDirection.Left
      case angle if angle >= 225 && angle < 315 => WorldDirection.Down
      case _                                    => WorldDirection.Right
    }
  def copy(params: CreatureParams): Creature
}
