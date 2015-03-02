package com.hero;

import net.minecraft.item.Item;

import com.hero.Items.FullCounter;
import com.hero.Items.Telum;
import com.hero.proxy.Proxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="HERO_Z0173", name="Hero", version="0.0.1")
public class HeroMod {
	
	@Instance("HERO_Z0173")
	public static HeroMod instance;
	
	@SidedProxy(clientSide="com.hero.proxy.ClientProxy", serverSide="com.hero.proxy.Proxy")
	public static Proxy proxy;
	
	public static Telum telum;
	public static FullCounter fullCounter;
	
	//load proxy
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		telum = new Telum();
		fullCounter = new FullCounter();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		// TODO
	}
}
