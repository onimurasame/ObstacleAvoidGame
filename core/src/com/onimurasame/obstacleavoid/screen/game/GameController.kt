package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pools
import com.onimurasame.obstacleavoid.config.DifficultyLevel
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.entity.Obstacle
import com.onimurasame.obstacleavoid.entity.Player
import com.onimurasame.obstacleavoid.util.GdxArray
import com.onimurasame.obstacleavoid.util.isKeyPressed
import com.onimurasame.obstacleavoid.util.isNotEmpty
import com.onimurasame.obstacleavoid.util.logger


class GameController {

    companion object {
            @JvmStatic
            private val log = logger<GameController>()
        }

    private val startPlayerY = 1f - Player.HALF_SIZE
    private val startPlayerX = (GameConfig.WORLD_WIDTH - Player.SIZE) / 2

    private var obstacleTimer = 0f
    private var scoreTimer = 0f
    private var difficultyLevel = DifficultyLevel.MEDIUM

    val gameOver
        get() = lives <= 0

    val obstacles = GdxArray<Obstacle>()

    var player: Player = Player().apply { setPosition(startPlayerX, startPlayerY); setSize(Player.SIZE, Player.SIZE) }
        private set
    var score = 0
        private set
    var displayScore = 0
        private set
    var lives = GameConfig.LIVES_START
        private set

    private val obstaclePool = Pools.get(Obstacle::class.java, 20)

    fun update(delta: Float) {
        if(gameOver) return

        var xSpeed = 0f

        when {
            Input.Keys.D.isKeyPressed() -> xSpeed = Player.MAX_X_SPEED
            Input.Keys.A.isKeyPressed() -> xSpeed = -Player.MAX_X_SPEED
        }

        player.x += xSpeed

        blockPlayerFromLeavingTheWorld()

        createNewObstacle(delta)
        updateObstacles()
        removePastObstacles()


        updateScore(delta)
        updateDisplayScore(delta)

        if (playerCollidedWithObstacle()) {
            log.debug("Collision detected...")
            lives--

            when {
                gameOver -> log.debug("Game Over!")
                else -> restart()
            }
        }
    }

    private fun restart() {
        obstaclePool.freeAll(obstacles)
        obstacles.clear()
        player.setPosition(startPlayerX, startPlayerY)
    }

    private fun removePastObstacles() {
        if(obstacles.isNotEmpty()) {
            val first = obstacles.first()
            val minObstacleY = -Obstacle.SIZE

            if (first.y < minObstacleY) {
                obstaclePool.free(first)
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

            val obstacleX = MathUtils.random(0f, GameConfig.WORLD_WIDTH - Obstacle.SIZE)
            val obstacle = obstaclePool.obtain()

            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            obstacle.setSize(Obstacle.SIZE, Obstacle.SIZE)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }

    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, 0f, GameConfig.WORLD_WIDTH - Player.SIZE)
    }
}