package com.mygdx.game.client

import com.badlogic.gdx.Game
import com.badlogic.gdx.backends.lwjgl3.{Lwjgl3Application, Lwjgl3ApplicationConfiguration}
import com.mygdx.game._

object MyGdxGameClient extends Game {

  var playScreen: MyGdxGamePlayScreen = _

  override def create(): Unit = {
    playScreen = PlayScreenClient()
    setScreen(playScreen)
  }

  def main(arg: Array[String]): Unit = {
    val config = new Lwjgl3ApplicationConfiguration
    config.setTitle("Drop")
    config.setWindowedMode(800, 480)
    config.useVsync(true)
    config.setForegroundFPS(60)
    new Lwjgl3Application(MyGdxGameClient, config)
  }
}
