package cursedflames.bountifulbaubles.item.base;

import java.util.List;

import javax.annotation.Nullable;

import cursedflames.bountifulbaubles.BountifulBaubles;
import cursedflames.bountifulbaubles.client.layer.IRenderObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenericItemBB extends Item implements IRenderObject{
//	public Property creativeOnly;
	
	public GenericItemBB(String name) {
		this(name, null);
	}

	public GenericItemBB(String name, CreativeTabs tab) {
		this(name, tab, true);
	}

	public GenericItemBB(String name, CreativeTabs tab, boolean configCreativeOnly) {
		setRegistryName(new ResourceLocation(BountifulBaubles.MODID, name));
		setTranslationKey(BountifulBaubles.MODID+"."+name);
		if (tab!=null) {
			setCreativeTab(tab);
		}
//		if (configCreativeOnly) {
//			Property unsynced = BountifulBaubles.config.addPropBoolean(
//					getRegistryName()+".creativeOnly", "Items",
//					"Whether or not "+getRegistryName()
//							+" is creative only. If enabled, recipes and loot tables for this item will not be added, and the item will have a creative only tooltip added.",
//					false, EnumPropSide.SYNCED);
//			unsynced.setRequiresMcRestart(true);
//			creativeOnly = BountifulBaubles.config
//					.getSyncedProperty(getRegistryName()+".creativeOnly");
//		} else {
//			creativeOnly = null;
//		}
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip,
			ITooltipFlag flagIn) {
		boolean isShifting = GuiScreen.isShiftKeyDown();
		// TODO add proxies instead of being lazy and using deprecated I18n
		String base = getTranslationKey()+".tooltip.";
		String shift = "";
		if (I18n.canTranslate(base+"0")) {
			if (isShifting&&I18n.canTranslate(base+"0s")) {
				shift = "s";
			}
			for (int i = 0; I18n.canTranslate(base+i+shift)&&i<100; i++) {
				tooltip.add(I18n.translateToLocal(base+i+shift));
			}
		}
//		if (creativeOnly!=null&&creativeOnly.getBoolean()) {
//			tooltip.add(BountifulBaubles.proxy.translate(BountifulBaubles.MODID+".creativeonly"));
//		}
	}

	@Override
	public void onRenderObject(ItemStack stack, EntityPlayer player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
		
	}
}