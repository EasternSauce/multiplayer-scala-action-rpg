package com.mygdx.game.message

trait MovementCommand

case class MovementCommandLeft(playerId: String) extends MovementCommand

case class MovementCommandRight(playerId: String) extends MovementCommand

case class MovementCommandUp(playerId: String) extends MovementCommand

case class MovementCommandDown(playerId: String) extends MovementCommand