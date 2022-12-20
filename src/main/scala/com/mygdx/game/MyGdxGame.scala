package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.utils.ScreenUtils
import com.esotericsoftware.kryonet.EndPoint
import com.mygdx.game.model.GameState

abstract class MyGdxGame extends Game {

  val playScreen: MyGdxGamePlayScreen

  var playerSprites: Map[String, Sprite] = _

  protected var batch: SpriteBatch = _
  protected var img: Texture = _

  val endPoint: EndPoint

  var gameState: GameState = _

  override def create(): Unit = {
    gameState = GameState()

    batch = new SpriteBatch
    img = new Texture("main/badlogic.jpg")

    playerSprites = Map()

    establishConnection()

    playScreen.init()
  }


  def onRender(): Unit = {
    playerSprites.foreach {
      case (playerId, sprite) => if (gameState.players.contains(playerId)) sprite.setPosition(gameState.players(playerId).x, gameState.players(playerId).y)
    }

    ScreenUtils.clear(1, 0, 0, 1)
    batch.begin()
    playerSprites.foreach {
      case (_, sprite) =>
        sprite.draw(batch)

    }
    batch.end()
  }

  def onUpdate(): Unit

  def establishConnection(): Unit


}
