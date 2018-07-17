package exter.foundry.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import exter.foundry.container.ContainerMeltingCrucible;
import exter.foundry.gui.button.GuiButtonFoundry;
import exter.foundry.tileentity.TileEntityFoundry.RedstoneMode;
import exter.foundry.tileentity.TileEntityFoundryHeatable;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMeltingCrucible extends GuiFoundry
{

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("foundry:textures/gui/crucible.png");

    public static final int TANK_WIDTH = 16;
    public static final int TANK_HEIGHT = 47;
    public static final int TANK_X = 107;
    public static final int TANK_Y = 22;

    public static final int HEAT_BAR_X = 41;
    public static final int HEAT_BAR_Y = 57;
    public static final int HEAT_BAR_WIDTH = 54;
    public static final int HEAT_BAR_HEIGHT = 12;

    public static final int PROGRESS_X = 79;
    public static final int PROGRESS_Y = 23;
    public static final int PROGRESS_WIDTH = 22;
    public static final int PROGRESS_HEIGHT = 15;

    public static final int TANK_OVERLAY_X = 176;
    public static final int TANK_OVERLAY_Y = 0;

    public static final int HEAT_BAR_OVERLAY_X = 176;
    public static final int HEAT_BAR_OVERLAY_Y = 53;

    public static final int HEAT_BAR_MELT_X = 176;
    public static final int HEAT_BAR_MELT_Y = 65;
    public static final int HEAT_BAR_MELT_WIDTH = 3;

    public static final int PROGRESS_OVERLAY_X = 176;
    public static final int PROGRESS_OVERLAY_Y = 78;

    private static final int RSMODE_X = 176 - 16 - 4;
    private static final int RSMODE_Y = 4;
    private static final int RSMODE_TEXTURE_X = 176;
    private static final int RSMODE_TEXTURE_Y = 100;

    private final TileEntityMeltingCrucibleBasic te_crucible;
    private GuiButtonFoundry button_mode;

    private final String STRING_MACHINE;

    public GuiMeltingCrucible(TileEntityMeltingCrucibleBasic ms, EntityPlayer player)
    {
        super(new ContainerMeltingCrucible(ms, player), player.inventory);
        allowUserInput = false;
        ySize = 166;
        te_crucible = ms;
        STRING_MACHINE = I18n.format("gui.foundry.crucible");
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == button_mode.id)
        {
            switch (te_crucible.getRedstoneMode())
            {
            case RSMODE_IGNORE:
                te_crucible.setRedstoneMode(RedstoneMode.RSMODE_OFF);
                break;
            case RSMODE_OFF:
                te_crucible.setRedstoneMode(RedstoneMode.RSMODE_ON);
                break;
            case RSMODE_ON:
                te_crucible.setRedstoneMode(RedstoneMode.RSMODE_IGNORE);
                break;
            case RSMODE_PULSE:
                te_crucible.setRedstoneMode(RedstoneMode.RSMODE_IGNORE);
                break;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(GUI_TEXTURE);
        int window_x = (width - xSize) / 2;
        int window_y = (height - ySize) / 2;
        drawTexturedModalRect(window_x, window_y, 0, 0, xSize, ySize);

        //Draw heat bar.
        int heat = (te_crucible.getTemperature() - TileEntityFoundryHeatable.TEMP_MIN) * HEAT_BAR_WIDTH
                / (te_crucible.getMaxTemperature() - TileEntityFoundryHeatable.TEMP_MIN);
        int melt_point = (te_crucible.getMeltingPoint() - TileEntityFoundryHeatable.TEMP_MIN) * HEAT_BAR_WIDTH
                / (te_crucible.getMaxTemperature() - TileEntityFoundryHeatable.TEMP_MIN);
        if (heat > 0)
        {
            drawTexturedModalRect(window_x + HEAT_BAR_X, window_y + HEAT_BAR_Y, HEAT_BAR_OVERLAY_X, HEAT_BAR_OVERLAY_Y,
                    heat, HEAT_BAR_HEIGHT);
        }
        if (melt_point > 0)
        {
            drawTexturedModalRect(window_x + HEAT_BAR_X + melt_point - HEAT_BAR_MELT_WIDTH / 2, window_y + HEAT_BAR_Y,
                    HEAT_BAR_MELT_X, HEAT_BAR_MELT_Y, HEAT_BAR_MELT_WIDTH, HEAT_BAR_HEIGHT);
        }

        //Draw progress bar.
        int progress = te_crucible.getProgress() * PROGRESS_WIDTH / TileEntityMeltingCrucibleBasic.SMELT_TIME;
        if (progress > 0)
        {
            drawTexturedModalRect(window_x + PROGRESS_X, window_y + PROGRESS_Y, PROGRESS_OVERLAY_X, PROGRESS_OVERLAY_Y,
                    progress, PROGRESS_HEIGHT);
        }

        displayTank(window_x, window_y, TANK_X, TANK_Y, TANK_HEIGHT, TANK_OVERLAY_X, TANK_OVERLAY_Y,
                te_crucible.getTank(0));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouse_x, int mouse_y)
    {
        super.drawGuiContainerForegroundLayer(mouse_x, mouse_y);

        fontRenderer.drawString(STRING_MACHINE, xSize / 2 - fontRenderer.getStringWidth(STRING_MACHINE) / 2, 6,
                0x404040);
        fontRenderer.drawString(getInventoryName(), 8, ySize - 96 + 2, 0x404040);

    }

    @Override
    public void drawScreen(int mousex, int mousey, float par3)
    {
        super.drawScreen(mousex, mousey, par3);

        //Draw tool tips.

        if (isPointInRegion(TANK_X, TANK_Y, 16, TANK_HEIGHT, mousex, mousey))
        {
            List<String> currenttip = new ArrayList<>();
            addTankTooltip(currenttip, mousex, mousey, te_crucible.getTank(0));
            drawHoveringText(currenttip, mousex, mousey, fontRenderer);
        }

        if (isPointInRegion(HEAT_BAR_X, HEAT_BAR_Y, HEAT_BAR_WIDTH, HEAT_BAR_HEIGHT, mousex, mousey))
        {
            List<String> currenttip = new ArrayList<>();
            int heat = te_crucible.getTemperature() / 100;
            int melt_point = te_crucible.getMeltingPoint() / 100;
            currenttip.add(I18n.format("gui.foundry.crucible.temperature", heat));
            if (melt_point > 0)
            {
                currenttip.add(I18n.format("gui.foundry.crucible.melt", melt_point));
            }
            drawHoveringText(currenttip, mousex, mousey, fontRenderer);
        }
        if (isPointInRegion(RSMODE_X, RSMODE_Y, button_mode.width, button_mode.height, mousex, mousey))
        {
            List<String> currenttip = new ArrayList<>();
            currenttip.add(getRedstoenModeText(te_crucible.getRedstoneMode()));
            drawHoveringText(currenttip, mousex, mousey, fontRenderer);
        }
    }

    @Override
    protected ResourceLocation getGUITexture()
    {
        return GUI_TEXTURE;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        int window_x = (width - xSize) / 2;
        int window_y = (height - ySize) / 2;
        button_mode = new GuiButtonFoundry(1, RSMODE_X + window_x, RSMODE_Y + window_y, 16, 15, GUI_TEXTURE,
                RSMODE_TEXTURE_X, RSMODE_TEXTURE_Y, RSMODE_TEXTURE_X + 16, RSMODE_TEXTURE_Y);
        buttonList.add(button_mode);
    }

}
