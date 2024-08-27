package mod.linguardium.invmgmtcompat;

import me.roundaround.inventorymanagement.client.InventoryButtonsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;

import java.util.Arrays;

import static mod.linguardium.invmgmtcompat.JsonConfigLoader.config;

public class InventoryManagementModCompatClient implements ClientModInitializer {
	public static final KeyBinding PrintInventoryKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("printinventory",InputUtil.GLFW_KEY_F5,"Inventory Management Compat"));
	public static final KeyBinding TryEnableSorting = KeyBindingHelper.registerKeyBinding(new KeyBinding("enablesort",InputUtil.GLFW_KEY_F6,"Inventory Management Compat"));
	@Override
	public void onInitializeClient() {
		JsonConfigLoader.init();
		InventoryButtonsManager manager = InventoryButtonsManager.INSTANCE;
		if (config != null) {
			for (String classPath : config.inventoryClasses) {
				try {
					Class<?> clazz = getClass().getClassLoader().loadClass(classPath);
					if (ScreenHandler.class.isAssignableFrom(clazz)) {
						registerScreenHandler( (Class<? extends ScreenHandler>)clazz);
					}
				} catch (ClassNotFoundException e) {

                }
			}
		}
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
	public static void registerScreenHandler(Class<? extends ScreenHandler> clazz) {
		InventoryButtonsManager manager = InventoryButtonsManager.INSTANCE;
		if (manager != null) {
			manager.registerSimpleInventorySortableHandler(clazz);
			manager.registerSimpleInventoryTransferableHandler(clazz);
		}
	}
}