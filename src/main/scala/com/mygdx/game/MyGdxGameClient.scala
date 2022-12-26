package com.mygdx.game

import com.badlogic.gdx.backends.lwjgl3.{Lwjgl3Application, Lwjgl3ApplicationConfiguration}
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.{Gdx, Input}
import com.esotericsoftware.kryonet.{Client, Connection, KryoSerialization, Listener}
import com.mygdx.game.actions.{ActionsWrapper, AddPlayer, RemovePlayer}
import com.mygdx.game.message._
import com.mygdx.game.model.GameState
import com.mygdx.game.model.ids.CreatureId
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

object MyGdxGameClient extends MyGdxGame {
  override val playScreen: MyGdxGamePlayScreen = MyGdxGamePlayScreen(this)

  override val endPoint: Client = {
    val kryo: Kryo = {
      val instantiator = new ScalaKryoInstantiator
      instantiator.setRegistrationRequired(true)
      instantiator.newKryo()
    }

    kryo.register(classOf[GameState])
    kryo.register(classOf[ActionsWrapper])

    new Client(8192, 2048, new KryoSerialization(kryo))
  }

  val playerId: CreatureId = CreatureId("Player " + scala.util.Random.nextInt())

  override def create(): Unit = {
    super.create()
    setScreen(playScreen)

  }

  override def onUpdate(): Unit = {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      endPoint.sendTCP(MovementCommandLeft(playerId))

    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      endPoint.sendTCP(MovementCommandRight(playerId))

    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      endPoint.sendTCP(MovementCommandUp(playerId))

    }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      endPoint.sendTCP(MovementCommandDown(playerId))

    }
  }

  override def establishConnection(): Unit = {

    endPoint.start()
    endPoint.connect(5000, "127.0.0.1", 54555, 54777)

    endPoint.addListener(new Listener() {
      override def received(connection: Connection, obj: Any): Unit = {
        obj match {
          case newGameState: GameState =>
            gameState = newGameState
          case ActionsWrapper(tickActions) =>
            val newGameState =
              tickActions.foldLeft(gameState)((gameState, action) => action.applyToGameState(gameState))

            tickActions.foreach {
              case AddPlayer(playerId, _, _) =>
                creatureSprites = creatureSprites.updated(playerId, new Sprite(img, 64, 64))
              case RemovePlayer(playerId) =>
                creatureSprites = creatureSprites.removed(playerId)
              case _ =>
            }

            gameState = newGameState
          case InitialState(initGameState) =>
            gameState = initGameState
            creatureSprites = gameState.creatures.map { case (playerId, _) => (playerId, new Sprite(img, 64, 64)) }
          case _ =>
        }
      }
    })

    endPoint.sendTCP(
      AskInitPlayer(playerId, scala.util.Random.nextInt().abs % 300, scala.util.Random.nextInt().abs % 300)
    )

  }

  override def dispose(): Unit = {
    endPoint.sendTCP(AskDeletePlayer(playerId))
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
