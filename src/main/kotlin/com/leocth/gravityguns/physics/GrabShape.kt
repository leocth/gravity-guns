package com.leocth.gravityguns.physics

import com.leocth.gravityguns.data.GravityGunsTags
import net.minecraft.block.BlockState
import net.minecraft.block.PistonBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

abstract class GrabShape {
    abstract fun grab(
        user: PlayerEntity,
        world: ServerWorld,
        direction: Direction,
        hitPoint: BlockPos,
        hitState: BlockState,
        power: Double
    ): Iterable<BlockPos>?

    // filtering blocks that cannot be grabbed:
    // 1) it cannot allow mobs to spawn inside, i.e. air & cave air
    // 2) it cannot house block entities, since block entity logic is tricky (although TODO I can add this later...?)
    // 3) it cannot be denied by the deny list
    protected fun isBlockImmobile(world: World, pos: BlockPos, state: BlockState): Boolean =
        state.isAir ||
        world.getBlockEntity(pos) != null ||
        state.block in GravityGunsTags.IMMOBILE_BLOCKS ||
        (
            state.block is PistonBlock &&
            state.get(PistonBlock.EXTENDED) // disallow extended pistons
        )

    protected fun getMobileStateOrNull(world: World, pos: BlockPos): BlockState? {
        val state = world.getBlockState(pos)
        return if (!isBlockImmobile(world, pos, state)) {
            state
        } else {
            null
        }
    }
}

object CubeGrabShape : GrabShape() {
    override fun grab(
        user: PlayerEntity,
        world: ServerWorld,
        direction: Direction,
        hitPoint: BlockPos,
        hitState: BlockState,
        power: Double
    ): Iterable<BlockPos>? {
        // don't even bother if the player is trying to grab an immutable block
        if (isBlockImmobile(world, hitPoint, hitState)) return null

        val half = power.toInt() - 1

        val off = direction.opposite.vector.multiply(half)
        val min = hitPoint.mutableCopy().move(-half, -half, -half).move(off)
        val max = hitPoint.mutableCopy().move(half, half, half).move(off)

        return BlockPos.iterate(min, max)
            .filter { isBlockImmobile(world, it, world.getBlockState(it)) }
    }
}

