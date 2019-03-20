package com.onimurasame.obstacleavoid.util

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

inline operator fun TextureAtlas.invoke(regionName: String): TextureRegion? = findRegion(regionName)

inline operator fun TextureAtlas.get(regionName: String): TextureRegion? = findRegion(regionName)