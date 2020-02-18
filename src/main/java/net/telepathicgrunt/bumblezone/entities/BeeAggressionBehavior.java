package net.telepathicgrunt.bumblezone.entities;

public class BeeAggressionBehavior
{

//
//    public static void playerTick(PlayerTickEvent event)
//    {
//        //grabs the capability attached to player for dimension hopping
//        PlayerEntity playerEntity = event.player;
//
//        //removes the wrath of the hive if it is disallowed outside dimension
//        if(!(BzConfig.allowWrathOfTheHiveOutsideBumblezone || playerEntity.dimension == BzDimension.bumblezone()) &&
//            playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
//        {
//            playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
//        }
//
//        //Makes it so player does not get killed for falling into the void
//        if(playerEntity.dimension == BzDimension.bumblezone() && playerEntity.getY() < -3)
//        {
//            playerEntity.setPosition(playerEntity.getX(), -3, playerEntity.getZ());
//        }
//
//        //Makes the fog redder when this effect is active
//        if(playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
//        {
//            BzWorldProvider.ACTIVE_WRATH = true;
//        }
//        else
//        {
//            BzWorldProvider.ACTIVE_WRATH = false;
//        }
//    }
}
