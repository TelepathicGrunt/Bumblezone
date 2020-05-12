package net.telepathicgrunt.bumblezone.modcompatibility;

import cy.jdkdigital.productivebees.block.AdvancedBeehive;
import cy.jdkdigital.productivebees.block.AdvancedBeehiveAbstract;
import cy.jdkdigital.productivebees.block.ExpansionBox;
import net.minecraft.block.BlockState;

public class ProductiveBeesCompat {
    public static void setupProductiveBees() {
	ModChecking.productiveBeesPresent = true;
    }

    /**
     * Is block is a ProductiveBees nest or beenest block
     */
    public static boolean PBIsAdvancedBeehiveAbstractBlock(BlockState block) {
	
	if (block.getBlock() instanceof ExpansionBox && block.get(AdvancedBeehive.EXPANDED)) {
	    return true; // expansion boxes only count as beenest when they expand a hive.
	} 
	else if (block.getBlock() instanceof AdvancedBeehiveAbstract) {
	    return true; // nests/hives here so return true
	}

	return false;
    }

}
