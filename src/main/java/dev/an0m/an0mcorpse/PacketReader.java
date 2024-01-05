package dev.an0m.an0mcorpse;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R3.PacketPlayInUseEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketReader {

    private final Player player;
    private final int count = 0;

    public PacketReader(Player player) {
        this.player = player;
    }

    public boolean inject() {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        Channel channel = craftPlayer.getHandle().playerConnection.networkManager.channel;

        if (channel.pipeline().get("Injector") != null) return false;

        channel.pipeline().addAfter("decoder", "Injector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {

            @Override
            protected void decode(ChannelHandlerContext context, PacketPlayInUseEntity packet, List<Object> list) throws Exception {
                list.add(packet); // Do not touch

                //int id = (int) getValue(packet, "a");


            }
        });
        return true;
    }
}
