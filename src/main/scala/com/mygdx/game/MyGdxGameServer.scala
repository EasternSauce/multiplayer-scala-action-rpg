package com.mygdx.game

import com.badlogic.gdx.backends.lwjgl3.{Lwjgl3Application, Lwjgl3ApplicationConfiguration}
import com.badlogic.gdx.graphics.g2d.Sprite
import com.esotericsoftware.kryonet.{Connection, KryoSerialization, Listener, Server}
import com.mygdx.game.actions._
import com.mygdx.game.message._
import com.mygdx.game.model.GameState
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

import scala.jdk.CollectionConverters.CollectionHasAsScala

object MyGdxGameServer extends MyGdxGame {
  override val playScreen: MyGdxGamePlayScreen = MyGdxGamePlayScreen(this)

  private val tickActions: java.util.Vector[GameStateAction] = new java.util.Vector()

  override val endPoint: Server = {
    val kryo: Kryo = {
      val instantiator = new ScalaKryoInstantiator
      instantiator.setRegistrationRequired(false)
      instantiator.newKryo()
    }

    kryo.register(classOf[GameState])
    kryo.register(classOf[ActionsWrapper])

    new Server(16384, 2048, new KryoSerialization(kryo))
  }

  override def create(): Unit = {
    super.create()
    setScreen(playScreen)
  }

  override def onUpdate(): Unit = {
    // we need to copy before we convert, otherwise ConcurrentModificationExeption happens
    val actions = new java.util.Vector[GameStateAction](tickActions).asScala.toList

    actions.foreach {
      case AddPlayer(playerId, _, _) =>
        playerSprites = playerSprites.updated(playerId, new Sprite(img, 64, 64))
      case RemovePlayer(playerId) =>
        playerSprites = playerSprites.removed(playerId)
      case _ =>
    }

    val newGameState = actions.foldLeft(gameState)((gameState, action) => action.applyToGameState(gameState))

    gameState = newGameState

    endPoint.sendToAllTCP(ActionsWrapper(actions))

    tickActions.clear()
  }

  override def establishConnection(): Unit = {

    new Thread() {
      override def run(): Unit = runServer()
    }.start()

    new Thread() {
      override def run(): Unit = broadcastGameState()
    }.start()

  }

  private def broadcastGameState(): Unit = {
    while (true) {
      Thread.sleep(3000)
      endPoint.sendToAllTCP(gameState)
    }
  }

  private def runServer(): Unit = {
    endPoint.start()
    endPoint.bind(54555, 54777)

    endPoint.addListener(new Listener() {
      override def received(connection: Connection, obj: Any): Unit = {
        obj match {

          case MovementCommandUp(playerId) =>
            val posChange = PositionChangeY(playerId, gameState.players(playerId).y + 1)
            tickActions.add(posChange)

          case MovementCommandDown(playerId) =>
            val posChange = PositionChangeY(playerId, gameState.players(playerId).y - 1)
            tickActions.add(posChange)

          case MovementCommandLeft(playerId) =>
            val posChange = PositionChangeX(playerId, gameState.players(playerId).x - 1)
            tickActions.add(posChange)

          case MovementCommandRight(playerId) =>
            val posChange = PositionChangeX(playerId, gameState.players(playerId).x + 1)
            tickActions.add(posChange)

          case AskInitPlayer(playerId, x, y) =>
            val addPlayer = AddPlayer(playerId, x, y)
            tickActions.add(addPlayer)

            connection.sendTCP(InitialState(gameState))

          case AskDeletePlayer(playerId) =>
            val removePlayer = RemovePlayer(playerId)
            tickActions.add(removePlayer)

          case _ =>
        }
      }
    })
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
