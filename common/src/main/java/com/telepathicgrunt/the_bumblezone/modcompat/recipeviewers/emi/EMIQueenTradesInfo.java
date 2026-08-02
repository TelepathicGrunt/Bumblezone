package com.telepathicgrunt.the_bumblezone.modcompat.recipeviewers.emi;

// TODO: Re-enable when EMI updates
//public class EMIQueenTradesInfo implements EmiRecipe {
//
//	private final EmiIngredient input;
//	private final TagKey<Item> inputTag;
//	private final List<EmiStack> outputs;
//	private final TagKey<Item> outputTag;
//	private final EmiIngredient visualOutputs;
//	private final int xpReward;
//	private final int weight;
//	private final int groupWeight;
//	private final Identifier id;
//
//	public EMIQueenTradesInfo(EmiIngredient input, TagKey<Item> inputTag, List<EmiStack> outputs, TagKey<Item> outputTag, int xp, int weight, int groupWeight, Identifier id) {
//		super();
//		this.input = input;
//		this.inputTag = inputTag;
//		this.outputs = outputs;
//		this.outputTag = outputTag;
//		this.visualOutputs = EmiIngredient.of(outputs);
//		this.xpReward = xp;
//		this.weight = weight;
//		this.groupWeight = groupWeight;
//		this.id = id;
//	}
//
//	public int getXpReward() {
//		return this.xpReward;
//	}
//
//	public int getWeight() {
//		return this.weight;
//	}
//
//	public int getGroupWeight() {
//		return this.groupWeight;
//	}
//
//	public TagKey<Item> getInputTag() {
//		return this.inputTag;
//	}
//
//	public TagKey<Item> getOutputTag() {
//		return this.outputTag;
//	}
//
//	@Override
//	public EmiRecipeCategory getCategory() {
//		return EMICompat.QUEEN_TRADES;
//	}
//
//	@Override
//	public @Nullable Identifier getId() {
//		return id;
//	}
//
//	@Override
//	public List<EmiIngredient> getInputs() {
//		return List.of(input);
//	}
//
//	@Override
//	public List<EmiStack> getOutputs() {
//		return outputs;
//	}
//
//	@Override
//	public int getDisplayWidth() {
//		return 124;
//	}
//
//	@Override
//	public int getDisplayHeight() {
//		return 28;
//	}
//
//	@Override
//	public void addWidgets(WidgetHolder widgets) {
//		widgets.add(new TextureWidget(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/recipe_viewer/queen_trades_layout.png"), 0, 0, getDisplayWidth(), getDisplayHeight(), 0, 0));
//
//		widgets.add(new SlotWidget(input, 5, 5));
//		widgets.add(new SlotWidget(visualOutputs, 63, 5).recipeContext(this));
//
//		widgets.add(new TextWidget(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_xp", getXpReward()).getVisualOrderText(), 100,  11, 0xFF404040, false));
//
//		if (this.getInputTag() != null) {
//			widgets.add(new TextureWidget(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/recipe_viewer/tag_icon.png"), 11, 11, 16, 16, 0, 0, 16, 16, 16, 16));
//		}
//		if (this.getOutputTag() != null) {
//			widgets.add(new TextureWidget(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "textures/gui/recipe_viewer/tag_icon.png"), 69, 11, 16, 16, 0, 0, 16, 16, 16, 16));
//		}
//
//		double percentValue = (double)(getWeight()) / (getGroupWeight()) * 100;
//		String percent = String.valueOf(percentValue);
//		String percentRounded;
//		if (percentValue < 1) {
//			percentRounded = String.valueOf(Math.max(Math.round(percentValue * 10D) / 10D, 0.1D));
//		}
//		else {
//			percentRounded = String.valueOf(Math.max(Math.round(percentValue), 1));
//		}
//
//		DrawableWidget tooltipWidget = new DrawableWidget(32, 2, 22, 20, (matrices, mouseX, mouseY, delta) -> {});
//		tooltipWidget.tooltip((x, z) -> List.of(ClientTooltipComponent.create(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_tooltip", percent.substring(0, Math.min(percent.length(), 5))).getVisualOrderText())));
//		widgets.add(tooltipWidget);
//
//		widgets.add(new TextWidget(Component.translatable("the_bumblezone.recipe_viewers.queen_trade_chance_text", percentRounded).getVisualOrderText(), 38 - (percentValue < 1 ? 6 : (percentRounded.length() * 3)), 11, 0xFF404040, false));
//	}
//}
