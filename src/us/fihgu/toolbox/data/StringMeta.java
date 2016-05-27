package us.fihgu.toolbox.data;


import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class StringMeta implements MetadataValue
{
	private String value;
	private JavaPlugin plugin;
	
	public StringMeta(String value, JavaPlugin plugin)
	{
		this.value = value;
		this.plugin = plugin;
	}

	@Override
	public Object value()
	{
		return value;
	}

	@Override
	public int asInt()
	{
		return 0;
	}

	@Override
	public float asFloat()
	{
		return 0;
	}

	@Override
	public double asDouble()
	{
		return 0;
	}

	@Override
	public long asLong()
	{
		return 0;
	}

	@Override
	public short asShort()
	{
		return 0;
	}

	@Override
	public byte asByte()
	{
		return 0;
	}

	@Override
	public boolean asBoolean()
	{
		return false;
	}

	@Override
	public String asString()
	{
		return value;
	}

	@Override
	public Plugin getOwningPlugin()
	{
		return this.plugin;
	}

	@Override
	public void invalidate()
	{
		
	}
}
