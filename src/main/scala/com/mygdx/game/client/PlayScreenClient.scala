package com.mygdx.game.client

import com.badlogic.gdx.{Gdx, Input, Screen}
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.Viewport
import com.esotericsoftware.kryonet.{Client, Connection, KryoSerialization, Listener}
import com.mygdx.game.actions.ActionsWrapper
import com.mygdx.game.{GameState, MovementCommandDown, MovementCommandLeft, MovementCommandRight, MovementCommandUp, MyGdxGamePlayScreen}
import com.twitter.chill.{Kryo, ScalaKryoInstantiator}

case class PlayScreenClient() extends MyGdxGamePlayScreen {

  var client: Client = _

  override def onUpdate(): Unit = {
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      client.sendTCP(MovementCommandLeft)

    }
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      client.sendTCP(MovementCommandRight)

    }
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      client.sendTCP(MovementCommandUp)

    }
    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      client.sendTCP(MovementCommandDown)

    }

  }

  override def establishConnection(): Unit = {
    val kryo: Kryo = {
      val instantiator = new ScalaKryoInstantiator
      instantiator.setRegistrationRequired(true)
      instantiator.newKryo()
    }

    kryo.register(classOf[GameState])
    kryo.register(classOf[ActionsWrapper])

    client = new Client(8192, 2048, new KryoSerialization(kryo))


    client.start()
    client.connect(5000, "127.0.0.1", 54555, 54777)


    client.addListener(new Listener() {
      override def received(connection: Connection, obj: Any): Unit = {
        obj match {
          case newGameState: GameState =>
            gameState = newGameState
          case ActionsWrapper(tickActions) =>
            val newGameState = tickActions.foldLeft(gameState)((gameState, action) => action.applyToGameState(gameState))

            gameState = newGameState
          case _ =>
        }
      }
    })

  }
}
