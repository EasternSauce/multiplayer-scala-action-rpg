package com.mygdx.game.model

import com.mygdx.game.model.ids.{AreaId, CreatureId}
import com.softwaremill.quicklens.{QuicklensMapAt, modify}

case class GameState(
  creatures: Map[CreatureId, Creature] = Map(),
  areas: Map[AreaId, Area] = Map(),
  currentAreaId: Option[AreaId] = None,
  defaultAreaId: AreaId = AreaId("placeholder")
) {
  def getPlayers: Map[CreatureId, Creature] = creatures.filter(_._2.params.isPlayer)
}

object GameState {
  def modifyCreature(action: Creature => Creature)(implicit creatureId: CreatureId, gameState: GameState): GameState =
    modify(gameState)(_.creatures.at(creatureId)).using(action)

  def getCreature(implicit creatureId: CreatureId, gameState: GameState): Creature =
    gameState.creatures(creatureId)
}
