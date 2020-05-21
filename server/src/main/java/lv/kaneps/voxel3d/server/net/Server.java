package lv.kaneps.voxel3d.server.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lv.kaneps.voxel3d.server.GameServer;
import lv.kaneps.voxel3d.server.utils.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Server
{
	public static Server createServer(GameServer gs, int port)
	{
		return createServer(gs, null, port);
	}

	public static Server createServer(GameServer gs, String host, int port)
	{
		return new Server(gs, host, port);
	}

	private final Map<String, ChannelHandlerContext> channelContextMap = new HashMap<>();
	private final Map<String, Integer> playerMap = new HashMap<>();

	private final AtomicReference<Channel> ch = new AtomicReference<>();
	private final ChannelInit channelInit;

	public final GameServer gs;
	public final String host;
	public final int port;

	private Server(GameServer gs, String host, int port)
	{
		this.gs = gs;
		this.host = host;
		this.port = port;

		channelInit = new ChannelInit(this);
	}

	public void broadcast(Object msg)
	{
		synchronized (channelContextMap)
		{
			for(Map.Entry<String, ChannelHandlerContext> entry : channelContextMap.entrySet())
			{
				ChannelHandlerContext ctx = entry.getValue();
				if(ctx == null) continue;
				ctx.writeAndFlush(msg);
			}
		}
	}

	public void addPlayerMapping(String id, int pId)
	{
		synchronized (playerMap)
		{
			playerMap.put(id, pId);
		}
	}

	public void removePlayerMapping(String id)
	{
		synchronized (playerMap)
		{
			playerMap.remove(id);
		}
	}

	public int getPlayerMapping(String id)
	{
		return playerMap.get(id);
	}

	public void setChannelContext(String id, ChannelHandlerContext ctx)
	{
		synchronized (channelContextMap)
		{
			channelContextMap.put(id, ctx);
		}
	}

	public void deleteChannelContext(String id)
	{
		synchronized (channelContextMap)
		{
			channelContextMap.remove(id);
		}
	}

	public ChannelHandlerContext getChannelContext(String id)
	{
		return channelContextMap.get(id);
	}

	public boolean containsChannelContext(String id)
	{
		return channelContextMap.containsKey(id);
	}

	public boolean isRunning()
	{
		return ch.get() != null;
	}

	public synchronized void start()
	{
		if (isRunning()) return;

		final Runnable runnable = () -> {
			EventLoopGroup gBoss = new NioEventLoopGroup(1);
			EventLoopGroup gWork = new NioEventLoopGroup();
			try
			{
				ServerBootstrap b = new ServerBootstrap();
				b.group(gBoss, gWork)
				 .channel(NioServerSocketChannel.class)
				 .option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(2 * 64 * 1024, 8 * 64 * 1024))
				 .option(ChannelOption.SO_RCVBUF, 1048576)
				 //.handler(new LoggingHandler(LogLevel.TRACE))
				 .childHandler(channelInit);
				ch.set((host == null ? b.bind(port) : b.bind(host, port)).sync().channel());
				Log.i("Listening on " + ch.get().localAddress());
				ch.get().closeFuture().sync();
				Log.i("Server stopped.");
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			finally
			{
				gBoss.shutdownGracefully();
				gWork.shutdownGracefully();
				ch.set(null);
			}
		};

		new Thread(runnable, "Net Server Thread").start();
	}

	public synchronized void stop()
	{
		if(!isRunning()) return;
		ch.get().close();
		if(ch.get() != null && ch.get().parent() != null) ch.get().parent().close();
	}
}
