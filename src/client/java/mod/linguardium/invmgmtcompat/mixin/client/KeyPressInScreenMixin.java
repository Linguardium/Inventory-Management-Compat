package mod.linguardium.invmgmtcompat.mixin.client;

import mod.linguardium.invmgmtcompat.JsonConfigLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mod.linguardium.invmgmtcompat.InventoryManagementModCompatClient.*;

@Mixin(HandledScreen.class)
public abstract class KeyPressInScreenMixin extends Screen{
	@Shadow public abstract <T extends ScreenHandler> T getScreenHandler();

	protected KeyPressInScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("HEAD"), method = "keyPressed")
	private void printScreenHandler(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		if (!HandledScreen.class.isAssignableFrom(this.getClass())) return;
		if (getScreenHandler() == null) return;
		ScreenHandler handler = getScreenHandler();
		if (client.player == null) return;
		if (PrintInventoryKey.matchesKey(keyCode,scanCode)) {
			client.player.sendMessage(Text.literal(handler.getClass().getCanonicalName()));
		} else if (TryEnableSorting.matchesKey(keyCode,scanCode)) {
			client.player.sendMessage(Text.literal("Attempting to register the current screen as supporting sorting."));
			client.player.sendMessage(Text.literal(handler.getClass().getCanonicalName()));
			JsonConfigLoader.config.inventoryClasses.add(handler.getClass().getCanonicalName());
			registerScreenHandler(handler.getClass());
			JsonConfigLoader.save();
		}
		// This code is injected into the start of MinecraftClient.run()V
	}
}