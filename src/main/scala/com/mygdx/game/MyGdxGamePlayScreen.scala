package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.{OrthographicCamera, Texture}
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.{FitViewport, Viewport}
import com.esotericsoftware.kryonet.EndPoint
import com.mygdx.game.model.GameState

abstract class MyGdxGamePlayScreen extends Screen {

  protected var batch: SpriteBatch = _
  protected var img: Texture = _
  //  protected var sprite: Sprite = _

  val endPoint: EndPoint

  var gameState: GameState = _

  var worldViewport: Viewport = _
  var worldCamera: OrthographicCamera = _

  var playerSprites: Map[String, Sprite] = _

  override def show(): Unit = {
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

    //    endPoint.addListener(new Listener() {
    //      override def received(connection: Connection, obj: Any): Unit = {
    //        obj match {
    //          case newGameState: GameState =>
    //            gameState = newGameState
    //          case ActionsWrapper(tickActions) =>
    //            val newGameState = tickActions.foldLeft(gameState)((gameState, action) => action.applyToGameState(gameState))
    //
    //            gameState = newGameState
    //          case _ =>
    //        }
    //      }
    //    })
  }

  override def render(delta: Float): Unit = {
    onUpdate()

    updateCamera()

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

  override def dispose(): Unit = {
    batch.dispose()
    img.dispose()
  }

  override def resize(width: Int, height: Int): Unit = {
    worldViewport.update(width, height)
  }

  override def pause(): Unit = {

  }

  override def resume(): Unit = {

  }

  override def hide(): Unit = {

  }

  def onUpdate(): Unit

  def establishConnection(): Unit

  def updateCamera(): Unit = {

    val camPosition = worldCamera.position

    val camX = 0 // TODO
    val camY = 0

    camPosition.x = (math.floor(camX * 100) / 100).toFloat
    camPosition.y = (math.floor(camY * 100) / 100).toFloat

    worldCamera.update()

  }
}
