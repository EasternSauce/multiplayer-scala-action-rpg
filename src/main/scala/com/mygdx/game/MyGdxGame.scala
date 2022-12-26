package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.utils.ScreenUtils
import com.esotericsoftware.kryonet.EndPoint
import com.mygdx.game.model.GameState
import com.mygdx.game.model.ids.CreatureId

abstract class MyGdxGame extends Game {

  val playScreen: MyGdxGamePlayScreen

  var creatureSprites: Map[CreatureId, Sprite] = _

  protected var batch: SpriteBatch = _
  protected var img: Texture = _

  val endPoint: EndPoint

  var gameState: GameState = _

  override def create(): Unit = {
    gameState = GameState()

    batch = new SpriteBatch
    img = new Texture("main/badlogic.jpg")

    creatureSprites = Map()

    establishConnection()

    playScreen.init()
  }

  def onRender(): Unit = {
    creatureSprites.foreach {
      case (creatureId, sprite) =>
        if (gameState.creatures.contains(creatureId))
          sprite.setPosition(gameState.creatures(creatureId).params.pos.x, gameState.creatures(creatureId).params.pos.y)
    }

    ScreenUtils.clear(1, 0, 0, 1)
    batch.begin()
    creatureSprites.foreach {
      case (_, sprite) =>
        sprite.draw(batch)

    }
    batch.end()
  }

  def onUpdate(): Unit

  def establishConnection(): Unit

}
