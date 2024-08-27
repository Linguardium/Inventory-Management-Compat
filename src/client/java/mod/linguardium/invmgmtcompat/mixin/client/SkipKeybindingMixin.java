package mod.linguardium.invmgmtcompat.mixin.client;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import mod.linguardium.invmgmtcompat.InventoryManagementModCompatClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.HashMap;
import java.util.Map;

@Mixin(KeyBinding.class)
public class SkipKeybindingMixin {
    @ModifyReceiver(method="updateKeysByCode",at= @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static Map skipKeybindingIdMap(Map instance, Object k, Object v) {
        return skipOrNot(instance, v);
    }
    @ModifyReceiver(method="<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at= @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/KeyBinding;KEY_TO_BINDINGS:Ljava/util/Map;"),to= @At(value = "FIELD", target = "Lnet/minecraft/client/option/KeyBinding;KEY_CATEGORIES:Ljava/util/Set;")))
    private Map skipKeybindingIdMapInConstructor(Map instance, Object k, Object v) {
        return skipOrNot(instance, v);
    }
    private static Map skipOrNot(Map instance, Object binding) {
        return (binding instanceof KeyBinding key && key.getCategory().equals("Inventory Management Compat"))? Maps.newHashMap() : instance;
    }
}
