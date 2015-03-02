package com.hero.Items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class FullCounter extends Telum {
	
	@SideOnly(Side.CLIENT)
	private IIcon icon0, icon1, icon2;
	
	public FullCounter() {
		super("fullcounter");
		this.setTextureName("hero:FullCounter_0");
		//this.setMaxDamage(2);
	}
	
	
	@Override
	public IIcon getIconFromDamage(int damage) {
		if(damage == 1) return icon1;
		if(damage == 0) return icon2;
		
		return icon0;
	}
	
	/*@Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount){
    	
    }*/
	
	/*@Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
    	return EnumAction.block;
    }*/
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
		super.registerIcons(iconRegister);
		icon0 = iconRegister.registerIcon("hero:FullCounter_0");
		icon1 = iconRegister.registerIcon("hero:FullCounter_1");
		icon2 = iconRegister.registerIcon("hero:FullCounter_2");
    }
}
