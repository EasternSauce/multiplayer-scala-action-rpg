package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.viewport.Viewport

case class MyGdxGamePlayScreen(game: MyGdxGame) extends Screen {

  var worldViewport: Viewport = _

  override def render(delta: Float): Unit = {
    game.onUpdate()

    game.updateCamera()

    game.onRender()
  }

  override def resize(width: Int, height: Int): Unit = {
    game.worldViewport.update(width, height)
  }

  override def show(): Unit = {}

  override def pause(): Unit = {}

  override def resume(): Unit = {}

  override def hide(): Unit = {}

  override def dispose(): Unit = {}

}
