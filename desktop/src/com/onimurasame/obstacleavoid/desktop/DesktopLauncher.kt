package com.onimurasame.obstacleavoid.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.onimurasame.obstacleavoid.ObstacleAvoidGame
import com.onimurasame.obstacleavoid.config.GameConfig

class DesktopLauncher {}

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()

    config.width = GameConfig.WIDTH
    config.height = GameConfig.HEIGHT

    LwjglApplication(ObstacleAvoidGame(), config)
}

