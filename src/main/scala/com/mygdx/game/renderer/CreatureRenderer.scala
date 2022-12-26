package com.mygdx.game.renderer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.math.Rectangle
import com.mygdx.game.model.GameState
import com.mygdx.game.model.GameState.getCreature
import com.mygdx.game.model.WorldDirection.WorldDirection
import com.mygdx.game.model.ids.CreatureId

case class CreatureRenderer(creatureId: CreatureId) {
  implicit val _creatureId: CreatureId = creatureId
  var sprite: Sprite = _

  var facingTextures: Array[TextureRegion] = _

  var runningAnimations: Array[Animation[TextureRegion]] = _

  var textureRegion: TextureRegion = _

  def init(atlas: TextureAtlas)(implicit gameState: GameState): Unit = {
    sprite = new Sprite()

    facingTextures = new Array[TextureRegion](4)

    runningAnimations = new Array[Animation[TextureRegion]](4)

    val creature = gameState.creatures(creatureId)

    textureRegion = atlas.findRegion(creature.params.textureName)

    for (i <- 0 until 4)
      facingTextures(i) = new TextureRegion(
        textureRegion,
        creature.params.neutralStanceFrame * creature.params.textureWidth,
        i * creature.params.textureHeight,
        creature.params.textureWidth,
        creature.params.textureHeight
      )

    for (i <- 0 until 4) {
      val frames =
        for { j <- (0 until creature.params.frameCount).toArray } yield new TextureRegion(
          textureRegion,
          j * creature.params.textureWidth,
          i * creature.params.textureHeight,
          creature.params.textureWidth,
          creature.params.textureHeight
        )
      runningAnimations(i) = new Animation[TextureRegion](creature.params.frameDuration, frames: _*)

    }

  }

  def runningAnimation(currentDirection: WorldDirection)(implicit gameState: GameState): TextureRegion = {
    val creature = gameState.creatures(creatureId)

    runningAnimations(creature.params.dirMap(currentDirection))
      .getKeyFrame(creature.params.animationTimer.time, true)
  }

  def facingTexture(gameState: GameState, currentDirection: WorldDirection): TextureRegion = {
    val creature = gameState.creatures(creatureId)

    facingTextures(creature.params.dirMap(currentDirection))
  }

  def update()(implicit gameState: GameState): Unit = {
    val creature = gameState.creatures(creatureId)

    sprite.setCenter(getCreature.params.pos.x, getCreature.params.pos.y)
    sprite.setSize(getCreature.params.width, getCreature.params.height)

    if (creature.isAlive) {
      val texture =
        if (!getCreature.isMoving) facingTexture(gameState, getCreature.facingDirection)
        else runningAnimation(getCreature.facingDirection)
      sprite.setRegion(texture)

//      if (creature.isEffectActive("immunityFrames")) {
//        val alpha = creature.params.effects("immunityFrames").remainingTime * 35f
//        val colorComponent = 0.3f + 0.7f * (Math.sin(alpha).toFloat + 1f) / 2f
//        sprite.setColor(1f, colorComponent, colorComponent, 1f)
//      } else {
      sprite.setColor(1, 1, 1, 1)
//      }
    } else {
      sprite.setOriginCenter()
      sprite.setRotation(90f)
    }

  }

  def render(drawingLayer: DrawingLayer): Unit =
    sprite.draw(drawingLayer.spriteBatch)

  def renderLifeBar(drawingLayer: DrawingLayer, gameState: GameState): Unit = {
    val lifeBarHeight = 0.16f
    val lifeBarWidth = 2.0f

    val creature = gameState.creatures(creatureId)

    val currentLifeBarWidth = lifeBarWidth * creature.params.currentLife / creature.params.maxLife
    val barPosX = creature.params.pos.x - lifeBarWidth / 2
    val barPosY = creature.params.pos.y + sprite.getWidth / 2 + 0.3125f

    drawingLayer
      .filledRectangle(new Rectangle(barPosX, barPosY, lifeBarWidth, lifeBarHeight), Color.ORANGE)
    if (creature.params.currentLife <= creature.params.maxLife)
      drawingLayer
        .filledRectangle(new Rectangle(barPosX, barPosY, currentLifeBarWidth, lifeBarHeight), Color.RED)
    else
      drawingLayer
        .filledRectangle(new Rectangle(barPosX, barPosY, lifeBarWidth, lifeBarHeight), Color.ROYAL)

  }
}
