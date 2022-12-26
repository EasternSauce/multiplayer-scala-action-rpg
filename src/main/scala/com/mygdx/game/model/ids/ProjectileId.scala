package com.mygdx.game.model.ids

case class ProjectileId(value: String)
object ProjectileId {
  def derive(creatureId: CreatureId, areaId: AreaId, abilityId: AbilityId, counter: Long): ProjectileId =
    ProjectileId(creatureId.value + "_" + areaId.value + "_" + abilityId.value + "_" + counter)
//    def derive(ability: Ability, counter: Long): ProjectileId =
//      derive(ability.params.creatureId, ability.params.areaId, ability.params.id, counter)
}
