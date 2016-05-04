package us.fihgu.toolbox.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;

import us.fihgu.toolbox.web.SelectionHandler;
import us.fihgu.toolbox.web.SelectorThreadPool;
import us.fihgu.toolbox.web.TimerThread;
import us.fihgu.toolbox.web.WebServer;

/**
 * This HTTP Server runs in its own thread.<br>
 * It uses non-block nio channels.<br>
 * @author fihgu
 *
 */
public class HTTPServer extends WebServer
{
	protected SelectorThreadPool readPool;
	protected SelectorThreadPool writePool;
	
	protected HTTPAcceptHandler acceptHandler = new HTTPAcceptHandler(this);
	
	protected CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	protected CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
	
	protected TimerThread timer = new TimerThread(this);
	
	public boolean debug = false;
	public boolean info = true;
	
	public int readBufferSize = 128;
	public int lineBuilderSize = 256;
	
	public int numReadThread = 4;
	public int numWriteThread = 4;
	
	public long timeOut = 120000;
	
	protected HashMap<String, IContextGenerator> contextGenerators = new HashMap<String, IContextGenerator>();

	public HTTPServer(InetSocketAddress address)
	{
		super(address);
	}
	
	public HTTPContext getContext(URL path)
	{
        IContextGenerator generator = this.contextGenerators.get(path.getHost() + path.getPath());
        if(generator != null)
        {
        	return generator.generateContext(path.getQuery());
        }
		
		return null;
	}
	
	/**
	 * @param path should contain the host name(for example: www.example.com/path/to/file.html)
	 * @param generator
	 */
	public void putContextGenerator(String path, IContextGenerator generator)
	{
		this.contextGenerators.put(path, generator);
	}
	
	public void removeContextGenerator(String path)
	{
		this.contextGenerators.remove(path);
	}
	
	@Override
	protected void init() throws IOException
	{
		readPool = new SelectorThreadPool(new HTTPReadHandler(this), this.numReadThread);
		readPool.startAll();
		writePool = new SelectorThreadPool(new HTTPWriteHandler(this), this.numWriteThread);
		writePool.startAll();
		timer.start();
		super.init();
	}

	@Override
	public void stopServer()
	{
		super.stopServer();
		timer.stopThread();
		readPool.closeAll();
		writePool.closeAll();
	}
	
	

	@Override
	public void onTimeOut(SelectionKey selectionKey)
	{
		if(this.info)
		{
			try
			{
				System.out.println("HTTP connection timeout: " + ((SocketChannel)selectionKey.channel()).getRemoteAddress());
			}
			catch (IOException e)
			{
				//should not happen
				e.printStackTrace();
			}
		}
		super.onTimeOut(selectionKey);
	}

	@Override
	public SelectionHandler getAcceptHandler()
	{
		return acceptHandler;
	}
}
