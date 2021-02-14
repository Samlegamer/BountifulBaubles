package cursedflames.bountifulbaubles.forge.common.item.items.ankhparts;

import java.util.List;
import java.util.function.Supplier;

import cursedflames.bountifulbaubles.forge.common.baubleeffect.EffectPotionNegate;
import cursedflames.bountifulbaubles.forge.common.baubleeffect.EffectPotionNegate.IPotionNegateItem;
import cursedflames.bountifulbaubles.forge.common.item.BBItem;
import cursedflames.bountifulbaubles.forge.common.util.CuriosUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class ItemPotionNegate extends BBItem implements IPotionNegateItem {
	protected final List<Supplier<StatusEffect>> cureEffects;
	
	protected static class Curio implements ICurio {
		ItemPotionNegate item;
		protected Curio(ItemPotionNegate item) {
			this.item = item;
		}
		@Override
		public void curioTick(String identifier, int index, LivingEntity livingEntity) {
			EffectPotionNegate.negatePotion(livingEntity, item.cureEffects);
		}
	}
	
	public ItemPotionNegate(String name, Settings props, List<Supplier<StatusEffect>> cureEffects) {
		super(name, props);
		this.cureEffects = cureEffects;
	}
	
	protected ICurio getCurio() {
		return new Curio(this);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		ICurio curio = getCurio();
		return CuriosUtil.makeSimpleCap(curio);
	}

	@Override
	public List<Supplier<StatusEffect>> getCureEffects() {
		return cureEffects;
	}
}
