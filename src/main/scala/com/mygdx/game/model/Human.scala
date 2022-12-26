package com.mygdx.game.model

case class Human(params: CreatureParams) extends Creature {
  override def copy(params: CreatureParams): Creature = Human(params)
}
