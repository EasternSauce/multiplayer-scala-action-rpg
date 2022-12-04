package com.mygdx.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.{OrthographicCamera, Texture}
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.{FitViewport, Viewport}
import com.esotericsoftware.kryonet.EndPoint
import com.mygdx.game.model.GameState

abstract class MyGdxGame extends Game {

  val playScreen: MyGdxGamePlayScreen

  var playerSprites: Map[String, Sprite] = _

  protected var batch: SpriteBatch = _
  protected var img: Texture = _

  val endPoint: EndPoint

  var gameState: GameState = _

  var worldViewport: Viewport = _
  var worldCamera: OrthographicCamera = _


  override def create(): Unit = {
    gameState = GameState()

    batch = new SpriteBatch
    img = new Texture("main/badlogic.jpg")

    worldCamera = new OrthographicCamera()

    worldViewport = new FitViewport(
      Constants.ViewpointWorldWidth / Constants.PPM,
      Constants.ViewpointWorldHeight / Constants.PPM,
      worldCamera
    )

    playerSprites = Map()

    establishConnection()

  }

  def updateCamera(): Unit = {

    val camPosition = worldCamera.position

    val camX = 0 // TODO
    val camY = 0

    camPosition.x = (math.floor(camX * 100) / 100).toFloat
    camPosition.y = (math.floor(camY * 100) / 100).toFloat

    worldCamera.update()

  }

  def onRender(): Unit = {
    playerSprites.foreach {
      case (playerId, sprite) => sprite.setPosition(gameState.players(playerId).x, gameState.players(playerId).y)
    }

    ScreenUtils.clear(1, 0, 0, 1)
    batch.begin()
    //    batch.draw(img, gameState.x, gameState.y)
    playerSprites.foreach {
      case (_, sprite) =>
        sprite.draw(batch)

    }
    batch.end()
  }

  def onUpdate(): Unit

  def establishConnection(): Unit


}
