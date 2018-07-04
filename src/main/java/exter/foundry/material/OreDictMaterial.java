package exter.foundry.material;

public class OreDictMaterial
{

    static public final OreDictMaterial[] MATERIALS = new OreDictMaterial[] {
            new OreDictMaterial("Aluminium", "ingot", "Aluminum"), new OreDictMaterial("Carbon", "dust"),
            new OreDictMaterial("Chrome", "ingot", "Chromium"), new OreDictMaterial("Cobalt", "ingot"),
            new OreDictMaterial("Copper", "ingot"), new OreDictMaterial("Gold", "ingot"),
            new OreDictMaterial("Iron", "ingot"), new OreDictMaterial("Lead", "ingot"),
            new OreDictMaterial("Manganese", "ingot"), new OreDictMaterial("Nickel", "ingot"),
            new OreDictMaterial("Osmium", "ingot"), new OreDictMaterial("Platinum", "ingot"),
            new OreDictMaterial("Silver", "ingot"), new OreDictMaterial("Tin", "ingot"),
            new OreDictMaterial("Titanium", "ingot"), new OreDictMaterial("Tungsten", "ingot"),
            new OreDictMaterial("Uranium", "ingot"), new OreDictMaterial("Zinc", "ingot"),
            new OreDictMaterial("Stone", "ingot"), new OreDictMaterial("Gunpowder", "dust"),
            new OreDictMaterial("Wood", "planks"), new OreDictMaterial("Endstone", "block"),
            new OreDictMaterial("Netherrack", "block"), new OreDictMaterial("Amethyst", "gem"),
            new OreDictMaterial("Brass", "ingot"), new OreDictMaterial("Bronze", "ingot"),
            new OreDictMaterial("Cobaltite", "dust"), new OreDictMaterial("Cupronickel", "ingot", "Constantan"),
            new OreDictMaterial("Diamond", "gem"), new OreDictMaterial("Electrum", "ingot"),
            new OreDictMaterial("Emerald", "gem"), new OreDictMaterial("GreenSapphire", "gem"),
            new OreDictMaterial("Invar", "ingot"), new OreDictMaterial("Kanthal", "ingot"),
            new OreDictMaterial("Nichrome", "ingot"), new OreDictMaterial("Obsidian", "dust"),
            new OreDictMaterial("Olivine", "gem"), new OreDictMaterial("Ruby", "gem"),
            new OreDictMaterial("Sapphire", "gem"), new OreDictMaterial("StainlessSteel", "ingot"),
            new OreDictMaterial("Steel", "ingot"), new OreDictMaterial("Topaz", "gem"),
            new OreDictMaterial("Redstone", "dust"), new OreDictMaterial("Glowstone", "dust"),
            new OreDictMaterial("RedAlloy", "ingot"), new OreDictMaterial("Rubber", "item"),
            new OreDictMaterial("Adamantine", "ingot"), new OreDictMaterial("Atlarus", "ingot"),
            new OreDictMaterial("Rubracium", "ingot"), new OreDictMaterial("Haderoth", "ingot"),
            new OreDictMaterial("Tartarite", "ingot"), new OreDictMaterial("Midasium", "ingot"),
            new OreDictMaterial("DamascusSteel", "ingot"), new OreDictMaterial("Angmallen", "ingot"),
            new OreDictMaterial("Quicksilver", "ingot"), new OreDictMaterial("Orichalcum", "ingot"),
            new OreDictMaterial("Celenegil", "ingot"), new OreDictMaterial("Vyroxeres", "ingot"),
            new OreDictMaterial("Sanguinite", "ingot"), new OreDictMaterial("Carmot", "ingot"),
            new OreDictMaterial("Infuscolium", "ingot"), new OreDictMaterial("Meutoite", "ingot"),
            new OreDictMaterial("Hepatizon", "ingot"), new OreDictMaterial("Eximite", "ingot"),
            new OreDictMaterial("Desichalkos", "ingot"), new OreDictMaterial("DeepIron", "ingot"),
            new OreDictMaterial("Ceruclase", "ingot"), new OreDictMaterial("BlackSteel", "ingot"),
            new OreDictMaterial("AstralSilver", "ingot"), new OreDictMaterial("Amordrine", "ingot"),
            new OreDictMaterial("Alduorite", "ingot"), new OreDictMaterial("Kalendrite", "ingot"),
            new OreDictMaterial("Lemurite", "ingot"), new OreDictMaterial("Inolashite", "ingot"),
            new OreDictMaterial("ShadowIron", "ingot"), new OreDictMaterial("ShadowSteel", "ingot"),
            new OreDictMaterial("Oureclase", "ingot"), new OreDictMaterial("Ignatius", "ingot"),
            new OreDictMaterial("Vulcanite", "ingot"), new OreDictMaterial("Prometheum", "ingot"),
            new OreDictMaterial("Ironwood", "ingot"), new OreDictMaterial("Steeleaf", "ingot"),
            new OreDictMaterial("Knightmetal", "ingot"), new OreDictMaterial("RedstoneAlloy", "ingot"),
            new OreDictMaterial("EnergeticAlloy", "ingot"), new OreDictMaterial("VibrantAlloy", "ingot"),
            new OreDictMaterial("DarkSteel", "ingot"), new OreDictMaterial("PulsatingIron", "ingot"),
            new OreDictMaterial("ElectricalSteel", "ingot"), new OreDictMaterial("Soularium", "ingot"),
            new OreDictMaterial("Alumina", "ingot"), new OreDictMaterial("Osmium", "ingot") };
    public final String suffix;
    public final String suffix_alias;

    public final String default_prefix;

    private OreDictMaterial(String suffix, String default_prefix)
    {
        this(suffix, default_prefix, null);
    }

    private OreDictMaterial(String suffix, String default_prefix, String suffix_alias)
    {
        this.suffix = suffix;
        this.default_prefix = default_prefix;
        this.suffix_alias = suffix_alias;
    }
}
