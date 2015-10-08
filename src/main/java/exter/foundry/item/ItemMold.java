package exter.foundry.item;

import java.util.List;

import exter.foundry.creativetab.FoundryTabMolds;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMold extends Item
{  
  static public final int MOLD_INGOT = 0;
  static public final int MOLD_INGOT_SOFT = 1;
  static public final int MOLD_CHESTPLATE = 2;
  static public final int MOLD_CHESTPLATE_SOFT = 3;
  static public final int MOLD_PICKAXE = 4;
  static public final int MOLD_PICKAXE_SOFT = 5;
  static public final int MOLD_BLOCK = 6;
  static public final int MOLD_BLOCK_SOFT = 7;
  static public final int MOLD_AXE = 8;
  static public final int MOLD_AXE_SOFT = 9;
  static public final int MOLD_SWORD = 10;
  static public final int MOLD_SWORD_SOFT = 11;
  static public final int MOLD_SHOVEL = 12;
  static public final int MOLD_SHOVEL_SOFT = 13;
  static public final int MOLD_HOE = 14;
  static public final int MOLD_HOE_SOFT = 15;
  static public final int MOLD_LEGGINGS = 16;
  static public final int MOLD_LEGGINGS_SOFT = 17;
  static public final int MOLD_HELMET = 18;
  static public final int MOLD_HELMET_SOFT = 19;
  static public final int MOLD_BOOTS = 20;
  static public final int MOLD_BOOTS_SOFT = 21;
  static public final int MOLD_GEAR = 22;
  static public final int MOLD_GEAR_SOFT = 23;
  static public final int MOLD_CABLE_IC2 = 24;
  static public final int MOLD_CABLE_IC2_SOFT = 25;
  static public final int MOLD_CASING_IC2 = 26;
  static public final int MOLD_CASING_IC2_SOFT = 27;
  static public final int MOLD_SLAB = 28;
  static public final int MOLD_SLAB_SOFT = 29;
  static public final int MOLD_STAIRS = 30;
  static public final int MOLD_STAIRS_SOFT = 31;
  static public final int MOLD_PLATE = 32;
  static public final int MOLD_PLATE_SOFT = 33;
  static public final int MOLD_CAP_TC = 34;
  static public final int MOLD_CAP_TC_SOFT = 35;
  static public final int MOLD_INSULATED_CABLE_IC2 = 36;
  static public final int MOLD_INSULATED_CABLE_IC2_SOFT = 37;
  static public final int MOLD_SICKLE = 38;
  static public final int MOLD_SICKLE_SOFT = 39;
  static public final int MOLD_BOW = 40;
  static public final int MOLD_BOW_SOFT = 41;
  static public final int MOLD_FLUXPLATE = 42;
  static public final int MOLD_FLUXPLATE_SOFT = 43;
  static public final int MOLD_BULLET = 44;
  static public final int MOLD_BULLET_SOFT = 45;
  static public final int MOLD_BULLET_HOLLOW = 46;
  static public final int MOLD_BULLET_HOLLOW_SOFT = 47;
  static public final int MOLD_BULLET_CASING = 48;
  static public final int MOLD_BULLET_CASING_SOFT = 49;
  static public final int MOLD_GUN_BARREL = 50;
  static public final int MOLD_GUN_BARREL_SOFT = 51;
  static public final int MOLD_REVOLVER_DRUM = 52;
  static public final int MOLD_REVOLVER_DRUM_SOFT = 53;
  static public final int MOLD_REVOLVER_FRAME = 54;
  static public final int MOLD_REVOLVER_FRAME_SOFT = 55;
  static public final int MOLD_WIRE_PR = 56;
  static public final int MOLD_WIRE_PR_SOFT = 57;
  static public final int MOLD_PELLET = 58;
  static public final int MOLD_PELLET_SOFT = 59;
  static public final int MOLD_SHELL_CASING = 60;
  static public final int MOLD_SHELL_CASING_SOFT = 61;
  static public final int MOLD_SHOTGUN_PUMP = 62;
  static public final int MOLD_SHOTGUN_PUMP_SOFT = 63;
  static public final int MOLD_SHOTGUN_FRAME = 64;
  static public final int MOLD_SHOTGUN_FRAME_SOFT = 65;
  static public final int MOLD_SHARD_TC = 66;
  static public final int MOLD_SHARD_TC_SOFT = 67;
  
  static public final String[] REGISTRY_NAMES = 
  {
    "moldIngot",
    "moldSoftIngot",
    "moldChestplate",
    "moldSoftChestplate",
    "moldPickaxe",
    "moldSoftPickaxe",
    "moldBlock",
    "moldSoftBlock",    
    "moldAxe",
    "moldSoftAxe",
    "moldSword",
    "moldSoftSword",
    "moldShovel",
    "moldSoftShovel",
    "moldHoe",
    "moldSoftHoe",
    "moldLeggings",
    "moldSoftLeggings",
    "moldHelmet",
    "moldSoftHelmet",
    "moldBoots",
    "moldSoftBoots",
    "moldGear",
    "moldSoftGear",
    "moldCable",
    "moldSoftCable",
    "moldCasing",
    "moldSoftCasing",
    "moldSlab",
    "moldSoftSlab",
    "moldStairs",
    "moldSoftStairs",
    "moldPlate",
    "moldSoftPlate",
    "moldCap",
    "moldSoftCap",
    "moldInsulatedCable",
    "moldSoftInsulatedCable",
    "moldSickle",
    "moldSoftSickle",
    "moldBow",
    "moldSoftBow",
    "moldFluxPlate",
    "moldSoftFluxPlate",
    "moldBullet",
    "moldSoftBullet",
    "moldBulletHollow",
    "moldSoftBulletHollow",
    "moldBulletCasing",
    "moldSoftBulletCasing",
    "moldGunBarrel",
    "moldSoftGunBarrel",
    "moldRevolverDrum",
    "moldSoftRevolverDrum",
    "moldRevolverFrame",
    "moldSoftRevolverFrame",
    "moldWire",
    "moldSoftWire",
    "moldPellet",
    "moldSoftPellet",
    "moldShellCasing",
    "moldSoftShellCasing",
    "moldShotgunPump",
    "moldSoftShotgunPump",
    "moldShotgunFrame",
    "moldSoftShotgunFrame",
    "moldShard",
    "moldSoftShard"
  };

  public ItemMold()
  {
    super();
    maxStackSize = 1;
    setCreativeTab(FoundryTabMolds.tab);
    setHasSubtypes(true);
    setUnlocalizedName("mold");
  }
  
  @Override
  public String getUnlocalizedName(ItemStack itemstack)
  {
    return getUnlocalizedName() + itemstack.getItemDamage();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tabs, @SuppressWarnings("rawtypes") List list)
  {
    int i;
    for (i = 0; i < REGISTRY_NAMES.length; i++)
    {
      ItemStack itemstack = new ItemStack(this, 1, i);
      list.add(itemstack);
    }
  }
}
