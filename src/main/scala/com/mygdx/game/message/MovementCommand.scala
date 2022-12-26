package com.mygdx.game.message

import com.mygdx.game.model.ids.CreatureId

trait MovementCommand

case class MovementCommandLeft(playerId: CreatureId) extends MovementCommand

case class MovementCommandRight(playerId: CreatureId) extends MovementCommand

case class MovementCommandUp(playerId: CreatureId) extends MovementCommand

case class MovementCommandDown(playerId: CreatureId) extends MovementCommand
