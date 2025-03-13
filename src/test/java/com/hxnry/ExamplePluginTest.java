package com.hxnry;

import com.hxnry.account_manager.AccountManagerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AccountManagerPlugin.class);
		RuneLite.main(args);
	}
}