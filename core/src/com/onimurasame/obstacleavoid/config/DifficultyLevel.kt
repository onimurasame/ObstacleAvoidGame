package com.onimurasame.obstacleavoid.config

enum class DifficultyLevel(val obstacleSpeed: Float) {

    EASY(GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(GameConfig.MED_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBTACLE_SPEED)

}