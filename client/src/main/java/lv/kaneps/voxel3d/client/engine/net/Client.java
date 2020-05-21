package lv.kaneps.voxel3d.client.engine.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.FailedFuture;
import lv.kaneps.voxel3d.client.Game;
import lv.kaneps.voxel3d.client.engine.GameEngine;
import lv.kaneps.voxel3d.client.engine.net.ex.ClientAckNotSentException;

import java.util.concurrent.atomic.AtomicReference;

public class Client
{
	public static Client createClient(GameEngine engine, String host, int port)
	{
		return new Client(engine, host, port);
	}

	public boolean clientAckSent = false;

	protected final AtomicReference<Channel> ch = new AtomicReference<Channel>();
	protected ChannelHandlerContext chContext;

	protected ChannelInit channelInit;

	public final GameEngine engine;
	public final String host;
	public final int port;

	private Client(GameEngine engine, String host, int port)
	{
		this.engine = engine;
		this.host = host;
		this.port = port;

		channelInit = new ChannelInit(this);
	}

	public Game getGame()
	{
		return engine.getGame();
	}

	public synchronized void start()
	{
		if (isRunning()) return;

		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				final EventLoopGroup group = new NioEventLoopGroup();

				try
				{
					Bootstrap bootstrap = new Bootstrap().group(group)
					                                     .channel(NioSocketChannel.class)
					                                     .option(ChannelOption.SO_KEEPALIVE, true)
					                                     .option(ChannelOption.TCP_NODELAY, true)
					                                     .option(ChannelOption.AUTO_READ, true)
					                                     .remoteAddress(host, port)
					                                     .handler(channelInit);
					ch.set(bootstrap.connect().sync().channel());

					ch.get().closeFuture().sync();
					System.out.println("Client stopped");
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				finally
				{
					group.shutdownGracefully();
					ch.set(null);
				}
			}
		};

		new Thread(runnable, "Net Client Thread").start();
	}

	public synchronized void stop()
	{
		if(!isRunning()) return;
		ch.get().close();
		if(ch.get() != null && ch.get().parent() != null) ch.get().parent().close();
	}

	public boolean isRunning()
	{
		return ch.get() != null;
	}

	public synchronized void setChannelContext(ChannelHandlerContext chContext)
	{
		this.chContext = chContext;
	}

	public synchronized ChannelFuture writeAndFlush(Object msg)
	{
		if(!clientAckSent) return ch.get() != null ? ch.get().newFailedFuture(new ClientAckNotSentException()) : null;
		if(chContext == null)
			return null;
		return chContext.writeAndFlush(msg);
	}
}
