package com.mygdx.game.server

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.esotericsoftware.kryonet.{Connection, KryoSerialization, Listener, Server}
import com.mygdx.game.actions.{ActionsWrapper, GameStateAction, PositionChangeX, PositionChangeY}
import com.mygdx.game.{GameState, MovementCommandDown, MovementCommandLeft, MovementCommandRight, MovementCommandUp, MyGdxGamePlayScreen}
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

import scala.jdk.CollectionConverters.CollectionHasAsScala

case class PlayScreenServer() extends MyGdxGamePlayScreen {

  private val tickActions: java.util.Vector[GameStateAction] = new java.util.Vector()

  var server: Server = _

  private def broadcastGameState(server: Server): Unit = {
    while (true) {
      Thread.sleep(3000)
      server.sendToAllTCP(gameState)
    }
  }

  private def runServer(server: Server): Unit = {
    server.start()
    server.bind(54555, 54777)

    server.addListener(new Listener() {
      override def received(connection: Connection, obj: Any): Unit = {
        obj match {

          case MovementCommandUp =>
            val posChange = PositionChangeY(gameState.y + 1)
            tickActions.add(posChange)

          case MovementCommandDown =>
            val posChange = PositionChangeY(gameState.y - 1)
            tickActions.add(posChange)

          case MovementCommandLeft =>
            val posChange = PositionChangeX(gameState.x - 1)
            tickActions.add(posChange)

          case MovementCommandRight =>
            val posChange = PositionChangeX(gameState.x + 1)
            tickActions.add(posChange)

          case _ =>
        }
      }
    })
  }


  override def onUpdate(): Unit = {
    // we need to copy before we convert, otherwise ConcurrentModificationExeption happens
    val actions = new java.util.Vector[GameStateAction](tickActions).asScala.toList

    val newGameState = actions.foldLeft(gameState)((gameState, action) => action.applyToGameState(gameState))

    gameState = newGameState

    server.sendToAllTCP(ActionsWrapper(actions))

    tickActions.clear()
  }

  override def establishConnection(): Unit = {
    val kryo: Kryo = {
      val instantiator = new ScalaKryoInstantiator
      instantiator.setRegistrationRequired(false)
      instantiator.newKryo()
    }

    kryo.register(classOf[GameState])
    kryo.register(classOf[ActionsWrapper])

    server = new Server(16384, 2048, new KryoSerialization(kryo))

    new Thread() {
      override def run(): Unit = runServer(server)
    }.start()

    new Thread() {
      override def run(): Unit = broadcastGameState(server)
    }.start()

  }
}
