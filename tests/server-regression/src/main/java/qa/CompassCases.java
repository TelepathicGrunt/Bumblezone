package qa;

import com.telepathicgrunt.the_bumblezone.items.datacomponents.*;
import com.telepathicgrunt.the_bumblezone.loot.functions.HoneyCompassLocateStructure;
import com.telepathicgrunt.the_bumblezone.modinit.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public final class CompassCases {
  public static void run(ServerLevel level) throws Exception {
    var type = HoneyCompassLocateStructure.class;
    Object fn =
        type.getConstructor(List.class, TagKey.class, int.class, boolean.class)
            .newInstance(
                List.of(),
                TagKey.create(Registries.STRUCTURE, Identifier.withDefaultNamespace("village")),
                50,
                true);
    var method =
        type.getDeclaredMethod(
            "setCompassFoundLocationData", WeakReference.class, LootContext.class, BlockPos.class);
    method.setAccessible(true);
    var context =
        new LootContext.Builder(new LootParams.Builder(level).create(LootContextParamSets.EMPTY))
            .create(Optional.empty());
    var pos = new BlockPos(15, 100, 15);
    Checks.test(
        "compass_consumed_stack_ignored",
        () -> {
          var stack = ItemStack.EMPTY;
          method.invoke(fn, new WeakReference<>(stack), context, pos);
          Checks.require(stack.isEmpty(), "Resurrected consumed stack");
        });
    Checks.test(
        "compass_cleared_reference_ignored",
        () -> method.invoke(fn, new WeakReference<ItemStack>(null), context, pos));
    Checks.test(
        "compass_missing_component_ignored",
        () -> {
          var stack = new ItemStack(BzItems.HONEY_COMPASS.get());
          stack.remove(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
          var before = stack.copy();
          method.invoke(fn, new WeakReference<>(stack), context, pos);
          Checks.require(
              ItemStack.isSameItemSameComponents(before, stack), "Malformed stack changed");
        });
    Checks.test(
        "compass_valid_result_preserves_flags",
        () -> {
          var stack = new ItemStack(BzItems.HONEY_COMPASS.get());
          stack.set(
              BzDataComponents.HONEY_COMPASS_STATE_DATA.get(),
              new HoneyCompassStateData(true, Optional.of(UUID.randomUUID()), true, false, true));
          method.invoke(fn, new WeakReference<>(stack), context, pos);
          var state = stack.get(BzDataComponents.HONEY_COMPASS_STATE_DATA.get());
          Checks.require(
              state.locked()
                  && state.locatedSpecialStructure()
                  && !state.isLoading()
                  && state.searchId().isEmpty(),
              "State flags changed incorrectly");
          Checks.require(
              stack
                  .get(BzDataComponents.HONEY_COMPASS_TARGET_DATA.get())
                  .targetPos()
                  .orElseThrow()
                  .equals(pos),
              "Target lost");
        });
  }
}
