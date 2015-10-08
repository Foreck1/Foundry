package exter.foundry.item.firearm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.MutablePair;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.creativetab.FoundryTabFirearms;
import exter.foundry.item.FoundryItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public abstract class ItemFirearm extends ItemTool
{
 
  static private Random random = new Random();
  
  @SuppressWarnings("rawtypes")
  public ItemFirearm()
  {
    super(2,ToolMaterial.IRON,new HashSet());
    setMaxDamage(800);
    setCreativeTab(FoundryTabFirearms.tab);
    setMaxStackSize(1);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public final boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_)
  {
    ItemStack mat = FoundryItems.ingot_stacks.get("Steel");
    if(mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, p_82789_2_, false))
    {
      return true;
    }
    return false;
  }

  static private MovingObjectPosition Trace(World world, EntityLivingBase shooter,Entity target,float spread)
  {
    Vec3 start = new Vec3(shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.1, shooter.posZ);
    Vec3 dir;
    if(target != null)
    {
      dir = new Vec3(target.posX - start.xCoord, target.posY - start.yCoord,target.posZ - start.zCoord).normalize();
    } else
    {
      float pitch = -shooter.rotationPitch;
      float yaw = -shooter.rotationYaw;
      float cyaw = MathHelper.cos(yaw * 0.017453292F - (float) Math.PI);
      float syaw = MathHelper.sin(yaw * 0.017453292F - (float) Math.PI);
      float cpitch = -MathHelper.cos(pitch * 0.017453292F);

      dir = new Vec3(
          syaw * cpitch,
          MathHelper.sin(pitch * 0.017453292F),
          cyaw * cpitch);
    }
    Vec3 vspread = new Vec3(
        (random.nextFloat() * 2 - 1),
        (random.nextFloat() * 2 - 1),
        (random.nextFloat() * 2 - 1)).normalize();
    spread = random.nextFloat() * spread;
    dir = dir.addVector(vspread.xCoord * spread, vspread.yCoord * spread, vspread.zCoord * spread).normalize();
    

    double distance = 150.0D;

    Vec3 end = start.addVector(dir.xCoord * distance,dir.yCoord * distance,dir.zCoord * distance);

    Vec3 tstart = new Vec3(start.xCoord, start.yCoord, start.zCoord);
    Vec3 tend = new Vec3(end.xCoord, end.yCoord, end.zCoord);
    MovingObjectPosition obj = world.rayTraceBlocks(tstart, tend, false, true, false);
    
    
    @SuppressWarnings("unchecked")
    List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(shooter, shooter.getBoundingBox().expand(150, 150, 100));
    double min_dist = obj != null?obj.hitVec.distanceTo(start):150;
    for(Entity ent:entities)
    {
      if(ent.canBeCollidedWith() && ent.getBoundingBox() != null)
      {
        MovingObjectPosition ent_obj = ent.getBoundingBox().expand(0.1, 0.1, 0.1).calculateIntercept(start, end);
        if(ent_obj != null)
        {
          if(ent_obj.typeOfHit == MovingObjectType.BLOCK)
          {
            ent_obj.typeOfHit = MovingObjectType.ENTITY;
            ent_obj.entityHit = ent;
          }
          double d = ent_obj.hitVec.distanceTo(start);
          if(obj == null || d < min_dist)
          {
            min_dist = d;
            
            obj = ent_obj;
          }
        }
      }
    }
    if(obj != null)
    {
      obj.hitInfo = start;
    }
    return obj;
  }
  
  static public final void Shoot(ItemStack round_item, World world, EntityLivingBase shooter,Entity target, int times, float spread, float damage_multiplier)
  {
    Map<EntityLivingBase,MutablePair<Float,Integer>> entities_hit = new HashMap<EntityLivingBase,MutablePair<Float,Integer>>();
    IFirearmRound round = (IFirearmRound) round_item.getItem();
    int i;
    for(i = 0; i < times; i++)
    {
      MovingObjectPosition obj = Trace(world, shooter, target, spread);
      if(obj != null)
      {
        switch(obj.typeOfHit)
        {
          case BLOCK:
            IBlockState b = world.getBlockState(obj.getBlockPos());
            if(round.breaksGlass(round_item) && b.getBlock().getMaterial() == Material.glass && b.getBlock().getBlockHardness(world, obj.getBlockPos()) < 0.4)
            {
              world.playAuxSFXAtEntity(null, 2001, obj.getBlockPos(), Block.getIdFromBlock(b.getBlock())+(b.getBlock().getMetaFromState(b)<<12));
              if(!world.isRemote)
              {
                world.setBlockToAir(obj.getBlockPos());
              }
            } else
            {
              round.onBulletHitBlock(round_item, shooter, (Vec3)obj.hitInfo, world, obj.getBlockPos(), obj.sideHit);
            }
            break;
          case ENTITY:
            if(obj.entityHit instanceof EntityLivingBase)
            {
              Vec3 end = new Vec3(obj.entityHit.posX, obj.entityHit.posY, obj.entityHit.posZ);
              double distance = end.distanceTo((Vec3)obj.hitInfo);
              double base_range = round.getBaseRange(round_item);
              double falloff_range = round.getFalloffRange(round_item);
              double base_damage = round.getBaseDamage(round_item);
              double damage;
              if(distance < base_range)
              {
                damage = base_damage;
              } else if(distance > base_range + falloff_range)
              {
                damage = 0;
              } else
              {
                damage = base_damage * (1.0f - (distance - base_range) / falloff_range);
              }
              damage *= damage_multiplier;
              if(damage >= 1)
              {
                MutablePair<Float,Integer> accum = entities_hit.get(obj.entityHit);
                if(accum == null)
                {
                  accum = new MutablePair<Float,Integer>(0.0f,0);
                  entities_hit.put((EntityLivingBase)obj.entityHit, accum);
                }
                accum.left += (float)damage;
                accum.right++;
              }
            }
            break;
          default:
            break;
        } 
      }
    }
    for(Map.Entry<EntityLivingBase, MutablePair<Float,Integer>> hit : entities_hit.entrySet())
    {
      EntityLivingBase en = hit.getKey();
      DamageSource damage = (new EntityDamageSource("bullet", shooter)).setProjectile();
      if(round.ignoresArmor(round_item))
      {
        damage.setDamageBypassesArmor();
      }
      if(en.attackEntityFrom(damage, hit.getValue().left))
      {
        round.onBulletDamagedLivingEntity(round_item, en,hit.getValue().right);
      }
    }
  }
  
  @Override
  public final int getMaxItemUseDuration(ItemStack p_77626_1_)
  {
    return 72000;
  }


  @Override
  public final int getItemStackLimit(ItemStack stack)
  {
    return 1;
  }

  @Override
  public final EnumAction getItemUseAction(ItemStack p_77661_1_)
  {
    return EnumAction.BOW;
  }
  
  public abstract void SetAmmo(ItemStack stack,int slot,ItemStack ammo);
  
  public abstract ItemStack GetAmmo(ItemStack stack,int slot);

  
  static public boolean RoundMatches(ItemStack stack,String type)
  {
    if(stack == null)
    {
      return false;
    }
    Item item = stack.getItem(); 
    if(!(item instanceof IFirearmRound))
    {
      return false;
    }
    return ((IFirearmRound)item).getRoundType(stack).equals(type);
  }
}
