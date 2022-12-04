package com.mygdx.game.model

case class GameState(
                      players: Map[String, Player] = Map()
                    )