package com.mygdx.game.server

import com.badlogic.gdx.Game
import com.badlogic.gdx.backends.lwjgl3.{Lwjgl3Application, Lwjgl3ApplicationConfiguration}
import com.esotericsoftware.kryonet.{Connection, KryoSerialization, Listener, Server}
import com.mygdx.game._
import com.mygdx.game.client.PlayScreenClient
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

import scala.jdk.CollectionConverters.ListHasAsScala

object MyGdxGameServer extends Game {

  var playScreen: MyGdxGamePlayScreen = _

  override def create(): Unit = {
    playScreen = PlayScreenServer()
    setScreen(playScreen)
  }

  def main(arg: Array[String]): Unit = {
    val config = new Lwjgl3ApplicationConfiguration
    config.setTitle("Drop")
    config.setWindowedMode(800, 480)
    config.useVsync(true)
    config.setForegroundFPS(60)
    new Lwjgl3Application(MyGdxGameServer, config)
  }
}
