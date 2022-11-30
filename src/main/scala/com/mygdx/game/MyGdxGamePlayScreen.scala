package com.mygdx.game

import com.badlogic.gdx.{Game, Screen}
import com.badlogic.gdx.graphics.{OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.{FitViewport, Viewport}

abstract class MyGdxGamePlayScreen extends Screen {

  protected var batch: SpriteBatch = _
  protected var img: Texture = _
  protected var sprite: Sprite = _

  var gameState: GameState = _

  var worldViewport: Viewport = _
  var worldCamera: OrthographicCamera = _

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

    sprite = new Sprite(img, 0, 0, 64, 64)

    establishConnection()
  }

  override def render(delta: Float): Unit = {
    onUpdate()

    updateCamera()
    sprite.setPosition(gameState.x, gameState.y)

    ScreenUtils.clear(1, 0, 0, 1)
    batch.begin()
//    batch.draw(img, gameState.x, gameState.y)
    sprite.draw(batch)
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
