package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.math.MathUtils
import com.onimurasame.obstacleavoid.config.DifficultyLevel
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.entity.Obstacle
import com.onimurasame.obstacleavoid.entity.Player
import com.onimurasame.obstacleavoid.util.GdxArray
import com.onimurasame.obstacleavoid.util.isNotEmpty


class GameController {

    private val startPlayerY = 1f
    private val startPlayerX = GameConfig.WORLD_WIDTH / 2

    private var obstacleTimer = 0f
    private var scoreTimer = 0f
    private var difficultyLevel = DifficultyLevel.MEDIUM

    val gameOver
        get() = lives <= 0

    val obstacles = GdxArray<Obstacle>()

    var player: Player = Player().apply { setPosition(startPlayerX, startPlayerY) }
        private set
    var score = 0
        private set
    var displayScore = 0
        private set
    var lives = GameConfig.LIVES_START
        private set


    fun update(delta: Float) {
        if(gameOver) return

        player.update()
        blockPlayerFromLeavingTheWorld()

        createNewObstacle(delta)
        updateObstacles()
        removePastObstacles()


        updateScore(delta)
        updateDisplayScore(delta)

        if (playerCollidedWithObstacle()) {
            lives--
        }
    }

    private fun removePastObstacles() {
        if(obstacles.isNotEmpty()) {
            val first = obstacles.first()
            val minObstacleY = -Obstacle.SIZE

            if (first.y < minObstacleY) {
                obstacles.removeValue(first, true)
            }
        }
    }

    private fun updateScore(delta: Float) {
        scoreTimer += delta

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            scoreTimer = 0f
            score += MathUtils.random(1, 5)
        }
    }

    private fun updateDisplayScore(delta: Float) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (60 * delta).toInt())
        }
    }

    private fun playerCollidedWithObstacle(): Boolean {
        obstacles.forEach {
            if (!it.isHit && it.isColliding(player)) {
                return true
            }
        }

        return false
    }

    private fun updateObstacles() {
        obstacles.forEach { it.update() }
    }

    private fun createNewObstacle(delta: Float) {
        obstacleTimer += delta

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            obstacleTimer = 0f

            val obstacleX = MathUtils.random(0f + Obstacle.HALF_SIZE, GameConfig.WORLD_WIDTH - Obstacle.HALF_SIZE)
            val obstacle = Obstacle()

            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }

    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, Player.HALF_SIZE, GameConfig.WORLD_WIDTH - Player.HALF_SIZE)
    }
}