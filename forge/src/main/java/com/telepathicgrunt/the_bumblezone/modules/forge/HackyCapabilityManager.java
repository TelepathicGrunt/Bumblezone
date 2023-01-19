package com.telepathicgrunt.the_bumblezone.modules.forge;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.objectweb.asm.Type;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class HackyCapabilityManager {

    private static final MethodHandle capabilityGetter = getGetHandle();

    /**
     * This is used to pull the {@link CapabilityManager#get(String, boolean)} method handle to allow for dynamic capability registration.
     * without using {@link net.minecraftforge.common.capabilities.CapabilityToken} which does not work due to the dynamic nature of the
     * {@link com.telepathicgrunt.the_bumblezone.modules.base.Module} class.
     *
     */
    private static MethodHandle getGetHandle() {
        try {
            Method getMethod = CapabilityManager.class.getDeclaredMethod("get", String.class, boolean.class);
            return MethodHandles.lookup().unreflect(getMethod);
        }catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a capability from the {@link CapabilityManager} without using {@link net.minecraftforge.common.capabilities.CapabilityToken}
     * <br>
     * But with using a {@link Class} reference.
     * <br><br>
     * Do note that the class still has to be the one that would have been used for the Capability tokens generic,
     * this does not circumvent any  of the conventions that forge has in place for how to use capabilities it merely just provides
     * away to get a capability with a class reference than a transformer hack with {@link net.minecraftforge.common.capabilities.CapabilityToken}.
     */
    public static <T> Capability<T> get(Class<T> clazz) {
        return get(clazz, false);
    }

    public static <T> void register(Class<T> clazz) {
        get(clazz, true);
    }

    @SuppressWarnings("unchecked")
    public static <T> Capability<T> get(Class<T> clazz, boolean register) {
        try {
            return (Capability<T>) capabilityGetter.invokeExact(CapabilityManager.INSTANCE, Type.getInternalName(clazz), register);
        }catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
