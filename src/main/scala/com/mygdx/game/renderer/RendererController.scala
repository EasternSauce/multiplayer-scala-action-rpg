package com.mygdx.game.renderer

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.{Color, OrthographicCamera}
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.model.GameState.getCreature
import com.mygdx.game.model.ids.{AreaId, CreatureId}
import com.mygdx.game.model.{GameState, Vector2}

object RendererController {
  var areaRenderers: Map[AreaId, AreaRenderer] = _

  var creatureSpriteRenderers: Map[CreatureId, CreatureRenderer] = _
//  var abilitySpriteRenderers: Map[AbilityId, AbilityRenderer] = _
//  var areaGateRenderers: List[AreaGateRenderer] = _

  var atlas: TextureAtlas = _

  var playerId: CreatureId = _

  var worldCamera: OrthographicCamera = _

  def init(
    atlas: TextureAtlas,
    maps: Map[AreaId, TiledMap] /*, areaGates: List[AreaGateBody]*/,
    mapScale: Float,
    playerId: CreatureId,
    worldCamera: OrthographicCamera
  )(implicit gameState: GameState): Unit = {
    this.atlas = atlas

    areaRenderers = maps.map { case (areaId, map) => areaId -> AreaRenderer(areaId, map, mapScale) }

    areaRenderers.values.foreach(_.init())

    creatureSpriteRenderers =
      gameState.creatures.keys.map(creatureId => creatureId -> CreatureRenderer(creatureId)).toMap
    creatureSpriteRenderers.values.foreach(_.init(atlas))

//    abilitySpriteRenderers = Map()
//
//    areaGateRenderers = areaGates.map(AreaGateRenderer)

    this.playerId = playerId
    this.worldCamera = worldCamera
  }

  def update()(implicit gameState: GameState): Unit = {
    creatureSpriteRenderers.foreach {
      case (_, renderer) => renderer.update()
    }
//    abilitySpriteRenderers.foreach {
//      case (_, renderer) => renderer.update()
//    }

    if (gameState.currentAreaId.nonEmpty) {
      areaRenderers(gameState.currentAreaId.get).setView(worldCamera)
    } else {
      areaRenderers(gameState.defaultAreaId).setView(worldCamera)
    }

  }

//  def addRenderer(abilityId: AbilityId)(implicit gameState: GameState): Unit = {
//    if (!abilitySpriteRenderers.contains(abilityId)) {
//
//      abilitySpriteRenderers = abilitySpriteRenderers.updated(abilityId, AbilityRenderer(abilityId))
//      abilitySpriteRenderers(abilityId).init(atlas)
//    }
//  }

  def renderAliveCreatures(drawingLayer: DrawingLayer, debugEnabled: Boolean)(implicit gameState: GameState): Unit = {

    gameState.creatures.filter { case (_, creature) => creature.isAlive }.keys.foreach { implicit creatureId =>
      if (
        creatureSpriteRenderers.contains(creatureId) &&
        gameState.currentAreaId.contains(getCreature.params.areaId)
      )
        creatureSpriteRenderers(creatureId).render(drawingLayer)
    }

    gameState.creatures.filter { case (_, creature) => creature.isAlive }.keys.foreach { creatureId =>
      if (
        creatureSpriteRenderers.contains(creatureId) &&
        gameState.currentAreaId.contains(gameState.creatures(creatureId).params.areaId)
      )
        creatureSpriteRenderers(creatureId).renderLifeBar(drawingLayer, gameState)
    }

  }

  def renderDeadCreatures(batch: DrawingLayer, debugEnabled: Boolean)(implicit gameState: GameState): Unit =
    gameState.creatures.filter { case (_, creature) => !creature.isAlive }.keys.foreach { creatureId =>
      if (
        creatureSpriteRenderers.contains(creatureId) &&
        gameState.currentAreaId.contains(gameState.creatures(creatureId).params.areaId)
      )
        creatureSpriteRenderers(creatureId).render(batch)
    }

//  def renderAbilities(drawingLayer: DrawingLayer)(implicit gameState: GameState): Unit =
//    gameState.abilities.keys.foreach { abilityId =>
//      implicit val aId: AbilityId = abilityId
//      val areaId = getAbility.params.areaId
//      if (abilitySpriteRenderers.contains(abilityId) && gameState.currentAreaId == areaId) {
//        abilitySpriteRenderers(abilityId).render(drawingLayer)
//      }
//    }

  def renderLifeAndStamina(drawingLayer: DrawingLayer)(implicit gameState: GameState): Unit = {
    val player = gameState.creatures(playerId)

    val maxLifeRect = new Rectangle(10, 40, 100, 10)
    val lifeRect =
      new Rectangle(10, 40, 100 * player.params.currentLife / player.params.maxLife, 10)
    val maxStaminaRect = new Rectangle(10, 25, 100, 10)
    val staminaRect =
      new Rectangle(10, 25, 100 * player.params.currentStamina / player.params.maxStamina, 10)

    drawingLayer.shapeDrawer.filledRectangle(maxLifeRect, Color.ORANGE)

    if (player.params.currentLife <= player.params.maxLife) {
      drawingLayer.shapeDrawer.filledRectangle(lifeRect, Color.RED)
    } else {
      drawingLayer.shapeDrawer.filledRectangle(maxLifeRect, Color.ROYAL)
    }

    drawingLayer.shapeDrawer.filledRectangle(maxStaminaRect, Color.ORANGE)
    drawingLayer.shapeDrawer.filledRectangle(staminaRect, Color.GREEN)
  }

  def renderHud(drawingLayer: DrawingLayer, mousePosition: Vector2)(implicit gameState: GameState): Unit = {

//    inventoryRenderer.render(gameState, batch, mousePosition)
//
//    lootPickupMenuRenderer.render(gameState, batch, mousePosition)

    renderLifeAndStamina(drawingLayer)
  }

//  def renderAreaGates(gameState: GameState, drawingLayer: DrawingLayer): Unit = {
//    areaGateRenderers.foreach(_.render(gameState, drawingLayer))
//  }
}
