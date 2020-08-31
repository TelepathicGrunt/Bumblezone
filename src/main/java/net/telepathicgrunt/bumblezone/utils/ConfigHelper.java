/*
The MIT License (MIT)
Copyright (c) 2019 Joseph Bettendorff aka "Commoble"
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package net.telepathicgrunt.bumblezone.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;



public class ConfigHelper
{
	/** as with the other register method, but the contexts are assumed **/
	public static <T> T register(
		final ModConfig.Type configType,
		final BiFunction<ForgeConfigSpec.Builder, Subscriber, T> configBuilder)
	{
		return register(ModLoadingContext.get(), FMLJavaModLoadingContext.get(), configType, configBuilder);
	}
	
	/** call this in either your @Mod class constructor or in FMLCommonSetupEvent or in FMLClientSetupEvent **/
	public static <T> T register(
		final ModLoadingContext modContext,
		final FMLJavaModLoadingContext fmlContext,
		final ModConfig.Type configType,
		final BiFunction<ForgeConfigSpec.Builder, Subscriber, T> configBuilder)
	{
		final List<ConfigValueListener<?>> subscriptionList = new ArrayList<>();
		final Pair<T, ForgeConfigSpec> entry = new ForgeConfigSpec.Builder().configure(builder -> configBuilder.apply(builder, getSubscriber(subscriptionList)));
		final T config = entry.getLeft();
		final ForgeConfigSpec spec = entry.getRight();
		
		modContext.registerConfig(configType, spec);
		
		final Consumer<ModConfigEvent> configUpdate = event ->
		{
			if(event.getConfig().getSpec() == spec)
				for(ConfigValueListener<?> value : subscriptionList)
					value.update();
		};
		
		fmlContext.getModEventBus().addListener(configUpdate);
		
		return config;
	}
	
	private static Subscriber getSubscriber(final List<ConfigValueListener<?>> list)
	{
		return new Subscriber(list);
	}
	
	public static class Subscriber
	{
		final List<ConfigValueListener<?>> list;
		
		Subscriber(final List<ConfigValueListener<?>> list)
		{
			this.list = list;
		}
		
		public <T> ConfigValueListener<T> subscribe(final ConfigValue<T> value)
		{
			return ConfigValueListener.of(value, this.list);
		}
	}
	
	public static class ConfigValueListener<T> implements Supplier<T>
	{
		private T value = null;
		private final ConfigValue<T> configValue;
		
		private ConfigValueListener(final ConfigValue<T> configValue)
		{
			this.configValue = configValue;
			//this.value = configValue.get();
		}
		
		public static <T> ConfigValueListener<T> of(final ConfigValue<T> configValue, final List<ConfigValueListener<?>> valueList)
		{
			final ConfigValueListener<T> value = new ConfigValueListener<T>(configValue);
			valueList.add(value);
			return value;
		}
		
		public void update()
		{
			this.value = this.configValue.get();
		}

		@Override
		public T get()
		{
			if (this.value == null)
				this.update();
			return this.value;
		}
	}

}
