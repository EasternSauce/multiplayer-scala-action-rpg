package com.mygdx.game.model
import com.badlogic.gdx.math.{Vector2 => GdxVector2}

case class Vector2(x: Float, y: Float) {

  def angleDeg(): Float = {
    (new GdxVector2(x, y)).angleDeg()
  }
}
