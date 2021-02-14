package cursedflames.bountifulbaubles.forge.common.baubleeffect;

import java.util.Map;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

// TODO do we want to fix the thud sound on falling > 3 blocks without taking damage?
// see: LivingFallEvent - want to cancel if we would end up cancelling the damage
// We need LivingEntity.computeFallDamage to see how much damage would end up happening though
// and it's protected, and I don't think this is important enough to merit an AT.

public class EffectFallDamageResistNegate {
	public static interface IFallDamageNegateItem {
		default boolean shouldNegate(LivingEntity entity, ItemStack stack) {
			return true;
		}
	}
	
	public static interface IFallDamageResistItem {
		float getFallResist(LivingEntity entity, ItemStack stack);
	}
	
	public static float onFall(LivingEvent event, LivingEntity entity) {
		LazyOptional<ICuriosItemHandler> opt = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
		float decrease = 0f;
		if (opt.isPresent()) {
			ICuriosItemHandler handler = opt.orElse(null);
			Map<String, ICurioStacksHandler> items = handler.getCurios();
			for (ICurioStacksHandler stackHandler : items.values()) {
				int size = stackHandler.getSlots();
				for (int i = 0; i < size; i++) {
					ItemStack stack = stackHandler.getStacks().getStackInSlot(i);
					Item item = stack.getItem();
					if (item instanceof IFallDamageNegateItem) {
						if (((IFallDamageNegateItem) item).shouldNegate(entity, stack)) {
							event.setCanceled(true);
							return 0f;
						}
					} else if (item instanceof IFallDamageResistItem) {
						decrease += Math.max(0, ((IFallDamageResistItem) item).getFallResist(entity, stack));
					}
				}
			}
		}
		return decrease;
	}
	
	public static void onFallDamage(LivingAttackEvent event, LivingEntity entity) {
		float decrease = onFall(event, entity);
		
		if (!event.isCanceled() && decrease > 0) {
			float finalDamage = Math.max(0, event.getAmount() - decrease);
			if (finalDamage == 0) {
				event.setCanceled(true);
			}
		}
	}
	
	public static void onFallDamage(LivingHurtEvent event, LivingEntity entity) {
		float decrease = onFall(event, entity);
		
		if (!event.isCanceled() && decrease > 0) {
			float finalDamage = Math.max(0, event.getAmount() - decrease);
			if (finalDamage == 0) {
				event.setCanceled(true);
			} else {
				event.setAmount(finalDamage);
			}
		}
	}
}
