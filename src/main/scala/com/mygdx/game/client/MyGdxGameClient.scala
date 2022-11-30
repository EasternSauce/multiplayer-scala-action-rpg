package com.mygdx.game.client

import com.badlogic.gdx.backends.lwjgl3.{Lwjgl3Application, Lwjgl3ApplicationConfiguration}
import com.badlogic.gdx.{Game, Gdx, Input}
import com.esotericsoftware.kryonet.{Client, Connection, KryoSerialization, Listener}
import com.mygdx.game.actions.ActionsWrapper
import com.mygdx.game._
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

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
