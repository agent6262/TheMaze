package com.hero.Items;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class Telum extends Item {
	
	private static final EnumAction FIRSTSTAGE = new EnumHelper().addAction("FIRSTSTAGE");
	private static final EnumAction SECONDSTAGE = new EnumHelper().addAction("SECONDSTAGE");
	private static final EnumAction THIRDSTAGE = new EnumHelper().addAction("THIRDSTAGE");
	
	public static ToolMaterial Z0173 = new EnumHelper().addToolMaterial("Z0173", 5, 10000, 10, 10, 10);
	
	public Telum(){
		super();
		maxStackSize = 1;
        setCreativeTab(CreativeTabs.tabCombat);
        setUnlocalizedName("Telum");
        
        GameRegistry.registerItem(this, "telum");
	}
	
	public Telum(String name) {
		super();
		maxStackSize = 1;
		this.setMaxDamage(2);
        setCreativeTab(CreativeTabs.tabCombat);
		this.setUnlocalizedName(name);
		
		GameRegistry.registerItem(this, name);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player){
    	player.setItemInUse(itemStack, 7200);
    	
        return itemStack;
    }
	
	@Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int itemInUseCount){
		if(Keyboard.isKeyDown(Keyboard.KEY_C))
    		if(itemStack.getItemDamage() > 0)
    			itemStack.setItemDamage(itemStack.getItemDamage()-1);
    	if(Keyboard.isKeyDown(Keyboard.KEY_X))
        	if(itemStack.getItemDamage() < 2)
        		itemStack.setItemDamage(itemStack.getItemDamage()+1);
    	if(Keyboard.isKeyDown(Keyboard.KEY_0)) itemStack.setItemDamage(0);
    	if(Keyboard.isKeyDown(Keyboard.KEY_1)) itemStack.setItemDamage(1);
    	if(Keyboard.isKeyDown(Keyboard.KEY_2)) itemStack.setItemDamage(2);
    }
    
	@Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
    	return EnumAction.block;
    }
}
