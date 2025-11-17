package io.github.alerithe.client.utilities;

import io.github.alerithe.client.extensions.LocalPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

public class WorldHelper {
    public static WorldClient getWorld() {
        return GameHelper.getGame().world;
    }

    public static Block getBlock(int x, int y, int z) {
        return getBlock(new BlockPos(x, y, z));
    }

    public static Block getBlock(BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

    public static IBlockState getBlockState(int x, int y, int z) {
        return getBlockState(new BlockPos(x, y, z));
    }

    public static IBlockState getBlockState(BlockPos pos) {
        return getWorld().getBlockState(pos);
    }

    public static double distance(Entity end) {
        return distance(EntityHelper.getUser(),
                end);
    }

    public static double distance(double endX, double endY, double endZ) {
        LocalPlayer user = EntityHelper.getUser();

        return distance(user.posX, user.posY, user.posZ,
                endX, endY, endZ);
    }

    public static double distance(Entity start, Entity end) {
        return distance(start,
                end.posX, end.posY, end.posY);
    }

    public static double distance(Entity start, double endX, double endY, double endZ) {
        return distance(start.posX, start.posY, start.posY,
                endX, endY, endZ);
    }

    public static double distance(double startX, double startY, double startZ, Entity end) {
        return distance(startX, startY, startZ,
                end.posX, end.posY, end.posZ);
    }

    public static double distance(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return Math.sqrt(distanceSq(startX, startY, startZ,
                endX, endY, endZ));
    }

    public static double distanceSq(Entity end) {
        return distanceSq(EntityHelper.getUser(),
                end);
    }

    public static double distanceSq(double startX, double startY, double startZ) {
        LocalPlayer user = EntityHelper.getUser();

        return distanceSq(startX, startY, startZ,
                user.posX, user.posY, user.posZ);
    }

    public static double distanceSq(Entity start, Entity end) {
        return distanceSq(start,
                end.posX, end.posY, end.posZ);
    }

    public static double distanceSq(Entity start, double endX, double endY, double endZ) {
        return distanceSq(start.posX, start.posY, start.posZ,
                endX, endY, endZ);
    }

    public static double distanceSq(double startX, double startY, double startZ, Entity end) {
        return distanceSq(startX, startY, startZ,
                end.posX, end.posY, end.posZ);
    }

    public static double distanceSq(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        double deltaX = endX - startX;
        double deltaY = endY - startY;
        double deltaZ = endZ - startZ;

        return (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
    }
}
