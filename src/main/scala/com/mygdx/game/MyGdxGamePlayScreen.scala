package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.{Box2DDebugRenderer, World}
import com.badlogic.gdx.utils.viewport.FitViewport

case class MyGdxGamePlayScreen(game: MyGdxGame) extends Screen {

  var world: World = _

  var debugRenderer: Box2DDebugRenderer = _

  var worldCamera: OrthographicCamera = _

  var worldViewport: FitViewport = _

  def init(): Unit = {
    world = new World(new Vector2(0, 0), true)

    debugRenderer = new Box2DDebugRenderer()

    worldCamera = new OrthographicCamera()

    worldViewport = new FitViewport(
      Constants.ViewpointWorldWidth / Constants.PPM,
      Constants.ViewpointWorldHeight / Constants.PPM,
      worldCamera
    )
  }

  override def render(delta: Float): Unit = {
    world.step(1 / 60f, 6, 2)
    debugRenderer.render(world, worldCamera.combined)

    game.onUpdate()

    updateCamera()

    game.onRender()
  }

  override def resize(width: Int, height: Int): Unit = {
    worldViewport.update(width, height)
  }

  def updateCamera(): Unit = {

    val camPosition = worldCamera.position

    val camX = 0 // TODO
    val camY = 0

    camPosition.x = (math.floor(camX * 100) / 100).toFloat
    camPosition.y = (math.floor(camY * 100) / 100).toFloat

    worldCamera.update()

  }

  override def show(): Unit = {}

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {}

}
