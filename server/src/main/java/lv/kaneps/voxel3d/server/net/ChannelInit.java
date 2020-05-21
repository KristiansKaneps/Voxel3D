package lv.kaneps.voxel3d.server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.SocketChannelConfig;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lv.kaneps.voxel3d.common.net.encoders._LastEncoder;
import lv.kaneps.voxel3d.server.net.decoders.ConnectedDecoder;
import lv.kaneps.voxel3d.server.net.decoders.GameplayDecoder;
import lv.kaneps.voxel3d.server.net.handlers.ConnectedHandler;
import lv.kaneps.voxel3d.server.net.handlers.PlayerUpdateHandler;

public class ChannelInit extends ChannelInitializer<SocketChannel>
{
	private static final int IO_TIMEOUT = 30;

	public final Server server;

	public ChannelInit(Server server)
	{
		this.server = server;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		SocketChannelConfig cfg = ch.config();
		cfg.setOption(ChannelOption.SO_SNDBUF, 1048576);
		cfg.setOption(ChannelOption.SO_RCVBUF, 1048576);
		cfg.setOption(ChannelOption.TCP_NODELAY, true);

		ChannelPipeline p = ch.pipeline();
		p.addLast("readth", new ReadTimeoutHandler(IO_TIMEOUT));
		p.addLast("writeth", new WriteTimeoutHandler(IO_TIMEOUT));

		p.addLast(new _LastEncoder());

		p.addLast(new ConnectedDecoder(server));
		p.addLast(new GameplayDecoder(server));

		p.addLast(new PlayerUpdateHandler(server));

		p.addLast(new ConnectedHandler(server));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		cause.printStackTrace();
	}
}
